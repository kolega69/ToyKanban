package board.hendlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;

import board.beans.Phase;
import board.beans.User;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class CardUpdater {

	DBCollection users;
	HttpServletRequest request;

	public CardUpdater(DB db, HttpServletRequest request) {
		this.users = db.getCollection("users");
		this.request = request;
	}

	public void update() {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		String thisCardId = request.getParameter("thisCardId");
		ObjectId cardId = new ObjectId(thisCardId);
		
		BasicDBObject modification = getModificationObject(user, thisCardId);
		if (modification ==  null) return;

		BasicDBObject query = new BasicDBObject("_id", user.getEmail())
			.append("cards._id", cardId);
		
		users.update(query, modification);
		session.setAttribute("user", user);
	}
	
	private BasicDBObject getModificationObject(User user, String thisCardId) {
		String action = request.getParameter("action");
		
		BasicDBObject objectForUpdate = null;
		switch(action) {
		case "toInProgress":
			if (!user.getBoard().moveToInProgress(thisCardId)) return null;
			objectForUpdate = new BasicDBObject("cards.$.phase", Phase.inProgress.toString());
			break;
		case "toRejected":
			if(!user.getBoard().moveToRejected(thisCardId)) return null;
			objectForUpdate = new BasicDBObject("cards.$.phase", Phase.rejected.toString());
			break;
		case "toExecuted":
			if(!user.getBoard().moveToExecuted(thisCardId)) return null;
			objectForUpdate = new BasicDBObject("cards.$.phase", Phase.executed.toString());
			break;
		case "updateInfo":
			String newCardName = request.getParameter("newCardName").trim();
			if (newCardName.equals("")) {
				newCardName = "Новая задача";
			}
			String newDescription = request.getParameter("newDescription").trim();
			if (newDescription.equals("")) {
				newDescription = "Здесь должно быть описание";
			}
			if(!user.getBoard().changeCardData(thisCardId, newCardName, newDescription, Phase.planned)) {
				return null;
			}
			objectForUpdate = new BasicDBObject("cards.$.cardName", newCardName)
				.append("cards.$.description", newDescription);
			break;
		case "changePriority":
			String newPriority = request.getParameter("newPriority");
			if(!user.getBoard().changeCardPriority(thisCardId, newPriority));
			objectForUpdate = new BasicDBObject("cards.$.priority", newPriority);
		default:
			return null;
		}
		return new BasicDBObject("$set", objectForUpdate);
	}
	
	
}

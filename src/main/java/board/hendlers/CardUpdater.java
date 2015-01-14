package board.hendlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import authentication.beans.User;
import board.beans.Phase;

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

		String thisCard = request.getParameter("thisCard");
		User user = (User) session.getAttribute("user");
		if (!user.getBoard().moveToInProgress(thisCard)) return;

		BasicDBObject query = new BasicDBObject("_id", user.getEmail())
			.append("cards.cardName", thisCard);
		BasicDBObject fieldToUpdate = new BasicDBObject("cards.$.phase", Phase.inProgress.toString());
		BasicDBObject modification = new BasicDBObject("$set", fieldToUpdate);
		users.update(query, modification);

		session.setAttribute("user", user);
	}
}

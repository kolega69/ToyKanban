package board.hendlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;

import board.beans.Card;
import board.beans.User;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class NewCardCreator {

	DBCollection users;
	HttpServletRequest request;
	
	public NewCardCreator(DB db, HttpServletRequest request) {
		this.users   = db.getCollection("users");
		this.request = request;
	}
	
	public void create() {
		ObjectId cId = new ObjectId();
		request.setAttribute("id", cId.toHexString());
		Card card = new Card(request);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		
		BasicDBObject dbCard = new BasicDBObject("_id", cId)
			.append("phase", card.getPhase().toString())
			.append("cardName", card.getName())
			.append("description", card.getDescription())
			.append("priority", card.getPriority())
			.append("cardDate", card.getCreationDate());
		BasicDBObject query = new BasicDBObject("_id", user.getEmail());
		BasicDBObject arrayToAdd = new BasicDBObject("cards", dbCard);
		BasicDBObject modification = new BasicDBObject("$addToSet", arrayToAdd);
		
		users.update(query, modification);
		
		user.getBoard().add(card);
		session.setAttribute("user", user);
	}
}

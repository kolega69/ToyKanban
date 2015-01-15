package board.hendlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;

import board.beans.User;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class CardRemover {
	
	private DBCollection users;
	private HttpServletRequest request;
	
	public CardRemover(DB db, HttpServletRequest request) {
		this.request = request;
		this.users = db.getCollection("users");
	}
	
	public void remove() {
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		
		String cardId = request.getParameter("thisCardId");
		if (!user.getBoard().removeCard(cardId)) return;
		ObjectId id = new ObjectId(cardId);
		
		BasicDBObject cardToRemove = new BasicDBObject("_id", id);
		BasicDBObject matchingCards = new BasicDBObject("cards", cardToRemove);
		BasicDBObject modification = new BasicDBObject("$pull", matchingCards);
		
		BasicDBObject query = new BasicDBObject("_id",user.getEmail())
			.append("cards._id", id);
		users.update(query, modification);
		
	}
}

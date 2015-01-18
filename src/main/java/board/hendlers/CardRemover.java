package board.hendlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;

import board.beans.User;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * The {@code CardRemover} class handles removing an existing kanban card
 * from the board of the given user and saves changes to the database.
 * 
 * @author Oleh Koniachenko
 */
public class CardRemover {
	
	private DBCollection users;
	private HttpServletRequest request;
	private Logger logger = LogManager.getLogger();
	
	/**
	 * Constructs a new {@code CardRemover} by initializing users collection
	 * and http servlet request.
	 * 
	 * @param db       NewCardCreator
	 * @param request  request from a board page
	 */
	public CardRemover(DB db, HttpServletRequest request) {
		this.request = request;
		this.users = db.getCollection("users");
	}
	
	/**
	 * Find given card by _id in the user document and removes it from one.
	 * Updates the user document in a database. Rewrite session "user"
	 * parameter with updated user instance. 
	 */
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
		logger.info("The card " + cardId + " was removed");
	}
}

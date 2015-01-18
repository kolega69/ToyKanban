package board.hendlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;

import board.beans.Card;
import board.beans.User;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * The {@code NewCardCreator} class handles creating a new kanban card. Saves
 * its instance to the user board and inserts one into the user document in the
 * database.
 * 
 * @author Oleh Koniachenko
 */
public class NewCardCreator {

	private DBCollection users;
	private HttpServletRequest request;
	private final Logger logger = LogManager.getLogger();

	/**
	 * Constructs a new {@code NewCardCreator} by initializing users collection
	 * and http servlet request.
	 * 
	 * @param db       NewCardCreator
	 * @param request  request from a board page
	 */
	public NewCardCreator(DB db, HttpServletRequest request) {
		this.users = db.getCollection("users");
		this.request = request;
	}

	/**
	 * Creates a new card instance and save it to a database. Gets all required
	 * data from http servlet request; Saves updated user instance back to the
	 * session.
	 */
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
		logger.info("Was created a new card with name " + card.getName());
	}
}

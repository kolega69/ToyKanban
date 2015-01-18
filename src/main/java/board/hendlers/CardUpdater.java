package board.hendlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;

import board.beans.Phase;
import board.beans.User;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * The {@code CardUpdater} class handles updating an existing kanban card
 * for the given user and saves changes to the database.
 * 
 * @author Oleh Koniachenko
 */
public class CardUpdater {

	private DBCollection users;
	private HttpServletRequest request;
	private final Logger logger = LogManager.getLogger(); 

	/**
	 * Constructs a new {@code CardUpdater} by initializing users collection
	 * and http servlet request.
	 * 
	 * @param db       NewCardCreator
	 * @param request  request from a board page
	 */
	public CardUpdater(DB db, HttpServletRequest request) {
		this.users = db.getCollection("users");
		this.request = request;
		
	}
	/**
	 * Updates card data by parameters retrieved from
	 * http request. Then update user instance and rewrite it
	 * to the session.
	 */
	public void update() {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		String thisCardId = request.getParameter("thisCardId");
		ObjectId cardId = new ObjectId(thisCardId);
		
		// document with data for update
		BasicDBObject modification = getModificationObject(user, thisCardId);
		if (modification == null) return;
		
		// query to find a document to update
		BasicDBObject query = new BasicDBObject("_id", user.getEmail())
				.append("cards._id", cardId);

		users.update(query, modification);
		session.setAttribute("user", user);
		logger.info("The card " + thisCardId + "was updated.");
	}

	/**
	 * Gets update action parameter from http request and creates appropriate
	 * database document object for the updating specified field(s).
	 * 
	 * @param user
	 *            user instance
	 * @param thisCardId
	 *            card id for the identification
	 * @return a database document
	 */
	private BasicDBObject getModificationObject(User user, String thisCardId) {
		String action = request.getParameter("action");
		logger.info("Update action is: " + action);
		BasicDBObject objectForUpdate = null;
		switch (action) {
			case "toInProgress":
				if (!user.getBoard().moveToInProgress(thisCardId)) return null;
				objectForUpdate = new BasicDBObject("cards.$.phase", Phase.inProgress.toString());
				break;
			case "toRejected":
				if (!user.getBoard().moveToRejected(thisCardId)) return null;
				objectForUpdate = new BasicDBObject("cards.$.phase", Phase.rejected.toString());
				break;
			case "toExecuted":
				if (!user.getBoard().moveToExecuted(thisCardId)) return null;
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
				
				if (!user.getBoard().changeCardData(thisCardId, newCardName, newDescription, Phase.planned)) {
					return null;
				}
				objectForUpdate = new BasicDBObject("cards.$.cardName", newCardName)
						.append("cards.$.description", newDescription);
				break;
			case "changePriority":
				String newPriority = request.getParameter("newPriority");
				if (!user.getBoard().changeCardPriority(thisCardId, newPriority));
				objectForUpdate = new BasicDBObject("cards.$.priority", newPriority);
			default:
				return null;
		}
		return new BasicDBObject("$set", objectForUpdate);
	}

}

package authentication.handlers;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import authentication.security.PasswordHash;
import board.beans.Board;
import board.beans.Card;
import board.beans.User;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * The {@code AuthHandler} class handles requests for the user registration and
 * authentication. It creates new user documents and saves them to a collection
 * user, or retrieve from a database existing user.
 * 
 * @author Oleh Koniachenko
 */
public class AuthHandler {

	private final DBCollection users;
	private final Logger logger = LogManager.getLogger();

	/**
	 * Constructs a new {@code AuthHandler} with connection to the "users"
	 * collection in the database.
	 * 
	 * @param db
	 *            instance of a client view of a logical database
	 */
	public AuthHandler(DB db) {
		this.users = db.getCollection("users");
	}

	/**
	 * Searches for a user document in a database by email (id) and given
	 * password. Then return the user instance if exists.
	 * 
	 * @param userEmail
	 * @param userPassword
	 * @return existing user document
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public User getUserIfExists(String userEmail, String userPassword)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		BasicDBObject user = getUserByEmail(userEmail);
		if (user == null) {
			logger.info("Email " + userEmail + " is not exists");
			return null;
		}

		String correctHash = user.getString("hash");
		boolean success = new PasswordHash().validatePassword(userPassword, correctHash);

		if (success) {
			return retrieveUser(user);
		}
		logger.info(String.format("The password to %s is not correct", userEmail));
		return null;
	}

	/**
	 * At beginning searches if a user with given email (id) exists. If not,
	 * then creates hash for the password and saves the user in a database. Then
	 * creates and return new user instance.
	 * 
	 * @param userName
	 * @param userEmail
	 * @param userPassword
	 * @return a user instance
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public User createUserIfNotExists(String userName, String userEmail, String userPassword)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		BasicDBObject user = getUserByEmail(userEmail);
		if (user != null) return null;

		String hash = new PasswordHash().createHash(userPassword);
		Date creationDate = new Date();

		user = new BasicDBObject("name", userName)
				.append("_id", userEmail)
				.append("hash", hash)
				.append("date", creationDate);
		users.insert(user);

		logger.info(String.format("A new user saved into database. Name: %s and Email: %s", userName, userEmail));
		return new User(userName, userEmail, creationDate);
	}

	/**
	 * Searching a user in a database
	 * 
	 * @param userEmail  a user email as id
	 * @return a user document object from a database
	 */
	public BasicDBObject getUserByEmail(String userEmail) {
		DBObject obj = users.findOne(new BasicDBObject("_id", userEmail));
		return obj == null ? null : (BasicDBObject) obj;
	}

	/**
	 * Retrieve a user data from a document and a kanban cards if they exist.
	 * 
	 * @param DBuser
	 *            a user document from a database
	 * @return an instance of a user
	 */
	private User retrieveUser(BasicDBObject DBuser) {
		String name = DBuser.getString("name");
		String email = DBuser.getString("_id");
		Date creationDate = DBuser.getDate("date");
		User user = new User(name, email, creationDate);
		logger.info("The user with email " + email + "was found in a database");

		BasicDBList cards = (BasicDBList) (DBuser.get("cards") == null ? null : DBuser.get("cards"));
		if (cards == null || cards.isEmpty()) {
			logger.info("The user " + email + " have an empty board");
			return user;
		}

		Board board = user.getBoard();

		for (Object card : cards) {
			BasicDBObject c = (BasicDBObject) card;
			String id = c.getObjectId("_id").toHexString();
			String phase = c.getString("phase");
			String cardName = c.getString("cardName");
			Date cardDate = c.getDate("cardDate");
			String description = c.getString("description");
			String priority = c.getString("priority");

			Card userCard = new Card(id, cardName, description, priority, phase);
			userCard.setCreationDate(cardDate);

			board.add(userCard);
		}

		logger.info("Cards was added to user " + email + " board");
		return user;
	}

}

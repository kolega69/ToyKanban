package authentication.handlers;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import board.beans.Board;
import board.beans.Card;
import board.beans.User;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class AuthHandler {

	private final DBCollection users;
	private final Logger logger = LogManager.getLogger();
	
	public AuthHandler(DB db) {
		this.users = db.getCollection("users");
	}
	
	public User getUserIfExists(String userEmail, String userPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
		BasicDBObject user = getUserByEmail(userEmail);
		if (user == null) return null;
		
		String correctHash = user.getString("hash");
		boolean success = new PasswordHash().validatePassword(userPassword, correctHash);
		
		if (success) {
			return retrieveUser(user);
		}
		return null;
	}
	
	public User createUserIfNotExists(String userName, String userEmail, String userPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
		BasicDBObject user = getUserByEmail(userEmail);
		logger.info(user);
		if (user != null) return null;
		
		String hash = new PasswordHash().createHash(userPassword);
		Date creationDate = new Date();
		
		user = new BasicDBObject("name", userName)
			.append("_id", userEmail)
			.append("hash", hash)
			.append("date", creationDate);
		users.insert(user);
		
		logger.info(String.format("New user inserted into database. Name: %s and Email: %s", userName, userEmail));
		return new User(userName, userEmail, creationDate);
	}
	
	public BasicDBObject getUserByEmail(String userEmail) {
		DBObject obj = users.findOne(new BasicDBObject("_id", userEmail));
		return obj == null ? null : (BasicDBObject)obj; 
	}
	
	private User retrieveUser(BasicDBObject DBuser) {
		
			
		String name = DBuser.getString("name");
		String email = DBuser.getString("_id");
		Date creationDate = DBuser.getDate("date"); 
		User user = new User(name, email, creationDate);
		Board board = user.getBoard();
		
		BasicDBList cards = (BasicDBList) (DBuser.get("cards") == null ? null : DBuser.get("cards")) ;
		if (cards == null || cards.isEmpty()) return user;
		
		for (Object card : cards) {
			BasicDBObject c = (BasicDBObject)card;
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
		
		return user;
	}
	
}

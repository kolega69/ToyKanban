package board.hendlers;

import board.beans.Card;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class CreateNewCard {

	DBCollection users;
	Card card;
	final String userEmail;
	
	public CreateNewCard(DB db, Card card, String userEmail) {
		this.users     = db.getCollection("users");
		this.card      = card;
		this.userEmail = userEmail;
	}
	
	public void execute() {
		BasicDBObject dbCard = new BasicDBObject("phase", card.getPhase())
			.append("cardName", card.getName())
			.append("description", card.getDescription())
			.append("priority", card.getPriority())
			.append("cardDate", card.getCreationDate());
		BasicDBObject query = new BasicDBObject("_id", userEmail);
		BasicDBObject arrayToAdd = new BasicDBObject("cards", dbCard);
		BasicDBObject modification = new BasicDBObject("$addToSet", arrayToAdd);
		
		users.update(query, modification);
	}
}

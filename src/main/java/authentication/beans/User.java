package authentication.beans;

import java.util.Date;
import java.util.List;
import java.util.Map;

import board.beans.Card;

public class User {
	
	private String email;
	private String name;
	private Date creationDate;
	private Map<String, List<Card>> cards;
	
	public User(String name, String email, Date creationDate) {
		this.name = name;
		this.email = email;
		this.creationDate = creationDate;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Map<String, List<Card>> getCards() {
		return cards;
	}

	public void setCards(Map<String, List<Card>> cards) {
		this.cards = cards;
	}
	
	
}

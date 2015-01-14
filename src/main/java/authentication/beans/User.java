package authentication.beans;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import board.beans.Board;
import board.beans.Card;

public class User {
	
	private String email;
	private String name;
	private Date creationDate;
	private Board board;
	
	public User(String name, String email, Date creationDate) {
		this.name = name;
		this.email = email;
		this.creationDate = creationDate;
		board = new Board();
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

	public Board getBoard() {
		return board;
	}
	
}

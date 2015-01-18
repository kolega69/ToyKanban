package board.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

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
	
	public String getDatef() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("d-MMM-yyyy");
		return dateFormat.format(creationDate);
	}
	
}

package board.beans;

import java.util.Date;

public class Card {
	
	private String name;
	private String description;
	private String priority;
	private String phase;
	private Date creationDate;
	
	public Card(String name, String description, String priority, String phase) {
		this.name = name;
		this.description = description;
		this.priority = priority;
		this.phase = phase;
		creationDate = new Date();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	

}

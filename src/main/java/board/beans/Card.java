package board.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class Card {
	
	private String name;
	private String description;
	private String priority;
	private Phase phase = Phase.planned;
	private String datef;
	private Date creationDate = new Date();
	
	public Card(String name, String description, String priority) {
		this.name = name;
		this.description = description;
		this.priority = priority;
		setFormattedDate(creationDate);
	}
	public Card(String name, String description, String priority, String phase) {
		this(name, description, priority);
		this.phase = Phase.valueOf(phase);
	}
	
	public Card(HttpServletRequest req) {
		this.name = req.getParameter("cardName");
		this.description = req.getParameter("description");
		this.priority = req.getParameter("priority");
		setFormattedDate(creationDate);
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

	public Phase getPhase() {
		return phase;
	}

	public void setPhase(Phase phase) {
		this.phase = phase;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
		setFormattedDate(creationDate);
	}
	
	private void setFormattedDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("d-MMM-yyyy");
		datef = dateFormat.format(date);
	}
	
	public String getDatef() {
		return datef;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result	+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phase == null) ? 0 : phase.hashCode());
		result = prime * result	+ ((priority == null) ? 0 : priority.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phase != other.phase)
			return false;
		if (priority == null) {
			if (other.priority != null)
				return false;
		} else if (!priority.equals(other.priority))
			return false;
		return true;
	}
}

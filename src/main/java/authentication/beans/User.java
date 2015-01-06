package authentication.beans;

import java.time.LocalDate;

public class User {
	
	private String email;
	private String name;
	private LocalDate creationDate;
	
	public User(String name, String email, LocalDate creationDate) {
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

	public LocalDate getCreationDate() {
		return creationDate;
	}
}

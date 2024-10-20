package project.slash.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
	@Id
	@Column(name = "user_id")
	private String id;
	private String role;
	private String name;
	private String password;
	private String email;
	@Column(name = "phone_num")
	private String phoneNum;
}

package project.slash.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
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

	public static User from(String id, String role, String name, String password, String email, String phoneNum) {
		return new User(id, role, name, password, email, phoneNum);
	}
}

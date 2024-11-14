package project.slash.system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Systems {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "system_id")
	private Long id;

	private String name;

	private Systems(String name) {
		this.name = name;
	}

	public static Systems of(String name){
		return new Systems(name);
	}
}

package project.slash.system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Systems {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "system_id")
	private Long id;
	private String name;
}

package project.slash.system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Equipment {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "equipment_id")
	private Long id;
	private String name;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "system_id")
	private Systems systems;

	private Equipment(String name, Systems systems) {
		this.name = name;
		this.systems = systems;
	}

	public static Equipment from(String name, Systems systems){
		return new Equipment(name, systems);
	}
}

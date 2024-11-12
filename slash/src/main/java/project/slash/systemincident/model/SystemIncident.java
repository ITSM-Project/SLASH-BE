package project.slash.systemincident.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import project.slash.taskrequest.model.TaskRequest;

@Entity
@Table(name = "system_incident")
public class SystemIncident {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "system_incident_id")
	private Long id;
	@Column(name = "incident_time")
	private long incidentTime;
	@OneToOne
	@JoinColumn(name = "request_id")
	private TaskRequest taskRequest;
}

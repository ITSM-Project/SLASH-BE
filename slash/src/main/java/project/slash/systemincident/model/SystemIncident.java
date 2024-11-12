package project.slash.systemincident.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import project.slash.taskrequest.model.TaskRequest;

@Entity
@Table(name = "system_incident")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SystemIncident {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "system_incident_id")
	private Long id;
	@Column(name = "incident_time")
	private long incidentTime;
	@OneToOne
	@JoinColumn(name = "request_id")
	private TaskRequest taskRequest;

	public static SystemIncident create(long incidentTime, TaskRequest taskRequest) {
		return SystemIncident
			.builder()
			.incidentTime(incidentTime)
			.taskRequest(taskRequest).build();
}
}

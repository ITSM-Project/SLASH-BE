package project.slash.systemincident.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.systemincident.model.SystemIncident;

public interface SystemIncidentRepository extends JpaRepository<SystemIncident, Long> {
}

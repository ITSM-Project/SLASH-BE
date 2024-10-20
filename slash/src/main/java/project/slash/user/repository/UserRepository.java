package project.slash.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.slash.user.model.User;

public interface UserRepository extends JpaRepository<User, String> {
}

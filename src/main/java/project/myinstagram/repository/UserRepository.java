package project.myinstagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.myinstagram.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}

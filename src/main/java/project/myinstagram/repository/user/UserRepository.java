package project.myinstagram.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import project.myinstagram.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByUsernameLike(String username);

    User findByUsername(String username);
}

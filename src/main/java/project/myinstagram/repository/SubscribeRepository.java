package project.myinstagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.myinstagram.entity.Subscribe;

import java.util.List;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {
    List<Subscribe> findALlByFromUserId(Long id);

    Subscribe findByFromUserIdAndToUserId(Long id, Long toUserId);
}

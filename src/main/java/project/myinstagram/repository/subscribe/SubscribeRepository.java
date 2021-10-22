package project.myinstagram.repository.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;
import project.myinstagram.entity.Subscribe;

import java.util.List;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long>, SubscribeCustomRepository {
    /*List<Subscribe> findALlByFromUserId(Long id);*/

    Subscribe findByFromUserIdAndToUserId(Long id, Long toUserId);
}

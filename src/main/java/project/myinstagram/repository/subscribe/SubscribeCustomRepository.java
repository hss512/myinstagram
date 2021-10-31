package project.myinstagram.repository.subscribe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.myinstagram.entity.Subscribe;

import java.util.List;

public interface SubscribeCustomRepository {

    Page<Subscribe> getSubscribeList(Long pageUserId, Pageable pageable);
}

package project.myinstagram.repository.subscribe;

import project.myinstagram.entity.Subscribe;

import java.util.List;

public interface SubscribeCustomRepository {

    List<Subscribe> getSubscribeList(Long pageUserId);
}

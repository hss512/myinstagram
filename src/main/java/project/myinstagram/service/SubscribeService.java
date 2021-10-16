package project.myinstagram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.myinstagram.entity.Subscribe;
import project.myinstagram.repository.SubscribeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;

    public List<Subscribe> getSubscribeList(Long id) {
        return subscribeRepository.findALlByFromUserId(id);
    }
}

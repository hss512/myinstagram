package project.myinstagram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.myinstagram.entity.Subscribe;
import project.myinstagram.entity.User;
import project.myinstagram.repository.subscribe.SubscribeRepository;
import project.myinstagram.repository.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<Subscribe> getSubscribeList(Long id, Pageable pageable) {
        Page<Subscribe> subscribeList = subscribeRepository.getSubscribeList(id, pageable);
        return subscribeList;
    }

    public Subscribe follow(Long toUserId, String fromUserId){

        User fromUser = userRepository.findById(Long.parseLong(fromUserId)).get();

        Subscribe newSubscribe = Subscribe
                .builder()
                .fromUser(fromUser)
                .toUser(userRepository.findById(toUserId).get())
                .build();

        subscribeRepository.save(newSubscribe);

        return newSubscribe;
    }

    public int followCancel(Long toUserId, String fromUserId) {

        User fromUser = userRepository.findById(Long.parseLong(fromUserId)).get();

        Subscribe getSubscribe = subscribeRepository.findByFromUserIdAndToUserId(fromUser.getId(), toUserId);

        subscribeRepository.delete(getSubscribe);

        return 1;
    }

    public int followCheck(Long toUserId, String fromUserId) {

        User fromUser = userRepository.findById(Long.parseLong(fromUserId)).get();

        Subscribe getSubscribe = subscribeRepository.findByFromUserIdAndToUserId(fromUser.getId(), toUserId);

        if(getSubscribe != null){
            return 1;
        }else{
            return 0;
        }
    }
}

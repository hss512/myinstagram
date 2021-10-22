package project.myinstagram.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.myinstagram.entity.Subscribe;
import project.myinstagram.repository.subscribe.SubscribeRepository;

import java.util.List;

@SpringBootTest
public class SubscribeRepositoryTest {

    @Autowired
    private SubscribeRepository subscribeRepository;

    @Test
    public void getList(){
        List<Subscribe> subscribeList = subscribeRepository.getSubscribeList(2L);

        for (Subscribe subscribe : subscribeList) {
            System.out.println("subscribe = " + subscribe.getToUser().getName());
        }
    }
}

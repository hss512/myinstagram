package project.myinstagram.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.myinstagram.entity.Subscribe;

import java.util.List;

@SpringBootTest
public class SubscribeServiceTest {

    @Autowired
    private SubscribeService subscribeService;

    @Test
    @Transactional(readOnly = true)
    public void getSubscribeList(){
        List<Subscribe> subscribeList = subscribeService.getSubscribeList(1L);
        for (Subscribe subscribe : subscribeList) {
            System.out.println("subscribe = " + subscribe.getToUser().getName());
        }
    }
}

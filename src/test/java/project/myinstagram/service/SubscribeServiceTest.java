package project.myinstagram.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        Page<Subscribe> subscribeList = subscribeService.getSubscribeList(1L, Pageable.unpaged());
        for (Subscribe subscribe : subscribeList) {
            System.out.println("subscribe = " + subscribe.getToUser().getName());
        }
    }
}

package project.myinstagram.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.myinstagram.dto.UserDTO;
import project.myinstagram.dto.ValidateDTO;
import project.myinstagram.entity.Subscribe;
import project.myinstagram.service.SubscribeService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SubscribeApiController {

    private final SubscribeService subscribeService;

    @GetMapping("/subscribes/{fromUserId}")
    public ResponseEntity<?> getSubscribeList(@PathVariable Long fromUserId){

        List<Subscribe> subscribeList = subscribeService.getSubscribeList(fromUserId);

        List<UserDTO> toUserDTO = new ArrayList<>();

        for (Subscribe subscribe : subscribeList) {
            toUserDTO.add(subscribe.getToUser().toDTO().subscribeUserDTO());
        }

        return new ResponseEntity<>(new ValidateDTO<>(1, "성공", toUserDTO), HttpStatus.OK);
    }
}

package project.myinstagram.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.myinstagram.dto.UserDTO;
import project.myinstagram.dto.ValidateDTO;
import project.myinstagram.entity.Subscribe;
import project.myinstagram.principal.CustomUserDetails;
import project.myinstagram.service.SubscribeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SubscribeApiController {

    private final SubscribeService subscribeService;

    @GetMapping("/subscribes/{pageUserId}/{userId}")
    public ResponseEntity<?> getSubscribeList(@PathVariable Long pageUserId,
                                              @PathVariable String userId){

        List<Subscribe> subscribeList = subscribeService.getSubscribeList(pageUserId);

        List<UserDTO> subscribeUserDTO = new ArrayList<>();

        for (Subscribe subscribe : subscribeList) {
            subscribeUserDTO.add(subscribe.getToUser().toDTO().subscribeUserDTO());
        }

        return new ResponseEntity<>(new ValidateDTO<>(1, "성공", subscribeUserDTO), HttpStatus.OK);
    }

    @PostMapping("/follow/{toUserId}")
    public ResponseEntity<?> follow(@PathVariable Long toUserId,
                                    @RequestBody Map<String,String> map){

        System.out.println("fromUserId = " + map.get("fromUserId"));

        Subscribe follow = subscribeService.follow(toUserId, map.get("fromUserId"));

        return new ResponseEntity<>(new ValidateDTO<>(1, "성공", follow), HttpStatus.OK);
    }

    @DeleteMapping("/follow/{toUserId}")
    public ResponseEntity<?> followCancel(@PathVariable Long toUserId,
                                          @RequestBody Map<String,String> map){

        System.out.println("fromUserId = " + map.get("fromUserId"));

        int i = subscribeService.followCancel(toUserId, map.get("fromUserId"));

        if(i == 1) {
            return new ResponseEntity<>(new ValidateDTO<>(1, "성공", i), HttpStatus.OK);
        }else
            return new ResponseEntity<>(new ValidateDTO<>(0, "실패", i), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

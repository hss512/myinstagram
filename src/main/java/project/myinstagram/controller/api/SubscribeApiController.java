package project.myinstagram.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.myinstagram.dto.SubscribeDTO;
import project.myinstagram.dto.ValidateDTO;
import project.myinstagram.dto.user.UserDTO;
import project.myinstagram.entity.Subscribe;
import project.myinstagram.service.SubscribeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class SubscribeApiController {

    private final SubscribeService subscribeService;

    @GetMapping("/subscribes/{pageUserId}/{userId}")
    public ResponseEntity<?> getSubscribeList(@PathVariable Long pageUserId,
                                              @PathVariable String userId){

        int checkNum = -1;

        List<Subscribe> pageUserSubscribe = subscribeService.getSubscribeList(pageUserId);

        List<Subscribe> userSubscribe = subscribeService.getSubscribeList(Long.parseLong(userId));

        List<UserDTO> subscribeDTO = new ArrayList<>();

        if(!pageUserId.equals(Long.parseLong(userId))){
            for (Subscribe puSubscribe : pageUserSubscribe) {
                for (Subscribe uSubscribe : userSubscribe) {
                    if(checkNum==-1) {
                        if (puSubscribe.getToUser().equals(uSubscribe.getToUser())) {
                            checkNum = 1; // 팔로우 한 상태
                        } else if (puSubscribe.getToUser().equals(uSubscribe.getFromUser())) {
                            checkNum = 2; // 팔로우 안한 상태
                        } else {
                            checkNum = 0; // 본인
                        }
                    } // 코드좀 고치자
                }
                subscribeDTO.add(puSubscribe.getToUser().toDTO().subscribeCheckDTO(checkNum));
                checkNum = -1;
            }
            log.info("다른 유저 팔로워 보기");
        }else{
            for (Subscribe subscribe : pageUserSubscribe) {
                subscribeDTO.add(subscribe.getToUser().toDTO().subscribeCheckDTO(1));
            }
            log.info("나의 팔로워 보기");
        }
        return new ResponseEntity<>(new ValidateDTO<>(1, "성공", subscribeDTO), HttpStatus.OK);
    }

    @PostMapping("/follow/{toUserId}")
    public ResponseEntity<?> follow(@PathVariable Long toUserId,
                                    @RequestBody Map<String,String> map){

        System.out.println("fromUserId = " + map.get("fromUserId"));

        SubscribeDTO follow = subscribeService.follow(toUserId, map.get("fromUserId")).toDTO();

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

package project.myinstagram.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.myinstagram.dto.ReplyDTO;
import project.myinstagram.dto.ValidateDTO;
import project.myinstagram.principal.CustomUserDetails;
import project.myinstagram.service.ReplyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ReplyApiController {

    private final ReplyService replyService;

    @PostMapping("/reply/board/{boardId}")
    public ResponseEntity<?> postReply(@PathVariable("boardId") String boardId,
                                       @AuthenticationPrincipal CustomUserDetails userDetails,
                                       ReplyDTO replyDTO){

        ReplyDTO reply = replyService.postReply(boardId, userDetails.getUserDTO(), replyDTO.getContent());

        return new ResponseEntity<>(new ValidateDTO<>(1, "reply api success", reply), HttpStatus.OK);
    }

    @DeleteMapping("/reply/{replyId}")
    public ResponseEntity<?> postReply(@PathVariable("replyId") String replyId){

        int result = replyService.deleteReply(Long.parseLong(replyId));

        if(result == 1) {
            return new ResponseEntity<>(new ValidateDTO<>(1, "reply api success", replyId), HttpStatus.OK);
        }else
            return new ResponseEntity<>(new ValidateDTO<>(0, "reply api fail", result), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

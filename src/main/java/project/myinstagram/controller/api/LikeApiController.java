package project.myinstagram.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.myinstagram.dto.ValidateDTO;
import project.myinstagram.principal.CustomUserDetails;
import project.myinstagram.service.LikeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeApiController {

    private final LikeService likeService;

    @PostMapping("/like/board/{boardId}")
    public ResponseEntity<?> boardLike(@PathVariable("boardId") String boardId,
                                       @AuthenticationPrincipal CustomUserDetails userDetails){

        int result = likeService.boardLike(boardId, userDetails.getUserDTO());

        if (result == 1) {
            return new ResponseEntity<>(new ValidateDTO<>(1, "성공", result), HttpStatus.OK);
        }else
            return new ResponseEntity<>(new ValidateDTO<>(0, "실패", result), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

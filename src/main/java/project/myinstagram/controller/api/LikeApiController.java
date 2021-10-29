package project.myinstagram.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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

        int boardLikeCount = likeService.getBoardLikes(Long.parseLong(boardId));

        if (result == 1) {
            return new ResponseEntity<>(new ValidateDTO<>(1, "성공", boardLikeCount), HttpStatus.OK);
        }else
            return new ResponseEntity<>(new ValidateDTO<>(0, "실패", boardLikeCount), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/like/board/{boardId}")
    public ResponseEntity<?> boardLikeCancel(@PathVariable("boardId") String boardId,
                                       @AuthenticationPrincipal CustomUserDetails userDetails){

        likeService.boardLikeCancel(boardId, userDetails.getUserDTO());

        int boardLikeCount = likeService.getBoardLikes(Long.parseLong(boardId));

        return new ResponseEntity<>(new ValidateDTO<>(1, "좋아요 삭제 성공", boardLikeCount), HttpStatus.OK);
    }
}

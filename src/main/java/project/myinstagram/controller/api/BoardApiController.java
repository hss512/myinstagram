package project.myinstagram.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import project.myinstagram.dto.ValidateDTO;
import project.myinstagram.dto.board.BoardJsonDTO;
import project.myinstagram.entity.Likes;
import project.myinstagram.principal.CustomUserDetails;
import project.myinstagram.service.BoardService;
import project.myinstagram.service.LikeService;
import project.myinstagram.service.ReplyService;
import project.myinstagram.service.SubscribeService;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardApiController {

    @Value("${file.path}")
    private String uploadPath;

    private final BoardService boardService;

    private final LikeService likeService;


    @GetMapping("/api/board/image")
    public ResponseEntity<byte[]> getImage(String username, String fileName, String boardId){

        ResponseEntity<byte[]> image = null;

        try{
            String decodeFileName = URLDecoder.decode(fileName, "UTF-8");

            File file = new File(uploadPath + username + File.separator + boardId + File.separator + decodeFileName);

            log.info("file={}", file);

            HttpHeaders header = new HttpHeaders();

            header.add("Content-Type", Files.probeContentType(file.toPath()));

            image = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);


        } catch (Exception e) {

            try {
                log.error(e.getMessage());

                File file = new File(uploadPath + "non.jpg");

                HttpHeaders header = new HttpHeaders();

                header.add("Content-Type", Files.probeContentType(file.toPath()));

                return new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);

            } catch (Exception exp) {
                log.error(exp.getMessage());
            }
        }
        return image;
    }

    @GetMapping("/api/board")
    public ResponseEntity<?> getBoardList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PageableDefault(size = 9, sort = "createDate", direction = Sort.Direction.ASC) Pageable pageable){

        Long userId = userDetails.getUserDTO().getId();

        log.info("userId={}",userId);

        List<Likes> likesList = likeService.likeCheck(userId);

        Page<BoardJsonDTO> boardList = boardService.getBoardList(userId, pageable);

        for (BoardJsonDTO boardJsonDTO : boardList) {
            for (Likes likes : likesList) {
                if(likes.getBoard().getId().equals(boardJsonDTO.getId())){
                    boardJsonDTO.setLikeCheck(1);
                }else {
                    if(boardJsonDTO.getLikeCheck() != 1) {
                        boardJsonDTO.setLikeCheck(0);
                    }
                }
            }
        }

        return new ResponseEntity<>(new ValidateDTO<>(1, "BoardListApi", boardList), HttpStatus.OK);
    }

    @GetMapping("/api/board/all")
    public ResponseEntity<?> getExploreBoard(@PageableDefault(size = 9, sort = "likeCount", direction = Sort.Direction.ASC) Pageable pageable){

        Page<BoardJsonDTO> result = boardService.getExploreBoard(pageable);

        return new ResponseEntity<>(new ValidateDTO<>(1, "explore api", result), HttpStatus.OK);
    }

    @GetMapping("/p/board/{boardId}")
    public ResponseEntity<?> getModalBoard(@PathVariable("boardId") String boardId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails){

        Long userId = userDetails.getUserDTO().getId();

        BoardJsonDTO board = boardService.getModalBoard(Long.parseLong(boardId), userDetails.getUserDTO());

        List<Likes> likesList = likeService.likeCheck(userId);

        for (Likes likes : likesList) {
            if(likes.getUser().getId().equals(userId) && likes.getBoard().getId() == Long.parseLong(boardId)){
                board.setLikeCheck(1);
            }else {
                if(board.getLikeCheck() != 1) {
                    board.setLikeCheck(0);
                }
            }
        }

        return new ResponseEntity<>(new ValidateDTO<>(1, "modalBoard", board), HttpStatus.OK);
    }

    @DeleteMapping("/board/{boardId}")
    public void deleteBoard(@AuthenticationPrincipal CustomUserDetails userDetails,
                            @PathVariable("boardId") Long boardId){

        Long userId = userDetails.getUserDTO().getId();

        boardService.deleteBoard(boardId, userId);
    }
}

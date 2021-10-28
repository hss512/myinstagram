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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.myinstagram.dto.ValidateDTO;
import project.myinstagram.dto.board.BoardJsonDTO;
import project.myinstagram.principal.CustomUserDetails;
import project.myinstagram.service.BoardService;
import project.myinstagram.service.SubscribeService;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class BoardApiController {

    @Value("${file.path}")
    private String uploadPath;

    private final BoardService boardService;

    private final SubscribeService subscribeService;

    @GetMapping("/board/image")
    public ResponseEntity<byte[]> getImage(String username, String fileName, String boardId){

        ResponseEntity<byte[]> image = null;

        try{
            String decodeFileName = URLDecoder.decode(fileName, "UTF-8");

            File file = new File(uploadPath + username + File.separator + boardId + File.separator + decodeFileName);

            log.info("file: " + file);

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

    @GetMapping("/board")
    public ResponseEntity<?> getBoardList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PageableDefault(size = 5, sort = "createDate", direction = Sort.Direction.ASC) Pageable pageable,
                                          String page){

        int pageNumber = Integer.parseInt(page);

        Long userId = userDetails.getUserDTO().getId();

        log.info("userId={}",userId);

        Page<BoardJsonDTO> boardList = boardService.getBoardList(userId, pageable);

        for (BoardJsonDTO board : boardList) {
            log.info("board={}",board.getUserDTO());
        }

        return new ResponseEntity<>(new ValidateDTO<>(1, "BoardListApi", boardList), HttpStatus.OK);
    }
}

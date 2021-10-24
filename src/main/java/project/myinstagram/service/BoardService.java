package project.myinstagram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import project.myinstagram.dto.BoardDTO;
import project.myinstagram.dto.user.SignUpDTO;
import project.myinstagram.dto.user.UserDTO;
import project.myinstagram.entity.Board;
import project.myinstagram.repository.BoardRepository;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    @Value("${file.path}")
    private String uploadPath;

    private final BoardRepository boardRepository;

    public List<Board> getBoardList(Long id) {

        return boardRepository.findALlByUserId(id);
    }

    public void boardUpload(BoardDTO boardDTO, SignUpDTO userDTO) {

        String originalFilename = boardDTO.getFile().getOriginalFilename();

        log.info("originalFilename={} ", originalFilename);

        String username = userDTO.getUsername();

        log.info("username={}", username);

        Board board = Board.builder()
                .user(userDTO.toEntity())
                .content(boardDTO.getContent())
                .imageUrl(originalFilename)
                .build();

        Board saveBoard = boardRepository.save(board);

        Long boardId = saveBoard.getId();

        try{

            if(Files.exists(Path.of(uploadPath + "/" + username))) {

                log.info("uploadPath={}", Path.of(uploadPath + "/" + username));

                File file = new File(uploadPath + "/" + username, String.valueOf(boardId));

                file.mkdir();

                boardDTO.getFile().transferTo(new File(file.getAbsolutePath(), originalFilename));

            }else{
                File file = new File(uploadPath, username);
                file.mkdir();

                File newFile = new File(file, String.valueOf(boardId));

                newFile.mkdir();

                boardDTO.getFile().transferTo(new File(newFile.getAbsolutePath(), originalFilename));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*boardRepository.save();*/
    }
}

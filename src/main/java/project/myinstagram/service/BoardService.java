package project.myinstagram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.myinstagram.dto.board.BoardDTO;
import project.myinstagram.dto.board.BoardJsonDTO;
import project.myinstagram.dto.user.SignUpDTO;
import project.myinstagram.entity.Board;
import project.myinstagram.repository.board.BoardRepository;

import java.io.File;
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

    private final SubscribeService subscribeService;

    public List<Board> getMyBoardList(Long id) {

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
    }

    public Page<BoardJsonDTO> getBoardList(Long id, Pageable pageable){

        return boardRepository.getBoardList(id, pageable);
    }
}

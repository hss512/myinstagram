package project.myinstagram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.myinstagram.dto.board.BoardDTO;
import project.myinstagram.dto.board.BoardJsonDTO;
import project.myinstagram.dto.user.SignUpDTO;
import project.myinstagram.entity.Board;
import project.myinstagram.repository.board.BoardRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    @Value("${file.path}")
    private String uploadPath;

    private final BoardRepository boardRepository;

    public List<BoardJsonDTO> getMyBoardList(Long id) {

        List<BoardJsonDTO> boardDtoList = new ArrayList<>();

        List<Board> boardList = boardRepository.findALlByUserId(id);

        for (Board board : boardList) {
            boardDtoList.add(board.toJsonDTO());
        }

        return boardDtoList;
    }

    @Transactional
    public void boardUpload(BoardDTO boardDTO, SignUpDTO userDTO) {

        String originalFilename = boardDTO.getFile().getOriginalFilename();

        log.info("originalFilename={} ", originalFilename);

        String username = userDTO.getUsername();

        log.info("username={}", username);

        UUID uuid = UUID.randomUUID();

        String uploadFilename = uuid+ "_" + originalFilename;

        Board board = Board.builder()
                .user(userDTO.toEntity())
                .content(boardDTO.getContent())
                .imageUrl(uploadFilename)
                .build();

        Board saveBoard = boardRepository.save(board);

        Long boardId = saveBoard.getId();

        BufferedImage image = null;

        MultipartFile imageFile = boardDTO.getFile();

        try{

            image = ImageIO.read(imageFile.getInputStream());

            int width = image.getWidth();
            int height = image.getHeight();

            if(width > height){
                width = height;
            }else{
                height = width;
            }

            BufferedImage thumbImage = Thumbnails.of(image)
                    .sourceRegion(Positions.CENTER, width, height)
                    .size(700, 700)
                    .outputQuality(1.0f).outputFormat("png") // 보다 고화질
                    .asBufferedImage();

            if(Files.exists(Path.of(uploadPath + "/" + username))) {

                log.info("uploadPath={}", Path.of(uploadPath + "/" + username));

                File file = new File(uploadPath + "/" + username, String.valueOf(boardId));

                file.mkdir();

                ImageIO.write(thumbImage, "png", new File(file.getAbsolutePath(), uploadFilename));

            }else{
                File file = new File(uploadPath, username);
                file.mkdir();

                File newFile = new File(file, String.valueOf(boardId));

                newFile.mkdir();

                ImageIO.write(thumbImage, "png", new File(newFile.getAbsolutePath(), uploadFilename));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Page<BoardJsonDTO> getBoardList(Long id, Pageable pageable){

        Page<BoardJsonDTO> boardList = boardRepository.getBoardList(id, pageable);

        for (BoardJsonDTO boardDTO : boardList) {

            LocalDateTime nowTime = LocalDateTime.now();

            LocalDateTime createdDate = boardDTO.getCreatedDate();

            Duration timeBetween = Duration.between(createdDate, nowTime);

            long seconds = timeBetween.getSeconds();

            if(seconds/60 == 0){
                boardDTO.setTime("방금 전"); // 방금 전
            }else if((seconds/60)/60 == 0){
                boardDTO.setTime(seconds/60 + "분 전"); // x분 전
            }else if(((seconds/60)/60)/24 == 0){
                boardDTO.setTime((seconds/60)/60 + "시간 전"); // x 시간 전
            }else if(((seconds/60)/60)/24 > 0){
                boardDTO.setTime(((seconds/60)/60)/24 + 1 + "일 전"); // x일 전
            }
        }

        return boardList;
    }

    public Page<BoardJsonDTO> getExploreBoard(Pageable pageable) {

        return boardRepository.getExploreBoard(pageable);
    }

    public BoardJsonDTO getModalBoard(Long boardId, SignUpDTO userDTO) {

        Board findBoard = boardRepository.findById(boardId).get();

        BoardJsonDTO boardDTO = findBoard.toJsonDTO();

        LocalDateTime nowTime = LocalDateTime.now();

        LocalDateTime createdDate = findBoard.createdDate;

        Duration timeBetween = Duration.between(createdDate, nowTime);

        boardDTO.setCreatedDate(createdDate);

        log.info("timeBetween={}",timeBetween.getSeconds());

        long seconds = timeBetween.getSeconds();

        if(seconds/60 == 0){
            boardDTO.setTime("방금 전"); // 방금 전
        }else if((seconds/60)/60 == 0){
            boardDTO.setTime(seconds/60 + 1 + "분 전"); // x분 전
        }else if(((seconds/60)/60)/24 == 0){
            boardDTO.setTime((seconds/60)/60 + "시간 전"); // x 시간 전
        }else if(((seconds/60)/60)/24 > 0){
            boardDTO.setTime(((seconds/60)/60)/24 + "일 전"); // x일 전
        }

        return boardDTO;
    }
}

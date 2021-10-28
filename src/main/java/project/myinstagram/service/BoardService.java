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

    private final SubscribeService subscribeService;

    public List<Board> getMyBoardList(Long id) {

        return boardRepository.findALlByUserId(id);
    }

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

        return boardRepository.getBoardList(id, pageable);
    }
}

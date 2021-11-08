package project.myinstagram.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import project.myinstagram.dto.board.BoardJsonDTO;
import project.myinstagram.entity.Board;
import project.myinstagram.entity.Reply;
import project.myinstagram.entity.User;
import project.myinstagram.repository.board.BoardRepository;
import project.myinstagram.repository.user.UserRepository;
import project.myinstagram.service.BoardService;

import java.util.List;

@SpringBootTest
@Rollback
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Test
    public void getReply(){

        Board board = boardRepository.findById(1L).get();


        System.out.println("board = " + board.getLikesList());
    }

    @Test
    public void addBoard(){
        Board board = boardRepository.findById(1L).get();

        for (int i = 0; i < 35; i++) {
            Board build = Board.builder()
                    .content(board.getContent())
                    .imageUrl(board.getImageUrl())
                    .user(board.getUser())
                    .build();
            boardRepository.save(build);
        }
    }

    @Test
    public void deleteBoard(){
        boardService.deleteBoard(5L, 1L);
    }
}

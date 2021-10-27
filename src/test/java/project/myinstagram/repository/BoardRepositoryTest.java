package project.myinstagram.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.myinstagram.dto.board.BoardJsonDTO;
import project.myinstagram.repository.board.BoardRepository;

import java.util.List;

@SpringBootTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void getBoardList(){


    }
}

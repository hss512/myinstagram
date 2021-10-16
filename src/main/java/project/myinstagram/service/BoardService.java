package project.myinstagram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.myinstagram.entity.Board;
import project.myinstagram.repository.BoardRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> getBoardList(Long id) {

        return boardRepository.findALlByUserId(id);
    }
}

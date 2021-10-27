package project.myinstagram.repository.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.myinstagram.dto.board.BoardJsonDTO;

public interface BoardCustomRepository {

    Page<BoardJsonDTO> getBoardList(Long id, Pageable pageable);
}

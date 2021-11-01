package project.myinstagram.repository.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.myinstagram.dto.board.BoardJsonDTO;
import project.myinstagram.dto.user.SignUpDTO;

public interface BoardCustomRepository {

    Page<BoardJsonDTO> getBoardList(Long id, Pageable pageable);

    Page<BoardJsonDTO> getExploreBoard(Pageable pageable);

    /*BoardJsonDTO getModalBoard(Long boardId, SignUpDTO userDTO);*/
}

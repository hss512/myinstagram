package project.myinstagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.myinstagram.entity.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findALlByUserId(Long id);
}

package project.myinstagram.repository.like;

import org.springframework.data.jpa.repository.JpaRepository;
import project.myinstagram.entity.Likes;

import java.util.List;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    List<Likes> findLikesByUserId(Long userId);

    Likes findLikesByBoardIdAndUserId(Long boardId, Long userId);

    List<Likes> findAllByBoardId(Long boardId);
}

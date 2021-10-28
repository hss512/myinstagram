package project.myinstagram.repository.like;

import org.springframework.data.jpa.repository.JpaRepository;
import project.myinstagram.entity.Likes;

public interface LikeRepository extends JpaRepository<Likes, Long> {
}

package project.myinstagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.myinstagram.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}

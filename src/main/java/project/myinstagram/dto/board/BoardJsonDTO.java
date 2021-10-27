package project.myinstagram.dto.board;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import project.myinstagram.dto.user.UserDTO;
import project.myinstagram.entity.User;

import java.time.LocalDateTime;

@Data
public class BoardJsonDTO{

    private Long id;

    private String imageUrl;

    private String content;

    private UserDTO userDTO;

    private Long likeCount;

    private LocalDateTime createDate;

    @QueryProjection
    public BoardJsonDTO(Long id, String imageUrl, String content, User user, Long likeCount, LocalDateTime createDate){
        this.id = id;
        this.imageUrl = imageUrl;
        this.content = content;
        this.userDTO = user.toDTO();
        this.likeCount = likeCount;
        this.createDate = createDate;
    }
}

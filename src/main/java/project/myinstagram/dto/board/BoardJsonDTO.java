package project.myinstagram.dto.board;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import project.myinstagram.dto.user.UserDTO;
import project.myinstagram.entity.Reply;
import project.myinstagram.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardJsonDTO{

    private Long id;

    private String imageUrl;

    private String content;

    private UserDTO userDTO;

    private Integer likeCount;

    private LocalDateTime createDate;

    private int likeCheck;

    private List<Reply> replyList;

    @QueryProjection
    public BoardJsonDTO(Long id, String imageUrl, String content, User user, Integer likeCount, LocalDateTime createDate, List<Reply> replyList){
        this.id = id;
        this.imageUrl = imageUrl;
        this.content = content;
        this.userDTO = user.toDTO();
        this.likeCount = likeCount;
        this.createDate = createDate;
        this.replyList = replyList;
    }
}

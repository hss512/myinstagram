package project.myinstagram.dto.board;

import lombok.*;
import project.myinstagram.dto.ReplyDTO;
import project.myinstagram.dto.user.UserDTO;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardJsonDTO{

    private Long id;

    private String imageUrl;

    private String content;

    private UserDTO userDTO;

    private Integer likeCount;

    private LocalDateTime createdDate;

    private int likeCheck;

    /*private List<Likes> likesList;*/

    private String time;

    private List<ReplyDTO> replyList;
}

package project.myinstagram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {

    private Long replyId;

    private Long userId;

    private String username;

    private Long boardId;

    private String content;

    private String profileImage;
}

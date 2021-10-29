package project.myinstagram.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import project.myinstagram.dto.ReplyDTO;

import javax.persistence.*;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reply {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User replyUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "board_id")
    private Board replyBoard;

    public ReplyDTO toDTO(Long userId, Long boardId, String username){
        return ReplyDTO.builder()
                .content(content)
                .boardId(boardId)
                .userId(userId)
                .username(username)
                .build();
    }
}

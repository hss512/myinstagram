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
    @Column(name = "reply_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User replyUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board replyBoard;

    public ReplyDTO toDTO(Long userId, Long boardId, String username){
        return ReplyDTO.builder()
                .replyId(id)
                .content(content)
                .boardId(boardId)
                .userId(userId)
                .username(username)
                .build();
    }

    public ReplyDTO toViewDTO(){
        return ReplyDTO
                .builder()
                .replyId(this.id)
                .username(this.replyUser.getUsername())
                .userId(this.replyUser.getId())
                .boardId(this.replyBoard.getId())
                .content(this.content)
                .build();
    }
}

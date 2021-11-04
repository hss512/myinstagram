package project.myinstagram.entity;

import lombok.*;
import project.myinstagram.dto.LikeDTO;

import javax.persistence.*;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "likes_uk",
                        columnNames = {"board_id","user_id"}
                )
        }
)
public class Likes {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public LikeDTO toDTO(){
        return LikeDTO.builder()
                .id(this.id)
                .boardId(this.board.getId())
                .userId(this.user.getId())
                .build();
    }
}
package project.myinstagram.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reply {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reply;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User replyUser;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board replyBoard;
}

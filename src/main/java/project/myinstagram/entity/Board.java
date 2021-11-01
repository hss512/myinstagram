package project.myinstagram.entity;

import lombok.*;
import project.myinstagram.dto.board.BoardJsonDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String imageUrl;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board")
    private List<Likes> likesList = new ArrayList<>();;

    @OneToMany(mappedBy = "replyBoard")
    private List<Reply> replyList = new ArrayList<>();

    public BoardJsonDTO toJsonDTO(){

        return BoardJsonDTO.builder()
                .id(this.id)
                .content(this.content)
                .imageUrl(this.imageUrl)
                .userDTO(this.user.toDTO())
                .likeCount(this.likesList.size())
                .replyList(this.replyList.stream().map(Reply::toViewDTO).collect(Collectors.toList()))
                /*.likesList(this.likesList)*/
                .build();
    }
}

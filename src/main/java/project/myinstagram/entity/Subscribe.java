package project.myinstagram.entity;

import lombok.*;
import project.myinstagram.dto.SubscribeDTO;

import javax.persistence.*;

@Entity
@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "subscribe_uk",
                        columnNames = {"from_user_id","to_user_id"}
                )
        }
)
public class Subscribe extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscribe_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;
// 최대한 단방향 Lazy 전략 사용해보자

    public SubscribeDTO toDTO(){
        return SubscribeDTO.builder()
                .id(this.id)
                .fromUser(this.fromUser.toDTO())
                .toUser(this.toUser.toDTO())
                .build();
    }
}

package project.myinstagram.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.myinstagram.dto.UserDTO;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, length = 20)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    private String sex;

    private String bio;

    private String phoneNumber;

    private String role;

    public UserDTO toDTO(){
        return UserDTO.builder()
                .id(this.id)
                .username(this.username)
                .name(this.name)
                .password(this.password)
                .email(this.email)
                .sex(this.sex)
                .bio(this.bio)
                .phoneNumber(this.phoneNumber)
                .role(this.role)
                .build();
    }
}

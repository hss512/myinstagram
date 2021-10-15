package project.myinstagram.entity;

import lombok.*;
import project.myinstagram.dto.UserDTO;

import javax.persistence.*;

@Entity
@Getter @Setter @Builder
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

    private String profileImage;

    private String sex;

    private String bio;

    private String phoneNumber;

    private String role;
    
    private String website;

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
                .profileImage(this.profileImage)
                .website(this.website)
                .build();
    }
}

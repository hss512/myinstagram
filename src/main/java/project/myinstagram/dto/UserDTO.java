package project.myinstagram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.myinstagram.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    @NotBlank //@Max -> 숫자 타입에서만 사용!
    @Size(min = 2, max = 20)
    private String username;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    @NotBlank @Email(message = "이메일 양식을 지켜주세요")
    private String email;

    private String role;

    private String profileImage;

    private String sex;

    private String bio;

    private String phoneNumber;

    private String website;

    public User toEntity(){
        return User.builder()
                .id(this.id)
                .username(this.username)
                .name(this.name)
                .password(this.password)
                .email(this.email)
                .role(this.role)
                .build();
    }

    public UserDTO userDTO(){
        return UserDTO.builder()
                .id(this.id)
                .username(this.username)
                .name(this.name)
                .build();
    }

    public UserDTO updateUserProfileDTO(){
        return UserDTO.builder()
                .username(this.username)
                .name(this.name)
                .email(this.email)
                .role(this.role)
                .bio(this.bio)
                .website(this.website)
                .build();
    }

    public UserDTO pageUserDTO(){
        return UserDTO.builder()
                .id(this.id)
                .username(this.username)
                .name(this.name)
                .bio(this.bio)
                .website(this.website)
                .build();
    }

    public UserDTO subscribeUserDTO(){
        return UserDTO.builder()
                .id(this.id)
                .username(this.username)
                .name(this.name)
                .profileImage(this.profileImage)
                .build();
    }
}

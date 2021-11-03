package project.myinstagram.dto.user;

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
public class SignUpDTO {

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

    private int followCheck = 0;

    public User toEntity(){
        return User.builder()
                .id(this.id)
                .username(this.username)
                .name(this.name)
                .password(this.password)
                .profileImage(this.profileImage)
                .email(this.email)
                .role(this.role)
                .build();
    }

    public SignUpDTO userDTO(){
        return SignUpDTO.builder()
                .id(this.id)
                .username(this.username)
                .name(this.name)
                .profileImage(this.profileImage)
                .build();
    }

    public SignUpDTO updateUserProfileDTO(){
        return SignUpDTO.builder()
                .username(this.username)
                .name(this.name)
                .email(this.email)
                .role(this.role)
                .bio(this.bio)
                .website(this.website)
                .build();
    }

    public SignUpDTO pageUserDTO(){
        return SignUpDTO.builder()
                .id(this.id)
                .username(this.username)
                .profileImage(this.profileImage)
                .name(this.name)
                .bio(this.bio)
                .website(this.website)
                .build();
    }

    public SignUpDTO subscribeUserDTO(int check){
        return SignUpDTO.builder()
                .id(this.id)
                .username(this.username)
                .name(this.name)
                .profileImage(this.profileImage)
                .followCheck(check)
                .build();
    }
}

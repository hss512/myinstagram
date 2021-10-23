package project.myinstagram.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    private String username;

    private String name;

    private String profileImage;

    int followCheck;

    public UserDTO subscribeCheckDTO(int check){
        return UserDTO.builder()
                .id(this.id)
                .username(this.username)
                .name(this.name)
                .profileImage(this.profileImage)
                .followCheck(check)
                .build();
    }
}

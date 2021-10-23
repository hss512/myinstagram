package project.myinstagram.dto;

import lombok.Builder;
import lombok.Data;
import project.myinstagram.dto.user.SignUpDTO;
import project.myinstagram.dto.user.UserDTO;

@Data
@Builder
public class SubscribeDTO {

    private Long id;

    private UserDTO fromUser;

    private UserDTO toUser;
}
package project.myinstagram.dto;

import lombok.Builder;
import lombok.Data;
import project.myinstagram.entity.User;

@Data
@Builder
public class SubscribeDTO {



    private int checkFollow;

}
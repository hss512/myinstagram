package project.myinstagram.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BoardDTO {

    private MultipartFile file;

    private String content;
}

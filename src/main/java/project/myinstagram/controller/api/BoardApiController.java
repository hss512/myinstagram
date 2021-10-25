package project.myinstagram.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class BoardApiController {

    @Value("${file.path}")
    private String uploadPath;

    @GetMapping("/board/image")
    public ResponseEntity<byte[]> getImage(String username, String fileName, String boardId){

        ResponseEntity<byte[]> image = null;

        try{
            String decodeFileName = URLDecoder.decode(fileName, "UTF-8");

            log.info("fileName : " + decodeFileName);

            File file = new File(uploadPath + username + File.separator + boardId + File.separator + decodeFileName);

            log.info("file: " + file);

            HttpHeaders header = new HttpHeaders();

            header.add("Content-Type", Files.probeContentType(file.toPath()));

            image = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);


        } catch (Exception e) {

            try {
                log.error(e.getMessage());

                File file = new File(uploadPath + "non.jpg");

                HttpHeaders header = new HttpHeaders();

                header.add("Content-Type", Files.probeContentType(file.toPath()));

                return new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);

            } catch (Exception exp) {
                log.error(exp.getMessage());
            }
        }
        return image;
    }
}

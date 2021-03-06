package project.myinstagram.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.myinstagram.dto.user.UserDTO;
import project.myinstagram.principal.CustomUserDetails;
import project.myinstagram.dto.user.SignUpDTO;
import project.myinstagram.dto.ValidateDTO;
import project.myinstagram.service.UserService;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Log4j2
public class UserApiController {

    private final UserService userService;

    @Value("${file.path}")
    private String uploadPath;

    @PutMapping("/user/{id}")
    public ResponseEntity<?> userProfileUpdate(@Validated SignUpDTO userDTO,
                                    BindingResult bindingResult,
                                    @AuthenticationPrincipal CustomUserDetails userDetails){


        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }
        SignUpDTO updateUser = userService.userUpdate(userDetails.getUserDTO().getId(), userDTO);

        userDetails.setUserDTO(updateUser);

        return new ResponseEntity<>(new ValidateDTO<>(1, "??????", updateUser.updateUserProfileDTO()), HttpStatus.OK);
    }

    @PutMapping("/userImage/{id}")
    public ResponseEntity<?> userImageUpdate(MultipartFile profileImageFile,
                                             @PathVariable Long id,
                                             @AuthenticationPrincipal CustomUserDetails userDetails)/* throws IOException*/ {

        System.out.println("formData : " + profileImageFile);

        String uploadFileName = userService.userImageUpdate(id, profileImageFile);

        return new ResponseEntity<>(new ValidateDTO<>(1, "??????", uploadFileName), HttpStatus.OK);
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> getImage(String username,
                                           String fileName){

        ResponseEntity<byte[]> image = null;

        try{
            String decodeFileName = URLDecoder.decode(fileName, "UTF-8");

            File file = new File(uploadPath + username + File.separator + decodeFileName);

            HttpHeaders header = new HttpHeaders();

            header.add("Content-Type", Files.probeContentType(file.toPath()));

            image = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);


        } catch (Exception e) {
            try {
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

    @GetMapping("/search/{searchText}")
    public ResponseEntity<?> getUserList(@PathVariable("searchText") String searchText,
                                         @AuthenticationPrincipal CustomUserDetails userDetails){

        List<UserDTO> userList = userService.getUserList(searchText, userDetails.getUserDTO().getId());

        if(userList != null) {
            return new ResponseEntity<>(new ValidateDTO<>(1, "??????", userList), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new ValidateDTO<>(0, "?????? ????????? ????????????", null), HttpStatus.OK);
        }
    }
}

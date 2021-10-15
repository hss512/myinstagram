package project.myinstagram.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.myinstagram.auth.principal.CustomUserDetails;
import project.myinstagram.dto.UserDTO;
import project.myinstagram.dto.ValidateDTO;
import project.myinstagram.service.UserService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserApiController {

    private final UserService userService;

    @PutMapping("/user/{id}")
    public ResponseEntity<?> userProfileUpdate(@Validated UserDTO userDTO,
                                    @AuthenticationPrincipal CustomUserDetails userDetails){

        System.out.println("userDTO = " + userDTO);

        UserDTO updateUser = userService.userUpdate(userDetails.getUserDTO().getId(), userDTO);

        userDetails.setUserDTO(updateUser);

        return new ResponseEntity<>(new ValidateDTO<>(1, "성공", updateUser.profileUserDTO()), HttpStatus.OK);
    }
}

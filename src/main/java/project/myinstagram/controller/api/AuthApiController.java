package project.myinstagram.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.myinstagram.dto.user.SignUpDTO;
import project.myinstagram.dto.ValidateDTO;
import project.myinstagram.service.AuthService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthApiController {

    private final AuthService authService;

    @PostMapping ("/api/idCheck")
    public ResponseEntity<?> idCheck(@RequestBody SignUpDTO userDTO){

        String username = userDTO.getUsername();

        int idCheck = authService.idCheck(username);

        return new ResponseEntity<>(new ValidateDTO<>(1, "성공", idCheck), HttpStatus.OK);
    }
}

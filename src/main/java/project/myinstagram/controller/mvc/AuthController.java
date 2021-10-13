package project.myinstagram.controller.mvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import project.myinstagram.dto.UserDTO;
import project.myinstagram.exception.ValidationException;
import project.myinstagram.service.AuthService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/signin")
    public String signin(){
        return "/auth/signin";
    }

    @GetMapping("/auth/signup")
    public String signup(){
        return "/auth/signup";
    }

    @PostMapping("/auth/signup")
    public String join(@Validated UserDTO userDTO, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
                log.error(error.getDefaultMessage());
                log.error(userDTO.toString());
            }
            throw new ValidationException("회원가입 에러", errorMap);
        }else{
            authService.signUp(userDTO);
            return "redirect:/auth/signin";
        }
    }
}

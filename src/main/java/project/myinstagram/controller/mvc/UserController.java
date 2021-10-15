package project.myinstagram.controller.mvc;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import project.myinstagram.auth.principal.CustomUserDetails;
import project.myinstagram.dto.UserDTO;
import project.myinstagram.service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/{id}")
    public String userPage(@PathVariable Long id,
                           @AuthenticationPrincipal CustomUserDetails userDetails,
                           Model model){

        model.addAttribute("userDetails", userDetails.getUserDTO().userDTO());

        return "/user/profile";
    }

    @GetMapping("/user/update")
    public String userProfilePage(@AuthenticationPrincipal CustomUserDetails userDetails,
                             Model model){

        model.addAttribute("userDetails", userDetails.getUserDTO());

        return "/user/update";
    }
}

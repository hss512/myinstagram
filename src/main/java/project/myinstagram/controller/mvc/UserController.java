package project.myinstagram.controller.mvc;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.myinstagram.auth.principal.PrincipalDetails;

@Controller
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){

        model.addAttribute("principal", principalDetails.getUserDTO());

        return "/board/story";
    }
}

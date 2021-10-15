package project.myinstagram.controller.mvc;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.myinstagram.auth.principal.CustomUserDetails;

@Controller
@RequiredArgsConstructor
public class BoardController {

    @GetMapping({"/", "/story"})
    public String home(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){

        model.addAttribute("userDetails", customUserDetails.getUserDTO().userDTO());

        return "/board/story";
    }

}

package project.myinstagram.controller.mvc;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.myinstagram.principal.CustomUserDetails;
import project.myinstagram.dto.UserDTO;
import project.myinstagram.entity.Board;
import project.myinstagram.entity.Subscribe;
import project.myinstagram.repository.UserRepository;
import project.myinstagram.service.BoardService;
import project.myinstagram.service.SubscribeService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final SubscribeService subscribeService;
    private final BoardService boardService;

    @GetMapping("/user/{id}")
    public String userPage(@PathVariable Long id,
                           @AuthenticationPrincipal CustomUserDetails userDetails,
                           Model model){

        UserDTO pageUserDTO = userRepository.findById(id).get().toDTO().pageUserDTO();
        List<Subscribe> subscribeList = subscribeService.getSubscribeList(pageUserDTO.getId());
        List<Board> boardList = boardService.getBoardList(id);


        model.addAttribute("userDetails", userDetails.getUserDTO().userDTO());
        model.addAttribute("pageUser", pageUserDTO);
        model.addAttribute("subscribeSize", subscribeList.size());
        model.addAttribute("boardSize", boardList.size());

        return "/user/profile";
    }

    @GetMapping("/user/update")
    public String userProfilePage(@AuthenticationPrincipal CustomUserDetails userDetails,
                             Model model){

        model.addAttribute("userDetails", userDetails.getUserDTO());

        return "/user/update";
    }
}

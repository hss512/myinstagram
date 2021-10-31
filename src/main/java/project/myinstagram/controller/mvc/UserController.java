package project.myinstagram.controller.mvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.myinstagram.principal.CustomUserDetails;
import project.myinstagram.dto.user.SignUpDTO;
import project.myinstagram.entity.Board;
import project.myinstagram.entity.Subscribe;
import project.myinstagram.repository.subscribe.SubscribeRepository;
import project.myinstagram.repository.user.UserRepository;
import project.myinstagram.service.BoardService;
import project.myinstagram.service.SubscribeService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final SubscribeService subscribeService;
    private final BoardService boardService;
    private final SubscribeRepository subscribeRepository;

    @GetMapping("/user/{id}")
    public String userPage(@PathVariable Long id,
                           @AuthenticationPrincipal CustomUserDetails userDetails,
                           Model model){

        SignUpDTO pageUserDTO = userRepository.findById(id).get().toSignUpDTO().pageUserDTO();
        /*List<Subscribe> subscribeList = subscribeService.getSubscribeList(pageUserDTO.getId());*/
        List<Subscribe> subscribeList = subscribeRepository.findAllByFromUserId(pageUserDTO.getId());
        List<Board> boardList = boardService.getMyBoardList(id);
        int followCheck = subscribeService.followCheck(id, userDetails.getUserDTO().getId().toString());


        model.addAttribute("userDetails", userDetails.getUserDTO().userDTO());
        model.addAttribute("pageUser", pageUserDTO);
        model.addAttribute("subscribeSize", subscribeList.size());
        model.addAttribute("boardList", boardList);
        model.addAttribute("boardSize", boardList.size());
        model.addAttribute("followCheck", followCheck);

        return "/user/profile";
    }

    @GetMapping("/user/update")
    public String userProfilePage(@AuthenticationPrincipal CustomUserDetails userDetails,
                             Model model){

        model.addAttribute("userDetails", userDetails.getUserDTO());

        return "/user/update";
    }
}

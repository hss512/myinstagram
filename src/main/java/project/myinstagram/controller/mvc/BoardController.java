package project.myinstagram.controller.mvc;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import project.myinstagram.dto.board.BoardDTO;
import project.myinstagram.principal.CustomUserDetails;
import project.myinstagram.service.BoardService;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping({"/", "/story"})
    public String home(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){

        model.addAttribute("userDetails", customUserDetails.getUserDTO());

        return "/board/story";
    }

    @GetMapping("/board/upload")
    public String board(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){

        model.addAttribute("userDetails", customUserDetails.getUserDTO());

        return "/board/upload";
    }

    @PostMapping("/board")
    public String boardUpload(BoardDTO boardDTO, @AuthenticationPrincipal CustomUserDetails userDetails, Model model){

        boardService.boardUpload(boardDTO, userDetails.getUserDTO());

        model.addAttribute("userDetail", userDetails);

        return "redirect:/user/" + userDetails.getUserDTO().getId();
    }

}

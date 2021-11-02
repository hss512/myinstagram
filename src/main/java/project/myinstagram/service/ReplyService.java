package project.myinstagram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.myinstagram.dto.ReplyDTO;
import project.myinstagram.dto.user.SignUpDTO;
import project.myinstagram.entity.Board;
import project.myinstagram.entity.Reply;
import project.myinstagram.entity.User;
import project.myinstagram.repository.board.BoardRepository;
import project.myinstagram.repository.reply.ReplyRepository;
import project.myinstagram.repository.user.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyService {

    private final ReplyRepository replyRepository;

    private final BoardRepository boardRepository;

    public ReplyDTO postReply(String boardId, SignUpDTO userDTO, String content) {

        User findUser = userDTO.toEntity();

        Board findBoard = boardRepository.findById(Long.parseLong(boardId)).get();

        Reply reply = Reply.builder()
                .replyBoard(findBoard)
                .content(content)
                .replyUser(findUser)
                .build();

        Reply savedReply = replyRepository.save(reply);

        return savedReply.toDTO(findUser.getId(), findBoard.getId(), findUser.getUsername());
    }

    public int deleteReply(Long replyId, Long userId) {

        Reply findReply = replyRepository.findById(replyId).get();

        if(findReply.getReplyUser().getId().equals(userId)){
            replyRepository.delete(findReply);
            return 1;
        }else{
            return 0;
        }
    }
}

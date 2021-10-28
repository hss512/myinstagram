package project.myinstagram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.myinstagram.dto.user.SignUpDTO;
import project.myinstagram.dto.user.UserDTO;
import project.myinstagram.entity.Board;
import project.myinstagram.entity.Likes;
import project.myinstagram.repository.board.BoardRepository;
import project.myinstagram.repository.like.LikeRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    private final BoardRepository boardRepository;

    public int boardLike(String boardId, SignUpDTO userDTO) {

        Board findBoard = boardRepository.findById(Long.parseLong(boardId)).get();

        Likes newLike = Likes.builder()
                .user(userDTO.toEntity())
                .board(findBoard)
                .build();

        Likes savedLike = likeRepository.save(newLike);

        if (savedLike!=null){
            return 1;
        }else
            return 0;
    }
}
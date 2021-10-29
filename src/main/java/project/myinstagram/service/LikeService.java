package project.myinstagram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.myinstagram.dto.user.SignUpDTO;
import project.myinstagram.entity.Board;
import project.myinstagram.entity.Likes;
import project.myinstagram.repository.board.BoardRepository;
import project.myinstagram.repository.like.LikeRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
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

    @Transactional(readOnly = true)
    public List<Likes> likeCheck(Long userId){

        return likeRepository.findLikesByUserId(userId);
    }

    public void boardLikeCancel(String boardId, SignUpDTO userDTO){

        Likes findLike = likeRepository.findLikesByBoardIdAndUserId(Long.parseLong(boardId), userDTO.getId());

        log.info("findLike={}",findLike.getId());

        likeRepository.delete(findLike);
    }

    public int getBoardLikes(Long boardId){
        List<Likes> findLikeList = likeRepository.findAllByBoardId(boardId);

        int size = findLikeList.size();

        return size;
    }
}
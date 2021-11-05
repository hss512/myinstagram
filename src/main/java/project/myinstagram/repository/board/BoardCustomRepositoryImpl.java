package project.myinstagram.repository.board;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import project.myinstagram.dto.board.BoardJsonDTO;
import project.myinstagram.entity.Board;
import project.myinstagram.entity.QLikes;
import project.myinstagram.entity.QReply;
import project.myinstagram.entity.QUser;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static project.myinstagram.entity.QBoard.board;
import static project.myinstagram.entity.QLikes.*;
import static project.myinstagram.entity.QReply.*;
import static project.myinstagram.entity.QSubscribe.subscribe;
import static project.myinstagram.entity.QUser.*;

public class BoardCustomRepositoryImpl implements BoardCustomRepository{

    private final JPAQueryFactory queryFactory;

    public BoardCustomRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BoardJsonDTO> getBoardList(Long id, Pageable pageable) {

        QueryResults<Board> boardResult = queryFactory
                .select(board)
                .from(board)
                .leftJoin(subscribe).on(subscribe.fromUser.id.eq(id))
                .leftJoin(board.replyList, reply)
                .leftJoin(board.likesList, likes)
                .where(board.user.id.eq(id).or(board.user.id.eq(subscribe.toUser.id)))
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.createdDate.desc())
                .fetchResults();

        List<BoardJsonDTO> boardResults = boardResult.getResults().stream().map(Board::toJsonDTO).collect(Collectors.toList());

        long total = boardResult.getTotal();

        return new PageImpl<>(boardResults, pageable, total);
    }

    @Override
    public Page<BoardJsonDTO> getExploreBoard(Pageable pageable){

        QueryResults<Board> boardResult = queryFactory
                .selectFrom(board)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.likesList.size().desc(), board.createdDate.desc())
                .fetchResults();

        List<Board> boardResults = boardResult.getResults();

        List<BoardJsonDTO> boardList = new ArrayList<>();

        for (Board board : boardResults) {
            Collections.reverse(board.getReplyList());
            BoardJsonDTO boardDTO = board.toJsonDTO();
            boardDTO.setCreatedDate(board.createdDate);
            boardList.add(boardDTO);
        }

        long total = boardResult.getTotal();

        return new PageImpl<>(boardList, pageable, total);
    }
}

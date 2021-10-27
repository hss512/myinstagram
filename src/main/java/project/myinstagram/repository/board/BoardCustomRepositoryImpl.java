package project.myinstagram.repository.board;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import project.myinstagram.dto.board.BoardJsonDTO;
import project.myinstagram.dto.board.QBoardJsonDTO;

import javax.persistence.EntityManager;

import java.util.List;

import static project.myinstagram.entity.QBoard.board;
import static project.myinstagram.entity.QSubscribe.subscribe;

public class BoardCustomRepositoryImpl implements BoardCustomRepository{

    private final JPAQueryFactory queryFactory;

    public BoardCustomRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BoardJsonDTO> getBoardList(Long id, Pageable pageable) {

        QueryResults<BoardJsonDTO> result = queryFactory
                .select(new QBoardJsonDTO(
                        board.id,
                        board.imageUrl,
                        board.content,
                        board.user,
                        board.likeCount,
                        board.createdDate
                ))
                .from(board)
                .where(board.user.id.eq(id).or(board.user.id.eq(subscribe.toUser.id)))
                .join(subscribe).on(subscribe.fromUser.id.eq(id))
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.createdDate.desc())
                .fetchResults();

        List<BoardJsonDTO> boardList = result.getResults();
        long total = result.getTotal();

        return new PageImpl<>(boardList, pageable, total);
    }
}

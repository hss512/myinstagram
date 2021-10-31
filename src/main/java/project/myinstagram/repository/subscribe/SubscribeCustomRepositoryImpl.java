package project.myinstagram.repository.subscribe;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import project.myinstagram.entity.QSubscribe;
import project.myinstagram.entity.QUser;
import project.myinstagram.entity.Subscribe;

import javax.persistence.EntityManager;
import java.util.List;

import static project.myinstagram.entity.QSubscribe.subscribe;
import static project.myinstagram.entity.QUser.user;

public class SubscribeCustomRepositoryImpl implements SubscribeCustomRepository{

    private final JPAQueryFactory queryFactory;

    public SubscribeCustomRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Subscribe> getSubscribeList(Long pageUserId, Pageable pageable){

        QueryResults<Subscribe> results = queryFactory
                .selectFrom(subscribe)
                .where(subscribe.fromUser.id.eq(pageUserId))
                .leftJoin(subscribe.toUser, user)
                .fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Subscribe> subscribeList = results.getResults();

        long total = results.getTotal();

        return new PageImpl<>(subscribeList, pageable, total);
    }

}

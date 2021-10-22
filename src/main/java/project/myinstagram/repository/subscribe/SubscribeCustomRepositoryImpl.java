package project.myinstagram.repository.subscribe;

import com.querydsl.jpa.impl.JPAQueryFactory;
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
    public List<Subscribe> getSubscribeList(Long pageUserId){

        return queryFactory
                .selectFrom(subscribe)
                .where(subscribe.fromUser.id.eq(pageUserId))
                .leftJoin(subscribe.toUser, user)
                .fetchJoin()
                .fetch();
    }

}

package com.umc.mada.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class TodoRespositoryCustomImpl implements TodoRepositoryCustom{
    private JPAQueryFactory jpaQueryFactory;

    public TodoRespositoryCustomImpl(JPAQueryFactory jpaQueryFactory){
        this.jpaQueryFactory = jpaQueryFactory;

    }

    @Override
    public List<StatisticsVO> getStatistics(Long uid, String weekOrMonth, LocalDate endDate, LocalDate startDate) {
//        if(weekOrMonth.equals("week")){
//
//        }else{
//            List<StatisticsVO> result = jpaQueryFactory
//                    .select(Projections.bean(
//                            StatisticsVO.class,
//                            todo.
//                    ))
//                    .from(
//                            selectDateQuery()
//                    )
//                    .groupBy(dateTable.date)
//                    .fetch();
//        }

        return null;
    }
//
//    private JPAQuery<String> selectDateQuery(LocalDate startDate, LocalDate endDate) {
//        QNumTable numTable = QNumTable.numTable;
//
//        JPAQuery<String> query = queryFactory
//                .select(numTable.num.add(startDate).toString())
//                .from(numTable)
//                .where(numTable.num.between(0, endDate.getDayOfMonth()));
//
//        return query;
//    }
}

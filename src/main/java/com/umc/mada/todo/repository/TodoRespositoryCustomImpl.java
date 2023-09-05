package com.umc.mada.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.umc.mada.todo.domain.QTodo;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class TodoRespositoryCustomImpl implements TodoRepositoryCustom{
    private JPAQueryFactory jpaQueryFactory;
    private QTodo todo;

    public TodoRespositoryCustomImpl(JPAQueryFactory jpaQueryFactory){
        this.jpaQueryFactory = jpaQueryFactory;
        this.todo = QTodo.todo;
    }

    @Override
    public List<StatisticsVO> getStatistics(Long uid, String weekOrMonth, LocalDate endDate, LocalDate startDate) {
        List<StatisticsVO> result = null;
//        if (weekOrMonth.equals("week")) {
////            result = jpaQueryFactory
////                    .select()
////                    .from()
////                    .groupBy()
//        } else {
//            result = jpaQueryFactory
//                    .select(Projections.bean(
//                            StatisticsVO.class,
//                            todo.complete.avg()
//                    ))
//                    .from(
//                            selectDateQuery()
//                    )
//                    .groupBy(dateTable.date)
//                    .fetch();
//        }

        return result;
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

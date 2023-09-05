//package com.umc.mada.todo.domain;
//
//import lombok.Getter;
//import org.hibernate.annotations.Immutable;
//import org.hibernate.annotations.Subselect;
//import org.hibernate.annotations.Synchronize;
//
//import javax.persistence.Entity;
//import javax.persistence.Id;
//
//@Entity
//@Subselect(
//        " SELECT num as id,DATE_FORMAT(ADDDATE(:startDate, INTERVAL @num:=@num+1 DAY), '%Y-%m-%d') as dayFormat" +
//                "FROM TODO, (SELECT @num:=-1) num" +
//                "LIMIT 31"
//)
//@Immutable
//@Synchronize("TODO")
//@Getter
//public class DateFrame {
//
//    private Long id;
//
//}

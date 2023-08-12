package com.umc.mada.todo.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter
public class RepeatMonthConverter implements AttributeConverter<RepeatMonth, Integer> {

    @Override
    public Integer convertToDatabaseColumn(RepeatMonth attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getDayOfMonth();
    }

    @Override
    public RepeatMonth convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        for (RepeatMonth repeatMonth : RepeatMonth.values()) {
            if (Objects.equals(repeatMonth.getDayOfMonth(), dbData)) {
                return repeatMonth;
            }
        }
        throw new IllegalArgumentException("dayOfMonth에 해당하는 RepeatMonth enum 상수가 없습니다.");
    }
}
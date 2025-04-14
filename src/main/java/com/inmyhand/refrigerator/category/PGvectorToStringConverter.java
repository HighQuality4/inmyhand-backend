package com.inmyhand.refrigerator.category;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;

@Converter
public class PGvectorToStringConverter implements AttributeConverter<String, Object> {

    @Override
    public Object convertToDatabaseColumn(String attribute) {
        return attribute;  // 그냥 그대로 저장
    }

    @Override
    public String convertToEntityAttribute(Object dbData) {
        if (dbData instanceof PGobject) {
            return ((PGobject) dbData).getValue();  // PGobject → String
        }
        return dbData != null ? dbData.toString() : null;
    }
}


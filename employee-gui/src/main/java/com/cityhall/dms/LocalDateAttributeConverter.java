package com.cityhall.dms;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public String convertToDatabaseColumn(LocalDate date) {
        return (date == null ? null : date.format(FORMATTER));
    }

    @Override
    public LocalDate convertToEntityAttribute(String dbData) {
        return (dbData == null ? null : LocalDate.parse(dbData, FORMATTER));
    }
}

/**
 * Robert Yantin Jr.
 * CEN 3024 - Software Development I
 * November 17, 2025
 * com.cityhall.dms.LocalDateAttributeConverter.java
 *
 * This class provides a JPA AttributeConverter that automatically converts
 * LocalDate objects into Strings when saving to the database, and converts
 * those Strings back into LocalDate objects when reading them.
 *
 * By applying the @Converter(autoApply = true) annotation, this converter
 * is applied to all LocalDate fields in the application without needing to
 * annotate each individual attribute. This ensures consistent, reliable
 * date formatting using the pattern "yyyy-MM-dd".
 */

package com.cityhall.dms;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A JPA AttributeConverter that converts between LocalDate and String
 * for database storage and retrieval, using the "yyyy-MM-dd" format.
 */
@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Converts a LocalDate into a String for storing in the database.
     *
     * @param date the LocalDate to convert
     * @return the formatted date String, or null if the date is null
     */
    @Override
    public String convertToDatabaseColumn(LocalDate date) {
        return (date == null ? null : date.format(FORMATTER));
    }

    /**
     * Converts a date String from the database back into a LocalDate.
     *
     * @param dbData the stored date String
     * @return a LocalDate instance, or null if the stored value is null
     */
    @Override
    public LocalDate convertToEntityAttribute(String dbData) {
        return (dbData == null ? null : LocalDate.parse(dbData, FORMATTER));
    }
}
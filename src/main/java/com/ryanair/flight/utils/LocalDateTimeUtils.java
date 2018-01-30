package com.ryanair.flight.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeUtils {

    /**
     * Parse a string to a valid LocalDateTime
     * 
     * @param dateTime
     *            string to parse
     * @return Parsed string on ISO_LOCAL_DATE_TIME format
     * @throws DateTimeParseException
     *             if the string is not a valid date with ISO_LOCAL_DATE_TIME
     *             format
     */

    public static LocalDateTime parseStringToLocalDateTime(String dateTime) throws DateTimeParseException {
	return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * Parses a string to a valid LocalTime
     * 
     * @param time
     *            string to parse
     * @return Parsed string on ISO_TIME format
     * @throws DateTimeParseException
     *             if the string is not a valid time with ISO_TIME format
     */
    public static LocalTime parseStringToLocalTime(String time) throws DateTimeParseException {
	return LocalTime.parse(time, DateTimeFormatter.ISO_TIME);
    }

}

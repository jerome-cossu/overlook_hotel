package com.example.overlook_hotel.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public final class DateUtils {
    private DateUtils() {}

    public static long nightsBetween(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) return 0;
        return Math.max(0, ChronoUnit.DAYS.between(checkIn, checkOut));
    }

    public static Optional<LocalDate> safeParse(String isoDate) {
        if (isoDate == null || isoDate.isBlank()) return Optional.empty();
        try {
            return Optional.of(LocalDate.parse(isoDate));
        } catch (DateTimeParseException ex) {
            return Optional.empty();
        }
    }

    public static boolean validRange(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) return false;
        return checkOut.isAfter(checkIn);
    }
}

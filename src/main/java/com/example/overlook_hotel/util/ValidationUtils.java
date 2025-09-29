package com.example.overlook_hotel.util;

import java.util.Objects;

public final class ValidationUtils {

    private ValidationUtils() {}

    public static <T> T requireNonNull(T obj, String message) {
        if (Objects.isNull(obj)) throw new IllegalArgumentException(message);
        return obj;
    }

    public static String requireNonBlank(String s, String message) {
        if (s == null || s.isBlank()) throw new IllegalArgumentException(message);
        return s;
    }

    public static void requireTrue(boolean condition, String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }
}

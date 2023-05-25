package com.example.proyectoviewnext.database.converter;

import androidx.room.TypeConverter;

import java.time.LocalDate;

public class LocalDateConverter {
    private LocalDateConverter() {
        // Constructor privado requerido
    }

    @TypeConverter
    public static LocalDate toDate(String dateString) {
        if (dateString == null) {
            return null;
        } else {
            return LocalDate.parse(dateString);
        }
    }

    @TypeConverter
    public static String toDateString(LocalDate date) {
        if (date == null) {
            return null;
        } else {
            return date.toString();
        }
    }
}

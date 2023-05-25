package com.example.proyectoviewnext.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.proyectoviewnext.database.converter.LocalDateConverter;
import com.example.proyectoviewnext.database.dao.InvoiceDAO;
import com.example.proyectoviewnext.invoice.InvoiceVO;

@Database(entities = {
        InvoiceVO.class
}, version = 1)
@TypeConverters({LocalDateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public static AppDatabase instance;

    public abstract InvoiceDAO invoiceDAO();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "invoices.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}

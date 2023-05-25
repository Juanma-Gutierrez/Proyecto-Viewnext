package com.example.proyectoviewnext.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.proyectoviewnext.invoice.InvoiceVO;

import java.util.List;

@Dao
public interface InvoiceDAO {
    @Query("SELECT * FROM InvoiceVO")
    List<InvoiceVO> getAll();

    @Insert
    void insertAll(InvoiceVO ... invoiceVOS);

    @Query("DELETE FROM InvoiceVO")
    void deleteAll();

    @Query("DELETE FROM sqlite_sequence WHERE name='InvoiceVO'")
    void resetID();

    @Query("SELECT COUNT(1) FROM InvoiceVO")
    int getSize();
}

package com.example.proyectoviewnext.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.proyectoviewnext.invoice.InvoiceVO;

import java.util.List;

@Dao
public interface InvoiceDAO {
    @Query("select * from InvoiceVO")
    List<InvoiceVO> getAll();

    @Insert
    void insertAll(InvoiceVO ... invoiceVOS);
}

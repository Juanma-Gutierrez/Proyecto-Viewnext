package com.example.proyectoviewnext.database.repository;

import com.example.proyectoviewnext.invoice.InvoiceVO;

import java.util.List;

public interface InvoiceVORepository {
    List<InvoiceVO> getAllItems();
    void insertInvoiceVO(InvoiceVO invoiceVO);

}

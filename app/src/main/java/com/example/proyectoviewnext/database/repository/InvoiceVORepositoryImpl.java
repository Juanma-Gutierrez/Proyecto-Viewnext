package com.example.proyectoviewnext.database.repository;

import com.example.proyectoviewnext.database.dao.InvoiceDAO;
import com.example.proyectoviewnext.invoice.InvoiceVO;

import java.util.List;

public class InvoiceVORepositoryImpl implements InvoiceVORepository {

    InvoiceDAO dao;

    public InvoiceVORepositoryImpl(InvoiceDAO dao) {
        this.dao = dao;
    }

    @Override
    public List<InvoiceVO> getAllItems() {
        return dao.getAll();
    }

    @Override
    public void insertInvoiceVO(InvoiceVO invoiceVO) {
        dao.insertAll(invoiceVO);
    }

    public void deleteAll(){
        dao.deleteAll();
    }

    public void resetID(){
        dao.resetID();
    }

    public int getSize(){
        return dao.getSize();
    }
}

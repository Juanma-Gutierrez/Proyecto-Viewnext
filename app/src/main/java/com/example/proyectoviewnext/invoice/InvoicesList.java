package com.example.proyectoviewnext.invoice;

import java.util.List;

public class InvoicesList {
    private int numFacturas;
    private List<InvoiceVO> facturas;

    public InvoicesList(int numFacturas, List<InvoiceVO> facturas) {
        this.numFacturas = numFacturas;
        this.facturas = facturas;
    }

    public int getNumFacturas() {
        return numFacturas;
    }

    public void setNumFacturas(int numFacturas) {
        this.numFacturas = numFacturas;
    }

    public List<InvoiceVO> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<InvoiceVO> facturas) {
        this.facturas = facturas;
    }
}

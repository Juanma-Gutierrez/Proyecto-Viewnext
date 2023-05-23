package com.example.proyectoviewnext.invoice;

import java.time.LocalDate;

public class InvoiceVO {
    private LocalDate fecha;
    private String descEstado;
    private double importeOrdenacion;

    public InvoiceVO(LocalDate fecha, String descEstado, double importeOrdenacion) {
        this.fecha = fecha;
        this.descEstado = descEstado;
        this.importeOrdenacion = importeOrdenacion;
    }

    public LocalDate getDate() {
        return fecha;
    }

    public void setDate(LocalDate date) {
        this.fecha = date;
    }

    public String getDescEstado() {
        return descEstado;
    }

    public void setDescEstado(String status) {
        this.descEstado = status;
    }

    public double getImporteOrdenacion() {
        return importeOrdenacion;
    }

    public void setImporteOrdenacion(double importeOrdenacion) {
        this.importeOrdenacion = importeOrdenacion;
    }
}

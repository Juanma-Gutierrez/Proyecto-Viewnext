package com.example.proyectoviewnext.invoice;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "InvoiceVO")
public class InvoiceVO {
    @PrimaryKey(autoGenerate = true)
    private int invoiceID;
    @ColumnInfo
    private LocalDate fecha;
    @ColumnInfo
    private String descEstado;
    @ColumnInfo
    private double importeOrdenacion;

    public InvoiceVO() {
    }

    public InvoiceVO(LocalDate fecha, String descEstado, double importeOrdenacion) {
        this.fecha = fecha;
        this.descEstado = descEstado;
        this.importeOrdenacion = importeOrdenacion;
    }

    public int getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(int invoiceID) {
        this.invoiceID = invoiceID;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate date) {
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

package com.example.proyectoviewnext.invoice;

import com.example.proyectoviewnext.utils.AppConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

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

/*    public LocalDate getDateAsDate() {
        Date newDate = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat(AppConstants.API_DATE_FORMAT);
            newDate = format.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }*/

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

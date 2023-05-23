package com.example.proyectoviewnext.invoice;

import com.example.proyectoviewnext.utils.AppConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InvoiceVO {
    private String date;
    private String descEstado;
    private double importeOrdenacion;

    public InvoiceVO(String date, String descEstado, double importeOrdenacion) {
        this.date = date;
        this.descEstado = descEstado;
        this.importeOrdenacion = importeOrdenacion;
    }

    public String getDate() {
        return date;
    }

    public Date getDateAsDate() {
        Date newDate = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat(AppConstants.API_DATE_FORMAT);
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public void setDate(String date) {
        this.date = date;
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

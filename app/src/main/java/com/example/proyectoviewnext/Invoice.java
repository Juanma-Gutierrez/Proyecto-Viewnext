package com.example.proyectoviewnext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Invoice {
    private String date;
    private String status;
    private double amount;

    public Invoice(String date, String status, double amount) {
        this.date = date;
        this.status = status;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public Date getDateAsDate() {
        Date new_date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            new_date = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new_date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

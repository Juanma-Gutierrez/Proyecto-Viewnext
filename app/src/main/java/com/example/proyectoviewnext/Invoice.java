package com.example.proyectoviewnext;

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

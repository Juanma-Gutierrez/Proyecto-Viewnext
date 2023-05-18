package com.example.proyectoviewnext.filter;

import java.util.Date;

public class Filter {
    private Date dateFrom;
    private Date dateUntil;
    private Date dateFromTemp;
    private Date dateUntilTemp;
    private int amountSelected;

    private double maxAmount;
    private boolean paid;
    private boolean cancelled;
    private boolean fixedFee;
    private boolean pendingPayment;
    private boolean paymentPlan;

    public Filter() {
        this.dateFrom = null;
        this.dateUntil = null;
        this.amountSelected = getAmountSelected();
        this.paid = false;
        this.cancelled = false;
        this.fixedFee = false;
        this.pendingPayment = false;
        this.paymentPlan = false;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateUntil() {
        return dateUntil;
    }

    public void setDateUntil(Date dateUntil) {
        this.dateUntil = dateUntil;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public int getAmountSelected() {
        return amountSelected;
    }

    public void setAmountSelected(int amountSelected) {
        this.amountSelected = amountSelected;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isFixedFee() {
        return fixedFee;
    }

    public void setFixedFee(boolean fixedFee) {
        this.fixedFee = fixedFee;
    }

    public boolean isPendingPayment() {
        return pendingPayment;
    }

    public void setPendingPayment(boolean pendingPayment) {
        this.pendingPayment = pendingPayment;
    }

    public boolean isPaymentPlan() {
        return paymentPlan;
    }

    public void setPaymentPlan(boolean paymentPlan) {
        this.paymentPlan = paymentPlan;
    }

    public Date getDateFromTemp() {
        return dateFromTemp;
    }

    public void setDateFromTemp(Date dateFromTemp) {
        this.dateFromTemp = dateFromTemp;
    }

    public Date getDateUntilTemp() {
        return dateUntilTemp;
    }

    public void setDateUntilTemp(Date dateUntilTemp) {
        this.dateUntilTemp = dateUntilTemp;
    }

    public void resetFilter() {
        this.dateFrom = null;
        this.dateFromTemp = null;
        this.dateUntil = null;
        this.dateUntilTemp = null;
        this.amountSelected = (int) getMaxAmount();
        this.paid = false;
        this.cancelled = false;
        this.fixedFee = false;
        this.pendingPayment = false;
        this.paymentPlan = false;
    }
}

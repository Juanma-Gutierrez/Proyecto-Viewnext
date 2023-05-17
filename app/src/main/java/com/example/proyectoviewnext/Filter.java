package com.example.proyectoviewnext;

import java.util.Date;

public class Filter {
    private Date date_from;
    private Date date_until;
    private Date date_from_temp;
    private Date date_until_temp;
    private int amount_selected;

    private double max_amount;
    private boolean paid;
    private boolean cancelled;
    private boolean fixed_fee;
    private boolean pending_payment;
    private boolean payment_plan;

    public Filter() {
        this.date_from = null;
        this.date_until = null;
        this.amount_selected = getAmount_selected();
        this.paid = false;
        this.cancelled = false;
        this.fixed_fee = false;
        this.pending_payment = false;
        this.payment_plan = false;
    }

    public Date getDate_from() {
        return date_from;
    }

    public void setDate_from(Date date_from) {
        this.date_from = date_from;
    }

    public Date getDate_until() {
        return date_until;
    }

    public void setDate_until(Date date_until) {
        this.date_until = date_until;
    }

    public double getMax_amount() {
        return max_amount;
    }

    public void setMax_amount(double max_amount) {
        this.max_amount = max_amount;
    }

    public int getAmount_selected() {
        return amount_selected;
    }

    public void setAmount_selected(int amount_selected) {
        this.amount_selected = amount_selected;
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

    public boolean isFixed_fee() {
        return fixed_fee;
    }

    public void setFixed_fee(boolean fixed_fee) {
        this.fixed_fee = fixed_fee;
    }

    public boolean isPending_payment() {
        return pending_payment;
    }

    public void setPending_payment(boolean pending_payment) {
        this.pending_payment = pending_payment;
    }

    public boolean isPayment_plan() {
        return payment_plan;
    }

    public void setPayment_plan(boolean payment_plan) {
        this.payment_plan = payment_plan;
    }

    public Date getDate_from_temp() {
        return date_from_temp;
    }

    public void setDate_from_temp(Date date_from_temp) {
        this.date_from_temp = date_from_temp;
    }

    public Date getDate_until_temp() {
        return date_until_temp;
    }

    public void setDate_until_temp(Date date_until_temp) {
        this.date_until_temp = date_until_temp;
    }

    public void resetFilter() {
        this.date_from = null;
        this.date_from_temp = null;
        this.date_until = null;
        this.date_until_temp = null;
        this.amount_selected = (int) getMax_amount();
        this.paid = false;
        this.cancelled = false;
        this.fixed_fee = false;
        this.pending_payment = false;
        this.payment_plan = false;
    }
}

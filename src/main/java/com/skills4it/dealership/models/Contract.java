package com.skills4it.dealership.models;

public abstract class Contract {
    private String date;
    private String customerName;
    private String customerEmail;
    private String dateSold;
    private String vehicleSold;
    private int totalPrice;
    private int monthlyPayment;

    public Contract(String date, String customerName, String customerEmail, String dateSold, String vehicleSold, int totalPrice, int monthlyPayment) {
        this.date = date;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.dateSold = dateSold;
        this.vehicleSold = vehicleSold;
        this.totalPrice = totalPrice;
        this.monthlyPayment = monthlyPayment;
    }

    public int getMonthlyPayment() {
        return monthlyPayment;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}

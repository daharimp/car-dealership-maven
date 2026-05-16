package com.skills4it.dealership.models;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public abstract class Contract {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private String contractDate;
    private String customerName;
    private String customerEmail;
    private boolean isVehicleSold;
    private Vehicle vehicleSold;
    private double totalPrice;
    private double monthlyPayment;

    public Contract(String contractDate, String customerName,
                    String customerEmail, Vehicle vehicleSold) {
        this.contractDate = Objects.requireNonNull(contractDate, "contractDate");
        this.customerName = Objects.requireNonNull(customerName, "customerName").trim();
        this.customerEmail = Objects.requireNonNull(customerEmail, "customerEmail").trim();
        this.vehicleSold = Objects.requireNonNull(vehicleSold, "vehicleSold");
        this.isVehicleSold = true;
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(String contractDate) {
        this.contractDate = contractDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public boolean isVehicleSold() {
        return isVehicleSold;
    }

    public void setVehicleSold(boolean vehicleSold) {
        isVehicleSold = vehicleSold;
    }

    public Vehicle getVehicleSold() {
        return vehicleSold;
    }

    public void setVehicleSold(Vehicle vehicleSold) {
        this.vehicleSold = vehicleSold;
    }

    public abstract double getMonthlyPayment();

    public abstract double getTotalPrice();

    /**
     * Template Method:
     * Subclasses provide their type label and their type-specific fields;
     *  the common header columns are formatted here once.
     */

    public final String toCsvLine() {
        Vehicle v = getVehicleSold();
        return String.join("|",
                getTypeLabel(),
                contractDate,
                customerName,
                customerEmail,
                String.valueOf(v.getVin()),
                String.valueOf(v.getYear()),
                v.getMake(),
                v.getModel(),
                v.getVehicleType().getDisplayName(),
                v.getColor(),
                String.valueOf(v.getOdometer()),
                formatMoney(v.getPrice()),
                getSpecificCsvFields());
    }

    protected abstract String getTypeLabel();

    protected abstract String getSpecificCsvFields();

    protected static String formatMoney(double amount) {
        return String.format(Locale.US, "%.2f", amount);
    }

}

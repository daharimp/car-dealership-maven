package com.skills4it.dealership.models;

import java.time.Year;

public class LeaseContract extends Contract {

    private static final double EXPECTED_ENDING_VALUE_RATE = 0.50;
    private static final double LEASE_FEE_RATE = 0.07;
    private static final double LEASE_APR = 0.04;
    private static final int LEASE_TERM_MONTHS = 36;
    private static final int MAX_VEHICLE_AGE_YEARS = 15;

    private double expectedEndingValue;
    private double leaseFee;

    public LeaseContract(String contractDate,
                         String customerName, String customerEmail,
                         Vehicle vehicleSold) {
        super(contractDate, customerName, customerEmail, vehicleSold);
        requireEligibleForLease(vehicleSold);
        double vehiclePrice = vehicleSold.getPrice();
        this.expectedEndingValue = vehiclePrice * EXPECTED_ENDING_VALUE_RATE;
        this.leaseFee = vehiclePrice * LEASE_FEE_RATE;
    }

    private static void requireEligibleForLease(Vehicle vehicle) {
        int currentYear = Year.now().getValue();
        int age = currentYear - vehicle.getYear();
        if (age > MAX_VEHICLE_AGE_YEARS) {


            throw new LeaseEligibilityException(
                    "Vehicle (VIN " + vehicle.getVin() + ", year " + vehicle.getYear()
                            + ") is " + age + " years old; leases require vehicles "
                            + MAX_VEHICLE_AGE_YEARS + " years old or newer.");
        }
    }

    public double getExpectedEndingValue() {
        return expectedEndingValue;
    }

    public void setExpectedEndingValue(double expectedEndingValue) {
        this.expectedEndingValue = expectedEndingValue;
    }

    public double getLeaseFee() {
        return leaseFee;
    }

    public void setLeaseFee(double leaseFee) {
        this.leaseFee = leaseFee;
    }

    @Override
    public double getMonthlyPayment() {
        return Loans.monthlyPayment(getTotalPrice(), LEASE_APR, LEASE_TERM_MONTHS);
    }

    @Override
    public double getTotalPrice() {
        return expectedEndingValue + leaseFee;
    }

    @Override
    protected String getTypeLabel() {
        return "LEASE";
    }

    @Override
    protected String getSpecificCsvFields() {
        return String.join("|",
                formatMoney(expectedEndingValue),
                formatMoney(leaseFee),
                formatMoney(getTotalPrice()),
                formatMoney(getMonthlyPayment()));
    }
}

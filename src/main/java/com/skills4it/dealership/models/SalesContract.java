package com.skills4it.dealership.models;

public class SalesContract extends Contract {

    private static final double SALES_TAX_RATE = 0.05;
    private static final double RECORDING_FEE = 100.00;
    private static final double PROCESSING_FEE_BELOW_THRESHOLD = 295.00;
    private static final double PROCESSING_FEE_AT_OR_ABOVE_THRESHOLD = 495.00;
    private static final double PRICE_TIER_THRESHOLD = 10_000.00;

    private static final double LOAN_APR_HIGH_TIER = 0.0425;
    private static final int LOAN_TERM_MONTHS_HIGH_TIER = 48;
    private static final double LOAN_APR_LOW_TIER = 0.0525;
    private static final int LOAN_TERM_MONTHS_LOW_TIER = 24;

    private double salesTaxAmount;
    private double recordingFee;
    private double processingFee;
    private boolean financed;

    public SalesContract(String contractDate,
                         String customerName,
                         String customerEmail,
                         Vehicle vehicleSold,
                         boolean financed) {
        super(contractDate, customerName, customerEmail, vehicleSold);
        double vehiclePrice = vehicleSold.getPrice();
        this.salesTaxAmount = vehiclePrice * SALES_TAX_RATE;
        this.recordingFee = RECORDING_FEE;
        this.processingFee = vehiclePrice < PRICE_TIER_THRESHOLD
                ? PROCESSING_FEE_BELOW_THRESHOLD
                : PROCESSING_FEE_AT_OR_ABOVE_THRESHOLD;
        this.financed = financed;
    }

    public double getSalesTaxAmount() {
        return salesTaxAmount;
    }

    public void setSalesTaxAmount(double salesTaxAmount) {
        this.salesTaxAmount = salesTaxAmount;
    }

    public double getRecordingFee() {
        return recordingFee;
    }

    public void setRecordingFee(double recordingFee) {
        this.recordingFee = recordingFee;
    }

    public double getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(double processingFee) {
        this.processingFee = processingFee;
    }

    public boolean isFinanced() {
        return financed;
    }

    public void setFinanced(boolean financed) {
        this.financed = financed;
    }





    @Override
    public double getMonthlyPayment() {
        if (!financed) return 0;
        double vehiclePrice = getVehicleSold().getPrice();
        double apr = vehiclePrice >= PRICE_TIER_THRESHOLD
                ? LOAN_APR_HIGH_TIER : LOAN_APR_LOW_TIER;
        int termMonths  = vehiclePrice >= PRICE_TIER_THRESHOLD
                ? LOAN_TERM_MONTHS_HIGH_TIER : LOAN_TERM_MONTHS_LOW_TIER;
        // todo: replace with Loans.monthlyPayment(getTotalPrice(), apr, termMonths); after loans class creation
        return getTotalPrice();
    }

    @Override
    public double getTotalPrice() {
        return getVehicleSold().getPrice() + salesTaxAmount + recordingFee + processingFee;
    }

    @Override
    protected String getTypeLabel() {
        return "SALE";
    }

    @Override
    protected String getSpecificCsvFields() {
        return String.join("|",
                formatMoney(salesTaxAmount),
                formatMoney(recordingFee),
                formatMoney(processingFee),
                formatMoney(getTotalPrice()),
                financed ? "YES" : "NO",
                formatMoney(getMonthlyPayment()));
    }
}

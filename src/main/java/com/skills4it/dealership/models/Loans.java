package com.skills4it.dealership.models;

/**
 * Financial math helpers for contracts. Package-private utility class.
 */
final class Loans {

    private Loans() { /* no instances */ }

    /**
     * Calculates the fixed monthly payment for a fully amortized loan
     * using the standard PMT formula:
     *
     *   PMT = P * r * (1 + r)^n / ((1 + r)^n - 1)
     *
     * where P is principal, r is the monthly interest rate, n is the
     * number of monthly payments.
     *
     * @param principal  amount being financed
     * @param annualRate APR as a decimal (e.g. 0.0425 for 4.25%)
     * @param termMonths number of monthly payments
     * @return monthly payment, or 0 if principal/term is non-positive
     */
    static double monthlyPayment(double principal, double annualRate, int termMonths) {
        if (principal <= 0 || termMonths <= 0) return 0.0;
        if (annualRate <= 0) return principal / termMonths;

        double monthlyRate = annualRate / 12.0;
        double growth = Math.pow(1.0 + monthlyRate, termMonths);
        return principal * monthlyRate * growth / (growth - 1.0);
    }
}

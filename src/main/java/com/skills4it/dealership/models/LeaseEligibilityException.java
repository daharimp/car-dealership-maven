package com.skills4it.dealership.models;

/**
 * Thrown when an attempt is made to create a lease for a vehicle that
 * fails the dealership's eligibility rules (e.g. too old).
 */
public class LeaseEligibilityException extends RuntimeException {

    public LeaseEligibilityException(String message) {
        super(message);
    }
}
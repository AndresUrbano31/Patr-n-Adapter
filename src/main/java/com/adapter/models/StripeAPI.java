package com.adapter.models;

// Simulates the real Stripe API (incompatible interface)
public class StripeAPI {

    public void makeCharge(double amount, String currency) {
        System.out.println("[Stripe] Charge processed: "
                + amount + " " + currency);
    }

    public void reverseCharge(double amount) {
        System.out.println("[Stripe] Refund processed: $" + amount);
    }
}

package com.adapter.models;

public class StripeAdapter implements PaymentGateway {

    private StripeAPI stripe;

    public StripeAdapter() {
        this.stripe = new StripeAPI();
    }

    @Override
    public void processPayment(double amount) {
        // Adapts: processPayment() → makeCharge()
        stripe.makeCharge(amount, "USD");
    }

    @Override
    public void refundPayment(double amount) {
        // Adapts: refundPayment() → reverseCharge()
        stripe.reverseCharge(amount);
    }
}
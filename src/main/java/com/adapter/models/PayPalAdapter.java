package com.adapter.models;

public class PayPalAdapter implements PaymentGateway {

    private PayPalAPI paypal;
    private String targetAccount;

    public PayPalAdapter(String targetAccount) {
        this.paypal = new PayPalAPI();
        this.targetAccount = targetAccount;
    }

    @Override
    public void processPayment(double amount) {
        // Adapts: processPayment() → sendMoney()
        paypal.sendMoney(targetAccount, amount);
    }

    @Override
    public void refundPayment(double amount) {
        // Adapts: refundPayment() → cancelTransaction()
        paypal.cancelTransaction(amount);
    }
}
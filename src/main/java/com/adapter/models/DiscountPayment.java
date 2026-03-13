package com.adapter.models;

public class DiscountPayment extends BasicPayment {

    private double discount; // percentage e.g: 0.10 = 10%

    public DiscountPayment(PaymentGateway gateway, double discount) {
        super(gateway);
        this.discount = discount;
    }

    @Override
    public void makePayment(double amount) {
        double finalAmount = amount - (amount * discount);
        System.out.println("Discount applied: " + (discount * 100) + "%");
        System.out.println("Original amount: $" + amount);
        System.out.println("Final amount:    $" + finalAmount);
        gateway.processPayment(finalAmount);
    }

    @Override
    public void requestRefund(double amount) {
        double finalAmount = amount - (amount * discount);
        gateway.refundPayment(finalAmount);
    }
}
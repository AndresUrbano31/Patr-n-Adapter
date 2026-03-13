package com.adapter.models;

public abstract class BasicPayment {

    protected PaymentGateway gateway;

    public BasicPayment(PaymentGateway gateway) {
        this.gateway = gateway;
    }

    public abstract void makePayment(double amount);
    public abstract void requestRefund(double amount);
}
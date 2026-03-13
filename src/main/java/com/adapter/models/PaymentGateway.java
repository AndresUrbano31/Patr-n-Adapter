package com.adapter.models;

public interface PaymentGateway {
    void processPayment(double amount);
    void refundPayment(double amount);
}
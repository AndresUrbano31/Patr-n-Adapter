package com.adapter.models;

// Simulates the real PayPal API (incompatible interface)
public class PayPalAPI {

    public void sendMoney(String account, double total) {
        System.out.println("[PayPal] Payment sent to "
                + account + " for $" + total);
    }

    public void cancelTransaction(double total) {
        System.out.println("[PayPal] Transaction cancelled for $" + total);
    }
}
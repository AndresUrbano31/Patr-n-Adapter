package com.adapter;

import com.adapter.models.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createWindow());
    }

    static void createWindow() {
        JFrame frame = new JFrame("Payment Gateway - Adapter & Bridge Pattern");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Payment Gateway System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(33, 97, 140));
        title.setBorder(new EmptyBorder(20, 0, 10, 0));
        frame.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(10, 30, 10, 30));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0;
        form.add(new JLabel("Select Gateway:"), g);
        g.gridx = 1;
        JComboBox<String> gatewayBox = new JComboBox<>(new String[]{"Stripe", "PayPal"});
        gatewayBox.setFont(new Font("Arial", Font.PLAIN, 14));
        form.add(gatewayBox, g);

        g.gridx = 0; g.gridy = 1;
        form.add(new JLabel("Payment Type:"), g);
        g.gridx = 1;
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Standard", "With Discount"});
        typeBox.setFont(new Font("Arial", Font.PLAIN, 14));
        form.add(typeBox, g);

        g.gridx = 0; g.gridy = 2;
        form.add(new JLabel("Amount ($):"), g);
        g.gridx = 1;
        JTextField amountField = new JTextField("100.0");
        amountField.setFont(new Font("Arial", Font.PLAIN, 14));
        form.add(amountField, g);

        g.gridx = 0; g.gridy = 3;
        JLabel discountLabel = new JLabel("Discount (%):");
        form.add(discountLabel, g);
        g.gridx = 1;
        JTextField discountField = new JTextField("10");
        discountField.setFont(new Font("Arial", Font.PLAIN, 14));
        form.add(discountField, g);

        g.gridx = 0; g.gridy = 4;
        JLabel accountLabel = new JLabel("PayPal Account:");
        form.add(accountLabel, g);
        g.gridx = 1;
        JTextField accountField = new JTextField("customer@email.com");
        accountField.setFont(new Font("Arial", Font.PLAIN, 14));
        form.add(accountField, g);

        accountLabel.setVisible(false);
        accountField.setVisible(false);
        discountLabel.setVisible(false);
        discountField.setVisible(false);

        gatewayBox.addActionListener(e -> {
            boolean isPayPal = gatewayBox.getSelectedItem().equals("PayPal");
            accountLabel.setVisible(isPayPal);
            accountField.setVisible(isPayPal);
            form.revalidate();
        });

        typeBox.addActionListener(e -> {
            boolean hasDiscount = typeBox.getSelectedItem().equals("With Discount");
            discountLabel.setVisible(hasDiscount);
            discountField.setVisible(hasDiscount);
            form.revalidate();
        });

        frame.add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton payBtn = new JButton("Process Payment");
        payBtn.setBackground(new Color(39, 174, 96));
        payBtn.setForeground(Color.WHITE);
        payBtn.setFont(new Font("Arial", Font.BOLD, 14));
        payBtn.setFocusPainted(false);

        JButton refundBtn = new JButton("Request Refund");
        refundBtn.setBackground(new Color(231, 76, 60));
        refundBtn.setForeground(Color.WHITE);
        refundBtn.setFont(new Font("Arial", Font.BOLD, 14));
        refundBtn.setFocusPainted(false);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setFont(new Font("Arial", Font.BOLD, 14));
        clearBtn.setFocusPainted(false);

        buttons.add(payBtn);
        buttons.add(refundBtn);
        buttons.add(clearBtn);

        JTextArea output = new JTextArea(10, 40);
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 13));
        output.setBackground(new Color(30, 30, 30));
        output.setForeground(new Color(0, 255, 100));
        output.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(output);
        scroll.setBorder(BorderFactory.createTitledBorder("Output"));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(new EmptyBorder(0, 20, 20, 20));
        bottom.add(buttons, BorderLayout.NORTH);
        bottom.add(scroll, BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);

        payBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String gateway = (String) gatewayBox.getSelectedItem();
                String type = (String) typeBox.getSelectedItem();
                PaymentGateway pg = buildGateway(gateway, accountField.getText());
                BasicPayment payment = buildPayment(type, pg, discountField.getText());
                output.append(">> Processing payment with " + gateway + "\n");
                captureOutput(payment, amount, false, output);
                output.append("\n");
            } catch (NumberFormatException ex) {
                output.append("[ERROR] Invalid amount or discount value\n\n");
            }
        });

        refundBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String gateway = (String) gatewayBox.getSelectedItem();
                String type = (String) typeBox.getSelectedItem();
                PaymentGateway pg = buildGateway(gateway, accountField.getText());
                BasicPayment payment = buildPayment(type, pg, discountField.getText());
                output.append(">> Processing refund with " + gateway + "\n");
                captureOutput(payment, amount, true, output);
                output.append("\n");
            } catch (NumberFormatException ex) {
                output.append("[ERROR] Invalid amount or discount value\n\n");
            }
        });

        clearBtn.addActionListener(e -> output.setText(""));

        frame.setVisible(true);
    }

    static PaymentGateway buildGateway(String gateway, String account) {
        if (gateway.equals("PayPal")) {
            return new PayPalAdapter(account);
        }
        return new StripeAdapter();
    }

    static BasicPayment buildPayment(String type, PaymentGateway pg, String discountText) {
        if (type.equals("With Discount")) {
            double discount = Double.parseDouble(discountText) / 100.0;
            return new DiscountPayment(pg, discount);
        }
        return new DiscountPayment(pg, 0.0);
    }

    static void captureOutput(BasicPayment payment, double amount,
                               boolean isRefund, JTextArea output) {
        PrintStreamCapture capture = new PrintStreamCapture(output);
        java.io.PrintStream old = System.out;
        System.setOut(capture);
        if (isRefund) {
            payment.requestRefund(amount);
        } else {
            payment.makePayment(amount);
        }
        System.setOut(old);
    }
}

package com.adapter;

import com.adapter.models.*;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class Main extends Application {

    private TextArea output;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Payment Gateway - Adapter & Bridge Pattern");

        // Title
        Label title = new Label("Payment Gateway System");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#21618C"));

        // Gateway selector
        Label gatewayLabel = new Label("Select Gateway:");
        ComboBox<String> gatewayBox = new ComboBox<>();
        gatewayBox.getItems().addAll("Stripe", "PayPal");
        gatewayBox.setValue("Stripe");
        gatewayBox.setPrefWidth(200);

        // Payment type
        Label typeLabel = new Label("Payment Type:");
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Standard", "With Discount");
        typeBox.setValue("Standard");
        typeBox.setPrefWidth(200);

        // Amount
        Label amountLabel = new Label("Amount ($):");
        TextField amountField = new TextField("100.0");
        amountField.setPrefWidth(200);

        // Discount
        Label discountLabel = new Label("Discount (%):");
        TextField discountField = new TextField("10");
        discountField.setPrefWidth(200);
        discountLabel.setVisible(false);
        discountField.setVisible(false);

        // PayPal account
        Label accountLabel = new Label("PayPal Account:");
        TextField accountField = new TextField("customer@email.com");
        accountField.setPrefWidth(200);
        accountLabel.setVisible(false);
        accountField.setVisible(false);

        // Show/hide fields
        gatewayBox.setOnAction(e -> {
            boolean isPayPal = gatewayBox.getValue().equals("PayPal");
            accountLabel.setVisible(isPayPal);
            accountField.setVisible(isPayPal);
        });

        typeBox.setOnAction(e -> {
            boolean hasDiscount = typeBox.getValue().equals("With Discount");
            discountLabel.setVisible(hasDiscount);
            discountField.setVisible(hasDiscount);
        });

        // Form grid
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 40, 20, 40));
        grid.add(gatewayLabel,  0, 0); grid.add(gatewayBox,  1, 0);
        grid.add(typeLabel,     0, 1); grid.add(typeBox,     1, 1);
        grid.add(amountLabel,   0, 2); grid.add(amountField, 1, 2);
        grid.add(discountLabel, 0, 3); grid.add(discountField,1, 3);
        grid.add(accountLabel,  0, 4); grid.add(accountField, 1, 4);

        // Buttons
        Button payBtn = new Button("Process Payment");
        payBtn.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 20;");

        Button refundBtn = new Button("Request Refund");
        refundBtn.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 20;");

        Button clearBtn = new Button("Clear");
        clearBtn.setStyle("-fx-font-size: 14px; -fx-padding: 10 20;");

        HBox buttons = new HBox(15, payBtn, refundBtn, clearBtn);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10, 0, 10, 0));

        // Output area
        output = new TextArea();
        output.setEditable(false);
        output.setStyle("-fx-control-inner-background: #1E1E1E; " +
                "-fx-text-fill: #00FF64; -fx-font-family: Monospaced; -fx-font-size: 13px;");
        output.setPrefHeight(200);

        TitledPane outputPane = new TitledPane("Output", output);
        outputPane.setCollapsible(false);

        // Layout
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.getChildren().addAll(title, grid, buttons, outputPane);

        // Button actions
        payBtn.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                PaymentGateway pg = buildGateway(
                        gatewayBox.getValue(), accountField.getText());
                BasicPayment payment = buildPayment(
                        typeBox.getValue(), pg, discountField.getText());
                output.appendText(">> Processing payment with "
                        + gatewayBox.getValue() + "\n");
                redirectOutput(payment, amount, false);
                output.appendText("\n");
            } catch (NumberFormatException ex) {
                output.appendText("[ERROR] Invalid amount or discount\n\n");
            }
        });

        refundBtn.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                PaymentGateway pg = buildGateway(
                        gatewayBox.getValue(), accountField.getText());
                BasicPayment payment = buildPayment(
                        typeBox.getValue(), pg, discountField.getText());
                output.appendText(">> Processing refund with "
                        + gatewayBox.getValue() + "\n");
                redirectOutput(payment, amount, true);
                output.appendText("\n");
            } catch (NumberFormatException ex) {
                output.appendText("[ERROR] Invalid amount or discount\n\n");
            }
        });

        clearBtn.setOnAction(e -> output.clear());

        Scene scene = new Scene(root, 620, 680);
        stage.setScene(scene);
        stage.show();
    }

    PaymentGateway buildGateway(String gateway, String account) {
        if (gateway.equals("PayPal")) return new PayPalAdapter(account);
        return new StripeAdapter();
    }

    BasicPayment buildPayment(String type, PaymentGateway pg, String discountText) {
        if (type.equals("With Discount")) {
            double discount = Double.parseDouble(discountText) / 100.0;
            return new DiscountPayment(pg, discount);
        }
        return new DiscountPayment(pg, 0.0);
    }

    void redirectOutput(BasicPayment payment, double amount, boolean isRefund) {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.PrintStream ps = new java.io.PrintStream(baos);
        java.io.PrintStream old = System.out;
        System.setOut(ps);
        if (isRefund) payment.requestRefund(amount);
        else payment.makePayment(amount);
        System.out.flush();
        System.setOut(old);
        output.appendText(baos.toString());
    }
}
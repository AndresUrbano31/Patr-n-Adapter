package com.adapter;

import javax.swing.*;
import java.io.*;

public class PrintStreamCapture extends PrintStream {

    private JTextArea textArea;

    public PrintStreamCapture(JTextArea textArea) {
        super(System.out, true);
        this.textArea = textArea;
    }

    @Override
    public void println(String x) {
        textArea.append(x + "\n");
    }

    @Override
    public void print(String x) {
        textArea.append(x);
    }
}

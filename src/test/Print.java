package test;

import java.awt.print.PrinterException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class Print {

    public static void main(String[] args) {
        JTextArea toPrint = new JTextArea("Text here.");
        try {
            System.out.println("--------");
            boolean done = toPrint.print();

            JOptionPane.showMessageDialog(new JFrame(), "Printing " + (done ? "completed" : "canceled"));
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(new JFrame(), "The printer messed up. " + ex.getMessage());
        }

    }
}

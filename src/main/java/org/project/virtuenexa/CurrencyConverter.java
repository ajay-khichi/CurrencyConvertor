package org.project.virtuenexa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class CurrencyConverter {
    private static final String API_KEY = "Your_API_KEY"; // Replace with your actual API key
    private static final String[] COUNTRIES = {"United States", "India", "United Kingdom", "Canada", "Australia"};
    private static final String[] CURRENCIES = {"USD", "INR", "GBP", "CAD", "AUD"};

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CurrencyConverter::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Currency Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Currency Converter", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(50, 50, 150));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(titleLabel, gbc);

        JComboBox<String> fromCountry = new JComboBox<>(COUNTRIES);
        JComboBox<String> toCountry = new JComboBox<>(COUNTRIES);
        JTextField amountField = new JTextField();
        JLabel resultLabel = new JLabel("Converted Amount: ");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton convertButton = new JButton("Convert");
        JButton swapButton = new JButton("Swap");

        styleButton(convertButton);
        styleButton(swapButton);

        swapButton.addActionListener(e -> {
            int fromIndex = fromCountry.getSelectedIndex();
            int toIndex = toCountry.getSelectedIndex();
            fromCountry.setSelectedIndex(toIndex);
            toCountry.setSelectedIndex(fromIndex);
        });

        convertButton.addActionListener(e -> {
            int fromIndex = fromCountry.getSelectedIndex();
            int toIndex = toCountry.getSelectedIndex();
            String fromCurrency = CURRENCIES[fromIndex];
            String toCurrency = CURRENCIES[toIndex];
            try {
                double amount = Double.parseDouble(amountField.getText());
                double convertedAmount = convertCurrency(fromCurrency, toCurrency, amount);
                resultLabel.setText("Converted Amount: " + convertedAmount);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        frame.add(new JLabel("From Country:"), gbc);
        gbc.gridx = 1;
        frame.add(fromCountry, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(new JLabel("To Country:"), gbc);
        gbc.gridx = 1;
        frame.add(toCountry, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        frame.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        frame.add(swapButton, gbc);
        gbc.gridx = 1;
        frame.add(convertButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        frame.add(resultLabel, gbc);

        frame.setVisible(true);
    }

    private static double convertCurrency(String from, String to, double amount) {
        try {
            String urlStr = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/" + from + "/" + to;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            Scanner scanner = new Scanner(url.openStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            JSONObject json = new JSONObject(response.toString());
            double rate = json.getDouble("conversion_rate");
            return amount * rate;
        } catch (IOException ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }

    private static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }
}

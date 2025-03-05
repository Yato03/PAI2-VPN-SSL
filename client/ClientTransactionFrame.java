package client;

import criptography.HashUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

public class ClientTransactionFrame {
    private JFrame frame;
    private JTextField accountOriginField;
    private JTextField accountDestinationField;
    private JTextField amountField;
    private JButton transferButton;
    private JButton logoutButton;

    public ClientTransactionFrame(PrintWriter output) {
        frame = new JFrame("Transacciones");
        frame.setSize(350, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Campo de cuenta origen
        JLabel originLabel = new JLabel("Cuenta origen:");
        originLabel.setBounds(30, 20, 120, 25);
        frame.add(originLabel);

        accountOriginField = new JTextField();
        accountOriginField.setBounds(160, 20, 150, 25);
        frame.add(accountOriginField);

        // Campo de cuenta destino
        JLabel destinationLabel = new JLabel("Cuenta destino:");
        destinationLabel.setBounds(30, 60, 120, 25);
        frame.add(destinationLabel);

        accountDestinationField = new JTextField();
        accountDestinationField.setBounds(160, 60, 150, 25);
        frame.add(accountDestinationField);

        // Campo de cantidad a transferir
        JLabel amountLabel = new JLabel("Cantidad:");
        amountLabel.setBounds(30, 100, 120, 25);
        frame.add(amountLabel);

        amountField = new JTextField();
        amountField.setBounds(160, 100, 150, 25);
        frame.add(amountField);

        // Botón de realizar transferencia
        transferButton = new JButton("Realizar transferencia");
        transferButton.setBounds(30, 140, 280, 30);
        frame.add(transferButton);

        // Botón de cerrar sesión
        logoutButton = new JButton("Cerrar sesión");
        logoutButton.setBounds(30, 180, 280, 30);
        frame.add(logoutButton);

        // Acción del botón de realizar transferencia
        transferButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int cuentaOrigen = Integer.parseInt(accountOriginField.getText());
                    int cuentaDestino = Integer.parseInt(accountDestinationField.getText());
                    int cantidad = Integer.parseInt(amountField.getText());

                    String message = String.format(cuentaOrigen + ", " + cuentaDestino + ", " + cantidad);
                    String nonce = HashUtil.createNonce();
                    String hmac = HashUtil.createHmacMessage(nonce+message);

                    output.println("transfer");
                    output.println(message);
                    output.println(nonce);
                    output.println(hmac);
                    output.flush();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Por favor, ingrese valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción del botón de cerrar sesión
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                output.println("logout");
                output.flush();
                JOptionPane.showMessageDialog(frame, "Sesión cerrada.");
                frame.dispose();
            }
        });

        frame.setVisible(true);
    }
}

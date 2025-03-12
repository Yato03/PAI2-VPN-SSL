package client;

import requests.ClientRequest;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

public class ClientMessageFrame {
    private JFrame frame;
    private JTextField messageField;
    private JButton sendButton;
    private JButton logoutButton;

    public ClientMessageFrame(PrintWriter output, String userId) {
        frame = new JFrame("Enviar mensaje");
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Centrar la ventana
        frame.setLocationRelativeTo(null);

        messageField = new JTextField();
        messageField.setBounds(30, 50, 280, 25);
        frame.add(messageField);

        // Botón de enviar el mensaje
        sendButton = new JButton("Enviar mensaje");
        sendButton.setBounds(30, 90, 280, 30);
        frame.add(sendButton);

        // Botón de cerrar sesión
        logoutButton = new JButton("Cerrar sesión");
        logoutButton.setBounds(30, 130, 280, 30);
        frame.add(logoutButton);

        // Acción del botón de realizar transferencia
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText();

                if (message.length() > 144) {
                    JOptionPane.showMessageDialog(frame, "El mensaje no puede superar los 144 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (message.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "El mensaje no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try{
                    ClientRequest.sendDataToServer("message", message, userId, output);
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                messageField.setText(""); // Limpiar el campo de texto después de enviar
            }
        });

        // Acción del botón de cerrar sesión
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try{
                    ClientRequest.sendDataToServer("logout", "logout", output);
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(frame, "Sesión cerrada.");
                frame.dispose();
            }
        });

        frame.setVisible(true);
    }
}

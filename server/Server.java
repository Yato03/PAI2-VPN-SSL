package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import criptography.HashUtil;

public class Server {

    /**
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException,
            InterruptedException {

        // perpetually listen for clients
        ServerSocket serverSocket = new ServerSocket(3343);
        while (true) {

            // wait for client connection and check login information
            try {
                System.err.println("Waiting for connection...");

                Socket socket = serverSocket.accept();

                // open BufferedReader for reading data from client
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				// read operation from client

				String operation = input.readLine();
                System.out.println("Operation: " + operation);
                String usernameAndPassword = input.readLine();
                String nonce = input.readLine();
                String hmac = input.readLine();

                // open PrintWriter for writing data to client
                PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

                // Check hmac
                String newHmac = HashUtil.createHmacMessage(nonce+usernameAndPassword);
                if(!hmac.equals(newHmac)) {
                    output.println("error");
                    output.println("El mensaje ha perdido la integridad");
                    output.close();
                    input.close();
                    socket.close();
                    continue;
                }

                // Check nonce
                String archivo = "server/nonces.txt";
                boolean encontrado = false;

                try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.trim().equals(nonce)) {
                            encontrado = true;
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error al leer el archivo: " + e.getMessage());
                }

                if (encontrado) {
                    output.println("error");
                    output.println("El nonce ya ha sido usado");
                    output.close();
                    input.close();
                    socket.close();
                    continue;
                }

                Files.write(Paths.get(archivo), (nonce + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

                String[] tokens = usernameAndPassword.split(",");

                if (tokens.length != 2) {
                    output.println("error");
                    output.println("Falta el usuario o contraseña");
                    output.close();
                    input.close();
                    socket.close();
                    continue;
                }

                // Check integrity users.txt
                if(!HashUtil.checkUsersIntegrity()){
                    System.out.println("Se ha perdido la integridad en la base de datos");
                }

                String username = tokens[0];
                String password = tokens[1];

                String registry = HashUtil.hashPassword(username, password);
                // Comprobar que el registro no existe en users.txt

                archivo = "server/users.txt";
                encontrado = false;

                try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.trim().equals(registry)) {
                            encontrado = true;
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error al leer el archivo: " + e.getMessage());
                }

				if(operation.equals("login")) {

                    if(!encontrado) {
                        output.println("error");
                        output.println("El usuario no existe");
                        output.close();
                        input.close();
                        socket.close();
                        continue;
                    }

                    output.println("success");
					output.println("Usuario logueado");
                    output.flush();
				}
				else if(operation.equals("register")) {

                    if(encontrado) {
                        output.println("error");
                        output.println("El usuario ya existe");
                        output.close();
                        input.close();
                        socket.close();
                        continue;
                    }

                    // Ingresar usuario en la base de datos
                    Files.write(Paths.get(archivo), (registry + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

                    HashUtil.saveNewIntegrityValue();

                    output.println("success");
					output.println("Usuario registrado");
                    output.flush();
				}
				else {
					output.println("Invalid operation");
				}

                // read operation from client

                operation = input.readLine();
                System.out.println("Operation: " + operation);
                String message = input.readLine();
                nonce = input.readLine();
                hmac = input.readLine();

                // Check hmac
                newHmac = HashUtil.createHmacMessage(nonce+message);
                if(!hmac.equals(newHmac)) {
                    output.println("error");
                    output.println("El mensaje ha perdido la integridad");
                    output.close();
                    input.close();
                    socket.close();
                    continue;
                }

                // Check nonce
                archivo = "server/nonces.txt";
                encontrado = false;

                try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.trim().equals(nonce)) {
                            encontrado = true;
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error al leer el archivo: " + e.getMessage());
                }

                if (encontrado) {
                    output.println("error");
                    output.println("El nonce ya ha sido usado");
                    output.close();
                    input.close();
                    socket.close();
                    continue;
                }

                Files.write(Paths.get(archivo), (nonce + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

                if(operation.equals("logout")){
                    output.println("success");
                    output.println("Usuario ha cerrado sesión");
                    output.close();
                    input.close();
                    socket.close();
                    continue;
                }
                else if(operation.equals("transfer")){
                    tokens = message.split(", ");
                    System.out.println(tokens.length);
                    //String origin = tokens[0];
                    //String destination = tokens[1];
                    //String amount = tokens[2];

                    if (tokens.length != 3) {
                        output.println("error");
                        output.println("Falta alguno de los valores");
                        output.flush();
                        output.close();
                        input.close();
                        socket.close();
                        continue;
                    }

                    output.println("success");
                    output.println("Transaccion registrada");
                    System.out.println("Transaccion registrada");
                    output.flush();
                }

                output.close();
                input.close();
                socket.close();

            } // end try
            // handle exception communicating with client
            catch (IOException ioException) {
                ioException.printStackTrace();
            }

        } // end while

    }

}

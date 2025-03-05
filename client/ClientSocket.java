package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;


public class ClientSocket {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
	try {

		// create Socket from factory
		Socket socket = new Socket("127.0.0.1", 3343);

		// create PrintWriter for sending login to server
		PrintWriter output = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));

		ClientLoginFrame loginFrame = new ClientLoginFrame(output);

		output.flush();

		// create BufferedReader for reading server response
		BufferedReader input = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

		// read response from server
		String statusCode = input.readLine();
		String message = input.readLine();

		// display response to user

		if(statusCode.equals("success")) {
			JOptionPane.showMessageDialog(null, message);

		}
		else{
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
		}

		if(message.equals("Usuario logueado")) {
			loginFrame.closeFrame();
			new ClientTransactionFrame(output);
			// read response from server
			statusCode = input.readLine();
			message = input.readLine();

			System.out.println(statusCode);

			// display response to user

			if(statusCode.equals("success")) {
				JOptionPane.showMessageDialog(null, message);

			}
			else{
				JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}



		// clean up streams and Socket
		//output.close();
		//input.close();
		//socket.close();

	} // end try

	// handle exception communicating with server
	catch (IOException ioException) {
		ioException.printStackTrace();
	}

	// exit application
	finally {
		System.exit(0);
	}

    }
}

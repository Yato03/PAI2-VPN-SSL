package requests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientRequest {

    private String operation;
    private String message;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOperation() {
        return operation;
    }

    public String getMessage() {
        return message;
    }

    public ClientRequest(String operation, String message) {
        this.operation = operation;
        this.message = message;
        this.userId = "userId";
    }

    public ClientRequest(String operation, String message, String userId) {
        this.operation = operation;
        this.message = message;
        this.userId = userId;
    }

    private void sendToServer(PrintWriter output) throws IOException {
        output.println(operation);
        output.println(message);
        output.println(userId);
        output.flush();
    }

    public static void sendDataToServer(String operation, String message, PrintWriter output) throws IOException {
        ClientRequest request = new ClientRequest(operation, message);
        request.sendToServer(output);
    }

    public static void sendDataToServer(String operation, String message, String userId, PrintWriter output) throws IOException {
        ClientRequest request = new ClientRequest(operation, message, userId);
        request.sendToServer(output);
    }

    public static ClientRequest recieveDataFromServer(BufferedReader input) throws IOException {
        String operation = input.readLine();
        String message = input.readLine();
        String userId = input.readLine();
        return new ClientRequest(operation, message, userId);
    }


}

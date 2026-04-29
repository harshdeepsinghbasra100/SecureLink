package client;

import Crypto.AESUtil;
import java.io.*;
import java.net.Socket;

public class VPNClient {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public void connect(String serverIP, int port) {
        try {
            socket = new Socket(serverIP, port);

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Connected to VPN Server");

        } catch (IOException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
    
    public interface MessageListener {
    void onMessageReceived(String message);
}
    private MessageListener listener;

    public void setMessageListener(MessageListener listener) {
    this.listener = listener;
}

    public void sendMessage(String message) {
    if (output != null) {

        String encrypted = AESUtil.encrypt(message);

        System.out.println("Encrypted: " + encrypted);

        output.println(encrypted);
    }
}

    public void receiveMessage() {
    new Thread(() -> {
        try {
            String response;
            while ((response = input.readLine()) != null) {

                if (listener != null) {
                    listener.onMessageReceived(response);
                }

            }
        } catch (Exception e) {
            System.out.println("Disconnected");
        }
    }).start();
}

    public void disconnect() {
        try {
            socket.close();
            System.out.println("Disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
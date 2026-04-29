package server;

import java.io.*;
import java.net.*;

public class VPNServer {

    private static final int PORT = 5000;

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("VPN Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Handle each client in separate thread
                new ClientHandler(clientSocket).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
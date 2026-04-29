package server;

import Crypto.AESUtil;
import java.io.*;
import java.net.Socket; // ✅ keep uppercase (as per your project)

public class ClientHandler extends Thread {

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            BufferedReader input = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            PrintWriter output = new PrintWriter(
                socket.getOutputStream(), true
            )
        ) {

            String message;

            while (true) {
                message = input.readLine();

                // ✅ Detect disconnect
                if (message == null) {
                    System.out.println("Client disconnected: " + socket.getInetAddress());
                    break;
                }

                System.out.println("Encrypted received: " + message);

                // ✅ Decrypt using correct import
                String decrypted = AESUtil.decrypt(message);

                System.out.println("Decrypted: " + decrypted);

                // ✅ Send response
                output.println("Server got: " + decrypted);
            }

        } catch (Exception e) {
            System.out.println("Client disconnected (exception): " + socket.getInetAddress());
            e.printStackTrace(); // ✅ helpful for debugging
        } finally {
            try {
                socket.close();
            } catch (Exception ignored) {}
        }
    }
}
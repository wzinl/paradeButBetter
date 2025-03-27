package main.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;


import java.net.URI;
import java.util.Scanner;


public class GameClient extends WebSocketClient {

    public GameClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("Connected to the server");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Server says: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public static void main(String[] args) throws Exception {
        GameClient client = new GameClient(new URI("ws://localhost:8887"));
        client.connectBlocking(); // Waits until connected

        Scanner scanner = new Scanner(System.in);
        System.out.println("Type your move:");

        while (client.isOpen()) {
            String msg = scanner.nextLine();
            client.send(msg);
        }

        scanner.close();
    }
}
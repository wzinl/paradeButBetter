package main.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import main.helpers.InputValidator;

import java.net.ConnectException;
import java.net.URI;
import java.util.Scanner;

public class GameClient extends WebSocketClient {

    private String playerId = "Unknown";

    public GameClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("Connected to server.");
        Scanner scanner = new Scanner(System.in);
        String name = InputValidator.getString("Enter your name: ");       
        send("NAME|" + name);
    }

    @Override
    public void onMessage(String message) {
        if (message.startsWith("WELCOME|")) {
            playerId = message.split("\\|")[1];
            System.out.println("You are " + playerId);
        } else {
            System.out.println(message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from server.");
    }

    @Override
    public void onError(Exception ex) {
        if (ex instanceof ConnectException cexp) {
            System.out.println("Connection Refused. Quitting Application.");
            return;
        }
        ex.printStackTrace();
    }

    public static void main(String[] args) throws Exception {
        GameClient client = new GameClient(new URI("ws://localhost:8887"));
        client.connectBlocking();

    }
}

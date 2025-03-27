package main.server;

import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;


import java.net.InetSocketAddress;

public class GameServer extends WebSocketServer {

    public GameServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onStart() {
        System.out.println("GameServer has started and is listening for connections...");
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New player connected: " + conn.getRemoteSocketAddress());
        conn.send("Welcome to the card game server!");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Player says: " + message);
        conn.send("You said: " + message); // Echo (or replace with game logic)
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Player disconnected: " + conn.getRemoteSocketAddress() + " Reason: " + reason);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("Error: " + ex.getMessage());
        ex.printStackTrace();
    }

    public static void main(String[] args) {
        GameServer server = new GameServer(8887); // Port 8887
        server.start();
        System.out.println("Parade Server started on port 8887");
        System.out.println();
    }
}
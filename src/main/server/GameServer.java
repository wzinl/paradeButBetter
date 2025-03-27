package main.server;

import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;


import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.smartcardio.CommandAPDU;

import main.helpers.InputValidator;

public class GameServer extends WebSocketServer {

    private Set<WebSocket> clients = Collections.synchronizedSet(new HashSet<>());
    private Map<WebSocket, String> playerNames = Collections.synchronizedMap(new HashMap<>());


    public GameServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        clients.add(conn);
        System.out.println("New connection: " + conn.getRemoteSocketAddress());
        conn.send("Welcome to the game server!");
        broadcast("A new player has joined.");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        clients.remove(conn);
        System.out.println("Connection closed: " + conn.getRemoteSocketAddress());
        broadcast("A player has left.");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        if (message.startsWith("NAME|")) {

            String name = message.substring(5).trim();
            playerNames.put(conn, name);
            conn.send("WELCOME|" + name);

            System.out.println(name + " has joined the game.");
            broadcast(name + " has joined the game.");
            return;
        }
        String name = playerNames.getOrDefault(conn, "Unknown");
        System.out.println(name + ": " + message);
        broadcast(name + ": " + message);
    }
    

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
    }

    public static void main(String[] args) {
        int port = 8887; // Default port
        GameServer server = new GameServer(port);
        server.start();
        String command = InputValidator.getString("");
        do{
            command = InputValidator.getString("");
        }
        while(!command.equalsIgnoreCase("exit"));

        try{
            server.stop();  // gracefully stop the server and free the port
            System.out.println("Server stopped.");
        } catch(InterruptedException e){
            System.out.println("Server stop has been interrupted!");
        }

    }
}

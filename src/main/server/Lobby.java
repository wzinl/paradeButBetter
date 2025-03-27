package main.server;

import org.java_websocket.WebSocket;
import java.util.*;

public class Lobby {
    private final String lobbyId;
    private final WebSocket host;
    private final List<WebSocket> guests = new ArrayList<>();


    public Lobby(String lobbyId, WebSocket host) {
        this.lobbyId = lobbyId;
        this.host = host;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public WebSocket getHost() {
        return host;
    }

    public List<WebSocket> getAllPlayers() {
        List<WebSocket> all = new ArrayList<>(guests);
        all.add(0, host);
        return all;
    }

    public void addGuest(WebSocket guest) {
        guests.add(guest);
    }

    public void broadcast(String message) {
        for (WebSocket client : getAllPlayers()) {
            client.send(message);
        }
    }

    public void removeClient(WebSocket conn) {
        guests.remove(conn);
    }

    public boolean isEmpty() {
        return guests.isEmpty();
    }
}

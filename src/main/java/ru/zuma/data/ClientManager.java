package ru.zuma.data;

import ru.zuma.Client;

import java.util.HashMap;

public class ClientManager {
    private HashMap<String, Client> clients;

    private ClientManager() {
        clients = new HashMap<>();
    }

    public void addClient(String sessionId, Client client) {
        clients.put(sessionId, client);
    }

    public void updateClient(String sessionId, Client client) {
        clients.put(sessionId, client);
    }

    public Client getClient(String sessionId) {
        return clients.get(sessionId);
    }

    public boolean containsSession(String sessionId) {
        return clients.containsKey(sessionId);
    }

    private static ClientManager clientManager;
    static {
        clientManager = new ClientManager();
    }

    public static ClientManager instance() {
        return clientManager;
    }
}

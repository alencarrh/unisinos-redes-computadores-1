package server;

import classes.ConnectionController;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * @class Server
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 25/03/2017
 */
public class Server implements Serializable {

    private final List<ConnectionListener> servers;
    private final ServerSocket serverSocket;
    private boolean running;

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.servers = new ArrayList<>();
        this.running = false;
    }

    public void startServer() throws IOException {
        if (isRunning()) {
            return;
        }
        this.running = true;
        System.out.println("Server iniciado!");
        while (running) {
            ConnectionController newConnection = new ConnectionController(serverSocket.accept());
            if (!ConnectionListener.connectionExists(newConnection)) {
                ConnectionListener connectionTemp = new ConnectionListener(newConnection);
                connectionTemp.addConnection(newConnection);
                servers.add(connectionTemp);
                connectionTemp.start();
            }
        }
    }

    public void stopServer() throws IOException {
        if (isRunning()) {
            this.running = false;
            this.servers.forEach((server) -> {
                server.interrupt();
            });
            this.serverSocket.close();
            System.out.println("Server parado!");
        }
    }

    public boolean isRunning() {
        return this.running;
    }

}

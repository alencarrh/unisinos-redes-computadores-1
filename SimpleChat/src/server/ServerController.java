package server;

import classes.Controller;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * @class ServerControler
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 25/03/2017 
 */
public class ServerController implements Serializable {

    private final List<Server> servers;
    private final ServerSocket serverSocket;
    private boolean running;

    public ServerController(int port) throws IOException {
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
            Controller newConnection = new Controller(serverSocket.accept());
            if (!Server.connectionExists(newConnection)) {
                Server serverTemp = new Server(newConnection);
                serverTemp.addConnection(newConnection);
                servers.add(serverTemp);
                serverTemp.start();
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

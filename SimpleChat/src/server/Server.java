package server;

import classes.Controller;
import classes.Mensagem;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @class Server
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 25/03/2017 
 */

public class Server extends Thread implements Serializable {

    private static final List<Controller> ALL_CONNECTIONS = new ArrayList<>();
    private final Controller controler;

    public Server(Controller controler) {
        this.controler = controler;
    }

    @Override
    public void run() {
        try {
            while (this.controler.isConectionOpen()) {
                Mensagem msg = controler.receber();
                System.out.println("Servidor recebeu: " + msg);
                if ("finish_connection".equalsIgnoreCase(msg.getId())) {
                    this.close();
                } else {
                    this.controler.enviar(new Mensagem("ok_connection", null));
                    enviarMensagemTodos(msg);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarMensagemTodos(Mensagem msg) {
        ALL_CONNECTIONS.forEach((connection) -> {
            if (connection.isConectionOpen() && !connection.equals(this.controler)) {
                connection.enviar(new Mensagem(msg.getId(), msg.getMensagem()));
            }
        });
    }

    public void addConnections(List<Controller> newConnections) {
        newConnections.forEach(connection -> {
            addConnection(connection);
        });
    }

    public void addConnection(Controller newConnection) {
        if (!connectionExists(newConnection)) {
            ALL_CONNECTIONS.add(newConnection);
        }
    }

    public static boolean connectionExists(Controller connection) {
        return ALL_CONNECTIONS.contains(connection);
    }

    private void close() throws IOException {
        ALL_CONNECTIONS.remove(this.controler);
        this.controler.close(true);
    }

    @Override
    public void interrupt() {
        try {
            this.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.interrupt();
    }

}

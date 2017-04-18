package server;

import classes.ConnectionController;
import classes.Mensagem;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @class ConnectionListener
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 25/03/2017
 */
public class ConnectionListener extends Thread implements Serializable {

    private static final List<ConnectionController> ALL_CONNECTIONS = new ArrayList<>();
    private final ConnectionController controler;

    public ConnectionListener(ConnectionController controler) {
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
            Logger.getLogger(ConnectionListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarMensagemTodos(Mensagem msg) {
        ALL_CONNECTIONS.forEach((connection) -> {
            if (connection.isConectionOpen() && !connection.equals(this.controler)) {
                connection.enviar(new Mensagem(msg.getId(), msg.getMensagem()));
            }
        });
    }

    public void addConnections(List<ConnectionController> newConnections) {
        newConnections.forEach(connection -> {
            addConnection(connection);
        });
    }

    public void addConnection(ConnectionController newConnection) {
        if (!connectionExists(newConnection)) {
            ALL_CONNECTIONS.add(newConnection);
        }
    }

    public static boolean connectionExists(ConnectionController connection) {
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
            Logger.getLogger(ConnectionListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.interrupt();
    }

}

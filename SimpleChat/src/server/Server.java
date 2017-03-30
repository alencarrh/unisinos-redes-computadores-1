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

    /**
     * Inicia a thread que faz o controle de uma conexão especifica.
     */
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

    /**
     * Envia uma mensagem para todas as conexões existentes exceto para a
     * conexão que está enviando.
     *
     * @param msg
     */
    public void enviarMensagemTodos(Mensagem msg) {
        ALL_CONNECTIONS.forEach((connection) -> {
            if (connection.isConectionOpen() && !connection.equals(this.controler)) {
                connection.enviar(new Mensagem(msg.getId(), msg.getMensagem()));
            }
        });
    }

    /**
     * Adiciona uma lista de conexões na lista de conexões.
     *
     * @param newConnections
     */
    public void addConnections(List<Controller> newConnections) {
        newConnections.forEach(connection -> {
            addConnection(connection);
        });
    }

    /**
     * Adiciona uma nova conexão na lista de conexões.
     *
     * @param newConnection
     */
    public void addConnection(Controller newConnection) {
        if (!connectionExists(newConnection)) {
            ALL_CONNECTIONS.add(newConnection);
        }
    }

    /**
     * Verifica a existencia da conexão <i>connection</i> na lista de conexões
     * ativas.
     *
     * @param connection
     * @return <i>true</i> se a conexão existe. <i>false</i> caso a conexão não
     * exista.
     */
    public static boolean connectionExists(Controller connection) {
        return ALL_CONNECTIONS.contains(connection);
    }

    /**
     * Finaliza e remove esta conexão da lista.
     *
     * @throws IOException
     */
    private void close() throws IOException {
        ALL_CONNECTIONS.remove(this.controler);
        this.controler.close(true);
    }

    /**
     * Finaliza a conexão. Após isto, faz a interrupção da thread.
     */
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

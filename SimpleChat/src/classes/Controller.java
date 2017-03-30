package classes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @class Controler
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 25/03/2017
 */
public class Controller implements Serializable {

    private final Socket socket;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    private boolean conectionOpen;

    public Controller(String destino, int porta) throws IOException {
        this(new Socket(destino, porta));
    }

    public Controller(Socket socket) throws IOException {
        this.socket = socket;
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(this.socket.getInputStream());
        this.conectionOpen = true;
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public ObjectInputStream getInput() {
        return input;
    }

    /**
     * Faz o envio de uma mensagem para o servidor.
     *
     * @param msg
     * @return <i>true</i> se a mensagem foi enviada com sucesso. <i>false</i>
     * caso houve algum problema no envio.
     */
    public boolean enviar(Mensagem msg) {
        try {
            if (isConectionOpen()) {
                output.writeObject(msg);
                output.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Aguarda o recebimento de uma mensagem do servidor.
     *
     * @return <i>mensagem</i> do servidor.
     */
    public Mensagem receber() {
        try {
            if (isConectionOpen()) {
                return (Mensagem) input.readObject();
            }
            return new Mensagem("finish_connection", null);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Finaliza esta conexão. Caso <i>sendSignalToClose</i> seja <i>true</i> é
     * feito o envio de uma mensagem com o sinal de finalização.
     *
     * @param sendSignalToClose
     * @throws IOException
     */
    public void close(boolean sendSignalToClose) throws IOException {
        if (sendSignalToClose) {
            enviar(new Mensagem("finish_connection", null));
        }
        this.conectionOpen = false;
        this.output.close();
        this.input.close();
        this.socket.close();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Controller other = (Controller) obj;
        return Objects.equals(this.socket, other.socket);
    }

    public boolean isConectionOpen() {
        return conectionOpen;
    }

}

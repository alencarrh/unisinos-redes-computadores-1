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
public class Controler implements Serializable {

    private final Socket socket;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    private boolean conectionOpen;

    public Controler(String destino, int porta) throws IOException {
        this(new Socket(destino, porta));
    }

    public Controler(Socket socket) throws IOException {
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

    public boolean enviar(Mensagem msg) {
        try {
            if (isConectionOpen()) {
                output.writeObject(msg);
                output.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(Controler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public Mensagem receber() {
        try {
            if (isConectionOpen()) {
                return (Mensagem) input.readObject();
            }
            return new Mensagem("finish_connection", null);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Controler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

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
        final Controler other = (Controler) obj;
        return Objects.equals(this.socket, other.socket);
    }

    public boolean isConectionOpen() {
        return conectionOpen;
    }

}

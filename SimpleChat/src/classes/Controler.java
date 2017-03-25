/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Objects;

/**
 *
 * @author alenc
 */
public class Controler implements Serializable {

    private final Socket socket;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    private boolean conectionOpen;

    public Controler(String destino, int porta) throws IOException {
        this.socket = new Socket(destino, porta);
        this.output = new ObjectOutputStream(this.socket.getOutputStream());
        this.input = new ObjectInputStream(this.socket.getInputStream());
        this.conectionOpen = true;
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
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public Mensagem receber() {
        try {
            if (isConectionOpen()) {
                return (Mensagem) input.readObject();
            }
            return new Mensagem("", "FIM");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
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

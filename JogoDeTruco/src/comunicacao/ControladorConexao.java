package comunicacao;

import enums.AcaoDaMensagem;
import enums.DirecaoDaMensagem;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @class ConnectionController
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 31/03/2017
 */
public class ControladorConexao implements Serializable {

    private final Socket socket;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    private final DirecaoDaMensagem direcaoDaMensagem;
    private boolean conectionOpen;

    public ControladorConexao(String destino, int porta, DirecaoDaMensagem direcaoDaMensagem) throws IOException {
        this(new Socket(destino, porta), direcaoDaMensagem);
    }

    public ControladorConexao(Socket socket, DirecaoDaMensagem direcaoDaMensagem) throws IOException {
        this.socket = socket;
        this.direcaoDaMensagem = direcaoDaMensagem;
        this.output = new ObjectOutputStream(this.socket.getOutputStream());
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
    public boolean enviar(Mensagem msg) throws IOException {
        if (isConectionOpen()) {
            output.writeObject(msg);
            output.flush();
        }
        return true;
    }

    /**
     * Aguarda o recebimento de uma mensagem do servidor.
     *
     * @return <i>mensagem</i> do servidor.
     */
    public Mensagem receber() throws IOException, ClassNotFoundException {
        if (isConectionOpen()) {
            return (Mensagem) input.readObject();
        }
        return new Mensagem(this.direcaoDaMensagem, AcaoDaMensagem.FINALIZAR_CONEXAO);

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
            enviar(new Mensagem(this.direcaoDaMensagem, AcaoDaMensagem.FINALIZAR_CONEXAO));
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
        final ControladorConexao other = (ControladorConexao) obj;
        return Objects.equals(this.socket, other.socket);
    }

    public boolean isConectionOpen() {
        return conectionOpen;
    }
}

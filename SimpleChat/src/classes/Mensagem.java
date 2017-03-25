package classes;

import java.io.IOException;
import java.io.Serializable;

/**
 * @class Mensagem
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 25/03/2017 
 */
public class Mensagem implements Serializable {

    private final String id;
    private String mensagem;

    public Mensagem(String id) throws IOException {
        this.id = id;
    }

    public Mensagem(String id, String mensagem) {
        this.id = id;
        this.mensagem = mensagem;
    }

    public String getId() {
        return id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @Override
    public String toString() {
        return this.id + "# " + this.mensagem;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author alenc
 */
public class Mensagem implements Serializable {

    private final String id;
    private String mensagem;

    public Mensagem(String id, String destino, int porta) throws IOException {
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

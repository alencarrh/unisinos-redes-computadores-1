package comunicacao.transporte;

import java.io.Serializable;

/**
 * @class Info
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 18/04/2017
 */
public class Info implements Serializable {

    private String informacao;

    public Info() {
    }

    public Info(String informacao) {
        this.informacao = informacao;
    }

    public String getInformacao() {
        return informacao;
    }

    public void setInformacao(String informacao) {
        this.informacao = informacao;
    }

    @Override
    public String toString() {
        return "Info{" + "informacao=" + informacao + '}';
    }

}

package comunicacao;

import enums.AcaoDaMensagem;
import java.io.Serializable;

/**
 * @param <T>
 * @class Mensagem
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 30/03/2017
 */
public class Mensagem<T extends Serializable> implements Serializable {

    private final AcaoDaMensagem acaoDaMensagem;
    private final T valor;

    public Mensagem(AcaoDaMensagem acaoDaMensagem, T valor) {
        this.acaoDaMensagem = acaoDaMensagem;
        this.valor = valor;
    }

    public AcaoDaMensagem getAcaoDaMensagem() {
        return acaoDaMensagem;
    }

    public T getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "Mensagem{" + "acaoDaMensagem=" + acaoDaMensagem + ", valor=" + valor + '}';
    }

}

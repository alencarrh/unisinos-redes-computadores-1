package comunicacao;

import enums.AcaoDaMensagem;
import enums.DirecaoDaMensagem;
import java.io.Serializable;

/**
 * @class Mensagem
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 30/03/2017
 */
public class Mensagem implements Serializable {

    private final AcaoDaMensagem acaoDaMensagem;

    public Mensagem(AcaoDaMensagem acaoDaMensagem) {
        this.acaoDaMensagem = acaoDaMensagem;
    }

    public AcaoDaMensagem getAcaoDaMensagem() {
        return acaoDaMensagem;
    }

    @Override
    public String toString() {
        return "Mensagem{" + "acaoDaMensagem=" + acaoDaMensagem + '}';
    }

}

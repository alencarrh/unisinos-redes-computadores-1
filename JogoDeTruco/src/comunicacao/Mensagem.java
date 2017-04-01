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

    private final DirecaoDaMensagem direcaoDaMensagem;
    private final AcaoDaMensagem acaoDaMensagem;

    public Mensagem(DirecaoDaMensagem direcaoDaMensagem, AcaoDaMensagem acaoDaMensagem) {
        this.direcaoDaMensagem = direcaoDaMensagem;
        this.acaoDaMensagem = acaoDaMensagem;
    }

    public DirecaoDaMensagem getDirecaoDaMensagem() {
        return direcaoDaMensagem;
    }

    public AcaoDaMensagem getAcaoDaMensagem() {
        return acaoDaMensagem;
    }

    @Override
    public String toString() {
        return "Mensagem{" + "direcaoDaMensagem=" + direcaoDaMensagem + ", acaoDaMensagem=" + acaoDaMensagem + '}';
    }

}

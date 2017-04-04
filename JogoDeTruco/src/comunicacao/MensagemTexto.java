package comunicacao;

import enums.AcaoDaMensagem;
import enums.DirecaoDaMensagem;

/**
 * @class MensagemTexto
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 04/04/2017
 */
public class MensagemTexto extends Mensagem {

    private String texto;

    public MensagemTexto(DirecaoDaMensagem direcaoDaMensagem, AcaoDaMensagem acaoDaMensagem) {
        super(direcaoDaMensagem, acaoDaMensagem);
    }

    public MensagemTexto(String texto, DirecaoDaMensagem direcaoDaMensagem, AcaoDaMensagem acaoDaMensagem) {
        super(direcaoDaMensagem, acaoDaMensagem);
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return "MensagemTexto{" + "texto=" + texto + '}';
    }

}

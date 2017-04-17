package comunicacao;

import enums.AcaoDaMensagem;

/**
 * @class MensagemTexto
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 04/04/2017
 */
public class MensagemTexto extends Mensagem {

    private String texto;

    public MensagemTexto(AcaoDaMensagem acaoDaMensagem) {
        super(acaoDaMensagem);
    }

    public MensagemTexto(String texto, AcaoDaMensagem acaoDaMensagem) {
        super(acaoDaMensagem);
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

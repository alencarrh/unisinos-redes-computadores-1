package comunicacao;

import enums.AcaoDaJogada;
import enums.AcaoDaMensagem;
import enums.Carta;

/**
 * @class MensagemJogada
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 03/04/2017
 */
public class MensagemJogada extends Mensagem {

    private final AcaoDaJogada acaoDaJogada;
    private final Carta carta;

    public MensagemJogada(AcaoDaJogada acaoDaJogada, Carta carta, AcaoDaMensagem acaoDaMensagem) {
        super(acaoDaMensagem);
        this.acaoDaJogada = acaoDaJogada;
        this.carta = carta;
    }

    public AcaoDaJogada getAcaoDaJogada() {
        return acaoDaJogada;
    }

    public Carta getCarta() {
        return carta;
    }

}

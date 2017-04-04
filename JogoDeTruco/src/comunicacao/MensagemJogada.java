package comunicacao;

import enums.AcaoDaJogada;
import enums.AcaoDaMensagem;
import enums.Carta;
import enums.DirecaoDaMensagem;

/**
 * @class MensagemJogada
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 03/04/2017
 */
public class MensagemJogada extends Mensagem {

    private AcaoDaJogada acaoDaJogada;
    private Carta carta;

    public MensagemJogada(DirecaoDaMensagem direcaoDaMensagem, AcaoDaMensagem acaoDaMensagem) {
        super(direcaoDaMensagem, acaoDaMensagem);
    }

}

package comunicacao;

import enums.AcaoDaMensagem;
import enums.DirecaoDaMensagem;
import java.io.Serializable;

/**
 * @class MensagemEntrarEmPartida
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 02/04/2017
 */
public class MensagemEntrarEmPartida extends Mensagem implements Serializable {

    private final String idSala;

    public MensagemEntrarEmPartida(DirecaoDaMensagem direcaoDaMensagem, AcaoDaMensagem acaoDaMensagem, String idSala) {
        super(direcaoDaMensagem, acaoDaMensagem);
        this.idSala = idSala;
    }

    public String getIdSala() {
        return idSala;
    }

    @Override
    public String toString() {
        return "MensagemEntrarEmPartida{" + "idSala=" + idSala + '}';
    }
    
    

}

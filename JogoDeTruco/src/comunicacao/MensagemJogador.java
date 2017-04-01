package comunicacao;

import enums.AcaoDaMensagem;
import enums.DirecaoDaMensagem;
import java.io.Serializable;
import jogo.Jogador;

/**
 * @class MensagemJogador
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 01/04/2017
 */
public class MensagemJogador extends Mensagem implements Serializable {

    private String nomeJogador;
    private Long idJogador;
    private int tentos;

    public MensagemJogador(String nomeJogador, Long idJogador, int tentos, DirecaoDaMensagem direcaoDaMensagem, AcaoDaMensagem acaoDaMensagem) {
        super(direcaoDaMensagem, acaoDaMensagem);
        this.nomeJogador = nomeJogador;
        this.idJogador = idJogador;
        this.tentos = tentos;
    }

    public MensagemJogador(DirecaoDaMensagem direcaoDaMensagem, AcaoDaMensagem acaoDaMensagem) {
        super(direcaoDaMensagem, acaoDaMensagem);
    }

    public MensagemJogador(DirecaoDaMensagem direcaoDaMensagem, AcaoDaMensagem acaoDaMensagem, Jogador jogador) {
        super(direcaoDaMensagem, acaoDaMensagem);
        this.nomeJogador = jogador.getNomeJogador();
        this.idJogador = jogador.getIdJogador();
        this.tentos = jogador.getTentos();
    }

    public String getNomeJogador() {
        return nomeJogador;
    }

    public void setNomeJogador(String nomeJogador) {
        this.nomeJogador = nomeJogador;
    }

    public Long getIdJogador() {
        return idJogador;
    }

    public void setIdJogador(Long idJogador) {
        this.idJogador = idJogador;
    }

    public int getTentos() {
        return tentos;
    }

    public void setTentos(int tentos) {
        this.tentos = tentos;
    }

    @Override
    public String toString() {
        return "MensagemJogador{" + "nomeJogador=" + nomeJogador + ", idJogador=" + idJogador + ", tentos=" + tentos + '}';
    }

}

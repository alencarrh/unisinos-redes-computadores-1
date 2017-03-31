package comunicacao;

import jogo.Jogada;
import enums.AcaoDaMensagem;
import enums.DirecaoDaMensagem;
import jogo.Jogador;

/**
 * @class Mensagem
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 30/03/2017
 */
public class Mensagem {

    private final DirecaoDaMensagem direcaoDaMensagem;
    private final AcaoDaMensagem acaoDaMensagem;
    private final Jogador jogador;
    private final Jogada jogada;
    private String opcao;

    public Mensagem(DirecaoDaMensagem direcaoDaMensagem, AcaoDaMensagem acaoDaMensagem, Jogador jogador, Jogada jogada) {
        this.direcaoDaMensagem = direcaoDaMensagem;
        this.acaoDaMensagem = acaoDaMensagem;
        this.jogador = jogador;
        this.jogada = jogada;
    }

    public DirecaoDaMensagem getDirecaoDaMensagem() {
        return direcaoDaMensagem;
    }

    public AcaoDaMensagem getAcaoDaMensagem() {
        return acaoDaMensagem;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public Jogada getJogada() {
        return jogada;
    }

    public String getOpcao() {
        return opcao;
    }

    public void setOpcao(String opcao) {
        this.opcao = opcao;
    }

}

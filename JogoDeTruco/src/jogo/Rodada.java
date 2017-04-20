package jogo;

import comunicacao.transporte.JogadorInfo;
import comunicacao.transporte.RodadaInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Rodada: É a fração da "Mão", em cada rodada os jogadores mostram uma carta.
 *
 * @class Rodada
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 31/03/2017
 */
public class Rodada implements Serializable {

    private Jogador jogadorGanhador;
    private final List<Jogada> jogadas;
    private boolean empatou;

    public Rodada() {
        jogadas = new ArrayList<>();
        this.empatou = false;
    }

    public List<Jogada> getJogadas() {
        return jogadas;
    }

    public Jogador getJogadorGanhador() {
        return jogadorGanhador;
    }

    public void setJogadorGanhador(Jogador jogadorGanhador) {
        this.jogadorGanhador = jogadorGanhador;
    }

    public RodadaInfo getInfoRodada() {
        return new RodadaInfo(this);
    }

    public boolean isEmpatou() {
        return empatou;
    }

    public void setEmpatou(boolean empatou) {
        this.empatou = empatou;
    }

    /**
     *
     * @param jogador
     * @return
     */
    public Jogada getUtilmaJogadaDoJogador(JogadorInfo jogador) {
        return getInfoRodada().getUtilmaJogadaDoJogador(jogador);
    }

    public Jogada getUtilmaJogadaDoJogador(Jogador jogador) {
        return getInfoRodada().getUtilmaJogadaDoJogador(jogador.getInfoJogador());
    }
    /**
     * TODO: métodos para somar pontuação caso a rodada seja de envido ou flor.
     * Caso contrário, será uma rodada simples ou de chamada de
     * Truco/Retruco/ValeQuatro que não somam pontos na rodada e para na
     * partida.
     */
    /**
     * TODO: pensar se vale a pena ir incrementando a pontuação a cada ação ou
     * se vale mais a pensa calcular a pontuação no final da mão. (Na questão
     * dos envidos, acho que seria interessante calcular logo no final da
     * Rodada)
     */
}

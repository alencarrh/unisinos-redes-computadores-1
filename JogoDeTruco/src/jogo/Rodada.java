package jogo;

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
    private int tentosDaRodada;

    public Rodada() {
        jogadas = new ArrayList<>();
        tentosDaRodada = 0;
    }

    public List<Jogada> getJogadas() {
        return jogadas;
    }

    public int getTentosDaRodada() {
        return tentosDaRodada;
    }

    public void aumentarTentosDaRodada(int tentosAAumentar) {
        this.tentosDaRodada += tentosAAumentar;
    }

    public Jogador getJogadorGanhador() {
        return jogadorGanhador;
    }

    public void setJogadorGanhador(Jogador jogadorGanhador) {
        this.jogadorGanhador = jogadorGanhador;
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

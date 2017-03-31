package jogo;

import enums.AcaoDaJogada;
import enums.Carta;

/**
 * Jogada: É a ação de um dos jogadores. Seja esta ação um jogada simples ou
 * chamada de Truco e seus aumentos, Envido e seus aumentos ou Flor e seus
 * aumentos.
 *
 * @class Jogada
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 30/03/2017
 */
public class Jogada {

    private final AcaoDaJogada acaoDaJogada;
    private final Carta carta;

    public Jogada(AcaoDaJogada acaoDaJogada, Carta carta) {
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

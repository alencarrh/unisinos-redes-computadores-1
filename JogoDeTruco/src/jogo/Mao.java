package jogo;

import enums.Carta;

/**
 * @class Mao
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 30/03/2017
 */
public class Mao {

    private final Carta carta1;
    private final Carta carta2;
    private final Carta carta3;

    public Mao(Carta carta1, Carta carta2, Carta carta3) {
        this.carta1 = carta1;
        this.carta2 = carta2;
        this.carta3 = carta3;
    }

    public Carta getCarta1() {
        return carta1;
    }

    public Carta getCarta2() {
        return carta2;
    }

    public Carta getCarta3() {
        return carta3;
    }

}

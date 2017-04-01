package jogo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Mão: Fração da partida, vale 1 ponto e poderá ter seu valor aumentado através
 * das disputas de Truco e Envido. É disputada em melhor de 3 rodadas.
 *
 * @class Mao
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 30/03/2017
 */
public class Mao implements Serializable {

    private final List<Rodada> rodadas;
    private int tentos;

    public Mao() {
        rodadas = new ArrayList<>();
        tentos = 1;
    }

    public int getTentos() {
        return tentos;
    }

    public void aumentarTentosDaMao(int tentosAAumentar) {
        this.tentos += tentosAAumentar;
    }

    public List<Rodada> getRodadas() {
        return rodadas;
    }

}

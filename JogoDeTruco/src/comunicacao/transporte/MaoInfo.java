package comunicacao.transporte;

import enums.EstadoDaMao;
import java.io.Serializable;
import java.util.List;
import jogo.Jogador;
import jogo.Mao;
import jogo.Rodada;

/**
 * @class MaoInfo
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 19/04/2017
 */
public class MaoInfo implements Serializable {

    private final List<Rodada> rodadas;
    private final EstadoDaMao estadoDaMao;
    private final Jogador jogadorGanhador;

    public MaoInfo(Mao mao) {
        this.rodadas = mao.getRodadas();
        this.estadoDaMao = mao.getEstadoDaMao();
        this.jogadorGanhador = mao.getJogadorGanhador();
    }

    public List<Rodada> getRodadas() {
        return rodadas;
    }

    public EstadoDaMao getEstadoDaMao() {
        return estadoDaMao;
    }

    public Jogador getJogadorGanhador() {
        return jogadorGanhador;
    }

}

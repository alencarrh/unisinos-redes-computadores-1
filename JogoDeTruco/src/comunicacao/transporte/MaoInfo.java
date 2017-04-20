package comunicacao.transporte;

import enums.EstadoDaMao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jogo.Mao;

/**
 * @class MaoInfo
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 19/04/2017
 */
public class MaoInfo implements Serializable {

    private final List<RodadaInfo> rodadas;
    private final EstadoDaMao estadoDaMao;
    private final JogadorInfo jogadorGanhador;

    public MaoInfo(Mao mao) {
        rodadas = new ArrayList<>();
        mao.getRodadas().stream().forEach(rodada -> {
            rodadas.add(rodada.getInfoRodada());
        });
        this.estadoDaMao = mao.getEstadoDaMao();
        this.jogadorGanhador = mao.getJogadorGanhador() != null ? mao.getJogadorGanhador().getInfoJogador() : null;
    }

    public List<RodadaInfo> getRodadas() {
        return rodadas;
    }

    public EstadoDaMao getEstadoDaMao() {
        return estadoDaMao;
    }

    public JogadorInfo getJogadorGanhador() {
        return jogadorGanhador;
    }

    @Override
    public String toString() {
        return "MaoInfo{" + "rodadas=" + rodadas + ", estadoDaMao=" + estadoDaMao + ", jogadorGanhador=" + jogadorGanhador + '}';
    }
    
    

}

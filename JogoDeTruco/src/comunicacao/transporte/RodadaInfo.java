package comunicacao.transporte;

import java.io.Serializable;
import java.util.List;
import jogo.Jogada;
import jogo.Rodada;

/**
 * @class RodadaInfo
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 18/04/2017
 */
public class RodadaInfo implements Serializable {

    private List<Jogada> jogadas;
    private JogadorInfo jogadorGanhador;
    private boolean empatou;

    public RodadaInfo(Rodada rodada) {
        this.jogadas = rodada.getJogadas();
        this.jogadorGanhador = rodada.getJogadorGanhador() != null ? rodada.getJogadorGanhador().getInfoJogador() : null;
        this.empatou = rodada.isEmpatou();
    }

    public RodadaInfo() {
    }

    public List<Jogada> getJogadas() {
        return jogadas;
    }

    public void setJogadas(List<Jogada> jogadas) {
        this.jogadas = jogadas;
    }

    public JogadorInfo getJogadorGanhador() {
        return jogadorGanhador;
    }

    public void setJogadorGanhador(JogadorInfo jogadorGanhador) {
        this.jogadorGanhador = jogadorGanhador;
    }

    public Jogada getUtilmaJogadaDoJogador(JogadorInfo jogadorInfo) {
        for (int i = jogadas.size() - 1; i >= 0; i--) {
            if (jogadas.get(i).getJogadorInfo().equals(jogadorInfo)) {
                return jogadas.get(i);
            }
        }
        return null;
    }

    public boolean isEmpatou() {
        return empatou;
    }

    public void setEmpatou(boolean empatou) {
        this.empatou = empatou;
    }

    @Override
    public String toString() {
        return "RodadaInfo{" + "jogadas=" + jogadas + ", jogadorGanhador=" + jogadorGanhador + '}';
    }

}

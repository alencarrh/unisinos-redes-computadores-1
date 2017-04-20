package comunicacao.transporte;

import java.io.Serializable;
import java.util.Objects;
import jogo.Jogador;
import jogo.Partida;

/**
 * @class PartidaInfo
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 17/04/2017
 */
public class PartidaInfo implements Serializable {

    private final Long idPartida;
    private final String nomePartida;
    private final JogadorInfo jogador1;
    private final JogadorInfo jogador2;
    private final JogadorInfo jogadorVencedor;

    public PartidaInfo(Long idPartida, String nomePartida, Jogador jogador1) {
        this.idPartida = idPartida;
        this.nomePartida = nomePartida;
        this.jogador1 = jogador1 == null ? null : new JogadorInfo(jogador1);
        this.jogador2 = null;
        this.jogadorVencedor = null;
    }

    public PartidaInfo(Long idPartida, String nomePartida, Jogador jogador1, Jogador jogador2) {
        this.idPartida = idPartida;
        this.nomePartida = nomePartida;
        this.jogador1 = new JogadorInfo(jogador1);
        this.jogador2 = new JogadorInfo(jogador2);
        this.jogadorVencedor = null;
    }

    public PartidaInfo(Partida partida) {
        this.idPartida = partida.getIdPartida();
        this.nomePartida = partida.getNomePartida();
        this.jogador1 = partida.getJogadores().get(0).getInfoJogador();
        this.jogador2 = partida.getJogadores().size() == 2 ? partida.getJogadores().get(1).getInfoJogador() : null;
        this.jogadorVencedor = partida.getIndiceJogadorVencedor() == -1 ? null : partida.getIndiceJogadorVencedor() == 0 ? this.jogador1 : this.jogador2;
    }

    public Long getIdPartida() {
        return idPartida;
    }

    public String getNomePartida() {
        return nomePartida;
    }

    public JogadorInfo getJogador1() {
        return jogador1;
    }

    public JogadorInfo getJogador2() {
        return jogador2;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    public JogadorInfo getJogadorVencedor() {
        return jogadorVencedor;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PartidaInfo other = (PartidaInfo) obj;
        return Objects.equals(this.idPartida, other.idPartida);
    }

    @Override
    public String toString() {
        return "PartidaInfo{" + "idPartida=" + idPartida + ", nomePartida=" + nomePartida + ", criadorDaPartida=" + jogador1 + ", outroJogador=" + jogador2 + '}';
    }

}

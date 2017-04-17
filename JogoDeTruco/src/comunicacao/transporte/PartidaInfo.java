package comunicacao.transporte;

import java.io.Serializable;
import java.util.Objects;
import jogo.Jogador;

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

    public PartidaInfo(Long idPartida, String nomePartida, Jogador jogador1) {
        this.idPartida = idPartida;
        this.nomePartida = nomePartida;
        this.jogador1 = jogador1 == null ? null : new JogadorInfo(jogador1);
        this.jogador2 = null;
    }

    public PartidaInfo(Long idPartida, String nomePartida, Jogador jogador1, Jogador jogador2) {
        this.idPartida = idPartida;
        this.nomePartida = nomePartida;
        this.jogador1 = new JogadorInfo(jogador1);
        this.jogador2 = new JogadorInfo(jogador2);
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

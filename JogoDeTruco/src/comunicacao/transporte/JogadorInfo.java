package comunicacao.transporte;

import enums.Carta;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jogo.Jogador;

/**
 * @class JogadorInfo
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 17/04/2017
 */
public class JogadorInfo implements Serializable {

    private final Long idJogador;
    private String nomeJogador;
    private int tentos;
    private final List<Carta> cartas;

    public JogadorInfo(Long idJogador) {
        this.idJogador = idJogador;
        this.cartas = null;
    }

    public JogadorInfo(Jogador jogador) {
        this.idJogador = jogador.getIdJogador();
        this.nomeJogador = jogador.getNomeJogador();
        this.tentos = jogador.getTentos();
        if (jogador.getCartas() != null) {
            this.cartas = new ArrayList<>(jogador.getCartas());
        } else {
            this.cartas = null;
        }
    }

    public Long getIdJogador() {
        return idJogador;
    }

    public String getNomeJogador() {
        return nomeJogador;
    }

    public void setNomeJogador(String nomeJogador) {
        this.nomeJogador = nomeJogador;
    }

    public int getTentos() {
        return tentos;
    }

    public void setTentos(int tentos) {
        this.tentos = tentos;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final JogadorInfo other = (JogadorInfo) obj;
        return Objects.equals(this.idJogador, other.idJogador);
    }

    @Override
    public String toString() {
        return "JogadorInfo{" + "idJogador=" + idJogador + ", nomeJogador=" + nomeJogador + ", tentos=" + tentos + ", cartas=" + cartas + '}';
    }

}

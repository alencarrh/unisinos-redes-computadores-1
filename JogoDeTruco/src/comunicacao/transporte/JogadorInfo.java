package comunicacao.transporte;

import enums.Carta;
import java.io.Serializable;
import java.util.List;
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
    private List<Carta> cartas;

    public JogadorInfo(Long idJogador) {
        this.idJogador = idJogador;
    }

    public JogadorInfo(Jogador jogador) {
        this.idJogador = jogador.getIdJogador();
        this.nomeJogador = jogador.getNomeJogador();
        this.tentos = jogador.getTentos();
        this.cartas = jogador.getCartas();
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

    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    }

    @Override
    public String toString() {
        return "JogadorInfo{" + "idJogador=" + idJogador + ", nomeJogador=" + nomeJogador + ", tentos=" + tentos + ", cartas=" + cartas + '}';
    }

}

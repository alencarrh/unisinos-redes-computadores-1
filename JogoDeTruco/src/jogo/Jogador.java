package jogo;

import comunicacao.ControladorConexao;
import enums.Carta;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @class Player
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 30/03/2017
 */
public class Jogador extends Thread implements Serializable {

    private final Long idJogador;
    private final ControladorConexao conexao;
    private String nomeJogador;
    private int tentos;
<<<<<<< HEAD
    private Carta[] cartas;
    private Boolean podeJogar;
=======
    private List<Carta> cartas;
>>>>>>> c91d0128a786c5ff11aafc32a7c8a567fda651cf

    public Jogador(Long idPlayer, String nome, ControladorConexao conexao) {
        this.idJogador = idPlayer;
        this.nomeJogador = nome;
        this.conexao = conexao;
        this.tentos = 0;
        this.podeJogar = false;
    }

    @Override
    public void run() {

    }

    public Long getIdJogador() {
        return idJogador;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    }

    public int getTentos() {
        return tentos;
    }

    public void setTentos(int tentos) {
        this.tentos = tentos;
    }

    public ControladorConexao getConexao() {
        return conexao;
    }

    public Boolean getPodeJogar() {
        return podeJogar;
    }

    public void setPodeJogar(Boolean podeJogar) {
        this.podeJogar = podeJogar;
    }

    public String getNomeJogador() {
        return nomeJogador;
    }

    public void setNomeJogador(String nomeJogador) {
        this.nomeJogador = nomeJogador;
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
        final Jogador other = (Jogador) obj;
        return Objects.equals(this.conexao, other.conexao);
    }

    @Override
    public String toString() {
        return "Jogador{" + "idJogador=" + idJogador + ", nomeJogador=" + nomeJogador + ", tentos=" + tentos + '}';
    }

}

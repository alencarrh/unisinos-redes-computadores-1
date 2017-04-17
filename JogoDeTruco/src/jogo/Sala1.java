package jogo;

import enums.StatusDaPartida;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @class Sala
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 31/03/2017
 */
public class Sala1 implements Serializable {

    
    /////REMOVER ESTA CLASSE
    
    
    private final Long idSala;
    private final String nome;
//    private final Partida partida;

    public Sala1(Long idSala) {
        this.idSala = idSala;
//        this.partida = null;
        this.nome = null;
    }

    public Sala1(Long idSala, String nome, Jogador primeiroJogador) {
        this.idSala = idSala;
        this.nome = nome;
//        this.partida = new Partida(primeiroJogador);
    }

//    public List<Jogador> getJogagores() {
//        return this.partida.getJogadores();
//    }
    /**
     *
     * @param outroJogador jogador a ser adicionado a sala
     * @return <i>true</i> se se o jogador foi adicionado. <i>false</i> casa a
     * sala já esteje cheia.
     */
    public boolean addJogador(Jogador outroJogador) {
//        if (StatusDaPartida.AGUARDANDO_JOGADOR.equals(this.partida.getStatus())) {
//            this.partida.addJogador(outroJogador);
//            this.partida.start();
//            return true;
//        }
        return false;
    }

    public StatusDaPartida getStatus() {
//        return this.partida.getStatus();
        return null;
    }

    public Long getIdSala() {
        return idSala;
    }

    public String getNome() {
        return nome;
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
        final Sala1 other = (Sala1) obj;
        return Objects.equals(this.idSala, other.idSala);
    }

}

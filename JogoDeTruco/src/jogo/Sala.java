package jogo;

import enums.StatusDaPartida;
import java.util.List;

/**
 * @class Sala
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 31/03/2017
 */
public class Sala {

    private StatusDaPartida status;
    private final Long idSala;
    private final String nome;
    private Partida partida;

    public Sala(Long idSala, String nome, Jogador primeiroJogador) {
        this.idSala = idSala;
        this.nome = nome;
        this.partida = new Partida(primeiroJogador);
        this.status = StatusDaPartida.AGUARDANDO_JOGADOR;
    }

    public List<Jogador> getJogagores() {
        return this.partida.getJogadores();
    }

    /**
     *
     * @param outroJogador jogador a ser adicionado a sala
     * @return <i>true</i> se se o jogador foi adicionado. <i>false</i> casa a
     * sala já esteje cheia.
     */
    public boolean addJogador(Jogador outroJogador) {
        if (StatusDaPartida.AGUARDANDO_JOGADOR.equals(this.status)) {
            this.partida.addJogador(outroJogador);
            this.partida.start();
            this.status = StatusDaPartida.EM_ANDAMENTO;
            return true;
        }
        return false;
    }

    public StatusDaPartida getStatus() {
        return status;
    }

    public Long getIdSala() {
        return idSala;
    }

    public String getNome() {
        return nome;
    }

}

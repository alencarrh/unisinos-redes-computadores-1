package jogo;

import comunicacao.transporte.MaoInfo;
import enums.EstadoDaMao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Mão: Fração da partida, vale 1 ponto e poderá ter seu valor aumentado através
 * das disputas de Truco e Envido. É disputada em melhor de 3 rodadas.
 *
 * @class Mao
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 30/03/2017
 */
public class Mao implements Serializable {

    private final List<Rodada> rodadas;
    private EstadoDaMao estadoDaMao;
    private Jogador jogadorGanhador;
    private Jogador jogadorTrocouEstado;
    private boolean chamadoEnvido;

    public Mao() {
        this.rodadas = new ArrayList<>();
        this.estadoDaMao = EstadoDaMao.SIMPLES;
    }

    public List<Rodada> getRodadas() {
        return rodadas;
    }

    public EstadoDaMao getEstadoDaMao() {
        return estadoDaMao;
    }

    public void setEstadoDaMao(EstadoDaMao estadoDaMao) {
        this.estadoDaMao = estadoDaMao;
    }

    public Jogador getJogadorGanhador() {
        return jogadorGanhador;
    }

    public void setJogadorGanhador(Jogador jogadorGanhador) {
        this.jogadorGanhador = jogadorGanhador;
    }

    public boolean isChamadoEnvido() {
        return chamadoEnvido;
    }

    public void setChamadoEnvido(boolean chamadoEnvido) {
        this.chamadoEnvido = chamadoEnvido;
    }

    public Jogador getJogadorTrocouEstado() {
        return jogadorTrocouEstado;
    }

    public void setJogadorTrocouEstado(Jogador jogadorTrocouEstado) {
        this.jogadorTrocouEstado = jogadorTrocouEstado;
    }

    public MaoInfo getInfoMao() {
        return new MaoInfo(this);
    }

}

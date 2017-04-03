package jogo;

import enums.AcaoDaJogada;
import enums.Carta;
import enums.Naipe;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Gabriel da Rosa
 */
public class Jogo {

    private Jogada jogada;
    private Mao mao;
    private static final Carta[] CARTAS = Carta.values();

    public Jogo(Jogada jogada, Mao mao) {
        this.jogada = jogada;
        this.mao = mao;
    }

    public Jogo() {
    }

    public void queroTruco() {
        mao.setTentos(2);

    }

    public void naoQueroTruco() {
        mao.setTentos(mao.getTentos() - 1);
    }

    public void retruco() {
        mao.setTentos(3);
    }

    public void naoQueroRetruco() {
        mao.setTentos(mao.getTentos() - 2);
    }

    public void valeQuatro() {
        mao.setTentos(4);
    }

    public void fugirValeQuatro() {
        mao.setTentos(mao.getTentos() - 3);
    }

    public Carta[] darCartas() {
        Carta[] cartas = new Carta[3];
        for (int i = 0; i < cartas.length; i++) {
            cartas[i] = this.encontrarCarta(this.randomNaipe(), this.randomRanking());
        }
        return cartas;
    }

    private Carta encontrarCarta(Naipe naipe, Integer ranking) {
        for (Carta CARTAS1 : CARTAS) {
            if (CARTAS1.getNaipe() == this.randomNaipe() && CARTAS1.getRanking() == ranking) {
                return CARTAS1;
            }
        }
        return null;
    }

    private Naipe randomNaipe() {
        Random random = new Random();
        int numero = random.nextInt(4);
        switch (numero) {
            case 0:
                return Naipe.COPAS;
            case 1:
                return Naipe.ESPADA;
            case 2:
                return Naipe.OURO;
            case 3:
                return Naipe.PAUS;
        }
        return null;
    }

    private Integer randomRanking() {
        return new Random().nextInt(15) + 1;
    }

}

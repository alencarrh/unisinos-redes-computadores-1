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

    private static final Carta[] CARTAS = Carta.values();

    public static void queroTruco(Mao mao) {
        mao.setTentos(2);
    }

    public static void naoQueroTruco(Mao mao) {
        mao.setTentos(mao.getTentos() - 1);
    }

    public static void retruco(Mao mao) {
        mao.setTentos(3);
    }

    public static void naoQueroRetruco(Mao mao) {
        mao.setTentos(mao.getTentos() - 2);
    }

    public static void valeQuatro(Mao mao) {
        mao.setTentos(4);
    }

    public static void fugirValeQuatro(Mao mao) {
        mao.setTentos(mao.getTentos() - 3);
    }

    public static void aceitarEnvido(Jogador jogador) {
        jogador.setTentos(jogador.getTentos() + 2);
    }

    public static void fugirEnvido(Jogador jogador) {
        jogador.setTentos(jogador.getTentos() - 1);
    }

    public static void aceitaRealEnvido(Jogador jogador) {
        jogador.setTentos(jogador.getTentos() + 5);
    }

    public static void fugirRealEnvido(Jogador jogador) {
        jogador.setTentos(jogador.getTentos() - 1);
    }

    public static void fugirFaltaEnvido(Jogador jogador) {
        jogador.setTentos(jogador.getTentos() - 1);
    }

    public static void fugirFaltaRealEnvido(Jogador jogador) {
        jogador.setTentos(jogador.getTentos() - 5);
    }

    public static Carta[] darCartas() {
        Carta[] cartas = new Carta[3];
        for (int i = 0; i < cartas.length; i++) {
            cartas[i] = encontrarCarta(randomNaipe(), randomRanking());
        }
        return cartas;
    }

    public static Boolean podePedirEnvido(Rodada rodada) {
        return rodada.getJogadas().isEmpty();
    }

    public static Boolean isCartaDoMesmoNaipe(Jogador jogador) {
        for (Carta carta : jogador.getCartas()) {
            if (carta == null) {
                return false;
            }
        }
        return jogador.getCartas()[0].getNaipe().equals(jogador.getCartas()[1].getNaipe()) && jogador.getCartas()[0].getNaipe().equals(jogador.getCartas()[2].getNaipe());
    }

    public static Boolean isVencedorPorFlor(Partida partida) {
        return !Jogo.isCartaDoMesmoNaipe(partida.getJogadores().get(0)) && Jogo.isCartaDoMesmoNaipe(partida.getJogadores().get(1));
    }

    public static void contraFlor(Partida partida) {
        for (int i = 0; i < partida.getJogadores().size(); i++) {
            partida.getJogadores().get(i).setTentos(0);
        }
    }

    public static void vencedorContraFlor(Jogador jogador) {
        jogador.setTentos(6);
    }

    public static void fugirFlor(Jogador jogador) {
        jogador.setTentos(-4);
    }

    private static Carta encontrarCarta(Naipe naipe, Integer ranking) {
        for (Carta CARTAS1 : CARTAS) {
            if (CARTAS1.getNaipe() == randomNaipe() && CARTAS1.getRanking() == ranking) {
                return CARTAS1;
            }
        }
        return null;
    }

    private static Naipe randomNaipe() {
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

    private static Integer randomRanking() {
        return new Random().nextInt(15) + 1;
    }

}

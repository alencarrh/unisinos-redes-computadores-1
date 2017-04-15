package jogo;

import enums.Carta;
import enums.EstadoDaMao;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Gabriel da Rosa
 */
public class Jogo {

    public static void aceitouTruco(Mao mao) {
        mao.setEstadoDaMao(EstadoDaMao.TRUCO);
    }

    public static void aceitouRetruco(Mao mao) {
        mao.setEstadoDaMao(EstadoDaMao.RETRUCO);
    }

    public static void aceitouValeQuatro(Mao mao) {
        mao.setEstadoDaMao(EstadoDaMao.VALE_QUATRO);
    }

    public static boolean podeChamarTruco(Mao mao) {
        return EstadoDaMao.SIMPLES.equals(mao.getEstadoDaMao());
    }

    public static boolean podeChamarRetruco(Mao mao) {
        return EstadoDaMao.TRUCO.equals(mao.getEstadoDaMao());
    }

    public static boolean podeChamarValeQuatro(Mao mao) {
        return EstadoDaMao.RETRUCO.equals(mao.getEstadoDaMao());
    }

    public static boolean podeChamarEnvido(Jogador jogador) {
        return jogador.getCartas().size() == 3;
    }

    public static boolean podeChamarFlor(Jogador jogador) {
        if (podeChamarEnvido(jogador)) {
            List<Carta> cartas = jogador.getCartas();
            //Carta1 == Carta2 && Carta1 == Carta3
            return (cartas.get(0).isMesmoNaipe(cartas.get(1))) && (cartas.get(0).isMesmoNaipe(cartas.get(2)));
        }
        return false;
    }

    public static boolean podeChamarContraFlor(Jogador jogador) {
        return podeChamarFlor(jogador);
    }

    public static void darCartas(Jogador jogador1, Jogador jogador2) {
        List<Carta> cartasJogador1 = new ArrayList<>();
        List<Carta> cartasJogador2 = new ArrayList<>();
        List<Carta> cartasDisponiveis = new ArrayList<>(Arrays.asList(Carta.values()));
        do {
            Carta randomCarta = gerarCartaAleatoria(cartasDisponiveis);
            cartasDisponiveis.remove(randomCarta);
            if (cartasJogador1.size() == cartasJogador2.size()) {
                //dar cartas para jogador1
                cartasJogador1.add(randomCarta);
            } else {
                //dar cartas para jogaodor2;
                cartasJogador2.add(randomCarta);
            }
        } while (cartasJogador1.size() < 3 || cartasJogador2.size() < 3);
        jogador1.setCartas(cartasJogador1);
        jogador2.setCartas(cartasJogador2);
    }

    private static Carta gerarCartaAleatoria(List<Carta> cartas) {
        return cartas.get(geraNumeroParaCarta(cartas));
    }

    private static Integer geraNumeroParaCarta(List<Carta> cartas) {
        return new Random().nextInt(cartas.size());
    }

}

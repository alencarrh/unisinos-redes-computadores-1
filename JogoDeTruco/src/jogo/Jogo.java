package jogo;

import enums.Carta;
import enums.EstadoDaMao;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Gabriel da Rosa
 */
public class Jogo {

    private static final Carta[] CARTAS = Carta.values();

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

    public static List<Carta> darCartas() {
        List<Carta> cartas = new ArrayList<>();
        cartas.add(gerarCartaAleatoria());
        cartas.add(gerarCartaAleatoria());
        cartas.add(gerarCartaAleatoria());
        return cartas;
    }

    private static Carta gerarCartaAleatoria() {
        return CARTAS[geraNumeroParaCarta()];
    }

    private static Integer geraNumeroParaCarta() {
        return new Random().nextInt(CARTAS.length);
    }

}
package jogo;

import enums.Carta;
import enums.Naipe;
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
            cartas[i] = gerarCartaAleatoria();
        }
        return cartas;
    }

    public static Boolean podePedirEnvido(Rodada rodada) {
        return rodada.getJogadas().isEmpty();
    }

    public static Boolean possuiFlor(Jogador jogador) {
        for (Carta carta : jogador.getCartas()) {
            if (carta == null) {
                return false;
            }
        }
        return jogador.getCartas()[0].getNaipe().equals(jogador.getCartas()[1].getNaipe()) && jogador.getCartas()[0].getNaipe().equals(jogador.getCartas()[2].getNaipe());
    }

    public static Boolean isVencedorPorFlor(Partida partida) {
        return !Jogo.possuiFlor(partida.getJogadores().get(0)) && Jogo.possuiFlor(partida.getJogadores().get(1));
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
    
//    public static void validacaoEnvido(Jogador jogador1, Jogador jogador2){
//        Cartas[] cartas = jogador1.getCartas();
//    }
    
    private static void validacaoEnvido(Carta[] cartas){
        if(cartas.length != 3){
            new Exception();
        }
        Integer maior= cartas[0].getRanking();
        Boolean temDoMesmoNaipe = false;
        for (int i = 1; i < cartas.length; i++) {
            if(cartas[i].getRanking()>maior){
                maior = cartas[i].getRanking();
            }
        }
//        if(cartas.length >= 2){
//            cartas[0].getNaipe().equals(cartas[1].)
//        }
    }
    
    private static boolean isMesmoNaipe(Carta carta1, Carta carta2){
        return carta1.getNaipe().equals(carta2.getNaipe());
    }

    private static Carta gerarCartaAleatoria() {
        return CARTAS[geraNumeroParaCarta()];
    }

    private static Integer geraNumeroParaCarta() {
        return new Random().nextInt(CARTAS.length);
    }

}

package util;

import comunicacao.Mensagem;
import java.io.IOException;
import java.util.Random;
import jogo.Jogador;

/**
 * @class Util
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 01/04/2017
 */
public class Util {

    /**
     *
     */
    public static final Random RANDOM_GENERATOR = new Random();

    public static String gerarNomeAleatorio() {
        int numeroPalavras = showRandomInteger(5, 10);
        final StringBuilder nomeAleatorio = new StringBuilder();
        for (int i = 0; i < numeroPalavras; i++) {
            if (showRandomInteger(5, 10) % 2 == 0) {
                nomeAleatorio.append(((char) showRandomInteger(65, 90)));
            } else {
                nomeAleatorio.append(((char) showRandomInteger(97, 119)));
            }
        }
        return nomeAleatorio.toString();
    }

    /**
     * Código copiado de http://www.javapractices.com/topic/TopicAction.do?Id=62
     * Generate random numbers > Example 2
     *
     * @param aStart
     * @param aEnd
     * @return
     */
    public static int showRandomInteger(int aStart, int aEnd) {
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long) aEnd - (long) aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * RANDOM_GENERATOR.nextDouble());
        int randomNumber = (int) (fraction + aStart);
        return randomNumber;
    }

    public static boolean isStringEmpty(String string) {
        return isNull(string) || "".equals(string);
    }

    public static boolean isNull(Object object) {
        return object == null;
    }

    public static void printarEnvioInfo(Jogador jogador, Mensagem msg) {
        System.out.println("Servidor enviando(#" + jogador.getNomeJogador() + "): " + msg);
    }

    public static void printarRecebimentoInfo(Jogador jogador, Mensagem msg) {
        System.out.println("Servidor recebeu(#" + jogador.getNomeJogador() + "): " + msg);
    }

    public static void limparCMD() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException ex) {
        }
    }

}

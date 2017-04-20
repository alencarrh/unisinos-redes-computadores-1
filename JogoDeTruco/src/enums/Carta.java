package enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @class Carta
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 30/03/2017
 */
public enum Carta implements Serializable {
    //Quatro melhores
    UM_ESPADA(14, Naipe.ESPADA, "1 Espada", 1), UM_PAUS(13, Naipe.PAUS, "1 Paus", 1), SETE_ESPADA(12, Naipe.ESPADA, "7 Espada", 7), SETE_OURO(11, Naipe.OURO, "7 Ouro", 7),
    //3s
    TRES_ESPADA(10, Naipe.ESPADA, "3 Espada", 3), TRES_PAUS(10, Naipe.PAUS, "3 Paus", 3), TRES_OURO(10, Naipe.OURO, "3 Ouro", 3), TRES_COPAS(10, Naipe.COPAS, "3 Copas", 3),
    //2s
    DOIS_ESPADA(9, Naipe.ESPADA, "2 Espada", 2), DOIS_PAUS(9, Naipe.PAUS, "2 Paus", 2), DOIS_OURO(9, Naipe.OURO, "2 Ouro", 2), DOIS_COPAS(9, Naipe.COPAS, "2 Copas", 2),
    //1s
    UM_OURO(8, Naipe.OURO, "1 Ouro", 1), UM_COPAS(8, Naipe.COPAS, "1 Copas", 1),
    //12s
    DOZE_ESPADA(7, Naipe.ESPADA, "12 Espada", 0), DOZE_PAUS(7, Naipe.PAUS, "12 Paus", 0), DOZE_OURO(7, Naipe.OURO, "12 Ouro", 0), DOZE_COPAS(7, Naipe.COPAS, "12 Copas", 0),
    //11s
    ONZE_ESPADA(6, Naipe.ESPADA, "11 Espada", 0), ONZE_PAUS(6, Naipe.PAUS, "11 Paus", 0), ONZE_OURO(6, Naipe.OURO, "11 Ouro", 0), ONZE_COPAS(6, Naipe.COPAS, "11 Copas", 0),
    //10s
    DEZ_ESPADA(5, Naipe.ESPADA, "10 Espada", 0), DEZ_PAUS(5, Naipe.PAUS, "10 Paus", 0), DEZ_OURO(5, Naipe.OURO, "10 Ouro", 0), DEZ_COPAS(5, Naipe.COPAS, "10 Copas", 0),
    //7s
    SETE_PAUS(4, Naipe.PAUS, "7 Paus", 7), SETE_COPAS(4, Naipe.COPAS, "7 Copas", 7),
    //6s
    SEIS_ESPADA(3, Naipe.ESPADA, "6 Espada", 6), SEIS_PAUS(3, Naipe.PAUS, "6 Paus", 6), SEIS_OURO(3, Naipe.OURO, "6 Ouro", 6), SEIS_COPAS(3, Naipe.COPAS, "6 Copas", 6),
    //5s
    CINCO_ESPADA(2, Naipe.ESPADA, "5 Espada", 5), CINCO_PAUS(2, Naipe.PAUS, "5 Paus", 5), CINCO_OURO(2, Naipe.OURO, "5 Ouro", 5), CINCO_COPAS(2, Naipe.COPAS, "5 Copas", 5),
    //4s
    QUATRO_ESPADA(1, Naipe.ESPADA, "4 Espada", 4), QUATRO_PAUS(1, Naipe.PAUS, "4 Paus", 4), QUATRO_OURO(1, Naipe.OURO, "4 Ouro", 4), QUATRO_COPAS(1, Naipe.COPAS, "4 Copas", 4);

    private final Integer ranking;
    private final Naipe naipe;
    private final String label;
    private final int pontosSomadosNoEnvidoOuFlor;

    private Carta(Integer ranking, Naipe naipe, String label, int pontosSomadosNoEnvidoOuFlor) {
        this.ranking = ranking;
        this.naipe = naipe;
        this.label = label;
        this.pontosSomadosNoEnvidoOuFlor = pontosSomadosNoEnvidoOuFlor;
    }

    public boolean isMesmoNaipe(Carta carta) {
        return this.naipe.equals(carta.getNaipe());
    }

    public Naipe getNaipe() {
        return naipe;
    }

    public int getRanking() {
        return ranking;
    }

    public String getLabel() {
        return label;
    }

    public int getPontosSomadosNoEnvido() {
        return pontosSomadosNoEnvidoOuFlor;
    }

    public static Carta gerarCartaAleatoria() {
        return gerarCartaAleatoria(new ArrayList<>(Arrays.asList(Carta.values())));
    }

    public static Carta gerarCartaAleatoria(List<Carta> cartas) {
        return cartas.get(geraNumeroParaCarta(cartas));
    }

    private static Integer geraNumeroParaCarta(List<Carta> cartas) {
        return new Random().nextInt(cartas.size());
    }

    @Override
    public String toString() {
        return this.label;
    }

}

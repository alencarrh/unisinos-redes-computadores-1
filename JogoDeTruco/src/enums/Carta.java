package enums;

/**
 *
 * @created 30/03/2017
 * @author alencar.hentges (CWI Software)
 */
public enum Carta {
    //Quatro melhores
    UM_ESPADA(14, Naipe.ESPADA), UM_PAUS(13, Naipe.PAUS), SETE_ESPADA(12, Naipe.ESPADA), SETE_OURO(11, Naipe.OURO),
    //3s
    TRES_ESPADA(10, Naipe.ESPADA), TRES_PAUS(10, Naipe.PAUS), TRES_OURO(10, Naipe.OURO), TRES_COPAS(10, Naipe.COPAS),
    //2s
    DOIS_ESPADA(9, Naipe.ESPADA), DOIS_PAUS(9, Naipe.PAUS), DOIS_OURO(9, Naipe.OURO), DOIS_COPAS(9, Naipe.COPAS),
    //1s
    UM_OURO(8, Naipe.OURO), UM_COPAS(8, Naipe.COPAS),
    //12s
    DOZE_ESPADA(7, Naipe.ESPADA), DOZE_PAUS(7, Naipe.PAUS), DOZE_OURO(7, Naipe.OURO), DOZE_COPAS(7, Naipe.COPAS),
    //11s
    ONZE_ESPADA(6, Naipe.ESPADA), ONZE_PAUS(6, Naipe.PAUS), ONZE_OURO(6, Naipe.OURO), ONZE_COPAS(6, Naipe.COPAS),
    //10s
    DEZ_ESPADA(5, Naipe.ESPADA), DEZ_PAUS(5, Naipe.PAUS), DEZ_OURO(5, Naipe.OURO), DEZ_COPAS(5, Naipe.COPAS),
    //7s
    SETE_PAUS(4, Naipe.PAUS), SETE_COPAS(4, Naipe.COPAS),
    //6s
    SEIS_ESPADA(3, Naipe.ESPADA), SEIS_PAUS(3, Naipe.PAUS), SEIS_OURO(3, Naipe.OURO), SEIS_COPAS(3, Naipe.COPAS),
    //5s
    CINCO_ESPADA(2, Naipe.ESPADA), CINCO_PAUS(2, Naipe.PAUS), CINCO_OURO(2, Naipe.OURO), CINCO_COPAS(2, Naipe.COPAS),
    //4s
    QUATRO_ESPADA(1, Naipe.ESPADA), QUATRO_PAUS(1, Naipe.PAUS), QUATRO_OURO(1, Naipe.OURO), QUATRO_COPAS(1, Naipe.COPAS);

    private final Integer ranking;
    private final Naipe naipe;

    private Carta(Integer ranking, Naipe naipe) {
        this.ranking = ranking;
        this.naipe = naipe;
    }

    public Naipe getNaipe() {
        return naipe;
    }

    public int getRanking() {
        return ranking;
    }

    public boolean isMesmoNaipe(Carta carta) {
        return this.naipe.equals(carta.getNaipe());
    }

}

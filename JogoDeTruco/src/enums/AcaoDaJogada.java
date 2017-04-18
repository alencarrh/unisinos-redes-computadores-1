package enums;

import java.io.Serializable;

/**
 * @class AcaoDoJogo
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 30/03/2017
 */
public enum AcaoDaJogada implements Serializable {

    JOGADA_SIMPLES("", "Jogou "),
    QUERO("Quero", "Aceitou"),
    NAO_QUERO("Não Quero", "Recusou"),
    TRUCO("Chamar Truco", "Chamou Truco"),
    RETRUCO("Chamar ReTruco", "Aumentou para ReTruco"),
    VALE_QUATRO("Chamar Vale Quatro", "Aumentou para Vale QUATRO"),
    ENVIDO("Chamar Envido", "Chamou Envido"),
    REAL_ENVIDO("Chamar Real Envido", "Chamou Real Envido"),
    FALTA_ENVIDO("Chamar Falta Envido", "Chamou Falta ENVIDO"),
    FLOR("Chamar Flor", "Chamou FLOR"),
    CONTRA_FLOR("Chamar Contra-Flor", "Chamou CONTRA-FLOR!"),
    CONTRA_FLOR_E_RESTO("Chamar Contra-Flor-e-Resto", "AUMENTOU PARA CONTRA-FLOR-E-RESTO!!"),
    IR_PARA_BARALHO("Correr", "Correu"),
    DAR_AS_CARTAS("Dar as cartas", "Deu as cartas");

    private final String opcaoMenu;
    private final String acaoRealizada;

    private AcaoDaJogada(String toString, String acaoRealizada) {
        this.opcaoMenu = toString;
        this.acaoRealizada = acaoRealizada;
    }

    public String getOpcaoMenu() {
        return opcaoMenu;
    }

    public String getAcaoRealizada() {
        return acaoRealizada;
    }

}

package enums;

import java.io.Serializable;

/**
 * @class EstadoDaMao
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 04/04/2017
 */
public enum EstadoDaMao implements Serializable {

    SIMPLES(1),
    TRUCO(2),
    RETRUCO(3),
    VALE_QUATRO(4);

    private final Integer valorDoEstado;

    private EstadoDaMao(Integer valorDoEstado) {
        this.valorDoEstado = valorDoEstado;
    }

    public Integer getValorDoEstado() {
        return valorDoEstado;
    }

}

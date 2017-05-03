package comunicacao.transporte;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jogo.Jogada;

/**
 * @class MenuAcoes
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 17/04/2017
 */
public class MenuAcoes implements Serializable {

    private final List<Jogada> jogadas;

    public MenuAcoes() {
        jogadas = new ArrayList<>();
    }

    public List<Jogada> getJogadas() {
        return jogadas;
    }

}

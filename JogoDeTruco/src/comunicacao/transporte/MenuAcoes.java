package comunicacao.transporte;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jogo.Jogada;

/**
 *
 * @created 17/04/2017
 * @author alencar.hentges (CWI Software)
 */
public class MenuAcoes implements Serializable {

    private final Jogada jogadaAnterior;
    private final List<Jogada> jogadas;

    public MenuAcoes(Jogada jogadaAnterior) {
        jogadas = new ArrayList<>();
        this.jogadaAnterior = jogadaAnterior;
    }

    public Jogada getJogadaAnterior() {
        return jogadaAnterior;
    }

    public List<Jogada> getJogadas() {
        return jogadas;
    }

}

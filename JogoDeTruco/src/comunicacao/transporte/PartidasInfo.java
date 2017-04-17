package comunicacao.transporte;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jogo.Partida;

/**
 * @class PartidasInfo
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 17/04/2017
 */
public class PartidasInfo implements Serializable {

    private final List<PartidaInfo> partidas;

    public PartidasInfo() {
        this.partidas = new ArrayList<>();
    }

    public PartidasInfo(List<Partida> partidas) {
        this.partidas = new ArrayList<>();
        partidas.stream().forEach((partida) -> {
            this.partidas.add(new PartidaInfo(partida.getIdPartida(), partida.getNomePartida(), partida.getJogadores().get(0)));
        });
    }

    public List<PartidaInfo> getPartidas() {
        return partidas;
    }

    @Override
    public String toString() {
        return "PartidasInfo{" + "partidas=" + partidas + '}';
    }

}

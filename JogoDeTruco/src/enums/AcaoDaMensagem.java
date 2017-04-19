package enums;

import java.io.Serializable;

/**
 * @class Acao
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 30/03/2017
 */
public enum AcaoDaMensagem implements Serializable {

    FINALIZAR_CONEXAO,
    DADOS_JOGADOR,
    ATUALIZAR_DADOS_JOGADOR,
    LISTAR_PARTIDAS,
    ESCOLHER_PARTIDA,
    INICIAR_PARTIDA,
    MOSTRAR_CARTAS,
    JOGAR,
    AGUARDAR_OUTRO_JOGADOR,
    DADOS_RODADA,
    INFORMAR_PERDA_CONEXAO,
    INFO_JOGADA_OPONENTE,

}

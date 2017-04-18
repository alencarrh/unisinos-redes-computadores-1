package servidor;

import comunicacao.Mensagem;
import comunicacao.transporte.JogadorInfo;
import comunicacao.transporte.PartidaInfo;
import comunicacao.transporte.PartidasInfo;
import enums.AcaoDaMensagem;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import jogo.Jogador;
import jogo.Partida;
import util.Util;

/**
 * Classe responável por controler uma única conexão (por objeto).
 *
 * @class JogadorListener
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 01/04/2017
 */
public class JogadorListener extends Thread implements Serializable {

    private static Long idNextPartida = new Long(10);
    private static final List<Partida> PARTIDAS = new ArrayList<>();
    private Partida partidaDesteJogador;
    private final Jogador jogador;

    public JogadorListener(Jogador jogador) {
        this.jogador = jogador;
    }

    /**
     * Inicia a thread que faz o controle de uma conexão especifica.
     */
    @Override
    public void run() {
        try {
            enviarInformacoesDeUsuario();
            this.partidaDesteJogador = null;
            while (this.jogador.getConexao().isConectionOpen() && this.partidaDesteJogador == null) {
                Mensagem msg = this.jogador.getConexao().receber();
                Util.printarRecebimentoInfo(jogador, msg);
                tratarMensagem(msg);
            }
        } catch (IOException | ClassNotFoundException ex) {
            PARTIDAS.remove(this.partidaDesteJogador);
            Logger.getLogger(JogadorListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JogadorListener other = (JogadorListener) obj;
        return Objects.equals(this.jogador, other.jogador);
    }

    /**
     * Finaliza a conexão antes de finalizar a Thread.
     */
    @Override
    public void interrupt() {
        try {
            this.jogador.getConexao().close();
        } catch (IOException ex) {
            Logger.getLogger(JogadorListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Obtém o ID para criação da próxima partida.
     *
     * @return id único para criação de uma partida.
     */
    public static synchronized Long getIdNextPartida() {
        return idNextPartida++;
    }

    /**
     * Faz o tratamento da mensagem recebida
     *
     * @param msg
     * @throws IOException
     */
    private void tratarMensagem(Mensagem msg) throws IOException {
        switch (msg.getAcaoDaMensagem()) {
            case LISTAR_PARTIDAS:
                enviarListaDePartidas();
                break;
            case ATUALIZAR_DADOS_JOGADOR:
                atualizarDadosJogador(msg);
                break;
            case ESCOLHER_PARTIDA:
                entrarNaPartida(msg);
                break;
            case FINALIZAR_CONEXAO:
                this.interrupt();
                break;
        }
    }

    /**
     * Envia as informações inicias(dados do usuário) para o cliente que abriu a
     * conexão
     *
     * @throws IOException
     */
    private void enviarInformacoesDeUsuario() throws IOException {
        Mensagem<JogadorInfo> msg = new Mensagem<>(AcaoDaMensagem.DADOS_JOGADOR, new JogadorInfo(jogador));
        Util.printarEnvioInfo(jogador, msg);
        this.jogador.getConexao().enviar(msg);
    }

    /**
     * Envia para o cliente a lista de partidas disponíveis
     *
     * @throws IOException
     */
    private void enviarListaDePartidas() throws IOException {
        Mensagem<PartidasInfo> msg = new Mensagem<>(AcaoDaMensagem.LISTAR_PARTIDAS, new PartidasInfo(PARTIDAS));
        Util.printarEnvioInfo(jogador, msg);
        this.jogador.getConexao().enviar(msg);
    }

    /**
     * Atualiza os dados do usuário
     *
     * @param msg
     */
    private void atualizarDadosJogador(Mensagem<JogadorInfo> msg) {
        this.jogador.setNomeJogador(msg.getValor().getNomeJogador());
    }

    /**
     * Cria uma nova partida, sendo o this.jogador como criador. Ou entra em uma
     * partida já existente. Após a criação ou Junção a uma partida,
     * JogadorListener é interrompido.
     *
     * @param msg se idPartida for null, é feito a criação de uma nova partida.
     * Caso contrário, é juntado a uma partida.
     */
    private void entrarNaPartida(Mensagem<PartidaInfo> msg) {
        if (msg.getValor() == null) {
            this.partidaDesteJogador = new Partida(getIdNextPartida(), this.jogador.getNomeJogador(), this.jogador);
            PARTIDAS.add(partidaDesteJogador);
        } else {
            this.partidaDesteJogador = PARTIDAS.get(PARTIDAS.indexOf(new Partida(msg.getValor().getIdPartida(), null, null)));
            PARTIDAS.remove(this.partidaDesteJogador);
            this.partidaDesteJogador.start(this.jogador);
        }
    }

}

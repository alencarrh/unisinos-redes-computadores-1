package servidor;

import comunicacao.ControladorConexao;
import comunicacao.Mensagem;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import util.Util;
import jogo.Jogador;

/**
 * Clase resonsável por receber a manter novas conexões.
 *
 * @class Server
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 01/04/2017
 */
public class Servidor implements Serializable {

    //TODO: não deve ser static pois pode-se ter mais de um servidor executando ao mesmo
    //tempo em uma porta diferente. Alterar isto.
    private static final List<JogadorListener> JOGADORES = new ArrayList<>();
    private Long nextJogadorId;
    private final ServerSocket serverSocket;
    private boolean rodando;

    public Servidor(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.rodando = Boolean.FALSE;
        this.nextJogadorId = new Long(1);
    }

    /**
     * Inicia o servidor.
     *
     * @throws IOException
     */
    public void iniciarServidor() throws IOException {
        if (isRodando()) {
            return;
        }
        this.rodando = Boolean.TRUE;
        System.out.println("Servidor Iniciado...");
        while (isRodando()) {
            ControladorConexao<Mensagem> novaConexao = new ControladorConexao(serverSocket.accept());

            Jogador novoJogador = new Jogador(getNextJogadorId(), Util.gerarNomeAleatorio(), novaConexao);
            JogadorListener novoJogadorListener = new JogadorListener(novoJogador);
            JOGADORES.add(novoJogadorListener);
            novoJogadorListener.start();
            
//INFO: TODO: acredito que ser necessário a verificação da pré-existência da conexão.
//Conexão é a única diferença para determinar se o jogador já existe ou não.
//            JogadorListener temp = new JogadorListener(new Jogador(null, null, novaConexao));
//            if (!JOGADORES.contains(temp)) {
//            }
        }
    }

    /**
     * Reinicia o JogadorListener do jogador para voltar a ouvi-lo do menu
     * principal.
     *
     * @param jogador
     */
    public static synchronized void iniciarJogadorListener(Jogador jogador) {
        JogadorListener novoJogadorListener = new JogadorListener(jogador);
        removerJogadorListener(jogador);
        novoJogadorListener.start();
        JOGADORES.add(novoJogadorListener);
    }

    /**
     * Remove o jogador(JogadorListener) da lista de jogadores.
     *
     * @param jogador
     */
    public static synchronized void removerJogadorListener(Jogador jogador) {
        JOGADORES.remove(new JogadorListener(jogador));
    }

    /**
     * Interrompe todos os JogadoresListener(fecha a conexão do socket) e
     * interrompe o servidor.
     *
     * @throws IOException
     */
    public synchronized void pararServidor() throws IOException {
        JOGADORES.stream().forEach(JogadorListener::interrupt);
        this.serverSocket.close();
    }

    /**
     * Retorna o status que o servidor se encontra[RODADNDO ou PARADO]
     *
     * @return
     */
    public boolean isRodando() {
        return rodando;
    }

    /**
     * Obtém o ID para um novo jogador.
     *
     * @return
     */
    public synchronized Long getNextJogadorId() {
        return this.nextJogadorId++;
    }

}

package servidor;

import comunicacao.ControladorConexao;
import enums.DirecaoDaMensagem;
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

    private static final List<JogadorListener> JOGADORES = new ArrayList<>();
    private Long nextJogadorId;
    private final ServerSocket serverSocket;
    private boolean rodando;

    public Servidor(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.rodando = Boolean.FALSE;
        this.nextJogadorId = new Long(1);
    }

    public void iniciarServidor() throws IOException {
        if (isRodando()) {
            return;
        }
        this.rodando = Boolean.TRUE;
        System.out.println("Servidor Iniciado...");
        while (isRodando()) {
            ControladorConexao novaConexao = new ControladorConexao(serverSocket.accept(), DirecaoDaMensagem.PARA_CLIENTE);
            //Conexão é a única diferença para determinar se o jogador já existe ou não.
            JogadorListener temp = new JogadorListener(new Jogador(null, null, novaConexao));
            if (!JOGADORES.contains(temp)) {
                Jogador novoJogador = new Jogador(getNextJogadorId(), Util.gerarNomeAleatorio(), novaConexao);
                JogadorListener novoJogadorListener = new JogadorListener(novoJogador);
                JOGADORES.add(novoJogadorListener);
                novoJogadorListener.start();
            }
        }

    }

    public void pararServidor() throws IOException {
        for (JogadorListener jogadorListener : JOGADORES) {
            jogadorListener.interrupt();
        }
        this.serverSocket.close();
    }

    public boolean isRodando() {
        return rodando;
    }

    public Long getNextJogadorId() {
        return this.nextJogadorId++;
    }

}
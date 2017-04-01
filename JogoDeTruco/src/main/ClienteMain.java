package main;

import comunicacao.ControladorConexao;
import comunicacao.Mensagem;
import comunicacao.MensagemJogador;
import enums.AcaoDaMensagem;
import enums.DirecaoDaMensagem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import jogo.Jogador;

/**
 * @class ClienteMain
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 01/04/2017
 */
public class ClienteMain {

    private static Jogador jogador;
    private static ControladorConexao conexao;
    private static final BufferedReader KEYBOARD_INPUT = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException, Exception {
        estabelecerConexaoComServidor(args);
        iniciarInformacoesSobreJogador();
        definirNomeJogador();

        //Cria Thread que ficará escutando a porta(retorno do servidor)
        Thread listener = createListener();
        listener.start();

//        while (conexao.isConectionOpen()) {
//            
//        }
    }

    private static void estabelecerConexaoComServidor(String[] args) throws IOException {
        //Verifica se o IP do servidor foi passado por parâmetro
        System.out.println("Estabelecendo conexão com o servidor...");
        if (args.length == 0) {
            conexao = new ControladorConexao("127.0.0.1", 6789, DirecaoDaMensagem.PARA_SERVIDOR);
        } else {
            conexao = new ControladorConexao(args[0], 6789, DirecaoDaMensagem.PARA_SERVIDOR);
        }
        System.out.println("Conexão estabelecida com sucesso!");
    }

    private static Thread createListener() {
        Thread listener = new Thread(() -> {
            while (conexao.isConectionOpen()) {
                Mensagem fromServer = conexao.receber();
                System.out.println(fromServer);
                switch (fromServer.getAcaoDaMensagem()) {
                    case JOGADOR_CRIADO:
                        break;
                    case FINALIZAR_CONEXAO:
                        break;
                    case MOSTRAR_RESULTADO_FINAL:
                        break;
                    case LISTA_PARTIDAS_DISPONIVEIS:
                        break;

                }
            }
        });

        return listener;
    }

    private static void iniciarInformacoesSobreJogador() throws Exception {
        System.out.println("Obtendo informações iniciados do jogo...");
        //Obtem informações iniciais do jogador, como ID e NOME_ALEATORIO
        Mensagem primeiraMensagem = conexao.receber();
        if (!AcaoDaMensagem.JOGADOR_CRIADO.equals(primeiraMensagem.getAcaoDaMensagem())) {
            throw new Exception("Problema ao obter informações do servidor.");
        }
        MensagemJogador informacoesDoJogador = (MensagemJogador) primeiraMensagem;
        jogador = new Jogador(informacoesDoJogador.getIdJogador(), informacoesDoJogador.getNomeJogador(), conexao);
    }

    private static void definirNomeJogador() throws IOException {
        System.out.print("\nDigite seu nome(#randam para escolher um nome aleatorio): ");
        String nomeJogador = KEYBOARD_INPUT.readLine();
        if (nomeJogador != null && !nomeJogador.isEmpty() && !nomeJogador.equalsIgnoreCase("#random")) {
            jogador.setName(nomeJogador);
        }
    }
}

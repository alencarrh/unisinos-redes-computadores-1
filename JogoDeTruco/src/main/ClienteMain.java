package main;

import comunicacao.ControladorConexao;
import comunicacao.Mensagem;
import comunicacao.MensagemEntrarEmPartida;
import comunicacao.MensagemJogador;
import comunicacao.MensagemOpcoes;
import comunicacao.MensagemTexto;
import comunicacao.Opcao;
import enums.AcaoDaMensagem;
import enums.DirecaoDaMensagem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
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

//        //Cria Thread que ficará escutando a porta(retorno do servidor)
//        Thread listener = createListener();
//        listener.start();
//        
        solicitaSalasDisponiveis();
        while (conexao.isConectionOpen()) {
            Mensagem msg = conexao.receber();
            tratarMensagem(msg);
        }
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
                try {
                    Mensagem fromServer = conexao.receber();
                    System.out.println(fromServer);
                    switch (fromServer.getAcaoDaMensagem()) {

                    }
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(ClienteMain.class.getName()).log(Level.SEVERE, null, ex);
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
        System.out.print("\nDigite seu nome(#random para escolher um nome aleatorio): ");
        String nomeJogador = KEYBOARD_INPUT.readLine();
        if (nomeJogador != null && !nomeJogador.isEmpty() && !nomeJogador.equalsIgnoreCase("#random")) {
            jogador.setNomeJogador(nomeJogador);
            conexao.enviar(new MensagemJogador(DirecaoDaMensagem.PARA_SERVIDOR, AcaoDaMensagem.JOGADOR_CRIADO, jogador));
        }
    }

    private static void tratarMensagem(Mensagem msg) throws IOException {
        switch (msg.getAcaoDaMensagem()) {
            case FINALIZAR_CONEXAO:
                //finalizar conexão
                break;
            case LISTA_PARTIDAS_DISPONIVEIS:
                //lista salas disponíveis
                mostrarSalasDiponiveis((MensagemOpcoes) msg);
                break;
            case MOSTRAR_RESULTADO_FINAL:
                //mostra placar final
                break;
            case JOGADA:
                //realizar jogada
                break;
            case TEXTO:
                System.out.println(((MensagemTexto) msg).getTexto());
                break;
        }
    }

    private static void solicitaSalasDisponiveis() throws IOException {
        conexao.enviar(new Mensagem(DirecaoDaMensagem.PARA_SERVIDOR, AcaoDaMensagem.LISTA_PARTIDAS_DISPONIVEIS));
    }

    private static void mostrarSalasDiponiveis(MensagemOpcoes mensagemOpcoes) throws IOException {
        mensagemOpcoes.getOpcoes().forEach((opcao) -> {
            System.out.println(opcao);
        });
        System.out.println(new Opcao("0", "Criar uma nova sala"));
        System.out.print(mensagemOpcoes.getPergunta());
        String inputUsuario = KEYBOARD_INPUT.readLine();
        Mensagem msg;
        if (inputUsuario.equals("0")) {
            msg = new Mensagem(DirecaoDaMensagem.PARA_SERVIDOR, AcaoDaMensagem.CRIAR_NOVA_PARTIDA);
            System.out.println("Sala criada com sucesso!");
            System.out.println("No aguardo de outro jogador para iniciar a partida...\n");
        } else {
            System.out.println("Entrando nada sala do jogador " + mensagemOpcoes.getOpcoes().get(new Integer(inputUsuario) - 1).labelOpcao);
            msg = new MensagemEntrarEmPartida(DirecaoDaMensagem.PARA_SERVIDOR, AcaoDaMensagem.ENTRAR_NA_PARTIDA, inputUsuario);
        }
        conexao.enviar(msg);
    }
}

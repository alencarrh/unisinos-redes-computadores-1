package main;

import comunicacao.ControladorConexao;
import comunicacao.Mensagem;
import comunicacao.transporte.Info;
import comunicacao.transporte.JogadorInfo;
import comunicacao.transporte.MaoInfo;
import comunicacao.transporte.MenuAcoes;
import comunicacao.transporte.PartidaInfo;
import comunicacao.transporte.PartidasInfo;
import comunicacao.transporte.RodadaInfo;
import enums.AcaoDaMensagem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidObjectException;
import java.util.List;
import jogo.Jogada;
import jogo.Jogador;
import util.Util;

/**
 * @class ClienteMain
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 01/04/2017
 */
public class ClienteMain {

    private static Jogador jogador;
    private static ControladorConexao<Mensagem> conexao;
    private static final BufferedReader KEYBOARD_INPUT = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException, Exception {
        estabelecerConexaoComServidor(args);
        iniciarInformacoesSobreJogador();
        atualizarDadosJogador();

        requisitarSalasDisponiveis();
        Util.limparCMD();
        while (conexao.isConectionOpen()) {
            Mensagem msg = conexao.receber();
            tratarMensagem(msg);
        }
    }

    private static void estabelecerConexaoComServidor(String[] args) throws IOException {
        //Verifica se o IP do servidor foi passado por parâmetro
        System.out.println("Estabelecendo conexão com o servidor...");
        if (args.length == 0) {
            conexao = new ControladorConexao("127.0.0.1", 6789);
        } else {
            conexao = new ControladorConexao(args[0], 6789);
        }
        System.out.println("Conexão estabelecida com sucesso!");
    }

    private static void iniciarInformacoesSobreJogador() throws Exception {
        System.out.println("Obtendo informações iniciais do jogo...");

        //Obtem informações iniciais do jogador, como ID e NOME_ALEATORIO
        Mensagem<JogadorInfo> dadosJogador = conexao.receber();
        if (!AcaoDaMensagem.DADOS_JOGADOR.equals(dadosJogador.getAcaoDaMensagem())) {
            throw new InvalidObjectException("Esperado DADOS_JOGADOR porém obtido " + dadosJogador.getAcaoDaMensagem() + '.');
        }
        jogador = new Jogador(dadosJogador.getValor().getIdJogador(), dadosJogador.getValor().getNomeJogador(), conexao);
    }

    private static void atualizarDadosJogador() throws IOException {
        System.out.print("\nDigite seu nome(#random para escolher um nome aleatorio): ");
        String nomeJogador = KEYBOARD_INPUT.readLine();
        if (nomeJogador != null && !nomeJogador.isEmpty() && !nomeJogador.equalsIgnoreCase("#random")) {
            jogador.setNomeJogador(nomeJogador);
            conexao.enviar(new Mensagem<>(AcaoDaMensagem.ATUALIZAR_DADOS_JOGADOR, new JogadorInfo(jogador)));
        }
    }

    private static void requisitarSalasDisponiveis() throws IOException {
        conexao.enviar(new Mensagem<>(AcaoDaMensagem.LISTAR_PARTIDAS, null));
    }

    private static void tratarMensagem(Mensagem msg) throws IOException {
        switch (msg.getAcaoDaMensagem()) {
            case LISTAR_PARTIDAS:
                listarSalasDisponiveis((PartidasInfo) msg.getValor());
                break;
            case INICIAR_PARTIDA:
                iniciarPartida(msg);
                break;
            case MOSTRAR_CARTAS:
                mostrarCartas(msg);
                break;
            case JOGAR:
                jogar(msg);
                break;
            case AGUARDAR_OUTRO_JOGADOR:
                System.out.println("\nAguardando outro jogador realizar sua jogada...\n");
                break;
            case DADOS_RODADA:
                mostrarDadosRodada(msg);
                break;
            case DADOS_MAO:
                mostrarDadosDaMao(msg);
                break;
            case INFO_JOGADA_OPONENTE:
                mostrarJogadaAnterior(msg);
                break;
            case INFORMAR_PERDA_CONEXAO:
                informarPerdaConexao(msg);
                requisitarSalasDisponiveis();
                break;
            case FINALIZAR_CONEXAO:
                conexao.close();
                break;
        }
    }

    public static void listarSalasDisponiveis(PartidasInfo partidas) throws IOException {
        System.out.println("0 - Sair");
        System.out.println("1 - Criar nova partida");
        System.out.println("2 - Atualizar partidas");
        partidas.getPartidas().stream().forEach((partida) -> {
            System.out.println(partida.getIdPartida() + " - " + partida.getNomePartida());
        });
        System.out.print("Opção: ");
        String op = KEYBOARD_INPUT.readLine();
        Long id = new Long(op);
        realizarAcaoListagemSala(id, partidas);
    }

    private static void realizarAcaoListagemSala(Long id, PartidasInfo partidas) throws IOException {
        switch (id.intValue()) {
            case 0:
                conexao.enviar(new Mensagem<>(AcaoDaMensagem.FINALIZAR_CONEXAO, null));
                conexao.close();
                break;
            case 1:
                conexao.enviar(new Mensagem<>(AcaoDaMensagem.ESCOLHER_PARTIDA, null));
                System.out.println("\nPartida criada... Aguardando outro jogador juntar-se a sua partida...\n");
                break;
            case 2:
                requisitarSalasDisponiveis();
                break;
            default:
                int pos = partidas.getPartidas().indexOf(new PartidaInfo(id, null, null));
                if (pos == -1) {
                    System.out.println("Opção inválida!");
                    requisitarSalasDisponiveis();
                } else {
                    conexao.enviar(new Mensagem<>(AcaoDaMensagem.ESCOLHER_PARTIDA, partidas.getPartidas().get(pos)));
                    System.out.println("\nConectando-se a partida de " + partidas.getPartidas().get(pos).getNomePartida() + "...\n");
                }
        }

    }

    private static void iniciarPartida(Mensagem<PartidaInfo> msg) {
        Util.limparCMD();
        System.out.println(msg.getValor().getJogador1().getNomeJogador() + " vs " + msg.getValor().getJogador2().getNomeJogador());
        System.out.println("\nIniciado partida...\n");
    }

    private static void mostrarCartas(Mensagem<JogadorInfo> msg) {
        System.out.println("Suas cartas: ");
        msg.getValor().getCartas().stream().forEach((carta) -> {
            System.out.print(carta + " ");
        });
        System.out.println("");
    }

    private static void jogar(Mensagem<MenuAcoes> msg) throws IOException {
        System.out.println("\nJogadas: ");
        List<Jogada> jogadas = msg.getValor().getJogadas();
        for (int i = 0; i < jogadas.size(); i++) {
            System.out.println((i + 1) + "-> " + jogadas.get(i).printarParaMenu());
        }
        System.out.print("Jogar: ");
        String opJogada = KEYBOARD_INPUT.readLine();
        //TODO: validar jogada
        Integer op = new Integer(opJogada) - 1;
        conexao.enviar(new Mensagem<>(AcaoDaMensagem.JOGAR, jogadas.get(op)));
        Util.limparCMD();
        System.out.println("\nVocê " + jogadas.get(op).getAcaoRealizada() + "\n");
    }

    private static void mostrarJogadaAnterior(Mensagem<Jogada> msgJogadaAnterior) {
        if (msgJogadaAnterior == null || msgJogadaAnterior.getValor() == null) {
            return;
        }
        Jogada jogadaAnterior = msgJogadaAnterior.getValor();
        System.out.println("\n" + jogadaAnterior.getJogadorInfo().getNomeJogador() + ' ' + jogadaAnterior.getAcaoRealizada() + "\n");
    }

    private static void mostrarDadosRodada(Mensagem<RodadaInfo> msg) {
        try {
            RodadaInfo r = msg.getValor();

            if (r.getJogadorGanhador() != null) {
                if (r.getJogadorGanhador().getNomeJogador().equals(jogador.getNomeJogador())) {
                    System.out.println("\nVocê ganhou esta rodada!\n\n");
                } else {
                    String temp = r.getUtilmaJogadaDoJogador(r.getJogadorGanhador()) != null ? r.getUtilmaJogadaDoJogador(r.getJogadorGanhador()).getAcaoRealizada() + " e " : "";
                    System.out.println("\n"
                            + r.getJogadorGanhador().getNomeJogador()
                            + " "
                            + temp
                            + " ganhou a rodada\n\n");
                }
            } else {
                Jogada ultimaJogada = r.getJogadas().get(r.getJogadas().size() - 1);
                System.out.println(ultimaJogada.getJogadorInfo().getNomeJogador() + "\n" + ultimaJogada.getAcaoRealizada());
            }
        } catch (Exception e) {
            System.out.println("\nDeu erro: " + e.getMessage() + "\n\n");
        }

    }

    private static void informarPerdaConexao(Mensagem<Info> msg) {
        System.out.println("\n" + msg.getValor().getInformacao() + "\n");
    }

    private static void mostrarDadosDaMao(Mensagem<MaoInfo> msg) {
        MaoInfo maoInfo = msg.getValor();
        if (!maoInfo.getRodadas().isEmpty()) {
            System.out.println("Rodadas anteriores: ");
            for (int i = 0; i < maoInfo.getRodadas().size(); i++) {
                RodadaInfo rodada = maoInfo.getRodadas().get(i);
                System.out.println("-Rodada " + (i + 1) + "");
                for (int j = 0; j < rodada.getJogadas().size(); j++) {
                    Jogada jogada = rodada.getJogadas().get(j);
                    System.out.println("-->Jodada " + (j + 1) + ": " + jogada.getJogadorInfo().getNomeJogador() + " " + jogada.getAcaoRealizada());
                }
                System.out.println("Ganhador: " + rodada.getJogadorGanhador().getNomeJogador());
            }
            if (maoInfo.getJogadorGanhador() != null) {
                System.out.println("\nGanhador da Mao: " + maoInfo.getJogadorGanhador().getNomeJogador());
            }
        }

        System.out.println("\n\nIniciando rodada número: " + (maoInfo.getRodadas().size() + 1));

    }

}

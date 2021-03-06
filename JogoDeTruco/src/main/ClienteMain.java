package main;

import comunicacao.ControladorConexao;
import comunicacao.Mensagem;
import comunicacao.transporte.PontosInfo;
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
import util.Teclado;
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
        System.out.print("\nEstabelecendo conexão com o servidor");
        switch (args.length) {
            case 0:
                System.out.print("[127.0.0.1:6789]...");
                conexao = new ControladorConexao("127.0.0.1", 6789);
                break;
            case 1:
                System.out.print("[" + args[0] + ":6789]...");
                conexao = new ControladorConexao(args[0], 6789);
                break;
            default:
                int porta = 6789;
                try {
                    porta = Integer.valueOf(args[1]);
                } catch (NumberFormatException ex) {
                    System.out.println("Parâmetro [1] deve ser inteiro. Utilizando porta padrão [6789]");
                }
                System.out.print("[" + args[0] + ": " + porta + "]...");
                conexao = new ControladorConexao(args[0], porta);
                break;
        }
        System.out.println("\nConexão estabelecida com sucesso!");
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
            case INFO_VENCEDOR_PONTOS:
                mostrarDadosPontos(msg);
                break;
            case PLACAR:
                mostrarPlacar(msg);
                break;
            case FINALIZAR_PARTIDA:
                finalizarPartida(msg);
                System.out.println("\n\n\n");
                requisitarSalasDisponiveis();
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
        Integer op = Teclado.lerInteiro("Opção: ");
        Long id = new Long(op == null ? 0 : op);
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
                    System.out.println("Opção inválida!\n");
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
        Integer op = Teclado.lerInteiro("Jogar: ", 1, jogadas.size());
        op--;
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
        if (maoInfo.getJogadorGanhador() != null) {
            System.out.println("****** FIM DA MÃO ******");
        }
        if (!maoInfo.getRodadas().isEmpty()) {
            System.out.println("Rodadas anteriores: ");
            for (int i = 0; i < maoInfo.getRodadas().size(); i++) {
                RodadaInfo rodada = maoInfo.getRodadas().get(i);
                System.out.println("-Rodada " + (i + 1) + "");
                for (int j = 0; j < rodada.getJogadas().size(); j++) {
                    Jogada jogada = rodada.getJogadas().get(j);
                    System.out.println("-->Jodada " + (j + 1) + ": " + jogada.getJogadorInfo().getNomeJogador() + " " + jogada.getAcaoRealizada());
                }
                System.out.println(">>Ganhador: " + (rodada.isEmpatou() ? "Empate" : rodada.getJogadorGanhador().getNomeJogador()));
            }

        }

        if (maoInfo.getJogadorGanhador() != null) {
            System.out.println("\nGanhador da Mão: " + maoInfo.getJogadorGanhador().getNomeJogador());
            System.out.println("Tentos: " + maoInfo.getEstadoDaMao().getValorDoEstado());
            System.out.println("\n\n\n\n\n\n****** INICIANDO PRÓXIMA MÃO ******");
        } else {
            System.out.println("\n\n****** INICIANDO PRÓXIMA RODADA (" + (maoInfo.getRodadas().size() + 1) + ")******");
        }

    }

    private static void mostrarPlacar(Mensagem<PartidaInfo> msg) {
        System.out.println("\nPLACAR:");
        JogadorInfo jogador1 = msg.getValor().getJogador1();
        JogadorInfo jogador2 = msg.getValor().getJogador2();
        System.out.print("   " + jogador1.getNomeJogador() + " " + jogador1.getTentos());
        System.out.print(" vs " + jogador2.getTentos() + " " + jogador2.getNomeJogador());
        System.out.println("\n");
    }

    private static void finalizarPartida(Mensagem<PartidaInfo> msg) {
        System.out.println("****** FIM DA PARTIDA *****");
        mostrarPlacar(msg);
        System.out.println(">>>>GANHADOR: " + msg.getValor().getJogadorVencedor().getNomeJogador());

    }

    private static void mostrarDadosPontos(Mensagem<PontosInfo> msg) {
        System.out.println("\n****** RESUTLADO DO " + msg.getValor().getAcao().toString() + " ******");
        boolean esteVenceu = jogador.getIdJogador().equals(msg.getValor().getJogadorVencedor().getIdJogador());
        if (esteVenceu) {
            System.out.println("### Você venceu! ###");
            System.out.println("Seus pontos: " + msg.getValor().getPontosJogadorVencedor());
            System.out.println("Pontos do oponente: " + msg.getValor().getPontosOutroVencedor());
            System.out.println("Tentos: " + msg.getValor().getTentosGanho());
        } else {
            System.out.println("### Você perdeu. ###");
            System.out.println("Seus pontos: " + msg.getValor().getPontosOutroVencedor());
            System.out.println("Pontos do oponente: " + msg.getValor().getPontosJogadorVencedor());
            System.out.println("Tentos: " + msg.getValor().getTentosGanho());
        }
    }

}

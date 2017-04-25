package jogo;

import comunicacao.Mensagem;
import comunicacao.transporte.EnvidoInfo;
import comunicacao.transporte.JogadorInfo;
import comunicacao.transporte.MenuAcoes;
import enums.AcaoDaJogada;
import enums.AcaoDaMensagem;
import enums.Carta;
import enums.EstadoDaMao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import util.Util;

/**
 * REGRAS
 * http://www.mtg.org.br/public/libs/kcfinder/upload/files/REGULAMENTOS/1_4_REGULAMENTO_ESPORTE.pdf
 * Não está 100% dentro das regras. Mas o que não está de acordo será
 * implementado mais tarde.
 *
 * @class Jogo
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 30/03/2017
 */
public class Jogo {

    public static final int PONTUACAO_MAXIMA = 24;

    //Pontos a ganhar quando jogador chamar os pontos;
    private static final int PONTOS_CHAMADA_ENVIDO = 1;
    private static final int PONTOS_CHAMADA_REAL_ENVIDO = 1;
    private static final int PONTOS_CHAMADA_FALTA_ENVIDO = 1;

    //Pontos a ganhar quando jogador aceitar chamada dos pontos; 
    private static final int PONTOS_ACEITE_ENVIDO = 2;
    private static final int PONTOS_ACEITE_REAL_ENVIDO = 3;

    //FLOR
    private static final int PONTOS_FLOR = 3;
    private static final int PONTOS_CONTRA_FLOR = 6;

    /**
     * Atualiza Estado da mão para Truco.
     *
     * @param mao
     */
    public static void aceitouTruco(Mao mao) {
        mao.setEstadoDaMao(EstadoDaMao.TRUCO);
    }

    /**
     * Atualiza Estado da mão para ReTruco.
     *
     * @param mao
     */
    public static void aceitouRetruco(Mao mao) {
        mao.setEstadoDaMao(EstadoDaMao.RETRUCO);
    }

    /**
     * Atualiza Estado da mão para Vale-Quatro.
     *
     * @param mao
     */
    public static void aceitouValeQuatro(Mao mao) {
        mao.setEstadoDaMao(EstadoDaMao.VALE_QUATRO);
    }

    /**
     * Verifica se o jogador pode chamar Truco.
     *
     * @param mao
     * @param jogador
     * @return
     */
    public static boolean podeChamarTruco(Mao mao, Jogador jogador) {
        return EstadoDaMao.SIMPLES.equals(mao.getEstadoDaMao()) && mao.getJogadorTrocouEstado() == null;
    }

    /**
     * Verifica se o jogador pode chamar ReTruco.
     *
     * @param mao
     * @param jogador
     * @return
     */
    public static boolean podeChamarRetruco(Mao mao, Jogador jogador) {
        return EstadoDaMao.TRUCO.equals(mao.getEstadoDaMao()) && !jogador.equals(mao.getJogadorTrocouEstado());
    }

    /**
     * Verifica se o jogador pode chamar Vale-Quatro.
     *
     * @param mao
     * @param jogador
     * @return
     */
    public static boolean podeChamarValeQuatro(Mao mao, Jogador jogador) {
        return EstadoDaMao.RETRUCO.equals(mao.getEstadoDaMao()) && !jogador.equals(mao.getJogadorTrocouEstado());
    }

    public static boolean possuiDuasCartasMesmoNaipe(Jogador jogador) {
        List<Carta> cartas = jogador.getCartas();
        switch (cartas.size()) {
            case 0:
                return false;
            case 1:
                return false;
            case 2:
                return cartas.get(0).isMesmoNaipe(cartas.get(1));
            case 3:
                //carta1 == carta2 || carta1 == carta3 || carta2 == carta3
                return cartas.get(0).isMesmoNaipe(cartas.get(1))
                        || cartas.get(0).isMesmoNaipe(cartas.get(2))
                        || cartas.get(1).isMesmoNaipe(cartas.get(2));
            default:
                return false;
        }
    }

    /**
     * Verifica se o jogador pode chamar Envido/Real Envido/Falta Envido.
     *
     * @param mao
     * @param jogador
     * @return
     */
    public static boolean podeChamarEnvido(Mao mao, Jogador jogador) {
        return jogador.getCartas().size() == 3 && !mao.isChamadoEnvido() && EstadoDaMao.SIMPLES.equals(mao.getEstadoDaMao());
    }

    /**
     * Verifica se o jogador pode chamar Flor.
     *
     * @param mao
     * @param jogador
     * @return
     */
    public static boolean podeChamarFlor(Mao mao, Jogador jogador) {
        if (podeChamarEnvido(mao, jogador)) {
            List<Carta> cartas = jogador.getCartas();
            //Carta1 == Carta2 && Carta1 == Carta3
            return (cartas.get(0).isMesmoNaipe(cartas.get(1))) && (cartas.get(0).isMesmoNaipe(cartas.get(2)));
        }
        return false;
    }

    /**
     * Verifica se o jogador pode chamar Contra-Flor. Segue o mesmo tratamento
     * da chamada de Flor normal.
     *
     * @param mao
     * @param jogador
     * @return
     */
    public static boolean podeChamarContraFlor(Mao mao, Jogador jogador) {
        return podeChamarFlor(mao, jogador);
    }

    /**
     * Distribuí as cartas para os jogadores.
     *
     * @param jogador1
     * @param jogador2
     */
    public static void darCartas(Jogador jogador1, Jogador jogador2) {
        List<Carta> cartasJogador1 = new ArrayList<>();
        List<Carta> cartasJogador2 = new ArrayList<>();
        List<Carta> cartasDisponiveis = new ArrayList<>(Arrays.asList(Carta.values()));
        do {
            Carta randomCarta = Carta.gerarCartaAleatoria(cartasDisponiveis);
            cartasDisponiveis.remove(randomCarta);
            if (cartasJogador1.size() == cartasJogador2.size()) {
                //dar cartas para jogador1;
                cartasJogador1.add(randomCarta);
            } else {
                //dar cartas para jogaodor2;
                cartasJogador2.add(randomCarta);
            }
        } while (cartasJogador1.size() < 3 || cartasJogador2.size() < 3);
        jogador1.setCartas(cartasJogador1);
        jogador2.setCartas(cartasJogador2);
    }

    /**
     * Envia as cartas para o jogador
     *
     * @param jogador
     * @throws IOException
     */
    public static void enviarCartas(Jogador jogador) throws IOException {
        Mensagem<JogadorInfo> msg = new Mensagem<>(AcaoDaMensagem.MOSTRAR_CARTAS, new JogadorInfo(jogador));
        Util.printarEnvioInfo(jogador, msg);
        jogador.getConexao().enviar(msg);
    }

    /**
     * Monta o menu com opções de jogadas que o jogador pode realizar
     *
     * @param mao
     * @param jogador
     * @param msgJogadaAnterior
     * @return
     */
    public static Mensagem<MenuAcoes> montarMenuJogador(Mao mao, Jogador jogador, Mensagem<Jogada> msgJogadaAnterior) {
        MenuAcoes menu = new MenuAcoes();
        List<Jogada> jogadasPossiveis = new ArrayList<>();
        if (msgJogadaAnterior == null) {
            montarMenuSimples(mao, jogador, jogadasPossiveis);
        } else {
            montarMenuComplexo(mao, jogador, jogadasPossiveis, msgJogadaAnterior.getValor());
        }
        menu.getJogadas().addAll(jogadasPossiveis);
        return new Mensagem<>(AcaoDaMensagem.JOGAR, menu);
    }

    /**
     * Monta o menu baseado nas cartas e no estado da mao. Não considera a
     * jogada anterior.
     *
     * @param mao
     * @param jogador
     * @param jogadasPossiveis
     */
    private static void montarMenuSimples(Mao mao, Jogador jogador, List<Jogada> jogadasPossiveis) {
        jogador.getCartas().stream().forEach((carta) -> {
            jogadasPossiveis.add(new Jogada(AcaoDaJogada.JOGADA_SIMPLES, carta, jogador.getInfoJogador()));
        });
        if (Jogo.podeChamarTruco(mao, jogador)) {
            jogadasPossiveis.add(new Jogada(AcaoDaJogada.TRUCO, null, jogador.getInfoJogador()));
        } else if (Jogo.podeChamarRetruco(mao, jogador)) {
            jogadasPossiveis.add(new Jogada(AcaoDaJogada.RETRUCO, null, jogador.getInfoJogador()));
        } else if (Jogo.podeChamarValeQuatro(mao, jogador)) {
            jogadasPossiveis.add(new Jogada(AcaoDaJogada.VALE_QUATRO, null, jogador.getInfoJogador()));
        }
        if (Jogo.podeChamarFlor(mao, jogador)) {
            jogadasPossiveis.add(new Jogada(AcaoDaJogada.FLOR, null, jogador.getInfoJogador()));
        } else if (Jogo.podeChamarEnvido(mao, jogador)) {
            jogadasPossiveis.add(new Jogada(AcaoDaJogada.ENVIDO, null, jogador.getInfoJogador()));
            jogadasPossiveis.add(new Jogada(AcaoDaJogada.REAL_ENVIDO, null, jogador.getInfoJogador()));
            jogadasPossiveis.add(new Jogada(AcaoDaJogada.FALTA_ENVIDO, null, jogador.getInfoJogador()));
        }
        jogadasPossiveis.add(new Jogada(AcaoDaJogada.IR_PARA_BARALHO, null, jogador.getInfoJogador()));
    }

    /**
     * Monta o menu de opções baseado na jogada anterior do outro jogador.
     *
     * @param mao
     * @param jogador
     * @param jogadasPossiveis
     * @param jogadaAnterior
     */
    private static void montarMenuComplexo(Mao mao, Jogador jogador, List<Jogada> jogadasPossiveis, Jogada jogadaAnterior) {
        if (jogadaAnterior == null) {
            montarMenuSimples(mao, jogador, jogadasPossiveis);
            return;
        }
        switch (jogadaAnterior.getAcaoDaJogada()) {
            case JOGADA_SIMPLES:
                montarMenuSimples(mao, jogador, jogadasPossiveis);
                return;
            case TRUCO:
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.QUERO, null, jogador.getInfoJogador()));
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.NAO_QUERO, null, jogador.getInfoJogador()));
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.RETRUCO, null, jogador.getInfoJogador()));
                return;
            case RETRUCO:
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.QUERO, null, jogador.getInfoJogador()));
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.NAO_QUERO, null, jogador.getInfoJogador()));
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.VALE_QUATRO, null, jogador.getInfoJogador()));
                return;
            case VALE_QUATRO:
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.QUERO, null, jogador.getInfoJogador()));
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.NAO_QUERO, null, jogador.getInfoJogador()));
                return;
            case ENVIDO:
                if (Jogo.podeChamarFlor(mao, jogador)) {
                    jogadasPossiveis.add(new Jogada(AcaoDaJogada.FLOR, null, jogador.getInfoJogador()));
                } else {
                    jogadasPossiveis.add(new Jogada(AcaoDaJogada.QUERO, null, jogador.getInfoJogador()));
                    jogadasPossiveis.add(new Jogada(AcaoDaJogada.NAO_QUERO, null, jogador.getInfoJogador()));
                    jogadasPossiveis.add(new Jogada(AcaoDaJogada.REAL_ENVIDO, null, jogador.getInfoJogador()));
                    jogadasPossiveis.add(new Jogada(AcaoDaJogada.FALTA_ENVIDO, null, jogador.getInfoJogador()));
                }
                return;
            case REAL_ENVIDO:
                if (Jogo.podeChamarFlor(mao, jogador)) {
                    jogadasPossiveis.add(new Jogada(AcaoDaJogada.FLOR, null, jogador.getInfoJogador()));
                } else {
                    jogadasPossiveis.add(new Jogada(AcaoDaJogada.QUERO, null, jogador.getInfoJogador()));
                    jogadasPossiveis.add(new Jogada(AcaoDaJogada.NAO_QUERO, null, jogador.getInfoJogador()));
                    jogadasPossiveis.add(new Jogada(AcaoDaJogada.FALTA_ENVIDO, null, jogador.getInfoJogador()));
                }
                return;
            case FALTA_ENVIDO:
                if (Jogo.podeChamarFlor(mao, jogador)) {
                    jogadasPossiveis.add(new Jogada(AcaoDaJogada.FLOR, null, jogador.getInfoJogador()));
                } else {
                    jogadasPossiveis.add(new Jogada(AcaoDaJogada.QUERO, null, jogador.getInfoJogador()));
                    jogadasPossiveis.add(new Jogada(AcaoDaJogada.NAO_QUERO, null, jogador.getInfoJogador()));
                }
                return;
            case FLOR:
                if (Jogo.podeChamarContraFlor(mao, jogador)) {
                    jogadasPossiveis.add(new Jogada(AcaoDaJogada.QUERO, null, jogador.getInfoJogador()));
                    jogadasPossiveis.add(new Jogada(AcaoDaJogada.NAO_QUERO, null, jogador.getInfoJogador()));
                    jogadasPossiveis.add(new Jogada(AcaoDaJogada.CONTRA_FLOR, null, jogador.getInfoJogador()));
                    jogadasPossiveis.add(new Jogada(AcaoDaJogada.CONTRA_FLOR_E_RESTO, null, jogador.getInfoJogador()));
                } else {
                    jogadasPossiveis.add(new Jogada(AcaoDaJogada.BOA, null, jogador.getInfoJogador()));
                }
        }
    }

    /**
     * Envia os dados(Opções de jogada) para o jogador que está na vez
     * informando o outro jogador para aguardar.
     *
     * @param jogadorQueVaiJogar
     * @param jogadorParaAguardar
     * @param mao
     * @param msgOutroJogador
     * @throws IOException
     */
    public static void enviarDadosJogada(Jogador jogadorQueVaiJogar, Jogador jogadorParaAguardar, Mao mao, Mensagem<Jogada> msgOutroJogador) throws IOException {
        if (msgOutroJogador != null) {
            jogadorQueVaiJogar.getConexao().enviar(new Mensagem<>(AcaoDaMensagem.INFO_JOGADA_OPONENTE, msgOutroJogador.getValor()));
        }
        jogadorQueVaiJogar.getConexao().enviar(Jogo.montarMenuJogador(mao, jogadorQueVaiJogar, msgOutroJogador));
        jogadorParaAguardar.getConexao().enviar(new Mensagem(AcaoDaMensagem.AGUARDAR_OUTRO_JOGADOR, null));
    }

    /**
     * Verifica o jogador que ganhou a rodada;
     *
     * @param mao
     * @param rodadaAtual
     * @param jogador1
     * @param jogador2
     * @param msgFromJogador1 Deve ser uma JOGADA_SIMPLES
     * @param msgFromJogador2 Deve ser uma JOGADA_SIMPLES
     * @param jogadorMao
     */
    public static void calcularGanhadorRodada(Mao mao, Rodada rodadaAtual, Jogador jogador1, Jogador jogador2, Mensagem<Jogada> msgFromJogador1, Mensagem<Jogada> msgFromJogador2, Jogador jogadorMao) {
        Jogada jogada1 = msgFromJogador1.getValor();
        Jogada jogada2 = msgFromJogador2.getValor();
        //Ambas jogadas serão JOGADAS_SIMPLES neste momento.
        jogador1.getCartas().remove(jogada1.getCarta());
        jogador2.getCartas().remove(jogada2.getCarta());
        calculaGanhadorJogadaSimples(rodadaAtual, jogador1, jogador2, jogada1.getCarta(), jogada2.getCarta(), jogadorMao);
    }

    /**
     * Verifica o jogador ganhador e adiciona ele como ganhador da rodada.
     *
     * @param rodadaAtual
     * @param jogador1
     * @param jogador2
     * @param carta1
     * @param carta2
     * @param jogadorMao
     */
    public static void calculaGanhadorJogadaSimples(Rodada rodadaAtual, Jogador jogador1, Jogador jogador2, Carta carta1, Carta carta2, Jogador jogadorMao) {
        if (carta1.getRanking() > carta2.getRanking()) {
            rodadaAtual.setJogadorGanhador(jogador1);
        } else if (carta1.getRanking() == carta2.getRanking()) {
            rodadaAtual.setJogadorGanhador(jogadorMao);
            rodadaAtual.setEmpatou(true);
        } else {
            rodadaAtual.setJogadorGanhador(jogador2);
        }
    }

    /**
     * Define o ganhador da mão e adiciona os tentos da mão para o jogador.
     *
     * @param mao
     * @param jogadorGanhador
     */
    public static void definirGanhadorMao(Mao mao, Jogador jogadorGanhador) {
        jogadorGanhador.addTentos(mao.getEstadoDaMao().getValorDoEstado());
        mao.setJogadorGanhador(jogadorGanhador);
    }

    /**
     * Define o ganhador da rodada
     *
     * @param rodada
     * @param jogadorGanhador
     */
    public static void definirGanhadorRodada(Rodada rodada, Jogador jogadorGanhador) {
        rodada.setJogadorGanhador(jogadorGanhador);
    }

    /**
     * Método principal encarregado de tratar as ações/jogadas principais dos
     * jogadores.
     *
     * @param mao
     * @param rodadaAtual
     * @param jogada
     * @param jogador1
     * @param jogador2
     * @param jogadorMao
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static boolean tratarJogada(Mao mao, Rodada rodadaAtual, Mensagem<Jogada> jogada, Jogador jogador1, Jogador jogador2, Jogador jogadorMao) throws IOException, ClassNotFoundException {
        rodadaAtual.getJogadas().add(jogada.getValor());
        boolean aceitou;
        switch (jogada.getValor().getAcaoDaJogada()) {
            case JOGADA_SIMPLES:
                return false;
            case IR_PARA_BARALHO:
                Jogo.definirGanhadorRodada(rodadaAtual, jogador2);
                Jogo.definirGanhadorMao(mao, jogador2);
                jogador2.getConexao().enviar(new Mensagem<>(AcaoDaMensagem.INFO_JOGADA_OPONENTE, jogada.getValor()));
                return false;
            case NAO_QUERO:
                Jogo.definirGanhadorRodada(rodadaAtual, jogador2);
                Jogo.definirGanhadorMao(mao, jogador2);
                jogador2.getConexao().enviar(new Mensagem<>(AcaoDaMensagem.INFO_JOGADA_OPONENTE, jogada.getValor()));
                return false;
            case QUERO://A principio, nunca chega neste case
                return false;
            case ENVIDO:
                Jogo.chamarEnvido(mao, rodadaAtual, jogador1, jogador2, jogada, jogadorMao);
                return !existeGanhador(mao, rodadaAtual, jogador1, jogador2);
            case REAL_ENVIDO:
                Jogo.chamarRealEnvido(mao, rodadaAtual, jogador1, jogador2, jogada, jogadorMao, 0);
                return !existeGanhador(mao, rodadaAtual, jogador1, jogador2);
            case FALTA_ENVIDO:
                Jogo.chamarFaltaEnvido(mao, rodadaAtual, jogador1, jogador2, jogada, jogadorMao, 0);
                return !existeGanhador(mao, rodadaAtual, jogador1, jogador2);
            case FLOR:
//                return chamarFlor(jogador1, jogador2);
                return true;
            case CONTRA_FLOR:
//                return chamarContraFlor(jogador1, jogador2);
                return true;
            case CONTRA_FLOR_E_RESTO:
//                return chamarContraFlorEResto(jogador1, jogador2);
                return true;
            case BOA:
                return false;
            case TRUCO:
                aceitou = Jogo.chamarTruco(mao, rodadaAtual, jogador1, jogador2, jogada, jogadorMao);
                if (aceitou) {
                    jogador1.getConexao().enviar(new Mensagem<>(AcaoDaMensagem.INFO_JOGADA_OPONENTE, new Jogada(AcaoDaJogada.QUERO, null, jogador2.getInfoJogador())));
                    return true;
                }
                //Colocar jogador1 como ganhador da mao e da rodada
                if (mao.getJogadorGanhador() == null) {
                    //Caso não existir ganhador ainda. Adiciona o ganhador.
                    Jogo.definirGanhadorRodada(rodadaAtual, jogador1);
                    Jogo.definirGanhadorMao(mao, jogador1);
                }
                return false;
            case RETRUCO:
                aceitou = Jogo.chamarReTruco(mao, rodadaAtual, jogador1, jogador2, jogada, jogadorMao);
                if (aceitou) {
                    jogador1.getConexao().enviar(new Mensagem<>(AcaoDaMensagem.INFO_JOGADA_OPONENTE, new Jogada(AcaoDaJogada.QUERO, null, jogador2.getInfoJogador())));
                    return true;
                }
                //Colocar jogador1 como ganhador da mao e da rodada
                if (mao.getJogadorGanhador() == null) {
                    //Caso não existir ganhador ainda. Adiciona o ganhador.
                    Jogo.definirGanhadorRodada(rodadaAtual, jogador1);
                    Jogo.definirGanhadorMao(mao, jogador1);
                }
                return false;
            case VALE_QUATRO:
                aceitou = Jogo.chamarValeQuatro(mao, rodadaAtual, jogador1, jogador2, jogada);
                if (aceitou) {
//                    jogador1.getConexao().enviar(new Mensagem<>(AcaoDaMensagem.INFO_JOGADA_OPONENTE, new Jogada(AcaoDaJogada.QUERO, null, jogador2.getInfoJogador())));
                    return true;
                }
                //Colocar jogador1 como ganhador da mao e da rodada
                if (mao.getJogadorGanhador() == null) {
                    //Caso não existir ganhador ainda. Adiciona o ganhador.
                    Jogo.definirGanhadorRodada(rodadaAtual, jogador1);
                    Jogo.definirGanhadorMao(mao, jogador1);
                }
                return false;
        }
        return false;
    }

    /**
     * Faz o pedido de truco para o jogador2.
     *
     * @param mao
     * @param rodadaAtual
     * @param jogador1
     * @param jogador2
     * @param jogada
     * @param jogadorMao
     * @return <i>true</i> se jogador2 aceitou. <i>false</i> se recusou
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static boolean chamarTruco(Mao mao, Rodada rodadaAtual, Jogador jogador1, Jogador jogador2, Mensagem<Jogada> jogada, Jogador jogadorMao) throws IOException, ClassNotFoundException {
        mao.setJogadorTrocouEstado(jogador1);
        enviarDadosJogada(jogador2, jogador1, mao, jogada);
        Mensagem<Jogada> msgJogadaJogador2 = jogador2.getConexao().receber();
        Util.printarRecebimentoInfo(jogador2, msgJogadaJogador2);
        switch (msgJogadaJogador2.getValor().getAcaoDaJogada()) {
            case QUERO:
                Jogo.aceitouTruco(mao);
                rodadaAtual.getJogadas().add(msgJogadaJogador2.getValor());
                return true;
            case NAO_QUERO:
                rodadaAtual.getJogadas().add(msgJogadaJogador2.getValor());
                return false;
            case IR_PARA_BARALHO:
                rodadaAtual.getJogadas().add(msgJogadaJogador2.getValor());
                return false;
            case RETRUCO:
                Jogo.aceitouTruco(mao);
                return tratarJogada(mao, rodadaAtual, msgJogadaJogador2, jogador2, jogador1, jogadorMao);
        }
        return true;
    }

    /**
     * Faz o pedido de ReTruco para o jogador2.
     *
     * @param mao
     * @param rodadaAtual
     * @param jogador1
     * @param jogador2
     * @param jogada
     * @param jogadorMao
     * @return <i>true</i> se jogador2 aceitou. <i>false</i> se recusou
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static boolean chamarReTruco(Mao mao, Rodada rodadaAtual, Jogador jogador1, Jogador jogador2, Mensagem<Jogada> jogada, Jogador jogadorMao) throws IOException, ClassNotFoundException {
        mao.setJogadorTrocouEstado(jogador1);
        enviarDadosJogada(jogador2, jogador1, mao, jogada);
        Mensagem<Jogada> msgJogadaJogador2 = jogador2.getConexao().receber();
        Util.printarRecebimentoInfo(jogador2, msgJogadaJogador2);
        switch (msgJogadaJogador2.getValor().getAcaoDaJogada()) {
            case QUERO:
                Jogo.aceitouRetruco(mao);
                rodadaAtual.getJogadas().add(msgJogadaJogador2.getValor());
                return true;
            case NAO_QUERO:
                rodadaAtual.getJogadas().add(msgJogadaJogador2.getValor());
                return false;
            case IR_PARA_BARALHO:
                rodadaAtual.getJogadas().add(msgJogadaJogador2.getValor());
                return false;
            case VALE_QUATRO:
                Jogo.aceitouRetruco(mao);
                return tratarJogada(mao, rodadaAtual, msgJogadaJogador2, jogador2, jogador1, jogadorMao);
        }
        return true;
    }

    /**
     * Faz o pedido de Vale Quatro para o jogador2.
     *
     * @param mao
     * @param rodadaAtual
     * @param jogador1
     * @param jogador2
     * @param jogada
     * @return <i>true</i> se jogador2 aceitou. <i>false</i> se recusou
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static boolean chamarValeQuatro(Mao mao, Rodada rodadaAtual, Jogador jogador1, Jogador jogador2, Mensagem<Jogada> jogada) throws IOException, ClassNotFoundException {
        mao.setJogadorTrocouEstado(jogador1);
        enviarDadosJogada(jogador2, jogador1, mao, jogada);
        Mensagem<Jogada> msgJogadaJogador2 = jogador2.getConexao().receber();
        Util.printarRecebimentoInfo(jogador2, msgJogadaJogador2);
        rodadaAtual.getJogadas().add(msgJogadaJogador2.getValor());
        switch (msgJogadaJogador2.getValor().getAcaoDaJogada()) {
            case QUERO:
                Jogo.aceitouValeQuatro(mao);
                return true;
            case NAO_QUERO:
                return false;
            case IR_PARA_BARALHO:
                return false;
        }
        return true;//se chegar neste ponto. Houve algum problema.
    }

    private static void chamarEnvido(Mao mao, Rodada rodadaAtual, Jogador jogador1, Jogador jogador2, Mensagem<Jogada> msgOutroJogador, Jogador jogadorMao) throws IOException, ClassNotFoundException {
        enviarDadosJogada(jogador2, jogador1, mao, msgOutroJogador);
        mao.setChamadoEnvido(true);
        Mensagem<Jogada> msgJogadaJogador2 = jogador2.getConexao().receber();
        Util.printarRecebimentoInfo(jogador2, msgJogadaJogador2);
        rodadaAtual.getJogadas().add(msgJogadaJogador2.getValor());
        switch (msgJogadaJogador2.getValor().getAcaoDaJogada()) {
            case QUERO:
                Jogo.calcularGanhadorDosPontos(jogador1, jogador2, PONTOS_ACEITE_ENVIDO, jogadorMao, AcaoDaJogada.ENVIDO);
                return;
            case NAO_QUERO:
                jogador1.addTentos(PONTOS_CHAMADA_ENVIDO);
                jogador1.getConexao().enviar(new Mensagem<>(AcaoDaMensagem.INFO_JOGADA_OPONENTE, msgJogadaJogador2.getValor()));
                return;
            case REAL_ENVIDO:
                chamarRealEnvido(mao, rodadaAtual, jogador2, jogador1, msgJogadaJogador2, jogadorMao, PONTOS_ACEITE_ENVIDO);
                return;
            case FALTA_ENVIDO:
                chamarFaltaEnvido(mao, rodadaAtual, jogador2, jogador1, msgJogadaJogador2, jogadorMao, PONTOS_ACEITE_ENVIDO);
                return;
            default:
                //Se chegar aqui, indica que houve algum problema.(Qual? não sei)
                return;
        }
    }

    private static void chamarRealEnvido(Mao mao, Rodada rodadaAtual, Jogador jogador1, Jogador jogador2, Mensagem<Jogada> msgOutroJogador, Jogador jogadorMao, int pontoAcumulados) throws IOException, ClassNotFoundException {
        enviarDadosJogada(jogador2, jogador1, mao, msgOutroJogador);
        mao.setChamadoEnvido(true);
        Mensagem<Jogada> msgJogadaJogador2 = jogador2.getConexao().receber();
        Util.printarRecebimentoInfo(jogador2, msgJogadaJogador2);
        rodadaAtual.getJogadas().add(msgJogadaJogador2.getValor());
        switch (msgJogadaJogador2.getValor().getAcaoDaJogada()) {
            case QUERO:
                Jogo.calcularGanhadorDosPontos(jogador1, jogador2, (PONTOS_ACEITE_REAL_ENVIDO + pontoAcumulados), jogadorMao, AcaoDaJogada.REAL_ENVIDO);
                return;
            case NAO_QUERO:
                //Caso foi aumentado o ENVIDO, vai está valendo 2 pontos.
                if (pontoAcumulados == 0) {
                    jogador1.addTentos(PONTOS_CHAMADA_REAL_ENVIDO);
                } else {
                    jogador1.addTentos(pontoAcumulados);
                }
                jogador1.getConexao().enviar(new Mensagem<>(AcaoDaMensagem.INFO_JOGADA_OPONENTE, msgJogadaJogador2.getValor()));
                return;
            case FALTA_ENVIDO:
                chamarFaltaEnvido(mao, rodadaAtual, jogador2, jogador1, msgJogadaJogador2, jogadorMao, (PONTOS_ACEITE_REAL_ENVIDO + pontoAcumulados));
                return;
            default:
                //Se chegar aqui, indica que houve algum problema.(Qual? não sei)
                return;
        }
    }

    private static void chamarFaltaEnvido(Mao mao, Rodada rodadaAtual, Jogador jogador1, Jogador jogador2, Mensagem<Jogada> msgOutroJogador, Jogador jogadorMao, int pontoAcumulados) throws IOException, ClassNotFoundException {
        enviarDadosJogada(jogador2, jogador1, mao, msgOutroJogador);
        mao.setChamadoEnvido(true);
        Mensagem<Jogada> msgJogadaJogador2 = jogador2.getConexao().receber();
        Util.printarRecebimentoInfo(jogador2, msgJogadaJogador2);
        rodadaAtual.getJogadas().add(msgJogadaJogador2.getValor());
        switch (msgJogadaJogador2.getValor().getAcaoDaJogada()) {
            case QUERO:
                int pontosFaltaParaTerminarPartida = obtemPontosFALTA(jogador1, jogador2);
                Jogo.calcularGanhadorDosPontos(jogador1, jogador2, pontosFaltaParaTerminarPartida, jogadorMao, AcaoDaJogada.FALTA_ENVIDO);
                return;
            case NAO_QUERO:
                if (pontoAcumulados == 0) {
                    jogador1.addTentos(1);
                } else {
                    jogador1.addTentos(pontoAcumulados);
                }
                jogador1.getConexao().enviar(new Mensagem<>(AcaoDaMensagem.INFO_JOGADA_OPONENTE, msgJogadaJogador2.getValor()));
                return;
            default:
                //Se chegar aqui, indica que houve algum problema.(Qual? não sei)
                return;
        }
    }

    private static void calcularGanhadorDosPontos(Jogador jogador1, Jogador jogador2, int pontos, Jogador jogadorMao, AcaoDaJogada acao) throws IOException {
        int pontuacaoJogador1 = calcularPontosJogador(jogador1);
        int pontuacaoJogador2 = calcularPontosJogador(jogador2);
        if (pontuacaoJogador1 > pontuacaoJogador2) {
            jogador1.addTentos(pontos);
            Jogo.informarGanhadorPontos(jogador1, jogador2, acao, pontos);
        } else if (pontuacaoJogador1 < pontuacaoJogador2) {
            jogador2.addTentos(pontos);
            Jogo.informarGanhadorPontos(jogador2, jogador1, acao, pontos);
        } else {
            //Empate indica que o jogador que iniciou a jogar(iniciou a mão) ganha os pontos
            jogadorMao.addTentos(pontos);
            Jogo.informarGanhadorPontos(jogadorMao, jogadorMao.equals(jogador1) ? jogador2 : jogador1, acao, pontos);
        }
    }

    private static int calcularPontosJogador(Jogador jogador) {
        boolean possuiCartasMesmoNaipe = Jogo.possuiDuasCartasMesmoNaipe(jogador);
        int pontosFinal = possuiCartasMesmoNaipe ? 20 : 0;
        List<Carta> cartasToEnvido = new ArrayList<>(jogador.getCartas());
        Carta maiorTemp;

        if (possuiCartasMesmoNaipe) {
            removerCartaNaipeDiferente(cartasToEnvido);
            pontosFinal = cartasToEnvido.stream().map((carta) -> carta.getPontosSomadosNoEnvido()).reduce(pontosFinal, Integer::sum);
        } else {
            //Obtém carta com maior pontuação para somar nos pontos
            maiorTemp = getCartaComMaiorPontuacaoParaEnvido(cartasToEnvido);
            pontosFinal += maiorTemp.getPontosSomadosNoEnvido();
        }
        return pontosFinal;
    }

    /**
     * Se duas cartas possuirem o mesmo naipe. Irá remover a carta de naipe
     * diferente.
     *
     * @param cartasToEnvido
     */
    private static void removerCartaNaipeDiferente(List<Carta> cartasToEnvido) {
        if (cartasToEnvido.get(0).isMesmoNaipe(cartasToEnvido.get(1))) {
            cartasToEnvido.remove(2);
        } else if (cartasToEnvido.get(0).isMesmoNaipe(cartasToEnvido.get(2))) {
            cartasToEnvido.remove(1);
        } else if (cartasToEnvido.get(1).isMesmoNaipe(cartasToEnvido.get(2))) {
            cartasToEnvido.remove(0);
        }
    }

    private static Carta getCartaComMaiorPontuacaoParaEnvido(List<Carta> from) {
        Carta cartaMaior = from.get(0);
        for (int i = 1; i < from.size(); i++) {
            if (cartaMaior.getPontosSomadosNoEnvido() < from.get(i).getPontosSomadosNoEnvido()) {
                cartaMaior = from.get(i);
            }
        }
        return cartaMaior;
    }

    /**
     * Envia as informações da disputa de pontos para ambos jogadores
     *
     * @param jogadorVencedor
     * @param jogador2
     * @param acao
     * @throws IOException
     */
    private static void informarGanhadorPontos(Jogador jogadorVencedor, Jogador jogador2, AcaoDaJogada acao, int tentos) throws IOException {
        EnvidoInfo env = new EnvidoInfo(jogadorVencedor.getInfoJogador(), calcularPontosJogador(jogadorVencedor), jogador2.getInfoJogador(), calcularPontosJogador(jogador2), acao, tentos);
        jogadorVencedor.getConexao().enviar(new Mensagem<>(AcaoDaMensagem.INFO_VENCEDOR_PONTOS, env));
        jogador2.getConexao().enviar(new Mensagem<>(AcaoDaMensagem.INFO_VENCEDOR_PONTOS, env));
    }

    /**
     * Obtém os pontos que falta para o jogador que está ganhando chegar a
     * PONTUACAO_MAXIMA (ganhar a partida)
     *
     * @param jogador1
     * @param jogador2
     * @return
     */
    private static int obtemPontosFALTA(Jogador jogador1, Jogador jogador2) {
        int pontosFaltaJogador1 = PONTUACAO_MAXIMA - jogador1.getTentos();
        int pontosFaltaJogador2 = PONTUACAO_MAXIMA - jogador2.getTentos();
        if (pontosFaltaJogador1 < pontosFaltaJogador2) {
            return pontosFaltaJogador1;
        }
        return pontosFaltaJogador2;
    }

    private static boolean existeGanhador(Mao mao, Rodada rodada, Jogador jogador1, Jogador jogador2) {
        if (jogador1.getTentos() >= PONTUACAO_MAXIMA || jogador2.getTentos() >= PONTUACAO_MAXIMA) {
            if (jogador1.getTentos() >= PONTUACAO_MAXIMA) {
                mao.setJogadorGanhador(jogador1);
                rodada.setJogadorGanhador(jogador1);
            } else if (jogador2.getTentos() >= PONTUACAO_MAXIMA) {
                mao.setJogadorGanhador(jogador2);
                rodada.setJogadorGanhador(jogador2);
            }
            return true;
        }
        return false;
    }

}

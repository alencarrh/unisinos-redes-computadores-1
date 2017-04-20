package jogo;

import comunicacao.Mensagem;
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
 * @class Jogo
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 30/03/2017
 */
public class Jogo {

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
        jogadasPossiveis.add(new Jogada(AcaoDaJogada.IR_PARA_BARALHO, null, jogador.getInfoJogador()));
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
        if (Jogo.podeChamarEnvido(mao, jogador)) {
            jogadasPossiveis.add(new Jogada(AcaoDaJogada.ENVIDO, null, jogador.getInfoJogador()));
            jogadasPossiveis.add(new Jogada(AcaoDaJogada.REAL_ENVIDO, null, jogador.getInfoJogador()));
            jogadasPossiveis.add(new Jogada(AcaoDaJogada.FALTA_ENVIDO, null, jogador.getInfoJogador()));
        }
        if (Jogo.podeChamarFlor(mao, jogador)) {
            jogadasPossiveis.add(new Jogada(AcaoDaJogada.FLOR, null, jogador.getInfoJogador()));
        }
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
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.QUERO, null, jogador.getInfoJogador()));
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.NAO_QUERO, null, jogador.getInfoJogador()));
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.REAL_ENVIDO, null, jogador.getInfoJogador()));
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.FALTA_ENVIDO, null, jogador.getInfoJogador()));
                return;
            case REAL_ENVIDO:
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.QUERO, null, jogador.getInfoJogador()));
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.NAO_QUERO, null, jogador.getInfoJogador()));
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.FALTA_ENVIDO, null, jogador.getInfoJogador()));
                return;
            case FALTA_ENVIDO:
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.QUERO, null, jogador.getInfoJogador()));
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.NAO_QUERO, null, jogador.getInfoJogador()));
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
                return;

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
     */
    public static void calcularGanhadorRodada(Mao mao, Rodada rodadaAtual, Jogador jogador1, Jogador jogador2, Mensagem<Jogada> msgFromJogador1, Mensagem<Jogada> msgFromJogador2) {
        Jogada jogada1 = msgFromJogador1.getValor();
        Jogada jogada2 = msgFromJogador2.getValor();
        //Ambas jogadas serão JOGADAS_SIMPLES neste momento.
        jogador1.getCartas().remove(jogada1.getCarta());
        jogador2.getCartas().remove(jogada2.getCarta());
        calculaGanhadorJogadaSimples(rodadaAtual, jogador1, jogador2, jogada1.getCarta(), jogada2.getCarta());
    }

    /**
     * Verifica o jogador ganhador e adiciona ele como ganhador da rodada.
     *
     * @param rodadaAtual
     * @param jogador1
     * @param jogador2
     * @param carta1
     * @param carta2
     */
    public static void calculaGanhadorJogadaSimples(Rodada rodadaAtual, Jogador jogador1, Jogador jogador2, Carta carta1, Carta carta2) {
        if (carta1.getRanking() > carta2.getRanking()) {
            rodadaAtual.setJogadorGanhador(jogador1);
        } else if (carta1.getRanking() == carta2.getRanking()) {
            rodadaAtual.setJogadorGanhador(jogador1);
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
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static boolean tratarJogada(Mao mao, Rodada rodadaAtual, Mensagem<Jogada> jogada, Jogador jogador1, Jogador jogador2) throws IOException, ClassNotFoundException {
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
//                return chamarEnvido(jogador1, jogador2);
                return true;
            case REAL_ENVIDO:
//                return chamarRealEnvido(jogador1, jogador2);
                return true;
            case FALTA_ENVIDO:
//                return chamarFaltaEnvido(jogador1, jogador2);
                return true;
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
                aceitou = Jogo.chamarTruco(mao, rodadaAtual, jogador1, jogador2, jogada);
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
                aceitou = Jogo.chamarReTruco(mao, rodadaAtual, jogador1, jogador2, jogada);
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
     * @return <i>true</i> se jogador2 aceitou. <i>false</i> se recusou
     */
    public static boolean chamarTruco(Mao mao, Rodada rodadaAtual, Jogador jogador1, Jogador jogador2, Mensagem<Jogada> jogada) throws IOException, ClassNotFoundException {
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
                return tratarJogada(mao, rodadaAtual, msgJogadaJogador2, jogador2, jogador1);
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
     * @return <i>true</i> se jogador2 aceitou. <i>false</i> se recusou
     */
    public static boolean chamarReTruco(Mao mao, Rodada rodadaAtual, Jogador jogador1, Jogador jogador2, Mensagem<Jogada> jogada) throws IOException, ClassNotFoundException {
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
                return tratarJogada(mao, rodadaAtual, msgJogadaJogador2, jogador2, jogador1);
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
}

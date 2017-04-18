package jogo;

import comunicacao.Mensagem;
import comunicacao.transporte.Info;
import comunicacao.transporte.JogadorInfo;
import comunicacao.transporte.MenuAcoes;
import comunicacao.transporte.PartidaInfo;
import comunicacao.transporte.RodadaInfo;
import enums.AcaoDaJogada;
import enums.AcaoDaMensagem;
import enums.Carta;
import enums.StatusDaPartida;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Util;

/**
 * Partida: Representa um jogo de truco. É composto por várias mãos. Termina
 * quando algum dos jogadores atingir o pontuação final (12 ou 24).
 *
 * @class Partida
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 30/03/2017
 */
public class Partida extends Thread {

    private final Long idPartida;
    private final String nomePartida;
    private final List<Jogador> jogadores;
    private final List<Mao> maos;

    private StatusDaPartida status;

    public Partida(Long id, String nome, Jogador primeiroJogador) {
        //TODO: verificase se o IF comentado é obrigatório ou não.
//        if (Util.isNull(id) || Util.isNull(primeiroJogador) || Util.isStringEmpty(nome)) {
//            throw new IllegalArgumentException("Parâmetros inválidos. [Contrutor classe Partida]");
//        }
        this.idPartida = id;
        this.nomePartida = nome;
        this.maos = new ArrayList<>();
        this.jogadores = new ArrayList<>();
        this.jogadores.add(primeiroJogador);
        this.status = StatusDaPartida.AGUARDANDO_JOGADOR;
    }

    public Long getIdPartida() {
        return idPartida;
    }

    public String getNomePartida() {
        return nomePartida;
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public StatusDaPartida getStatus() {
        return status;
    }

    public PartidaInfo getInfoPartida() {
        if (this.jogadores.size() == 1) {
            return new PartidaInfo(this.idPartida, this.nomePartida, this.jogadores.get(0));
        }
        return new PartidaInfo(this.idPartida, this.nomePartida, this.jogadores.get(0), this.jogadores.get(1));
    }

    public synchronized void start(Jogador jogador) {
        if (this.jogadores.isEmpty() || !StatusDaPartida.AGUARDANDO_JOGADOR.equals(this.status)) {
            throw new IllegalStateException("Partida não está AGUARDANDO_JOGADOR. É necessário a existência de dois jogadores para a partida poder iniciar.");
        }
        this.jogadores.add(jogador);
        super.start();
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Partida other = (Partida) obj;
        return Objects.equals(this.idPartida, other.idPartida);
    }

    @Override
    public void run() {
        this.status = StatusDaPartida.EM_ANDAMENTO;
        jogadores.forEach(jogador -> {
            try {
                Mensagem<PartidaInfo> msg = new Mensagem<>(AcaoDaMensagem.INICIAR_PARTIDA, getInfoPartida());
                Util.printarEnvioInfo(jogador, msg);
                jogador.getConexao().enviar(msg);
            } catch (IOException ex) {
                Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        while (this.jogadores.get(0).getConexao().isConectionOpen() && this.jogadores.get(1).getConexao().isConectionOpen() && StatusDaPartida.EM_ANDAMENTO.equals(this.status)) {
            try {
                Mao maoAtual = new Mao();
                if ((maos.size() + 1) % 2 == 0) {//jogador 2 começa
                    iniciarMao(maoAtual, jogadores.get(1), jogadores.get(0));
                } else {//jogador 2 começa
                    iniciarMao(maoAtual, jogadores.get(0), jogadores.get(1));
                }
                maos.add(maoAtual);
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    if (!jogadores.get(0).getConexao().isConectionOpen()) {
                        informarPerdaDeConexao(jogadores.get(1));
                        break;
                    }
                    if (!jogadores.get(1).getConexao().isConectionOpen()) {
                        informarPerdaDeConexao(jogadores.get(0));
                        break;
                    }
                } catch (IOException ex1) {
                    Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex1);
                    break;
                }
            }
        }

        // TODO: aqui estará toda a lógica do jogo. 
        // 1. Deverá aguarda a ação do jogadorX e tratar a jogada deste.
        // 2. Aguardar para que o jogadorX+1 jogue para tratar sua jogada e 
        //   determinar se termina a rodada ou não.
        // 3. Caso a rodada não tenha terminado, volta para o passo 1; Caso a 
        //   rodada tenha sido terminada, sorteia novas cartas iniciando nova 
        //   rodada e aguarda jogadorX+1 jogar. 
        // 4. Quando jogadorX+1 jogar, aguarda o jogadorX jogar e trata sua 
        //   jogada. Determina se a rodada acabou ou não.
        // 5. Se a rodada terminou, sorteia as cartas iniciando nova rodada e 
        //   vai para etapa 1.
        //
    }

    /**
     * Envia as cartas para o jogador
     *
     * @param jogador
     * @throws IOException
     */
    private void enviarCartas(Jogador jogador) throws IOException {
        Mensagem<JogadorInfo> msg = new Mensagem<>(AcaoDaMensagem.MOSTRAR_CARTAS, new JogadorInfo(jogador));
        Util.printarEnvioInfo(jogador, msg);
        jogador.getConexao().enviar(msg);
    }

    /**
     * Realiza e controla uma Mao.
     *
     * @param mao
     * @param jogador1
     * @param jogador2
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void iniciarMao(Mao mao, Jogador jogador1, Jogador jogador2) throws IOException, ClassNotFoundException {
        Jogo.darCartas(jogador1, jogador2);
        enviarCartas(jogador1);
        enviarCartas(jogador2);
        Mensagem msgFromJogador1 = null, msgFromJogador2 = null;
        while (mao.getJogadorGanhador() == null) {//Enquanto a mão não tiver um ganhador.
            Rodada rodadaAtual = new Rodada();

            while (rodadaAtual.getJogadorGanhador() == null) {//Enquanto a rodada não tiver um ganhador
                enviarDadosJogada(jogador1, jogador2, mao, msgFromJogador2);
                msgFromJogador1 = jogador1.getConexao().receber();
                Util.printarRecebimentoInfo(jogador1, msgFromJogador1);

                enviarDadosJogada(jogador2, jogador1, mao, msgFromJogador1);
                msgFromJogador2 = jogador2.getConexao().receber();
                Util.printarRecebimentoInfo(jogador2, msgFromJogador2);

                calcularGanhadorRodada(mao, rodadaAtual, jogador1, jogador2, msgFromJogador1, msgFromJogador2);
                if (rodadaAtual.getJogadorGanhador() != null) {
                    msgFromJogador1 = msgFromJogador2 = null;
                    if (jogador2.equals(rodadaAtual.getJogadorGanhador())) {
                        //Se o jogador2 ganhar a rodada, ele deverá começar a próxima. 
                        //Logo ele passa a ser o jogador1
                        Jogador temp = jogador1;
                        jogador1 = jogador2;
                        jogador2 = temp;
                    }
                }
            }
            enviarDadosFinalRodada(rodadaAtual);
            calcularGanhadorDaMao(rodadaAtual, mao);
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
    private void enviarDadosJogada(Jogador jogadorQueVaiJogar, Jogador jogadorParaAguardar, Mao mao, Mensagem<Jogada> msgOutroJogador) throws IOException {
        jogadorQueVaiJogar.getConexao().enviar(montarMenuJogador(mao, jogadorQueVaiJogar, msgOutroJogador));
        jogadorParaAguardar.getConexao().enviar(new Mensagem(AcaoDaMensagem.AGUARDAR_OUTRO_JOGADOR, null));
    }

    /**
     * Monta o menu com opções de jogadas que o jogador pode realizar
     *
     * @param mao
     * @param jogador
     * @param msgOutraJogada
     * @return
     */
    private Mensagem<MenuAcoes> montarMenuJogador(Mao mao, Jogador jogador, Mensagem<Jogada> msgOutraJogada) {
        MenuAcoes menu = new MenuAcoes(msgOutraJogada == null ? null : msgOutraJogada.getValor());
        List<Jogada> jogadasPossiveis = new ArrayList<>();
        if (msgOutraJogada == null) {
            montarMenuSimples(mao, jogador, jogadasPossiveis);
        } else {
            montarMenuComplexo(mao, jogador, jogadasPossiveis, msgOutraJogada.getValor());
        }
        jogadasPossiveis.add(new Jogada(AcaoDaJogada.IR_PARA_BARALHO, null));
        menu.getJogadas().addAll(jogadasPossiveis);
        return new Mensagem<>(AcaoDaMensagem.JOGAR, menu);
    }

    private void calcularGanhadorRodada(Mao mao, Rodada rodadaAtual, Jogador jogador1, Jogador jogador2, Mensagem<Jogada> msgFromJogador1, Mensagem<Jogada> msgFromJogador2) {
        Jogada jogada1 = msgFromJogador1.getValor();
        Jogada jogada2 = msgFromJogador2.getValor();
        rodadaAtual.getJogadas().add(jogada1);
        rodadaAtual.getJogadas().add(jogada2);
        boolean mesmaAcao = jogada1.getAcaoDaJogada().equals(jogada2.getAcaoDaJogada());
        switch (jogada1.getAcaoDaJogada()) {
            case JOGADA_SIMPLES:
                if (mesmaAcao) {
                    jogador1.getCartas().remove(jogada1.getCarta());
                    jogador2.getCartas().remove(jogada2.getCarta());
                    calculaGanhadorJogadaSimples(rodadaAtual, jogador1, jogador2, jogada1.getCarta(), jogada2.getCarta());
                }
        }
    }

    private void calculaGanhadorJogadaSimples(Rodada rodadaAtual, Jogador jogador1, Jogador jogador2, Carta carta1, Carta carta2) {
        if (carta1.getRanking() >= carta2.getRanking()) {
            rodadaAtual.setJogadorGanhador(jogador1);
        } else {
            rodadaAtual.setJogadorGanhador(jogador2);
        }
    }

    private void enviarDadosFinalRodada(Rodada rodadaAtual) {
        jogadores.forEach(jogador -> {
            try {
                Mensagem<RodadaInfo> msg = new Mensagem<>(AcaoDaMensagem.DADOS_RODADA, rodadaAtual.getInfoRodada());
                Util.printarEnvioInfo(jogador, msg);
                jogador.getConexao().enviar(msg);
            } catch (IOException ex) {
                Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void calcularGanhadorDaMao(Rodada rodadaAtual, Mao mao) {
        //TODO
    }

    /**
     * Monta o menu baseado nas cartas e no estado da mao. Não considera a
     * jogada anterior.
     *
     * @param mao
     * @param jogador
     * @param jogadasPossiveis
     */
    private void montarMenuSimples(Mao mao, Jogador jogador, List<Jogada> jogadasPossiveis) {
        jogador.getCartas().stream().forEach((carta) -> {
            jogadasPossiveis.add(new Jogada(AcaoDaJogada.JOGADA_SIMPLES, carta, jogador.getInfoJogador()));
        });
        if (Jogo.podeChamarTruco(mao)) {
            jogadasPossiveis.add(new Jogada(AcaoDaJogada.TRUCO, null, jogador.getInfoJogador()));
        } else if (Jogo.podeChamarRetruco(mao)) {
            jogadasPossiveis.add(new Jogada(AcaoDaJogada.RETRUCO, null, jogador.getInfoJogador()));
        } else if (Jogo.podeChamarValeQuatro(mao)) {
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
    private void montarMenuComplexo(Mao mao, Jogador jogador, List<Jogada> jogadasPossiveis, Jogada jogadaAnterior) {
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
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.QUERO, null, jogador.getInfoJogador()));
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.NAO_QUERO, null, jogador.getInfoJogador()));
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.CONTRA_FLOR, null, jogador.getInfoJogador()));
                jogadasPossiveis.add(new Jogada(AcaoDaJogada.CONTRA_FLOR_E_RESTO, null, jogador.getInfoJogador()));
                return;

        }
    }

    private void informarPerdaDeConexao(Jogador jogador) throws IOException {
        jogador.getConexao().enviar(new Mensagem<>(AcaoDaMensagem.INFORMAR_PERDA_CONEXAO, new Info("O outro jogador perdeu a conexão com a partida...")));
    }

}

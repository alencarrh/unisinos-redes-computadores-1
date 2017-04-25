package jogo;

import comunicacao.Mensagem;
import comunicacao.transporte.Info;
import comunicacao.transporte.MaoInfo;
import comunicacao.transporte.PartidaInfo;
import enums.AcaoDaMensagem;
import enums.StatusDaPartida;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import servidor.Servidor;
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
    private int indiceJogadorVencedor;
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
        this.indiceJogadorVencedor = -1;
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

    public int getIndiceJogadorVencedor() {
        return indiceJogadorVencedor;
    }

    public void setIndiceJogadorVencedor(int indiceJogadorVencedor) {
        this.indiceJogadorVencedor = indiceJogadorVencedor;
    }

    public PartidaInfo getInfoPartida() {
        return new PartidaInfo(this);
    }

    @Override
    public int hashCode() {
        int hash = idPartida.intValue();
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

    /**
     * Inicia a partida
     *
     * @param jogador
     */
    public synchronized void start(Jogador jogador) {
        if (this.jogadores.isEmpty() || !StatusDaPartida.AGUARDANDO_JOGADOR.equals(this.status)) {
            throw new IllegalStateException("Partida não está AGUARDANDO_JOGADOR. É necessário a existência de dois jogadores para a partida poder iniciar.");
        }
        this.jogadores.add(jogador);
        super.start();
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

        boolean semErros = true;

        while (this.jogadores.get(0).getConexao().isConectionOpen() && this.jogadores.get(1).getConexao().isConectionOpen() && StatusDaPartida.EM_ANDAMENTO.equals(this.status) && semErros) {
            try {
                Mao maoAtual = new Mao();
                if ((maos.size() + 1) % 2 == 0) {//jogador 2 começa
                    iniciarMao(maoAtual, jogadores.get(1), jogadores.get(0));
                } else {//jogador 1 começa
                    iniciarMao(maoAtual, jogadores.get(0), jogadores.get(1));
                }
                maos.add(maoAtual);
                if (existeGanhadorPartida()) {
                    finalizarPartida();
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
                semErros = false;
            }
        }
        if (semErros) {
            informarPlacarFinal();
            //Reinicia o JogadorListener de ambos jogadores (para voltarem ao menu principal)
            Servidor.iniciarJogadorListener(jogadores.get(0));
            Servidor.iniciarJogadorListener(jogadores.get(1));
        } else {
            informarJogadoresPerdaConexao();
        }
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
        Mensagem<Jogada> msgJogadaJogador1, msgJogadaJogador2;
        Jogador jogadorMao = jogador1;
        //Enquanto a mão não tiver um ganhador.
        while (mao.getJogadorGanhador() == null) {
            Jogo.enviarCartas(jogador1);
            Jogo.enviarCartas(jogador2);
            Rodada rodadaAtual = new Rodada();
            mao.getRodadas().add(rodadaAtual);
            //Enquanto a rodada não tiver um ganhador
            while (rodadaAtual.getJogadorGanhador() == null) {
                msgJogadaJogador1 = realizarJogada(mao, rodadaAtual, jogador1, jogador2, null, jogadorMao);

                //Verificação para caso o jogador1 IR_PARA_BARALHO
                if (rodadaAtual.getJogadorGanhador() == null) {
                    msgJogadaJogador2 = realizarJogada(mao, rodadaAtual, jogador2, jogador1, msgJogadaJogador1, jogadorMao);
                } else {
                    //Inverter jogador1 e jogador2 pois jogador2 venceu está rodada
                    Jogador temp = jogador1;
                    jogador1 = jogador2;
                    jogador2 = temp;
                    break;
                }

                //Verificação para caso o jogador2 IR_PARA_BARALHO
                if (rodadaAtual.getJogadorGanhador() != null) {
                    //Indica que o jogador2 correu. Segue para próxima rodada.
                    continue;
                }

                //Neste momento, é garantido que ambas jogadas foram JOGADAS_SIMPLES
                Jogo.calcularGanhadorRodada(mao, rodadaAtual, jogador1, jogador2, msgJogadaJogador1, msgJogadaJogador2, jogadorMao);

                if (existeGanhadorPartida()) {
                    //Retorna para o while principal
                    return;
                }

                //Aqui já deve-se, obrigatóriamente ter um ganhador
                if (jogador2.equals(rodadaAtual.getJogadorGanhador())) {
                    //Se o jogador2 ganhar a rodada, ele deverá começar a próxima. 
                    //Logo ele passa a ser o jogador1
                    Jogador temp = jogador1;
                    jogador1 = jogador2;
                    jogador2 = temp;
                }
            }
            if (mao.getJogadorGanhador() == null) {
                calcularGanhadorDaMao(mao);
            }
            if (existeGanhadorPartida()) {
                //Retorna para o while principal
                return;
            } else {
                enviarDadosDaMao(mao);
                enviarPlacar();
            }
        }
    }

    public Mensagem<Jogada> realizarJogada(Mao mao, Rodada rodadaAtual, Jogador jogador1, Jogador jogador2, Mensagem<Jogada> jogadaAnterior, Jogador jogadorMao) throws IOException, ClassNotFoundException {
        Mensagem<Jogada> jogadaDesteJogador;
        boolean vezDesteJogador;
        do {
            Jogo.enviarDadosJogada(jogador1, jogador2, mao, jogadaAnterior);
            jogadaDesteJogador = jogador1.getConexao().receber();
            Util.printarRecebimentoInfo(jogador1, jogadaDesteJogador);
            vezDesteJogador = Jogo.tratarJogada(mao, rodadaAtual, jogadaDesteJogador, jogador1, jogador2, jogadorMao);
        } while (vezDesteJogador);
        return jogadaDesteJogador;
    }

    /**
     * Envia os dados da rodada para os jogadores.
     *
     * @param rodadaAtual
     */
    private void enviarDadosDaMao(Mao mao) {
        jogadores.forEach(jogador -> {
            try {
                Mensagem<MaoInfo> msg = new Mensagem<>(AcaoDaMensagem.DADOS_MAO, mao.getInfoMao());
                Util.printarEnvioInfo(jogador, msg);
                jogador.getConexao().enviar(msg);
            } catch (IOException ex) {
                Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void calcularGanhadorDaMao(Mao mao) {
        //rodada atual vai ser adicionada na lista Mao.rodadas após 
        switch (mao.getRodadas().size()) {
            case 0:
                return;
            case 1:
                return;
            case 2:
                if (mao.getRodadas().get(0).isEmpatou()) {
                    //Se empatou a 1ª rodada, o jogador que ganhar a 2ª vence.
                    if (mao.getRodadas().get(1).isEmpatou()) {
                        //TODO: se empatou a primeira e a segunda, o ganhador da terceira rodada vence.
                        //  Se empatar as 3 rodadas, o jogador mão vence.
                    }
                    Jogo.definirGanhadorMao(mao, mao.getRodadas().get(1).getJogadorGanhador());
                } else if (mao.getRodadas().get(1).isEmpatou()) {
                    //Se empatou a 2ª rodada, o jogador que ganhou 1ª vence
                    Jogo.definirGanhadorMao(mao, mao.getRodadas().get(0).getJogadorGanhador());
                }
                //Verifica se um dos jogadores ganhou as duas primeiras rodadas
                calcularJogadorGanhador(mao);
                return;
            case 3:
                if (mao.getRodadas().get(2).isEmpatou()) {
                    //Se empatou a 3ª rodada, o jogador que ganhou a 2ª vence.
                    //TODO: verificar se outras rodadas também não empataram
                    Jogo.definirGanhadorMao(mao, mao.getRodadas().get(1).getJogadorGanhador());
                }
                calcularJogadorGanhador(mao);
        }
    }

    private void calcularJogadorGanhador(Mao mao) {
        int rodadasGanhasJogador1 = 0, rodadasGanhasJogador2 = 0;
        for (Rodada rodada : mao.getRodadas()) {
            if (rodada.getJogadorGanhador().equals(jogadores.get(0))) {
                rodadasGanhasJogador1++;
            } else {
                rodadasGanhasJogador2++;
            }
        }
        //Jogador que ganhou 2 rodadas vence
        if (rodadasGanhasJogador1 == 2) {
            Jogo.definirGanhadorMao(mao, jogadores.get(0));
        } else if (rodadasGanhasJogador2 == 2) {
            Jogo.definirGanhadorMao(mao, jogadores.get(1));
        }
    }

    /**
     * Envia as informações de ganhador e perdedor junto com demais informações
     * da partida para todos os jogadores.
     */
    private void informarPlacarFinal() {
        Jogador jogadorGanhador = jogadores.stream().filter((jogador) -> (jogador.getTentos() >= Jogo.PONTUACAO_MAXIMA)).collect(Collectors.toList()).get(0);
        this.indiceJogadorVencedor = this.jogadores.indexOf(jogadorGanhador);
        jogadores.forEach(jogador -> {
            try {
                Mensagem<PartidaInfo> msg = new Mensagem<>(AcaoDaMensagem.FINALIZAR_PARTIDA, this.getInfoPartida());
                Util.printarEnvioInfo(jogador, msg);
                jogador.getConexao().enviar(msg);
            } catch (IOException ex) {
                Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     * Verifica se já existe um jogador com a pontuação necessária para ganhar a
     * partida.
     *
     * @return
     */
    private boolean existeGanhadorPartida() {
        return jogadores.stream().anyMatch((jogador) -> (jogador.getTentos() >= Jogo.PONTUACAO_MAXIMA));
    }

    /**
     * Finaliza a partida trocando seu status para FINALIZADA.
     */
    private void finalizarPartida() {
        this.status = StatusDaPartida.FINALIZADA;
    }

    /**
     * Verifica os usuários conectados e envia mensagem que foi perdido a
     * conexão com o outro usuário, reiniciando a sua thread principal para
     * voltar ao menu principal.
     */
    private void informarJogadoresPerdaConexao() {
        try {
            if (jogadores.get(0).getConexao().isConectionOpen()) {
                informarPerdaDeConexao(jogadores.get(0));
                Servidor.iniciarJogadorListener(jogadores.get(0));
            } else {
                Servidor.removerJogadorListener(jogadores.get(0));
            }
        } catch (IOException ex1) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex1);
        }
        try {
            if (jogadores.get(1).getConexao().isConectionOpen()) {
                informarPerdaDeConexao(jogadores.get(1));
                Servidor.iniciarJogadorListener(jogadores.get(1));
            } else {
                Servidor.removerJogadorListener(jogadores.get(1));

            }
        } catch (IOException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Envia mensagem informando parda de conexão com o outro jogador.
     *
     * @param jogador
     * @throws IOException
     */
    private void informarPerdaDeConexao(Jogador jogador) throws IOException {
        jogador.getConexao().enviar(new Mensagem<>(AcaoDaMensagem.INFORMAR_PERDA_CONEXAO, new Info("O outro jogador perdeu a conexão com a partida...")));
    }

    /**
     * Envia informações de placar
     */
    private void enviarPlacar() {
        jogadores.forEach(jogador -> {
            try {
                Mensagem<PartidaInfo> msg = new Mensagem<>(AcaoDaMensagem.PLACAR, this.getInfoPartida());
                Util.printarEnvioInfo(jogador, msg);
                jogador.getConexao().enviar(msg);
            } catch (IOException ex) {
                Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}

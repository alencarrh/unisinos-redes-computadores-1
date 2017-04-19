package jogo;

import comunicacao.Mensagem;
import comunicacao.transporte.Info;
import comunicacao.transporte.PartidaInfo;
import comunicacao.transporte.RodadaInfo;
import enums.AcaoDaMensagem;
import enums.StatusDaPartida;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
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
                } else {//jogador 2 começa
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
        Jogo.enviarCartas(jogador1);
        Jogo.enviarCartas(jogador2);
        Mensagem<Jogada> msgJogadaJogador1, msgJogadaJogador2;

        //Enquanto a mão não tiver um ganhador.
        while (mao.getJogadorGanhador() == null) {
            Rodada rodadaAtual = new Rodada();
            mao.getRodadas().add(rodadaAtual);
            //Enquanto a rodada não tiver um ganhador
            while (rodadaAtual.getJogadorGanhador() == null) {
                msgJogadaJogador1 = realizarJogada(mao, rodadaAtual, jogador1, jogador2, null);

                //Verificação para caso o jogador1 IR_PARA_BARALHO
                if (rodadaAtual.getJogadorGanhador() == null) {
                    msgJogadaJogador2 = realizarJogada(mao, rodadaAtual, jogador2, jogador1, msgJogadaJogador1);
                } else {
                    jogador2.addTentos(mao.getEstadoDaMao().getValorDoEstado());
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
                Jogo.calcularGanhadorRodada(mao, rodadaAtual, jogador1, jogador2, msgJogadaJogador1, msgJogadaJogador2);

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
            enviarDadosRodada(rodadaAtual);
            calcularGanhadorDaMao(rodadaAtual, mao);
            if (existeGanhadorPartida()) {
                //Retorna para o while principal
                return;
            }
        }
    }

    public Mensagem<Jogada> realizarJogada(Mao mao, Rodada rodadaAtual, Jogador jogador1, Jogador jogador2, Mensagem<Jogada> jogadaAnterior) throws IOException, ClassNotFoundException {
        Mensagem<Jogada> jogadaDesteJogador;
        boolean vezDesteJogador;
        do {
            Jogo.enviarDadosJogada(jogador1, jogador2, mao, jogadaAnterior);
            jogadaDesteJogador = jogador1.getConexao().receber();
            Util.printarRecebimentoInfo(jogador1, jogadaDesteJogador);
            vezDesteJogador = Jogo.tratarJogada(mao, rodadaAtual, jogadaDesteJogador, jogador1, jogador2);
        } while (vezDesteJogador);
        return jogadaDesteJogador;
    }

    /**
     * Envia os dados da rodada para os jogadores.
     *
     * @param rodadaAtual
     */
    private void enviarDadosRodada(Rodada rodadaAtual) {
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
     * Envia as informações de ganhador e perdedor junto com demais informações
     * da partida para todos os jogadores.
     */
    private void informarPlacarFinal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Verifica se já existe um jogador com a pontuação necessária para ganhar a
     * partida.
     *
     * @return
     */
    private boolean existeGanhadorPartida() {
        return jogadores.stream().anyMatch((jogador) -> (jogador.getTentos() >= 24));
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

    private void informarPerdaDeConexao(Jogador jogador) throws IOException {
        jogador.getConexao().enviar(new Mensagem<>(AcaoDaMensagem.INFORMAR_PERDA_CONEXAO, new Info("O outro jogador perdeu a conexão com a partida...")));
    }
}

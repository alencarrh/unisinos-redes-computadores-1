package jogo;

import comunicacao.Mensagem;
import comunicacao.MensagemOpcoes;
import comunicacao.MensagemTexto;
import comunicacao.Opcao;
import enums.AcaoDaMensagem;
import enums.StatusDaPartida;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        if (Util.isNull(id) || Util.isNull(primeiroJogador) || Util.isStringEmpty(nome)) {
            throw new IllegalArgumentException("Parâmetros inválidos. [Contrutor partida]");
        }
        this.idPartida = id;
        this.nomePartida = nome;
        this.maos = new ArrayList<>();
        this.jogadores = new ArrayList<>();
        this.jogadores.add(primeiroJogador);
        this.status = StatusDaPartida.AGUARDANDO_JOGADOR;
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public StatusDaPartida getStatus() {
        return status;
    }

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
        jogadores.forEach((jogador) -> {
            try {
                jogador.getConexao().enviar(new MensagemTexto("Partida está sendo iniciada!", AcaoDaMensagem.TEXTO));
            } catch (IOException ex) {
                Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        Mensagem msg;
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

    private void enviarCartas(Jogador jogador) throws IOException {
        Mensagem mensagemOpcoes = new MensagemOpcoes(AcaoDaMensagem.MOSTRAR_CARTAS);
        for (int i = 0; i < jogador.getCartas().size(); i++) {
            ((MensagemOpcoes) mensagemOpcoes).addOpcao(new Opcao(String.valueOf(i), jogador.getCartas().get(i).getLabel()));
        }
        jogador.getConexao().enviar(mensagemOpcoes);
    }

    private void iniciarMao(Mao mao, Jogador jogador1, Jogador jogador2) throws IOException, ClassNotFoundException {
        Jogo.darCartas(jogador1, jogador2);
        enviarCartas(jogador1);
        enviarCartas(jogador2);

        while (mao.getJogadorGanhador() == null) {
            Mensagem msgFromJogador1 = jogador1.getConexao().receber();
            mao.setJogadorGanhador(jogador1);
        }
    }

}

package jogo;

import comunicacao.Mensagem;
import comunicacao.MensagemOpcoes;
import comunicacao.MensagemTexto;
import comunicacao.Opcao;
import enums.AcaoDaJogada;
import enums.AcaoDaMensagem;
import enums.Carta;
import enums.DirecaoDaMensagem;
import enums.StatusDaPartida;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Partida: Representa um jogo de truco. É composto por várias mãos. Termina
 * quando algum dos jogadores atingir o pontuação final (12 ou 24).
 *
 * @class Partida
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 30/03/2017
 */
public class Partida extends Thread {

    private final StatusDaPartida status;
    private final List<Jogador> jogadores;
    private final List<Mao> maos;

    public Partida(Jogador primeiroJogador, StatusDaPartida status) {
        this.maos = new ArrayList<>();
        this.jogadores = new ArrayList<>();
        this.jogadores.add(primeiroJogador);
        this.status = status;
    }

    public void addJogador(Jogador outroJogador) {
        this.jogadores.add(outroJogador);
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    @Override
    public void run() {
        //PRIMEIRA AÇÃO É DAR AS CARTAS
//        if (status.equals(status.EM_ANDAMENTO)) {
//            System.out.println("Distribuindo Cartas...");
//            for (int i = 0; i < jogadores.size(); i++) {
//                jogadores.get(i).setCartas(new Jogo().darCartas());
//            }
//
        jogadores.forEach((jogador) -> {
            try {
                jogador.getConexao().enviar(new MensagemTexto("Partida está sendo iniciada!", DirecaoDaMensagem.PARA_CLIENTE, AcaoDaMensagem.TEXTO));
                jogador.setCartas(Jogo.darCartas());
            } catch (IOException ex) {
                Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        Mensagem msg;
        while (this.jogadores.get(0).getConexao().isConectionOpen() && this.jogadores.get(1).getConexao().isConectionOpen()) {
            try {
//                if(){
                this.enviarCartas(jogadores.get(0));
                this.enviarCartas(jogadores.get(1));
                this.jogadores.get(0).getConexao().receber();
//                }
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
        Mensagem mensagemOpcoes = new MensagemOpcoes(DirecaoDaMensagem.PARA_CLIENTE, AcaoDaMensagem.MOSTRAR_CARTAS);
        for (int i = 0; i < jogador.getCartas().size(); i++) {
            ((MensagemOpcoes) mensagemOpcoes).addOpcao(new Opcao(String.valueOf(i), jogador.getCartas().get(i).getLabel()));
        }
        jogador.getConexao().enviar(mensagemOpcoes);
    }

}

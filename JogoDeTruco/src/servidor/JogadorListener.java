package servidor;

import comunicacao.Mensagem;
import comunicacao.MensagemJogador;
import enums.AcaoDaMensagem;
import enums.DirecaoDaMensagem;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import jogo.Jogador;
import jogo.Sala;

/**
 * Classe responável por controler uma única conexão (por objeto).
 *
 * @class JogadorListener
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 01/04/2017
 */
public class JogadorListener extends Thread implements Serializable {

    private static final List<Sala> SALAS = new ArrayList<>();
    private final Jogador jogador;

    public JogadorListener(Jogador jogador) {
        this.jogador = jogador;
    }

    /**
     * Inicia a thread que faz o controle de uma conexão especifica.
     */
    @Override
    public void run() {
        this.jogador.getConexao().enviar(new MensagemJogador(DirecaoDaMensagem.PARA_CLIENTE, AcaoDaMensagem.JOGADOR_CRIADO, this.jogador));
        while (this.jogador.getConexao().isConectionOpen()) {
            Mensagem msg = this.jogador.getConexao().receber();
            System.out.println("Servidor recebeu: " + msg);
            tratarMensagem(msg);
        }
    }

    private void tratarMensagem(Mensagem msg) {
        switch (msg.getAcaoDaMensagem()) {
            case FINALIZAR_CONEXAO:
                //TODO: finalizar esta conexão
                break;
            case LISTA_PARTIDAS_DISPONIVEIS:
                //TODO: enviar mensagem para usuário com lista de salas disponiveis
                break;
            case CRIAR_PARTIDA:
                //TODO: criar uma nova sala/partida
                break;
            case SAIR_DA_PARTIDA:
                //TODO: sair da partida // veriricar se será realmente implementado
                break;
            case ENTRAR_NA_PARTIDA:
                //TODO: adicionar o usuário a partida que ele selecionou
                break;
            case JOGADA:
                //TODO: fazer a jogada do jogador//verificar se será tratado aqui
                break;
            case MOSTRAR_RESULTADO_FINAL:
                //TODO: verificar se estará neste local. Deve retornar o placar final da partida.
                break;
        }
    }

    @Override
    public void interrupt() {
        try {
            this.jogador.getConexao().close(true);
        } catch (IOException ex) {
            Logger.getLogger(JogadorListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.interrupt();
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final JogadorListener other = (JogadorListener) obj;
        return Objects.equals(this.jogador, other.jogador);
    }

}

package servidor;

import comunicacao.Mensagem;
import comunicacao.MensagemJogador;
import enums.AcaoDaMensagem;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import jogo.Jogador;
import jogo.Partida;

/**
 * Classe responável por controler uma única conexão (por objeto).
 *
 * @class JogadorListener
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 01/04/2017
 */
public class JogadorListener extends Thread implements Serializable {

    private static Long idsPartidas = new Long(1);
    private static final List<Partida> PARTIDAS = new ArrayList<>();
    private Partida partidaDesteJogador;
    private final Jogador jogador;

    public JogadorListener(Jogador jogador) {
        this.jogador = jogador;
    }

    /**
     * Inicia a thread que faz o controle de uma conexão especifica.
     */
    @Override
    public void run() {
        try {
            enviarInformacoesDeUsuario();
            while (this.jogador.getConexao().isConectionOpen()) {
                Mensagem msg = this.jogador.getConexao().receber();
                System.out.println("Servidor recebeu(#" + jogador.getNomeJogador() + "): " + msg);
                tratarMensagem(msg);
            }
        } catch (IOException | ClassNotFoundException ex) {
            PARTIDAS.remove(this.partidaDesteJogador);
            Logger.getLogger(JogadorListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void tratarMensagem(Mensagem msg) throws IOException {
        switch (msg.getAcaoDaMensagem()) {
            //TODO: REFAZER ESTÁ PARTE...
        }
    }

    @Override
    public void interrupt() {
        try {
            this.jogador.getConexao().close();
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

    private void enviarInformacoesDeUsuario() throws IOException {
        Mensagem msg = new MensagemJogador(AcaoDaMensagem.JOGADOR_CRIADO, this.jogador);
        System.out.println("Servidor enviando(#" + this.jogador.getNomeJogador() + "): " + msg);
        this.jogador.getConexao().enviar(msg);
    }
}

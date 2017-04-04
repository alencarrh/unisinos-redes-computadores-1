package servidor;

import comunicacao.Mensagem;
import comunicacao.MensagemEntrarEmPartida;
import comunicacao.MensagemJogada;
import comunicacao.MensagemJogador;
import comunicacao.MensagemOpcoes;
import comunicacao.Opcao;
import enums.AcaoDaMensagem;
import enums.DirecaoDaMensagem;
import enums.StatusDaPartida;
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

    private static Long idsSalas = new Long(1);
    private static final List<Sala> SALAS = new ArrayList<>();
    private Sala salaDesteJogador;
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
            SALAS.remove(this.salaDesteJogador);
            Logger.getLogger(JogadorListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void tratarMensagem(Mensagem msg) throws IOException {
        switch (msg.getAcaoDaMensagem()) {
            case FINALIZAR_CONEXAO:
                //TODO: finalizar esta conexão
                break;
            case LISTA_PARTIDAS_DISPONIVEIS:
                //TODO: enviar mensagem para usuário com lista de salas disponiveis
                retornarSalasDisponiveis();
                break;
            case CRIAR_NOVA_PARTIDA:
                //TODO: criar uma nova sala/partida
                criarNovaPartida();
                break;
            case SAIR_DA_PARTIDA:
                //TODO: sair da partida // veriricar se será realmente implementado
                break;
            case ENTRAR_NA_PARTIDA:
                //TODO: adicionar o usuário a partida que ele selecionou
                adicionarJogadorNaPartida(msg);
                break;
            case JOGADA:
                //TODO: fazer a jogada do jogador//verificar se será tratado aqui
                realizarJogada((MensagemJogada) msg);
                break;
            case MOSTRAR_RESULTADO_FINAL:
                //TODO: verificar se estará neste local. Deve retornar o placar final da partida.
                break;
            case JOGADOR_CRIADO:
                //TODO: atualizar informações do jogador
                atualizarDadosJogador((MensagemJogador) msg);
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

    private void retornarSalasDisponiveis() throws IOException {
        MensagemOpcoes msg = new MensagemOpcoes(DirecaoDaMensagem.PARA_CLIENTE, AcaoDaMensagem.LISTA_PARTIDAS_DISPONIVEIS, "Opção: ");
        SALAS.stream().filter((sala) -> (StatusDaPartida.AGUARDANDO_JOGADOR.equals(sala.getStatus()))).forEachOrdered((sala) -> {
            msg.addOpcao(new Opcao(sala.getIdSala().toString(), sala.getJogagores().get(0).getNomeJogador()));
        });
        System.out.println("Servidor enviando(#" + this.jogador.getNomeJogador() + "): " + msg);
        this.jogador.getConexao().enviar(msg);
    }

    private void atualizarDadosJogador(MensagemJogador mensagemJogador) {
        this.jogador.setNomeJogador(mensagemJogador.getNomeJogador());
    }

    private void criarNovaPartida() {
        Sala novaSala = new Sala(getNextSalaId(), this.jogador.getNomeJogador(), jogador);
        SALAS.add(novaSala);
    }

    private Long getNextSalaId() {
        return idsSalas++;
    }

    private void adicionarJogadorNaPartida(Mensagem msgTemp) {
        MensagemEntrarEmPartida msg = (MensagemEntrarEmPartida) msgTemp;
        this.salaDesteJogador = SALAS.get(new Integer(msg.getIdSala()) - 1);
        if (StatusDaPartida.AGUARDANDO_JOGADOR.equals(this.salaDesteJogador.getStatus())) {
            this.salaDesteJogador.addJogador(jogador);
        }
    }

    private void enviarInformacoesDeUsuario() throws IOException {
        Mensagem msg = new MensagemJogador(DirecaoDaMensagem.PARA_CLIENTE, AcaoDaMensagem.JOGADOR_CRIADO, this.jogador);
        System.out.println("Servidor enviando(#" + this.jogador.getNomeJogador() + "): " + msg);
        this.jogador.getConexao().enviar(msg);
    }

    private void realizarJogada(MensagemJogada mensagemJogada) {
//        this.salaDesteJogador.jogar(mensagemJogada);
    }

}

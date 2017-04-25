package comunicacao.transporte;

import enums.AcaoDaJogada;
import java.io.Serializable;

/**
 *
 * @created 24/04/2017
 * @author alencar.hentges (CWI Software)
 */
public class EnvidoInfo implements Serializable {

    private JogadorInfo jogadorVencedor;
    private int pontosJogadorVencedor;
    private JogadorInfo outroJogador;
    private int pontosOutroVencedor;
    private AcaoDaJogada acao;
    private int tentosGanho;

    public EnvidoInfo() {
    }

    public EnvidoInfo(JogadorInfo jogadorVencedor, int pontosJogadorVencedor, JogadorInfo outroJogador, int pontosOutroVencedor, AcaoDaJogada acao, int tentosGanho) {
        this.jogadorVencedor = jogadorVencedor;
        this.pontosJogadorVencedor = pontosJogadorVencedor;
        this.outroJogador = outroJogador;
        this.pontosOutroVencedor = pontosOutroVencedor;
        this.acao = acao;
        this.tentosGanho = tentosGanho;
    }

    public JogadorInfo getJogadorVencedor() {
        return jogadorVencedor;
    }

    public void setJogadorVencedor(JogadorInfo jogadorVencedor) {
        this.jogadorVencedor = jogadorVencedor;
    }

    public int getPontosJogadorVencedor() {
        return pontosJogadorVencedor;
    }

    public void setPontosJogadorVencedor(int pontosJogadorVencedor) {
        this.pontosJogadorVencedor = pontosJogadorVencedor;
    }

    public JogadorInfo getOutroJogador() {
        return outroJogador;
    }

    public void setOutroJogador(JogadorInfo outroJogador) {
        this.outroJogador = outroJogador;
    }

    public int getPontosOutroVencedor() {
        return pontosOutroVencedor;
    }

    public void setPontosOutroVencedor(int pontosOutroVencedor) {
        this.pontosOutroVencedor = pontosOutroVencedor;
    }

    public AcaoDaJogada getAcao() {
        return acao;
    }

    public void setAcao(AcaoDaJogada acao) {
        this.acao = acao;
    }

    public int getTentosGanho() {
        return tentosGanho;
    }

    public void setTentosGanho(int tentosGanho) {
        this.tentosGanho = tentosGanho;
    }

}

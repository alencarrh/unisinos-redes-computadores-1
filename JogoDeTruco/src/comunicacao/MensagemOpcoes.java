package comunicacao;

import enums.AcaoDaMensagem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @class MensagemOpcoes
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 02/04/2017
 */
public class MensagemOpcoes extends Mensagem implements Serializable {

    private String pergunta;
    private final List<Opcao> opcoes;

    public MensagemOpcoes(AcaoDaMensagem acaoDaMensagem) {
        super(acaoDaMensagem);
        this.opcoes = new ArrayList<>();
        this.pergunta = null;
    }

    public MensagemOpcoes(AcaoDaMensagem acaoDaMensagem, String pergunta) {
        super(acaoDaMensagem);
        this.opcoes = new ArrayList<>();
        this.pergunta = pergunta;
    }

    public List<Opcao> getOpcoes() {
        return opcoes;
    }

    public Opcao getOpcao(String idOpcao) {
        return this.opcoes.get(this.opcoes.indexOf(new Opcao(idOpcao, null)));
    }

    public void addOpcao(Opcao opcao) {
        this.opcoes.add(opcao);
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    @Override
    public String toString() {
        String part1 = "MensagemOpcoes{" + "pergunta=" + pergunta + ", opcoes=[";
        String part2 = "";
        for (Opcao opcao : opcoes) {
            part2 += opcao.toString() + ",";
        }
        String part3 = "]}";
        return part1 + part2 + part3;
    }

}

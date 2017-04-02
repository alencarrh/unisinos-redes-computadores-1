package comunicacao;

import java.io.Serializable;
import java.util.Objects;

/**
 * @class Opcao
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 02/04/2017
 */
public class Opcao implements Serializable {

    public final String idOpcao;
    public final String labelOpcao;

    public Opcao(String idOpcao, String labelOpcao) {
        this.idOpcao = idOpcao;
        this.labelOpcao = labelOpcao;
    }

    public String getIdOpcao() {
        return idOpcao;
    }

    public String getLabelOpcao() {
        return labelOpcao;
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
        final Opcao other = (Opcao) obj;
        return Objects.equals(this.idOpcao, other.idOpcao);
    }

    @Override
    public String toString() {
        return idOpcao + " - " + labelOpcao;
    }

}

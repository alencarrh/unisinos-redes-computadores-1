package exemplo3;

import java.io.*;

class Pessoa implements Serializable {

    String nome;
    String dia, mes, ano;

    public Pessoa(String nome, int dia, int mes, int ano) {
        this.nome = nome;
        this.dia = dia + "";
        this.mes = mes + "";
        this.ano = ano + "";
    }

    public String getNome() {
        return nome;
    }

    public String getData() {
        return dia + "/" + mes + "/" + ano;
    }
}

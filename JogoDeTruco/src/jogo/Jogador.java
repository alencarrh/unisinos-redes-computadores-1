package jogo;

/**
 * @class Player
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 30/03/2017
 */
public class Jogador extends Thread {

    private Long idPlayer;
    private final String nome;
    private int tentos;

    public Jogador(Long idPlayer, String nome) {
        this.idPlayer = idPlayer;
        this.nome = nome;
        this.tentos = 0;
    }

    public Jogador(String nome) {
        this.nome = nome;
        this.tentos = 0;
    }

    @Override
    public void run() {

    }

    public Long getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(Long idPlayer) {
        this.idPlayer = idPlayer;
    }

    public int getTentos() {
        return tentos;
    }

    public void setTentos(int tentos) {
        this.tentos = tentos;
    }

}

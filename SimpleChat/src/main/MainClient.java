package main;

import classes.ConnectionController;
import classes.Mensagem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @class MainClient
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 25/03/2017
 */
public class MainClient {

    private static ConnectionController controler;
    private static final BufferedReader KEYBOARD_INPUT = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        System.out.print("Digite seu nome: ");
        String clienteName = KEYBOARD_INPUT.readLine();
        System.out.println("Digite '#sair' para sair do chat!\n");
        
        //Verifica se o IP do servidor foi passado por parâmetro
        if (args.length == 0) {
            controler = new ConnectionController("127.0.0.1", 6789);
        }else{
            controler = new ConnectionController(args[0], 6789);
        }
        Mensagem toServer;

        //Cria Thread que ficará escutando a porta(retorno do servidor)
        Thread listener = createListener();
        listener.start();

        //#sair encerra a conexão com o servidor.
        while (controler.isConectionOpen()) {
            toServer = new Mensagem(clienteName, KEYBOARD_INPUT.readLine());
            if (controler.isConectionOpen()) {
                if ("#sair".equalsIgnoreCase(toServer.getMensagem())) {
                     //envia mensagem indicando que o cliente irá finalizar a conexão.
                    controler.enviar(new Mensagem("finish_connection", null));
                } else {
                    //envia mensagem
                    controler.enviar(toServer);
                }
            }
        }
    }

    private static Thread createListener() {
        Thread listener = new Thread(() -> {
            while (controler.isConectionOpen()) {
                Mensagem fromServer = controler.receber();
                //Sinal do servidor para finalizar a conexão do cliente.
                if ("finish_connection".equalsIgnoreCase(fromServer.getId())) {
                    try {
                        controler.close(false);
                        //TODO tudo é finalizado, porém, fica no aguardo de um enter do "readLine()",
                        //e só após isto encerra a execução, então, o System.exit(0) se encarrega de encerrar imaditamente.
                        System.exit(0);
                    } catch (IOException ex) {
                        Logger.getLogger(MainClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //Retorno de confirmação de recebimento de mensagem do servidor.
                if (!"ok_connection".equalsIgnoreCase(fromServer.getId())) {
                    System.out.println(fromServer);
                }

            }
        });

        return listener;
    }
}

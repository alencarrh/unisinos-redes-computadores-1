/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import classes.Controler;
import classes.Mensagem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alenc
 */
public class MainClient {

    private static Controler controler;
    private static final BufferedReader KEYBOARD_INPUT = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        System.out.print("Digite seu nome: ");
        String clienteName = KEYBOARD_INPUT.readLine();
        System.out.println("Digite '#sair' para sair do chat!\n");
        Mensagem toServer;
        controler = new Controler("127.0.0.1", 6789);

        Thread listener = createListener();
        listener.start();

        while (controler.isConectionOpen()) {
            toServer = new Mensagem(clienteName, KEYBOARD_INPUT.readLine());
            if (controler.isConectionOpen()) {
                if ("#sair".equalsIgnoreCase(toServer.getMensagem())) {
                    controler.enviar(new Mensagem("finish_connection", null));
                } else {
                    controler.enviar(toServer);
                }
            }
        }
    }

    private static Thread createListener() {
        Thread listener = new Thread(new Runnable() {
            @Override
            public void run() {
                while (controler.isConectionOpen()) {
                    Mensagem fromServer = controler.receber();
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
                    if (!"ok_connection".equalsIgnoreCase(fromServer.getId())) {
                        System.out.println(fromServer);
                    }

                }
            }
        });

        return listener;
    }

}

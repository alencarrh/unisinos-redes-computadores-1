package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.ServerController;

/**
 * @class MainServer
 * @author Alencar Rodrigo Hentges <alencarhentges@gmail.com>
 * @date 25/03/2017
 */
public class MainServer {

    static ServerController server;
    private static final BufferedReader KEYBOARD_INPUT = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException, InterruptedException {
        server = new ServerController(6789);

        Thread startServer = new Thread(() -> {
            try {
                server.startServer();
            } catch (IOException ex) {
                Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        startServer.start();

        System.out.println("Preciona 'Enter' a qualquer momento para parar o servidor...");
        KEYBOARD_INPUT.readLine();

        server.stopServer();
    }
}

package exemplo3;

import java.io.*;
import java.net.*;

public class Cliente3 {

    public static void main(String argv[]) throws Exception {
        System.out.println("CLIENTE3!");
        String frase;
        String fraseModificada;
        ObjectOutputStream output;
        Pessoa p = new Pessoa("Fulano", 01, 01, 1990);

        Socket socketCliente = new Socket("127.0.0.1", 6789);

        output = new ObjectOutputStream(socketCliente.getOutputStream());
        output.flush();
        output.writeObject(p);
        output.flush();
        output.close();

        socketCliente.close();

    }
}

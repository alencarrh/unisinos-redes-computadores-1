package exemplo1;

import java.io.*;
import java.net.*;

class Servidor {

    public static void main(String argv[]) throws Exception {

        String fraseCliente;
        String fraseMaiusculas;

        ServerSocket socketRecepcao = new ServerSocket(6789);

        while (true) {
            Socket socketConexao = socketRecepcao.accept();
            BufferedReader doCliente = new BufferedReader(new InputStreamReader(socketConexao.getInputStream()));
            DataOutputStream paraCliente = new DataOutputStream(socketConexao.getOutputStream());
            fraseCliente = doCliente.readLine();
            fraseMaiusculas = fraseCliente.toUpperCase() + '\n';
            paraCliente.writeBytes(fraseMaiusculas);
        }
    }
}

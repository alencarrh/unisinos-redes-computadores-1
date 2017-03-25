package exemplo2;

import java.io.*;
import java.net.*;

class Servidor2 {
    public static void main(String argv[]) throws Exception {
        
        String fraseCliente;
        String fraseMaiusculas;

        ServerSocket socketRecepcao = new ServerSocket(6789);

        Socket socketConexao = socketRecepcao.accept();
        BufferedReader doCliente = new BufferedReader(new InputStreamReader(socketConexao.getInputStream()));
        DataOutputStream paraCliente = new DataOutputStream(socketConexao.getOutputStream());
        
        while(true) {
            
            fraseCliente= doCliente.readLine();
            if(fraseCliente.equals("FIM")){
                socketConexao.close();
                break;
            }else{
                fraseMaiusculas= fraseCliente.toUpperCase() + '\n';
                paraCliente.writeBytes(fraseMaiusculas);
            }
        }
    }
}


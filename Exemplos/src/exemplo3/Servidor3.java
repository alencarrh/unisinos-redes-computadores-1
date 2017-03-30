package exemplo3;

import java.io.*;
import java.net.*;

class Servidor3 {

    //private Pessoa p;
    public static void main(String argv[]) throws Exception {
        System.out.println("SERVIDOR3!");
        Pessoa p;
        String fraseCliente;
        String fraseMaiusculas;

        ServerSocket socketRecepcao = new ServerSocket(6789);

        Socket socketConexao = socketRecepcao.accept();
        //BufferedReader doCliente = new BufferedReader(new InputStreamReader(socketConexao.getInputStream()));
        //DataOutputStream paraCliente = new DataOutputStream(socketConexao.getOutputStream());

        ObjectInputStream input = new ObjectInputStream(socketConexao.getInputStream());
        p = (Pessoa) input.readObject();

        System.out.println(p.getNome() + " nasceu em " + p.getData());

        /*while(true) {
            
            fraseCliente= doCliente.readLine();
            if(fraseCliente.equals("FIM")){
                socketConexao.close();
                break;
            }else{
                fraseMaiusculas= fraseCliente.toUpperCase() + '\n';
                paraCliente.writeBytes(fraseMaiusculas);
            }
        }*/
        socketConexao.close();
    }
}

package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @created 02/05/2017
 * @author alencar.hentges (CWI Software)
 */
public class Teclado {

    private static final BufferedReader KEYBOARD_INPUT = new BufferedReader(new InputStreamReader(System.in));
    private static final String CARACTER_SAIDA = "0";

    public static Integer lerInteiro() {
        return lerInteiro(null, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static Integer lerInteiro(String msg) {
        return lerInteiro(msg, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static Integer lerInteiro(String msg, Integer minValue, Integer maxValue) {
        if (msg != null) {
            System.out.print(msg);
        }
        String input = "";
        do {
            try {
                input = KEYBOARD_INPUT.readLine();
                if (CARACTER_SAIDA.equals(input)) {
                    return null;
                }
                Integer value = Integer.valueOf(input);
                if (value > maxValue || value < minValue) {
                    System.out.println("Valor Inválido! Intervalo permitido [" + minValue + ", " + maxValue + "]");
                    if (msg != null) {
                        System.out.print("\n" + msg);
                    }
                    continue;
                }
                return value;
            } catch (IOException | NumberFormatException ex) {
                System.out.println("Entrada Inválida[" + input + "]!");
                if (msg != null) {
                    System.out.print("\n" + msg);
                }
            }
        } while (true);
    }

}

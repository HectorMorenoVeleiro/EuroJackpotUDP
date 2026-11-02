package ejercicio_eurojackpot.EuroJackpotUDP;

import java.util.Scanner;

public class Metodologia {

	 // convierte un array de ints de primitivas a cadena
	public static String jckAString(Scanner sc) {
        int[] jackpot = new int[5];
        String cadena = "";
        
        /* aquí creas una cadena ints y la rellenas por scanner en un bucle
        *  y luego le pasas esa cadena a un string mas un " "
        */
        for (int i = 0; i < jackpot.length; i++) {
            boolean condicion;
            do {
                condicion = false;
                System.out.println("Introduce el numero " + i + " para el eurojackpot: ");
                jackpot[i] = sc.nextInt();
                
                // metodo para prevenir que un numero sea menor que 0 o mayor que 50
                if (jackpot[i] < 0 || jackpot[i] > 50) {
                    throw new IllegalArgumentException("Error: el numero debe estar entre 0 y 50.");
                }
                
                // metodo para prevenir que se repitan numeros
                for (int j = 0; j < i; j++) {
                    if (jackpot[i] == jackpot[j]) {
                        throw new IllegalArgumentException("Error: no se pueden introducir numeros repetidos.");
                    }
                    
                }
                
            } while (condicion);
            cadena += jackpot[i] + " ";
        }

        return cadena;
    }
	
	public static int[] jckAIntArray(String cadena) {
        String[] jackpot = new String[5];
        
        // primero recortamos la cadena para quitar paréntesis extra y luego
        // la dividimos en espacios dentro de un StringArray
        jackpot = cadena.trim().split(" ");
        int[] jckcorregido = new int[5];
        
        // luego solamente pasamos el array de Strings a un array de ints de uno en uno
        for (int i = 0; i < jckcorregido.length; i++) {
            jckcorregido[i] = Integer.parseInt(jackpot[i]);
        }
        return jckcorregido;
    }
	

}

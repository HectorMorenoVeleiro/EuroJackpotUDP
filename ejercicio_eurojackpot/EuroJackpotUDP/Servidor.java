package ejercicio_eurojackpot.EuroJackpotUDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Servidor {

    public static void main(String[] args) {
        int puerto = 6000;
        
        // Array para la combinación ganadora
        int[] combinacionGanadora = new int[7];

        // 1. Generar los 5 primeros números
        for (int i = 0; i < 5; i++) {
            int numAleatorio;
            boolean repetido;
            do {
                repetido = false;
                // Math.random() * 50 da de 0 a 49, al sumar 1 va de 1 a 50
                numAleatorio = (int) (Math.random() * 50) + 1;
                
                // Comprobar que no se repita en los anteriores
                for (int j = 0; j < i; j++) {
                    if (combinacionGanadora[j] == numAleatorio) {
                        repetido = true;
                        break;
                    }
                }
            } while (repetido);
            combinacionGanadora[i] = numAleatorio;
        }

        // 2. Generar los 2 últimos números
        for (int i = 5; i < 7; i++) {
            int numAleatorio;
            boolean repetido;
            do {
                repetido = false;
                // Math.random() * 12 da de 0 a 11, al sumar 1 va de 1 a 12
                numAleatorio = (int) (Math.random() * 12) + 1;
                
                // Comprobar que no se repita en estos últimos
                for (int j = 5; j < i; j++) {
                    if (combinacionGanadora[j] == numAleatorio) {
                        repetido = true;
                        break;
                    }
                }
            } while (repetido);
            combinacionGanadora[i] = numAleatorio;
        }

        // Mostrar por consola la combinación ganadora generada para control
        System.out.println("=== COMBINACIÓN GANADORA GENERADA ===");
        System.out.print("Principales (1-50): ");
        for (int i = 0; i < 5; i++) {
            System.out.print(combinacionGanadora[i] + " ");
        }
        System.out.print("\nEstrellas (1-12): ");
        for (int i = 5; i < 7; i++) {
            System.out.print(combinacionGanadora[i] + " ");
        }
        System.out.println("\n-------------------------------------\n");

        try (DatagramSocket socket = new DatagramSocket(puerto)) {
            System.out.println("=== SERVIDOR UDP EUROJACKPOT INICIADO ===");
            System.out.println("Escuchando en el puerto " + puerto + "...\n");

            byte[] buffer = new byte[1024];

            while (true) {
                // Recibir el paquete del cliente
                DatagramPacket paqueteRecibido = new DatagramPacket(buffer, buffer.length);
                socket.receive(paqueteRecibido);

                // Obtener el String que mandó el cliente
                String mensajeCliente = new String(paqueteRecibido.getData(), 0, paqueteRecibido.getLength());
                System.out.println("Apuesta recibida del cliente: " + mensajeCliente.trim());

                // Transformar el String recibido a array de enteros usando tu método
                int[] numerosCliente = Metodologia.jckAIntArray(mensajeCliente);

                // Comprobar aciertos (comparando los números del cliente con los 5 principales ganadores)
                int aciertos = 0;
                for (int i = 0; i < numerosCliente.length; i++) {
                    for (int j = 0; j < 5; j++) { // Comparamos con los 5 primeros ganadores
                        if (numerosCliente[i] == combinacionGanadora[j]) {
                            aciertos++;
                        }
                    }
                }
                System.out.println("-> El cliente ha obtenido " + aciertos + " aciertos.");

                // Generar respuesta según los aciertos
                String respuesta;
                if (aciertos == 5) {
                    respuesta = "¡JACKPOT! Has acertado los 5 números principales. ¡Premio Gordo!";
                } else if (aciertos >= 3) {
                    respuesta = "¡Premio menor! Has conseguido " + aciertos + " aciertos.";
                } else {
                    respuesta = "Tienes " + aciertos + " aciertos. ¡Sigue probando!";
                }

                // Enviar la respuesta de vuelta al cliente
                byte[] bufferRespuesta = respuesta.getBytes();
                InetAddress direccionCliente = paqueteRecibido.getAddress();
                int puertoCliente = paqueteRecibido.getPort();

                DatagramPacket paqueteRespuesta = new DatagramPacket(
                        bufferRespuesta,
                        bufferRespuesta.length,
                        direccionCliente,
                        puertoCliente
                );

                socket.send(paqueteRespuesta);
                System.out.println("-> Resultado enviado al cliente.\n");
            }

        } catch (Exception e) {
            System.out.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
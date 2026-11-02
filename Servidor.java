package ejercicio_eurojackpot.EuroJackpotUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Servidor {
public static void main(String[] args) {
        int puertoLocal = 12346; // El puerto por el que escucha el receptor
        int puertoDestino = 12345; // El puerto al que responde
        byte[] buffer = new byte[1024];
        boolean continuar = true;

        try (DatagramSocket socket = new DatagramSocket(puertoLocal);
             Scanner scanner = new Scanner(System.in)) {
            
            InetAddress destino = InetAddress.getLocalHost();
            System.out.println("--- CHAT INICIADO (Receptor) ---");
            System.out.println("Esperando mensajes de la otra parte...");

            DatagramPacket recibo = new DatagramPacket(buffer, buffer.length);

            while (continuar) {
                
                socket.receive(recibo);

                int bytesRec = recibo.getLength();
                String paquete = new String(recibo.getData(), 0, bytesRec, StandardCharsets.UTF_8);

                if ("quit".equalsIgnoreCase(paquete.trim())) {
                    System.out.println("Orden 'quit' recibida. Cerrando receptor...");
                    continuar = false;
                    break;
                } else {
                    System.out.println("Compi: " + paquete);
                }

                
                System.out.print("Tú: ");
                String mensajeTexto = scanner.nextLine();

                if ("quit".equalsIgnoreCase(mensajeTexto.trim())) {
                    continuar = false;
                }

                byte[] mensajeBytes = mensajeTexto.getBytes(StandardCharsets.UTF_8);
                DatagramPacket envio = new DatagramPacket(mensajeBytes, mensajeBytes.length, destino, puertoDestino);
                socket.send(envio);
            }
            System.out.println("Chat cerrado correctamente.");

        } catch (IOException e) {
            System.err.println("Error en el chat: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

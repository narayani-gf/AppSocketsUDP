import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cliente {
    public static void main(String[] args) throws SocketException, UnknownHostException, IOException {
        // Creamos un mensaje para enviar
        String mensaje = new String("Dame la hora local.");
        // Establecemos al servidor
        String servidor = new String("localhost");

        // Establecemos un puerto acordado con el servidor
        int puerto = 8080;
        // Establecemos una espera maxima en milisegundos
        int espera = 5000;

        // Creamos Socket UDP
        DatagramSocket socketUDP = new DatagramSocket();
        // Obtengo la IP de localhost
        InetAddress hostServidor = InetAddress.getByName(servidor);

        // Se crea la petición UDP con el mensaje, IP y puerto
        DatagramPacket peticion = new DatagramPacket(mensaje.getBytes(), mensaje.getBytes().length, 
                hostServidor, puerto);
        // Establecemos que esperamos un tiempo maximo en milisegundos
        socketUDP.setSoTimeout(espera);
        System.out.println("Esperamos datos en un maximo de " + espera + " milisegundos...");
        // Se envía la petición UDP al servidor
        socketUDP.send(peticion);

        // Esperamos respuesta
        try {
            // Esperamos la respuesta no mayor a 1024 bytes
            byte[] buffer = new byte[1024];
            DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length);

            // Recibimos la respuesta
            socketUDP.receive(respuesta);

            // Convertimos a cadena los datos recibidos
            String strText = new String(respuesta.getData(), 0, respuesta.getLength());

            // Convertimos la cadena a tipo Fecha
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime horaServidor = LocalDateTime.parse(strText, formatter);

            // Imprimimos la hora del servidor
            System.out.println("Hora del servidor es: " + horaServidor.format(formatter));


            LocalDateTime horaCliente = LocalDateTime.now();
            System.out.println("Hora del cliente es:" + horaCliente.format(formatter));

            Duration diferencia = Duration.between(horaServidor, horaCliente);
            double segundos = diferencia.getSeconds() + diferencia.getNano() / 1_000_000_000.0;
            String duracion = String.format("%.3f", segundos);
            
            System.out.println("La diferencia entre las dos horas es: " + duracion + " segundos");

            // Cerramos la conexión
            socketUDP.close();
        } catch (SocketTimeoutException s) {
            System.out.println("Tiempo expirado para recibir respuesta: " + s.getMessage());
        }
    }
}

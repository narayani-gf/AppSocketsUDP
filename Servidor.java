import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Servidor {
    public static void main(String[] args) throws SocketException, IOException {
        // Establecemos un puerto acordado con el cliente
        int puerto = 8080;
        // Establecemos un retardo en milisegundos
        int retardo = 4000;

        // Creamos Socket UDP
        // Usamos try para que cierre el socket automáticamente al terminar
        try (DatagramSocket socketUDP = new DatagramSocket(puerto)) {
            // Esperamos petición no mayor a 1024 bytes
            byte[] buffer = new byte[1024];
            System.out.println("Servidor UDP escuchando en puerto: " + puerto + "...");

            // Comenzamos un bucle infinito para escuchar clientes
            while (true) {
                DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
                // Recibimos la respuesta
                socketUDP.receive(peticion);

                // Mostramos la IP y puerto del cliente
                System.out.println("Datagrama recibido del host: " + peticion.getAddress());
                System.out.println("desde el puerto remoto: " + peticion.getPort());
                System.out.println("Datos recibidos: " + new String(peticion.getData()));

                // Obtenemos la fecha y hora actual
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String strFecha = now.format(formatter);
                System.out.println("La hora del servidor es: " + strFecha);

                // Simulamos un retardo en milisegundos
                try {
                    System.out.println("Simulamos un retardo en milisegundos: " + retardo);
                    Thread.sleep(retardo);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Se crea la respuesta UDP con el mensaje, IP y puerto
                DatagramPacket respuesta = new DatagramPacket(strFecha.getBytes(), strFecha.getBytes().length,
                        peticion.getAddress(), peticion.getPort());
                // Se envía la respuesta al cliente
                socketUDP.send(respuesta);
                System.out.println("Datos enviados al cliente.");
            }
        }
    }
}
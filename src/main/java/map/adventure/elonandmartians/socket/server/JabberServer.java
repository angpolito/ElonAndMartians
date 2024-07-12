/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * La classe JabberServer avvia un server socket che accetta connessioni client e crea un nuovo thread per ciascun client.
 */
public class JabberServer {

    /**
     * Avvia il server socket sulla porta 6667 e accetta connessioni dai client.
     * Per ogni connessione accettata, viene creato un nuovo thread che gestisce la comunicazione con il client.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6667);
        System.out.println("Server started: " + serverSocket);

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Connection accepted: " + socket);
                new Thread(new ClientHandler(socket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


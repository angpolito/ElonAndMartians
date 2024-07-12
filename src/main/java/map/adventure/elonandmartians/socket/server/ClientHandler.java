/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.socket.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * La classe ClientHandler gestisce le comunicazioni con un client tramite socket.
 *
 */
class ClientHandler implements Runnable {
    private Socket socket;

    /**
     * Costruttore che inizializza il ClientHandler con un socket specificato.
     *
     * @param newSocket il socket associato al client.
     */
    ClientHandler(final Socket newSocket) {
        this.socket = newSocket;
    }

    /**
     * Gestisce la comunicazione con il client, legge i messaggi dal client,
     * li registra in un file CSV e risponde al client.
     */
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out;
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            while (true) {
                String str = in.readLine();
                if (str == null || str.equals("END")) {
                    break;
                }
                if (!str.trim().isEmpty()) {
                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    String socketInfo = socket.getInetAddress() + ":" + socket.getPort();

                    try (BufferedWriter fileOut = new BufferedWriter(new FileWriter("./src/main/java/map/adventure/elonandmartians/socket/server/error_log.csv", true))) {
                        File file;
                        file = new File("./src/main/java/map/adventure/elonandmartians/socket/server/error_log.csv");
                        if (file.length() == 0) {
                            fileOut.write("Date & Time,Socket,Report");
                            fileOut.newLine();
                        }
                        fileOut.write(timeStamp + "," + socketInfo + ",\"" + str + "\"");
                        fileOut.newLine();
                    }
                    System.out.println("Messaggio ricevuto dal Server: " + str);
                    out.println("Segnalazione effettuata con successo: " + str);
                } else {
                    System.out.println("Messaggio vuoto ricevuto, non salvato.");
                    out.println("Segnalazione vuota, non salvata.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

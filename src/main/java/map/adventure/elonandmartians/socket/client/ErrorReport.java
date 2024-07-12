/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.socket.client;

import map.adventure.elonandmartians.game.Engine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * La classe ErrorReport fornisce metodi per segnalare errori a un server specificato.
 */
public class ErrorReport {
    private ErrorReport() {}

    /**
     * Il metodo reportErrors stabilisce una connessione socket a localhost sulla porta 6667,
     * invia un messaggio di errore, riceve una risposta e la aggiunge ad un output.
     *
     * @param errorMessage
     * @throws IOException
     */
    public static void reportErrors(final String errorMessage) throws IOException {
        InetAddress addr = InetAddress.getByName("localhost");
        Socket socket = new Socket(addr, 6667);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out;
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            out.println(errorMessage);
            String response = in.readLine();
            Engine.appendToOutput(response);
            out.println("END");
        } finally {
            socket.close();
        }
    }
}


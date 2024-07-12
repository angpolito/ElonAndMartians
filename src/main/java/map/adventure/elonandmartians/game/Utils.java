/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import map.adventure.elonandmartians.type.Room;

/**
 * La classe Utils fornisce metodi di generica utilita' (attualmente gestisce i file e il parsing delle stringhe).
 */
public final class Utils {
    private static boolean Tabulation = false;

    private Utils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Carica un file di testo e restituisce un set di stringhe contenenti le righe del file.
     *
     * @param file il file da cui leggere le righe
     * @return un set di stringhe contenenti le righe del file
     * @throws IOException se si verifica un errore durante la lettura del file
     */
    public static Set<String> loadFileListInSet(final File file) throws IOException {
        Set<String> set = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                set.add(line.trim().toLowerCase());
            }
        }
        return set;
    }

    /**
     * Scompone una stringa in una lista di token, rimuovendo le stopwords specificate.
     *
     * @param string    la stringa da parseare
     * @param stopwords il set di stopwords da rimuovere
     * @return una lista di token ottenuti dalla stringa, senza le stopwords
     */
    public static List<String> parseString(final String string, final Set<String> stopwords) {
        List<String> tokens = new ArrayList<>();
        String[] split = string.toLowerCase().split("\\s+");
        for (String t : split) {
            if (!stopwords.contains(t)) {
                tokens.add(t);
            }
        }
        return tokens;
    }

    /**
     * Trova un oggetto nell'inventario.
     *
     * @param description la descrizione del gioco
     * @param id dell'oggetto da verificare
     * @return l'indice dell'oggetto se presente, altrimenti -1
     */
    public static int findInventoryIndex(final GameDescription description, final int id) {
        for (int i = 0; i < description.getInventory().size(); i++) {
            if (description.getInventory().get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Trova un oggetto in una stanza.
     *
     * @param room la stanza in cui cercare
     * @param id dell'oggetto da verificare
     * @return l'indice dell'oggetto della stanza se presente, altrimenti -1
     */
    public static int findObjectIndex(final Room room, final int id) {
        for (int i = 0; i < room.getObjects().size(); i++) {
            if (room.getObjects().get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Trova l'indice di una stanza.
     *
     * @param description
     * @param id della stanza da verificare
     * @return l'id della stanza se presente, altrimeni -1
     */
    public static int findRoomIndex(final GameDescription description, final int id) {
        for (int i = 0; i < description.getRooms().size(); i++) {
            if (description.getRooms().get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Dato una stringa, riconosce se è femminile o maschile
     * in base alla sua desinenza restituisce la stringa con l'articolo indeterminativo corretto e il nome.
     *
     * @param text la parola da processare
     * @return la parola con l'articolo indeterminativo corretto e il nome
     */
    public static String getArticle(final String text) {
        // Definisci i pattern per parole femminili e maschili
        Pattern femininePattern = Pattern.compile("\\b.*[aA]\\b");
        Pattern masculinePattern = Pattern.compile("\\b.*[oOeE]\\b");
        Pattern startsWithVowelPattern = Pattern.compile("[aeiouAEIOU][a-zA-Z]*[aeiouAEIOU]");
        Pattern startsWithSConsonantPattern = Pattern.compile("^[sS][^aeiouAEIOU].*");
        Pattern startsWithZPattern = Pattern.compile("^[zZ].*");

        String result = "";
        String[] words = text.split("\\s+");
        if (words.length > 0) {
            String firstWord = words[0];
            if (!startsWithUpperCase(firstWord)) {
                 if (femininePattern.matcher(firstWord).matches()) {
                    if (startsWithVowelPattern.matcher(firstWord).matches()) {
                        result = "Un'" + text;
                    } else {
                        result = "Una " + text;
                    }
                } else if (masculinePattern.matcher(firstWord).matches()) {
                    if (startsWithSConsonantPattern.matcher(firstWord).matches()
                            || startsWithZPattern.matcher(firstWord).matches()) {
                        result = "Uno " + text;
                    } else {
                        result = "Un " + text;
                    }
                } else {
                    result = "Articolo non riconosciuto per " + text;
                }
            } else {
                result = "" + text;
            }
        }
        return result;
    }

    /**
     * Verifica se il primo carattere di una stringa è maiuscolo
     * @param str la stringa da verificare
     * @return true se il primo carattere è maiuscolo, false altrimenti
     */
    public static boolean startsWithUpperCase(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        char firstChar = str.charAt(0);
        return Character.isUpperCase(firstChar);
    }


    /**
     * Stampa la posizione corrente del giocatore.
     */
    public static void printCurrentLocation() {
        Engine.appendToOutput("\n======================================================================================"
        + "==========");
        Engine.appendToOutput(Engine.getGame().getCurrentRoom().getName() + "\n");
        Engine.appendToOutput(Engine.getGame().getCurrentRoom().getDescription());
        Engine.appendToOutput("======================================================================================"
                + "==========\n");
    }

    /**
     * Gestisce la corretta formattazione del testo.
     *
     * @param text il testo da formattare
     * @return la stringa formattata
     */
    public static String wrapText(final String text) {
        int lineWidth;
        if (Tabulation) {
            lineWidth = 87;
        } else {
            lineWidth = 96;
        }
        StringBuilder wrappedText = new StringBuilder();
        int start = 0;

        while (start < text.length()) {
            // Trova l'indice del prossimo ritorno a capo
            int nextNewLine = text.indexOf('\n', start);

            // Se non ci sono altri ritorni a capo, considera il resto del testo
            if (nextNewLine == -1) {
                nextNewLine = text.length();
            }

            // Suddividi il testo in linee gestendo il ritorno a capo
            int end = Math.min(start + lineWidth, nextNewLine);

            while (start < nextNewLine) {
                if (end >= nextNewLine) {
                    wrappedText.append(text, start, nextNewLine);
                    wrappedText.append("\n ");
                    start = nextNewLine + 1;
                    break;
                }

                // Cerca l'ultimo spazio entro il limite della riga
                int spaceIndex = text.lastIndexOf(' ', end);
                if (spaceIndex > start) {
                    wrappedText.append(text, start, spaceIndex);
                    wrappedText.append("\n ");
                    if (Tabulation && !text.substring(start, spaceIndex).trim().startsWith("\t-")) {
                        wrappedText.append("\t  ");
                    }
                    start = spaceIndex + 1;
                } else {
                    // Nessuno spazio trovato, spezza la parola
                    wrappedText.append(text, start, end);
                    wrappedText.append("\n ");
                    if (Tabulation && !text.substring(start, end).trim().startsWith("\t-")) {
                        wrappedText.append("\t  ");
                    }
                    start = end;
                }
                end = Math.min(start + lineWidth, nextNewLine);
            }
            start = nextNewLine + 1;
        }
        return wrappedText.toString();
    }

    /**
     * Imposta il valore della variabile Tabulation che determina se il testo richiede di essere indentato.
     *
     * @param value il valore da impostare
     */
    public static void setTabulation(boolean value) {
        Tabulation = value;
    }
}

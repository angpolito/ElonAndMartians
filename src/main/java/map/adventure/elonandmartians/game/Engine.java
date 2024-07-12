/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.game;

import map.adventure.elonandmartians.parser.Parser;
import map.adventure.elonandmartians.parser.ParserOutput;
import map.adventure.elonandmartians.type.CommandType;
import map.adventure.elonandmartians.type.Room;
import map.adventure.elonandmartians.swing.App;
import map.adventure.elonandmartians.database.Database;

import javax.swing.JTextArea;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Set;
import java.util.Scanner;

/**
 * Classe Engine gestisce l'input ricevuto dalla classe App.
 * Gestisce il flusso di gioco e le interazioni tra l'utente e il gioco.
 */
public class Engine implements Serializable {
    private static GameDescription game = null;
    private static Parser parser;
    private static ParserOutput p;
    private static JTextArea outputTextArea = null;
    private int alertGravity = 0;
    private int alertTelescope = 0;
    private String[] msgAlert = {
            "La gravita' di Marte ti provoca giramenti di testa, potrebbe anche essere colpa del Mezcal by Tesla"
                    + " che hai sorseggiato prima del viaggio, nel dubbio, aspetterei a procedere",
            "Meglio fermarsi e riflettere per il momento, la gravita' di Marte ti provoca una forte nausea.",
            "Non sei per nulla abituato a questa gravita', ad ogni tentativo di mettere un passo i conati si fanno"
                    + " piu' forti e frequenti."
    };

    /**
     * Costruttore della classe Engine.
     * Inizializza il gioco e il parser.
     *
     * @param newGame la partita da inizializzare
     * @param outputTextA l'area di testo dove visualizzare l'output del gioco
     */
    public Engine(final GameDescription newGame, final JTextArea outputTextA) {
        this.game = newGame;
        this.outputTextArea = outputTextA;
        try {
            this.game.init();
        } catch (Exception ex) {
            appendToOutput(ex.toString());
        }
        try {
            Set<String> stopwords = Utils.loadFileListInSet(new File("./resources/stopwords"));
            parser = new Parser(stopwords);
        } catch (IOException ex) {
            appendToOutput(ex.toString());
        }
    }

    /**
     * Metodo eseguito per avviare il gioco.
     * Visualizza il messaggio di benvenuto e gestisce l'input dell'utente.
     */
    public void execute() {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        appendToOutput(game.getAsciiArt());
        appendToOutput(game.getWelcomeMsg());
        Utils.setTabulation(true);
        appendToOutput("\t- Smith (Pilota): \"Dannazione Elon! Un guasto al motore non identificato... "
                + "Presto salteremo in aria! Elabora un piano! Fa in fretta!\"\n");

        String command = scanner.nextLine();
        p = parser.parse(command, game.getCommands(), game.getCurrentRoom().getObjects(),
                game.getInventory());
        game.nextMove(p, new PrintStream(new OutputStream() {
            @Override
            public void write(final int b) {
            }
        }));

        processInput(command);
    }

    /**
     * Riceve l'input dalla classe App, lo controlla e processa.
     *
     * @param command il comando da processare
     */
    public void processInput(final String command) {
        Room prisons = game.getRooms().get(Utils.findRoomIndex(game, 1));
        Room astro = game.getRooms().get(Utils.findRoomIndex(game, 3));
        if (command.equalsIgnoreCase("help")) {
            Utils.setTabulation(true);
            game.showHelp();
        } else if (prisons.getObjects().get(Utils.findObjectIndex(prisons, 9)).isStateOn()) {
            switch (command.toLowerCase()) {
                case "3":
                case "terra":
                    p.getObject().setStateOn(false);
                    appendToOutput("\nIl pulsante emette un leggero ronzio e l'intera stanza si illumina di una"
                            + " luce soffusa, segno che il campo gravitazionale sta cambiando.\n");
                    break;
                case "4":
                case "marte":
                    appendToOutput("\nSei gia' su Marte!!\n");
                    p.getObject().setUsed(false);
                    p.getObject().setStateOn(false);
                    break;
                case "9":
                case "deimos":
                    appendToOutput("Stai sperimentando la quasi assenza di gravita', ad un tratto decidi di provare"
                            + " a saltare, non avresti potuto fare scelta peggiore: ci vorranno decine di minuti per"
                            + " rimettere i piedi per terra, mentre ti dimeni per aria come un pesce fuor d'acqua un"
                            + " alieno ti nota e mostra compassione per te... uccidendoti.");
                    App.getInstance().stopMusic();
                    break;
                default:
                    Engine.appendToOutput("Che stupido... hai sprecato un'ottima chance allertando "
                            + "le guardie.\nMeriti di morire per mano di Zylok e dei suoi. Addio!");
                    App.getInstance().stopMusic();
                    break;
            }
        } else if (astro.getObjects().get(Utils.findObjectIndex(astro, 10)).isStateOn()) {
            command.toLowerCase();
            switch (command) {
                case "aiuto":
                case "sos":
                case "soccorsi":
                case "soccorso":
                    appendToOutput("Per un pelo... La tua richiesta di aiuto e' stata inviata al pianeta Terra. "
                            + "Spera che qualcuno sia sveglio a quest'ora.\n");
                    p.getObject().setStateOn(false);
                    break;
                default:
                    if (alertTelescope < 3) {
                        appendToOutput("\nPensa a qualcosa di breve ed efficace\n");
                        alertTelescope++;
                    } else {
                        appendToOutput("\nChe stupido... hai sprecato un'ottima chance allertando \n"
                                + "le guardie.\nMeriti di morire per mano di Zylok e dei suoi. Addio!");
                        App.getInstance().stopMusic();
                    }
                    break;
            }
        } else {
            p = parser.parse(command, game.getCommands(), game.getCurrentRoom().getObjects(), game.getInventory());
            if (p == null || p.getCommand() == null) {
                appendToOutput("Non credo di aver capito...");
            } else if (p.getCommand().getType() == CommandType.NORD
                    && !prisons.getObjects().get(Utils.findObjectIndex(prisons, 9)).isStateOn()
                    && game.getCurrentRoom().getId() == 1 && !prisons.getObjects().get(Utils.findObjectIndex(prisons,
                    9)).isUsed()) {
                if (alertGravity < 3) {
                    appendToOutput(msgAlert[alertGravity]);
                    game.setCurrentRoom(prisons);
                } else {
                    appendToOutput("Stremato e in preda ai conati raggiungi la soglia della porta che ti "
                            + " separa dalla prossima stanza.\nTuttavia, gli sgradevoli suoni che emetti allertano "
                            + " gli alieni, che non sono affatto tolleranti nei confronti dei fuggitivi.\n"
                            + "Vieni ucciso senza alcuna pieta'.");
                    App.getInstance().stopMusic();
                }
                alertGravity++;
            } else if (p.getCommand() != null && p.getCommand().getType() == CommandType.END) {
                appendToOutput("Hai rinunciato troppo presto. Il tuo equipaggio merita un uomo piu' valoroso di "
                            + "te... addio!");
                App.getInstance().stopMusic();
            } else {
                game.nextMove(p, new PrintStream(new OutputStream() {
                    @Override
                    public void write(final int b) {
                    }
                }));
                if (game.getCurrentRoom() == null) {
                    appendToOutput(ElonAndMartians.getInstance().getEnding());
                    App.getInstance().stopMusic();
                    if(game.getCharacterLook()) {
                        try {
                            Database.createTable();
                            Database.updateDatabase();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    /**
     * Restituisce l'istanza corrente del gioco.
     *
     * @return l'istanza corrente del gioco
     */
    public static GameDescription getGame() {
        return game;
    }

    /**
     * Imposta una nuova istanza del gioco.
     *
     * @param newGame il nuovo gioco da impostare
     */
    public void setGame(final GameDescription newGame) {
        this.game = newGame;
    }

    /**
     * Restituisce il parser corrente.
     *
     * @return il parser corrente
     */
    public static Parser getParser() {
        return parser;
    }

    /**
     * Imposta un nuovo parser.
     *
     * @param newParser il nuovo parser da impostare
     */
    public void setParser(final Parser newParser) {
        this.parser = newParser;
    }

    /**
     * Scrive testo nelll'area di output del gioco.
     *
     * @param text il testo da aggiungere
     */
    public static void appendToOutput(final String text) {
        outputTextArea.append(Utils.wrapText(text) + "\n ");
        Utils.setTabulation(false);
    }
}

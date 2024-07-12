/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.observer;

import map.adventure.elonandmartians.game.GameDescription;
import map.adventure.elonandmartians.game.Utils;
import map.adventure.elonandmartians.parser.ParserOutput;
import map.adventure.elonandmartians.rest.ChatGPTIntegration;
import map.adventure.elonandmartians.type.CommandType;

import java.io.Serializable;

/**
 * La classe TalkObserver gestisce il comando TALK per i dialoghi nel gioco.
 */
public class TalkObserver implements GameObserver, Serializable {
    /**
     * Aggiorna l'osservatore con lo stato del gioco e il comando dell'utente.
     *
     * @param description la descrizione aggiornata del gioco
     * @param parserOutput l'output del parser che rappresenta il comando dell'utente
     * @return il messaggio risultato dell'azione
     */
    @Override
    public String update(final GameDescription description, final ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.TALK_TO) {
            if (parserOutput.getObject() != null) {
                Utils.setTabulation(true);
                if (description.getCurrentRoom().getId() == 0) {
                    if (parserOutput.getObject().getId() == 21) {
                        msg.append("\t- Smith (Pilota): " + parserOutput.getObject().getSentences(0));
                    } else if (parserOutput.getObject().getId() == 22) {
                        msg.append("\t- Miller (Ing. di bordo): " + parserOutput.getObject().getSentences(0));
                    }
                } else if (description.getCharacterLook()) {
                    if (parserOutput.getObject().getId() == 20) {
                        try {
                            msg.append("\t- Kif Kroker: " + ChatGPTIntegration.chatGPTApi("interpreti Kif Kroker,"
                                            + " un alieno marziano di pattuglia in una base spaziale marziana. Rispondi in italiano"
                                            + " e talvolta includi una battuta ironica. Non dilungarti troppo nelle risposte e non"
                                            + " utilizzare emoji.",
                                    "Sgrida (eventualmente minaccia) un essere umano fuggito dalle prigioni della base spaziale"));
                        } catch (Exception e) {
                            msg.append("\t- Kif Kroker: Hey fuggitivo, torna subito nella tua cella o saro' costretto ad abbatterti!");
                        }

                    }
                    if (!description.getCrewIsFree()) {
                        if (parserOutput.getObject().getId() == 21) {
                            msg.append("\t- Smith (Pilota): " + parserOutput.getObject().getSentences(1));
                        } else if (parserOutput.getObject().getId() == 22) {
                            msg.append("\t- Miller (Ing. di bordo): " + parserOutput.getObject().getSentences(1));
                        }
                    } else {
                        if (parserOutput.getObject().getId() == 21) {
                            msg.append("\t- Smith (Pilota): " + parserOutput.getObject().getSentences(2));
                        } else if (parserOutput.getObject().getId() == 22) {
                            msg.append("\t- Miller (Ing. di bordo): " + parserOutput.getObject().getSentences(2));
                        }
                    }
                } else {
                    if (parserOutput.getObject().getId() == 21) {
                        msg.append("\t- Smith (Pilota): " + parserOutput.getObject().getSentences(3));
                    } else if (parserOutput.getObject().getId() == 22) {
                        msg.append("\t- Miller (Ing. di bordo): " + parserOutput.getObject().getSentences(3));
                    } else if (parserOutput.getObject().getId() == 20) {
                        try {
                            msg.append("\t- Kif Kroker: " + ChatGPTIntegration.chatGPTApi("interpreti Kif Kroker,"
                                    + " un amichevole alieno marziano di pattuglia in una base spaziale marziana."
                                    + " Rispondi in italiano e talvolta includi una battuta ironica. Non dilungarti troppo"
                                    + " nelle risposte e non utilizzare emoji.", "Saluta amichevolmente un altro marziano."));
                        } catch (Exception e) {
                            msg.append("\t- Kif Kroker: Saluti amico marziano!");
                        }

                    }
                }


            } else {
                msg.append("Capisco che tu ti senta solo, data la mancanza di altre forme di vita intelligenti"
                        + " quanto te, ma e' un po' presto per iniziare a parlare da solo");
            }
        }
        return msg.toString();
    }
}

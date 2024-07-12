/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.observer;

import map.adventure.elonandmartians.game.GameDescription;
import map.adventure.elonandmartians.parser.ParserOutput;
import map.adventure.elonandmartians.type.CommandType;

import java.io.Serializable;

/**
 * La classe PushObserver implementa GameObserver e gestisce la pressione dei bottoni.
 */
public class PushObserver implements GameObserver, Serializable {

    /**
     *
     * @param description la descrizione aggiornata del gioco
     * @param parserOutput l'output del parser che rappresenta il comando dell'utente
     * @return il messaggio risultato dell'azione
     */
    @Override
    public String update(final GameDescription description, final ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.PUSH) {
            //ricerca oggetti pushabili
            if (parserOutput.getObject() != null && parserOutput.getObject().isPushable()) {
                if (parserOutput.getObject().isUsed()) {
                    msg.append("Sei gia' passato alla gravita' terrestre.\n");
                } else {
                    msg.append("Hai premuto: " + parserOutput.getObject().getName() + "\n");
                    if (parserOutput.getObject().getId() == 9) {
                        msg.append("Ora puoi selezionare il pianeta del quale emulare la gravita'.\n"
                                + "1 : Mercurio\n2 : Venere\n3 : Terra\n4 : Marte (Gravita' attuale)\n"
                                + "5 : Giove\n6 : Saturno\n7 : Urano\n8 : Nettuno\n9 : Deimos (Luna di Marte)\n");
                        parserOutput.getObject().setStateOn(true);
                        parserOutput.getObject().setUsed(true);
                    }
                }
            } else {
                msg.append("Non ci sono oggetti che puoi premere qui.\n");
            }
        }
        return msg.toString();
    }
}

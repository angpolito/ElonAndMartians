/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.observer;

import map.adventure.elonandmartians.game.GameDescription;
import map.adventure.elonandmartians.game.ElonAndMartians;
import map.adventure.elonandmartians.parser.ParserOutput;
import map.adventure.elonandmartians.type.CommandType;
import map.adventure.elonandmartians.type.Room;

import java.io.Serializable;

/**
 * La classe PickUpObserver implementa GameObserver e gestisce la raccolta degli oggetti.
 */
public class PickUpObserver implements GameObserver, Serializable {

    /**
     *
     * @param description rappresenta la descrizione aggiornata del gioco
     * @param parserOutput e' l'output del parser che rappresenta il comando dell'utente
     * @return il messaggio risultato dell'azione
     */
    @Override
    public String update(final GameDescription description, final ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.PICK_UP) {
            if (parserOutput.getObject() != null) {
                if (parserOutput.getObject().isPickupable()) {
                    description.getInventory().add(parserOutput.getObject());
                    description.getCurrentRoom().getObjects().remove(parserOutput.getObject());

                    description.getCurrentRoom().setLook(ElonAndMartians.getInstance().createLook(description.getCurrentRoom()));

                    msg.append("Hai raccolto: " + parserOutput.getObject().getName() + "\n"
                            + parserOutput.getObject().getDescription() + "\n");

                } else {
                    msg.append("Non puoi raccogliere questo oggetto.\n");
                }
            } else {
                msg.append("Non c'e' niente da raccogliere qui.\n");
            }
        }
        return msg.toString();
    }
}

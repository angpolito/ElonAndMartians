/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.observer;

import map.adventure.elonandmartians.game.GameDescription;
import map.adventure.elonandmartians.game.Utils;
import map.adventure.elonandmartians.parser.ParserOutput;
import map.adventure.elonandmartians.type.CommandType;
import map.adventure.elonandmartians.type.Room;

import java.io.Serializable;

/**
 * La classe gestisce l'aggiornamento relativo alla descrizione degli oggetti guardati nel gioco e
 * implementa l'interfaccia GameObserver per monitorare le modifiche.
 */
public class LookAtObserver implements GameObserver, Serializable {
    /**
     * Genera un messaggio di aggiornamento sull'oggetto osservato, stampandone la descrizione.
     * @param description la descrizione aggiornata del gioco
     * @param parserOutput l'output del parser che rappresenta il comando dell'utente
     * @return il messaggio risultato dell'azione
     */
    @Override
    public String update(final GameDescription description, final ParserOutput parserOutput) {
        boolean cellLooked = false;
        StringBuilder msg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.LOOK_AT) {
            if (parserOutput.getObject() != null) {
                if (parserOutput.getInvObject() != null) {
                    msg.append(parserOutput.getInvObject2().getDescription());
                } else {
                    msg.append(parserOutput.getObject().getDescription());
                    System.out.println();
                    if (parserOutput.getObject().getId() == 23 && !cellLooked) {
                        Room prisons = description.getRooms().get(Utils.findRoomIndex(description, 1));
                        prisons.getObject(23).setName("serratura");
                        prisons.getObject(23).setDescription("non prevede una chiave, pare serva una tessera.");
                        prisons.getObject(23).setAlias(new String[]{"lucchetto"});
                        cellLooked = true;
                    }

                }
            } else if (description.getCurrentRoom().getLook() != null && !description.isRoomHidden()) {
                description.getCurrentRoom().printLook();
            } else {
                msg.append("Non c'e' niente di interessante qui.\n");
            }
        }
        return msg.toString();
    }
}

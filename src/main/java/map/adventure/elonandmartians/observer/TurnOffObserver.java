/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.observer;

import map.adventure.elonandmartians.game.Engine;
import map.adventure.elonandmartians.game.GameDescription;
import map.adventure.elonandmartians.game.Utils;
import map.adventure.elonandmartians.parser.ParserOutput;
import map.adventure.elonandmartians.type.CommandType;
import map.adventure.elonandmartians.type.Room;


import java.io.Serializable;

/**
 * La classe TurnOffObserver implementa GameObserver e gestisce lo 'spegnimento' di un oggetto.
 */
public class TurnOffObserver implements GameObserver, Serializable {

    /**
     * Aggiorna l'osservatore con lo stato del gioco e il comando dell'utente.
     *
     * @param description rappresenta la descrizione aggiornata del gioco
     * @param parserOutput e' l'output del parser che rappresenta il comando dell'utente
     * @return il messaggio risultato dell'azione
     */
    @Override
    public String update(final GameDescription description, final ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.TURN_OFF) {
            if (parserOutput.getObject() != null && parserOutput.getObject().isSwitchable()) {
                if (parserOutput.getObject().getId() == 7) {
                    parserOutput.getObject().setStateOn(false);
                    Engine.appendToOutput("Hai spento: " + parserOutput.getObject().getName());
                    Engine.appendToOutput("\nOttimo lavoro! Il motore e' stato spento e Smith"
                            + " e' riuscito ad innescare "
                            + "un atterraggio d'emergenza.\nSmith: \"Sara' necessario revisionare questi shuttle Elon, "
                            + "ma ora ringraziamo di essere atterrati sani e salvi\"\n");
                    Engine.appendToOutput(("Appena mettete piede sulla superficie marziana...BOO! Ecco il capo dei "
                            + "marziani Zylok con il suo esercito.\nZylok: Sbatteteli in prigione, SUBITO!"));

                    description.setCurrentRoom(description.getRooms().get(Utils.findRoomIndex(description, 8)));
                    moveCrewToCell(description);


                    description.getInventory().get(Utils.findInventoryIndex(description, 15)).setVisible(true);
                }
            } else if (parserOutput.getInvObject() != null) {
                if (parserOutput.getInvObject().isSwitchable()) {
                    parserOutput.getInvObject().setStateOn(false);
                    msg.append("Hai spento: " + parserOutput.getInvObject().getName() + "\n");
                } else {
                    msg.append("Non puoi spegnere: " + parserOutput.getInvObject().getName() + "\n");
                }
            } else {
                msg.append("Non c'e' niente da spegnere qui.\n");
            }
        }
        return msg.toString();
    }

    /**
     * Sposta l'equipaggio dello shuttle nella cella delle prigioni
     *
     * @param description rappresenta la descrizione aggiornata del gioco
     */
    private void moveCrewToCell(final GameDescription description) {
        Room shuttleRoom = description.getRooms().get(Utils.findRoomIndex(description, 0));
        Room prisonsRoom = description.getRooms().get(Utils.findRoomIndex(description, 1));
        shuttleRoom.getObject(21).setVisible(false);
        shuttleRoom.getObject(22).setVisible(false);
        prisonsRoom.getObjects().add(shuttleRoom.getObjects().get(Utils.findObjectIndex(shuttleRoom, 21)));
        shuttleRoom.getObjects().remove(shuttleRoom.getObjects().get(Utils.findObjectIndex(shuttleRoom, 21)));
        prisonsRoom.getObjects().add(shuttleRoom.getObjects().get(Utils.findObjectIndex(shuttleRoom, 22)));
        shuttleRoom.getObjects().remove(shuttleRoom.getObjects().get(Utils.findObjectIndex(shuttleRoom, 22)));
    }

}

/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.observer;

import map.adventure.elonandmartians.game.Engine;
import map.adventure.elonandmartians.game.GameDescription;
import map.adventure.elonandmartians.game.Utils;
import map.adventure.elonandmartians.parser.ParserOutput;
import map.adventure.elonandmartians.type.AdvObject;
import map.adventure.elonandmartians.type.CommandType;
import map.adventure.elonandmartians.type.Room;

import java.io.Serializable;

/**
 * La classe ShootObserver gestisce il comando di tipo SHOOT.
 */
public class ShootObserver implements GameObserver, Serializable {
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
        if (parserOutput.getCommand().getType() == CommandType.SHOOT) {
            if (parserOutput.getObject() != null) {
                if (parserOutput.getObject().getId() == 20) {
                    if (Utils.findInventoryIndex(description, 5) != -1) {
                        killAlien(description);
                    } else {
                        Engine.appendToOutput("Non ti aspetterai che metta le mani addosso a quel marziano,"
                                + " sembra cosi' viscido...");
                    }
                } else {
                    Engine.appendToOutput("Sono fermamente contro la violenza gratuita verso cose o persone");
                }
            } else {
                Engine.appendToOutput("Vuoi per caso sparare in aria? Non siamo mica in Texas");
            }
        }
        return msg.toString();
    }

    /**
     * Gestisce l'uccisione dell'alieno
     *
     * @param description la descrizione aggiornata del gioco
     */
    private static void killAlien(final GameDescription description) {
        Engine.appendToOutput("Centro! l'alieno si accascia morente, sembra che lascia cadere qualcosa.");
        Engine.appendToOutput("Hai raccolto: \n");
        Room currentRoom = description.getCurrentRoom();
        AdvObject card = currentRoom.getObjects().get(Utils.findObjectIndex(currentRoom, 18));
        description.getInventory().add(card);
        description.getInventory().get(Utils.findInventoryIndex(description, 18)).setVisible(true);
        Engine.appendToOutput("\t- " + card.getName() + "\n");
        currentRoom.getObjects().remove(currentRoom.getObjects().get(Utils.findObjectIndex(currentRoom, 18)));
        currentRoom.getObjects().get(Utils.findObjectIndex(currentRoom, 20)).setVisible(false);
    }
}



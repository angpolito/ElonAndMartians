/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.observer;

import map.adventure.elonandmartians.game.GameDescription;
import map.adventure.elonandmartians.game.Utils;
import map.adventure.elonandmartians.parser.ParserOutput;
import map.adventure.elonandmartians.type.CommandType;
import map.adventure.elonandmartians.type.AdvObject;

import java.io.Serializable;

/**
 * La classe gestisce l'aggiornamento relativo all'inventario del gioco e
 * implementa l'interfaccia GameObserver per monitorare le modifiche all'inventario.
 */
public class InventoryObserver implements GameObserver, Serializable {

    /**
     * Genera un messaggio di aggiornamento sull'inventario, stampando l'elenco dettagliato degli
     * oggetti presenti al suo interno.
     * @param description la descrizione aggiornata del gioco
     * @param parserOutput l'output del parser che rappresenta il comando dell'utente
     * @return il messaggio risultato dell'azione
     */
    @Override
    public String update(final GameDescription description, final ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.INVENTORY) {
            if (description.getInventory().isEmpty() || description.isInventoryHidden()) {
                msg.append("Il tuo inventario e' vuoto!\n");
            } else {
                Utils.setTabulation(true);
                int visibleItemCount = (int) description.getInventory().stream().filter(AdvObject::isVisible).count();
                if (visibleItemCount == 1) {
                    msg.append("Nel tuo inventario c'e':\n");
                } else {
                    msg.append("Nel tuo inventario ci sono:\n");
                }
                for (int i = 0; i < description.getInventory().size(); i++) {
                    if (description.getInventory().get(i).isVisible()) {
                       msg.append("\t- ").append(description.getInventory().get(i).getName()).append(":"
                    + " ").append(description.getInventory().get(i).getDescription()).append("\n");
                    }
                }
            }
        }
        return msg.toString();
    }
}

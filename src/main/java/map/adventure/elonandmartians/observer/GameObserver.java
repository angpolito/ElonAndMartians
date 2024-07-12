/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.observer;

import map.adventure.elonandmartians.game.GameDescription;
import map.adventure.elonandmartians.parser.ParserOutput;

/**
 * L'interfaccia GameObserver rappresenta un osservatore che reagisce ai cambiamenti nel gioco.
 * Implementa il metodo update per aggiornare lo stato in base alla descrizione del gioco e all'output del parser.
 */
public interface GameObserver {

    /**
     * Aggiorna l'osservatore con lo stato del gioco e il comando dell'utente.
     *
     * @param description la descrizione aggiornata del gioco
     * @param parserOutput l'output del parser che rappresenta il comando dell'utente
     * @return una stringa che rappresenta la risposta dell'osservatore al cambiamento
     */
    String update(GameDescription description, ParserOutput parserOutput);

}

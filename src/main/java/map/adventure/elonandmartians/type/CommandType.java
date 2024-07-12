/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.type;

/**
 * Enum che rappresenta i diversi tipi di comandi che possono essere utilizzati nel gioco.
 * Ogni costante rappresenta un comando specifico che il giocatore può eseguire.
 */
public enum CommandType {

    /**
     * END command: consente di abbandonare il gioco.
     */
    END,

    /**
     * INVENTORY command: consente di visualizzare l'inventario.
     */
    INVENTORY,

    /**
     * NORD command: consente di spostarsi a nord.
     */
    NORD,

    /**
     * SOUTH command: consente di spostarsi a sud.
     */
    SOUTH,

    /**
     * EAST command: consente di spostarsi ad est.
     */
    EAST,

    /**
     * WEST command: consente di spostarsi ad ovest.
     */
    WEST,

    /**
     * OPEN command: consente di aprire un oggetto contenitore.
     */
    OPEN,

    /**
     * PUSH command: consente di premere un oggetto.
     */
    PUSH,

    /**
     * PICK_UP command: consente raccogliere un oggetto.
     */
    PICK_UP,

    /**
     * TALK_TO command: consente di parlare con un personaggio secondario.
     */
    TALK_TO,

    /**
     * USE command: consente di usare un oggetto.
     */
    USE,

    /**
     * LOOK_AT command: consente di guardare un oggetto o una stanza.
     */
    LOOK_AT,

    /**
     * TURN_OFF command: consente di spegnere un oggetto.
     */
    TURN_OFF,

    /**
     * SHOOT command: consente di sparare un nemico.
     */
    SHOOT
}

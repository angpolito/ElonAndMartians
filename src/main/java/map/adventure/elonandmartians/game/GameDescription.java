/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.game;

import map.adventure.elonandmartians.parser.ParserOutput;
import map.adventure.elonandmartians.type.AdvObject;
import map.adventure.elonandmartians.type.Command;
import map.adventure.elonandmartians.type.Room;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

    /**
     * La classe definisce la struttura e il comportamento del gioco.
     * Contiene liste di stanze, comandi e oggetti dell'inventario,
     * nonché informazioni sullo stato del personaggio e dell'equipaggio.
     * È una classe astratta che deve essere estesa per implementare i metodi astratti.
     */
    public abstract class GameDescription implements Serializable {

    private final List<Room> rooms = new ArrayList<>();

    private final List<Command> commands = new ArrayList<>();

    private List<AdvObject> inventory = new ArrayList<>();

    private Room currentRoom;

    private boolean characterLook = true; //true umano, false alieno

    private boolean crewIsFree = false;

        /**
         * Restituisce la lista delle stanze del gioco.
         *
         * @return la lista delle stanze
         */
        public List<Room> getRooms() {
        return rooms;
    }

        /**
         * Restituisce la lista dei comandi del gioco.
         *
         * @return la lista dei comandi
         * */
        public List<Command> getCommands() {
        return commands;
    }

        /**
         * Restituisce la stanza corrente.
         *
         * @return la stanza corrente
         */
    public Room getCurrentRoom() {
        return currentRoom;
    }

        /**
         * Imposta la stanza corrente.
         *
         * @param newRoom la nuova stanza corrente
         */
    public void setCurrentRoom(final Room newRoom) {
        this.currentRoom = newRoom;
    }

        /**
         * Restituisce la lista degli oggetti dell'inventario.
         *
         * @return la lista degli oggetti dell'inventario
         */
    public List<AdvObject> getInventory() {
        return inventory;
    }

        /**
         * Restituisce l'aspetto del personaggio.
         *
         * @return true se il personaggio è umano, false se è alieno
         */
    public boolean getCharacterLook() {
        return characterLook;
    }

        /**
         * Imposta l'aspetto del personaggio.
         *
         * @param state true per impostare il personaggio come umano, false come alieno
         */
    public void setCharacterLook(final boolean state) {
        this.characterLook = state;
    }

        /**
         * Restituisce lo stato di libertà dell'equipaggio.
         *
         * @return true se l'equipaggio è libero, false altrimenti
         */
    public boolean getCrewIsFree() {
        return crewIsFree;
    }

        /**
         * Imposta lo stato di libertà dell'equipaggio.
         *
         * @param state true per impostare l'equipaggio come libero, false altrimenti
         */
    public void setCrewIsFree(final boolean state) {
        this.crewIsFree = state;
    }

        /**
         * Verifica se l'inventario non presenta oggetti visibili.
         *
         * @return true se non ci sono oggetti visibili, false altrimenti
         */
    public boolean isInventoryHidden() {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).isVisible()) {
                return false;
            }
        }
        return true;
    }

        /**
         * Controlla la visibilità degli oggetti nella stanza corrente.
         *
         * @return true se tutti gli oggetti nella stanza sono nascosti, false altrimenti
         */
    public boolean isRoomHidden() {
        for (int i = 0; i < currentRoom.getObjects().size(); i++) {
            if (currentRoom.getObjects().get(i).isVisible()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Esegue operazioni di inizializzazione e
     * puo' essere implementato dalle classi che estendono GameDescription.
     **/
    public abstract void init() throws Exception;

    /**
     * Gestisce l'inserimento della mossa successiva.
     *
     * @param p rappresenta un'istanza di ParserOutput
     * @param out rappresenta un'istanza di PrintStream
     */
    public abstract void nextMove(ParserOutput p, PrintStream out);

    /**
     * Inizializza il messaggio di benvenuto.
     * @return String
     **/
    public abstract String getWelcomeMsg();

    /**
     * Inizializza la schermata iniziale.
     * @return String
     */
    public abstract String getAsciiArt();

    /**
     * Il metodo stampa un elenco di tutti i comandi possibili.
     **/
    public abstract void showHelp();
}

/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.type;

import map.adventure.elonandmartians.game.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta una stanza all'interno del gioco di avventura.
 * Ogni stanza ha un identificatore univoco, un nome, una descrizione e puo' contenere oggetti.
 * Le stanze possono essere collegate tra loro in quattro direzioni: nord, sud, est e ovest.
 */
public class Room implements Serializable {

    private final int id;

    private String name;

    private String description;

    private List<String> look;

    private boolean visible = true;

    private Room south = null;

    private Room north = null;

    private Room east = null;

    private Room west = null;

    private final List<AdvObject> objects = new ArrayList<>();

    /**
    * Costruisce una stanza con l'identificatore specificato.
    *
    * @param newId l'identificatore univoco della stanza
    **/
    public Room(final int newId) {
        this.id = newId;
    }

    /**
     * Costruisce una stanza con l'identificatore, il nome e la descrizione specificati.
     *
     * @param newId l'identificatore univoco della stanza
     * @param newName il nome della stanza
     * @param newDescription la descrizione della stanza
     */
    public Room(final int newId, final String newName, final String newDescription) {
        this.id = newId;
        this.name = newName;
        this.description = newDescription;
    }

    /**
     * @return il nome della stanza
     */
    public String getName() {
        return name;
    }

    /**
     * Imposta il nome della stanza.
     *
     * @param newName il nuovo nome della stanza
     */
    public void setName(final String newName) {
        this.name = newName;
    }

    /**
     * @return la descrizione della stanza
     */
    public String getDescription() {
        return description;
    }

    /**
     * Imposta la descrizione della stanza.
     *
     * @param newDescription la nuova descrizione della stanza
     */
    public void setDescription(final String newDescription) {
        this.description = newDescription;
    }

    /**
     * @return true se la stanza e' visibile, false altrimenti
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Imposta la visibilita' della stanza.
     *
     * @param newVisible la nuova visibilita' della stanza
     */
    public void setVisible(final boolean newVisible) {
        this.visible = newVisible;
    }

    /**
     * @return la stanza a sud
     */
    public Room getSouth() {
        return south;
    }

    /**
     * Imposta la stanza a sud.
     *
     * @param newSouth la nuova stanza a nord
     */
    public void setSouth(final Room newSouth) {
        this.south = newSouth;
    }

    /**
     * @return la stanza a nord
     */
    public Room getNorth() {
        return north;
    }

    /**
     * Imposta la stanza a nord.
     *
     * @param newNorth la nuova stanza a nord
     */
    public void setNorth(final Room newNorth) {
        this.north = newNorth;
    }

    /**
     * @return la stanza a est
     */
    public Room getEast() {
        return east;
    }

    /**
     * Imposta la stanza a est.
     *
     * @param newEast la nuova stanza a nord
     */
    public void setEast(final Room newEast) {
        this.east = newEast;
    }

    /**
     * @return la stanza a ovest
     */
    public Room getWest() {
        return west;
    }

    /**
     * Imposta la stanza a ovest.
     *
     * @param newWest la nuova stanza a nord
     */
    public void setWest(final Room newWest) {
        this.west = newWest;
    }

    /**
     * @return la lista degli oggetti nella stanza
     */
    public List<AdvObject> getObjects() {
        return objects;
    }

    /**
     * @return l'identificatore univoco della stanza
     */
    public int getId() {
        return id;
    }

    /**
     * Calcola il codice hash della stanza.
     *
     * @return il codice hash della stanza
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.id;
        return hash;
    }

    /**
     * Verifica se la stanza e' uguale a un altro oggetto.
     *
     * @param obj l'oggetto da confrontare
     * @return true se la stanza e' uguale all'oggetto specificato, altrimenti false
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Room other = (Room) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    /**
     * @return la descrizione visiva della stanza
     */
    public List<String> getLook() {
        return look;
    }

    /**
     * Stampa la lista degli oggetti presenti nella stanza.
     */
    public void printLook() {
        System.out.println("Vedo:");
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).isVisible()) {
                System.out.println("\t- " + Utils.getArticle(objects.get(i).getName()));
            }
        }
        System.out.println();
    }


    /**
     * Imposta una descrizione visiva della stanza.
     *
     * @param newLook la nuova descrizione visiva della stanza
     */
    public void setLook(final List<String> newLook) {
        this.look = newLook;
    }

    /**
     * Restituisce un oggetto nella stanza basato sul suo ID.
     *
     * @param newId l'ID dell'oggetto da cercare
     * @return l'oggetto con l'ID specificato, oppure null se non trovato
     */
    public AdvObject getObject(final int newId) {
        for (AdvObject o : objects) {
            if (o.getId() == newId) {
                return o;
            }
        }
        return null;
    }
}

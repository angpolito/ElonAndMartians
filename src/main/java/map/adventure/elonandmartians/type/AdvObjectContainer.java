/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Classe che rappresenta un contenitore di oggetti nel gioco.
 * Estende {@link AdvObject} e implementa {@link Serializable} per permettere la serializzazione.
 * Un contenitore può contenere una lista di oggetti {@link AdvObject}.
 */
public class AdvObjectContainer extends AdvObject implements Serializable {

    private List<AdvObject> list = new ArrayList<>();

    /**
     * Costruttore che inizializza un contenitore con un ID.
     *
     * @param id l'ID del contenitore
     */
    public AdvObjectContainer(final int id) {
        super(id);
    }

    /**
     * Costruttore che inizializza un contenitore con un ID e un nome.
     *
     * @param id l'ID del contenitore
     * @param name il nome del contenitore
     */
    public AdvObjectContainer(final int id, final String name) {
        super(id, name);
    }

    /**
     * Costruttore che inizializza un contenitore con un ID, un nome e una descrizione.
     *
     * @param id l'ID del contenitore
     * @param name il nome del contenitore
     * @param description la descrizione del contenitore
     */
    public AdvObjectContainer(final int id, final String name, final String description) {
        super(id, name, description);
    }

    /**
     * Costruttore che inizializza un contenitore con un ID, un nome, una descrizione e un insieme di alias.
     *
     * @param id l'ID del contenitore
     * @param name il nome del contenitore
     * @param description la descrizione del contenitore
     * @param alias l'insieme di alias del contenitore
     */
    public AdvObjectContainer(final int id, final String name, final String description, final Set<String> alias) {
        super(id, name, description, alias);
    }

    /**
     * Restituisce la lista degli oggetti contenuti nel contenitore.
     *
     * @return la lista degli oggetti
     */
    public List<AdvObject> getList() {
        return list;
    }

    /**
     * Imposta una nuova lista di oggetti nel contenitore.
     *
     * @param newList la nuova lista di oggetti
     */
    public void setList(final List<AdvObject> newList) {
        this.list = newList;
    }

    /**
     * Aggiunge un oggetto al contenitore.
     *
     * @param o l'oggetto da aggiungere
     */
    public void add(final AdvObject o) {
        list.add(o);
    }

    /**
     * Rimuove un oggetto dal contenitore.
     *
     * @param o l'oggetto da rimuovere
     */
    public void remove(final AdvObject o) {
        list.remove(o);
    }
}

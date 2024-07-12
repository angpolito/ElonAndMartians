/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * La classe AdvObject rappresenta un oggetto nel gioco.
 * Implementa {@link Serializable} per permettere la serializzazione degli oggetti.
 * Include proprietà come nome, descrizione, alias e vari stati booleani per determinare il comportamento dell'oggetto.
 */
 public class AdvObject implements Serializable {

    private final int id;

    private String name;

    private String description;

    private Set<String> alias;

    private boolean openable = false;

    private boolean pickupable = true;

    private boolean pushable = false;

    private boolean open = false;

    private boolean push = false;

    private boolean wearable = false;

    private boolean switchable = false;

    private boolean stateOn = false;

    private boolean visible = true;

    private boolean used = false;

    private String[] sentences;

    /**
     * Costruttore che inizializza un oggetto con un ID.
     *
     * @param newId l'ID dell'oggetto
     */
    public AdvObject(final int newId) {
        this.id = newId;
    }

    /**
     * Costruttore che inizializza un oggetto con un ID e un nome.
     *
     * @param newId l'ID dell'oggetto
     * @param newName il nome dell'oggetto
     */
    public AdvObject(final int newId, final String newName) {
        this.id = newId;
        this.name = newName;
    }

    /**
     * Costruttore che inizializza un oggetto con un ID, un nome e una descrizione.
     *
     * @param newId l'ID dell'oggetto
     * @param newName il nome dell'oggetto
     * @param newDescription la descrizione dell'oggetto
     */
    public AdvObject(final int newId, final String newName, final String newDescription) {
        this.id = newId;
        this.name = newName;
        this.description = newDescription;
    }

    /**
     * Costruttore che inizializza un oggetto con un ID, un nome, una descrizione e un insieme di alias.
     *
     * @param newId l'ID dell'oggetto
     * @param newName il nome dell'oggetto
     * @param newDescription la descrizione dell'oggetto
     * @param newAlias l'insieme di alias dell'oggetto
     */
    public AdvObject(final int newId, final String newName, final String newDescription, final Set<String> newAlias) {
        this.id = newId;
        this.name = newName;
        this.description = newDescription;
        this.alias = newAlias;
    }

    /**
     * Costruttore che inizializza un oggetto con un ID, un nome, una descrizione, un insieme di alias e frasi.
     *
     * @param newId l'ID dell'oggetto
     * @param newName il nome dell'oggetto
     * @param newDescription la descrizione dell'oggetto
     * @param newAlias l'insieme di alias dell'oggetto
     * @param newSentences l'array di frasi associate all'oggetto
     */
     public AdvObject(final int newId, final String newName, final String newDescription, final Set<String> newAlias, final String[] newSentences) {
         this.id = newId;
         this.name = newName;
         this.description = newDescription;
         this.alias = newAlias;
         this.sentences = newSentences;
     }

    /**
     * Restituisce il nome dell'oggetto.
     *
     * @return il nome dell'oggetto
     */
    public String getName() {
        return name;
    }

    /**
     * Imposta il nome dell'oggetto.
     *
     * @param newName il nuovo nome dell'oggetto
     */
    public void setName(final String newName) {
        this.name = newName;
    }

    /**
     * Restituisce la descrizione dell'oggetto.
     *
     * @return la descrizione dell'oggetto
     */
    public String getDescription() {
        return description;
    }

    /**
     * Imposta la descrizione dell'oggetto.
     *
     * @param newDescription la nuova descrizione dell'oggetto
     */
    public void setDescription(final String newDescription) {
        this.description = newDescription;
    }

    /**
     * Verifica se l'oggetto è apribile o meno.
     *
     * @return true se l'oggetto è apribile, false altrimenti
     */
    public boolean isOpenable() {
        return openable;
    }

    /**
     * Imposta se l'oggetto è apribile o meno.
     *
     * @param isOpenable true se l'oggetto deve essere apribile, false altrimenti
     */
    public void setOpenable(final boolean isOpenable) {
        this.openable = isOpenable;
    }

    /**
     * Verifica se l'oggetto è raccoglibile o meno.
     *
     * @return true se l'oggetto è raccoglibile, false altrimenti
     */
    public boolean isPickupable() {
        return pickupable;
    }

    /**
     * Imposta se l'oggetto è raccoglibile o meno.
     *
     * @param isPickupable  true se l'oggetto è raccoglibile, false altrimenti
     */
    public void setPickupable(final boolean isPickupable) {
        this.pickupable = isPickupable;
    }

    /**
     * Verifica se l'oggetto può essere schiacciato o meno.
     *
     * @return true se l'oggetto è raccoglibile, false altrimenti
     */
    public boolean isPushable() {
        return pushable;
    }

    /**
     * Imposta se l'oggetto è può essere schiacciato o meno.
     *
     * @param isPushable true se l'oggetto è raccoglibile, false altrimenti
     */
    public void setPushable(final boolean isPushable) {
        this.pushable = isPushable;
    }

    /**
     * Verifica se l'oggetto è stato aperto o meno.
     *
     * @return true se l'oggetto è aperto, false altrimenti
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Imposta se l'oggetto è stato aperto o meno.
     *
     * @param isOpen true se l'oggetto è aperto, false altrimenti
     */
    public void setOpen(final boolean isOpen) {
        this.open = isOpen;
    }

    /**
     * Verifica se l'oggetto è indossabile o meno.
     *
     * @return true se l'oggetto è indossabile, false altrimenti
     */
    public boolean isWearable() {
        return wearable;
    }

    /**
     * Imposta se l'oggetto è indossabile o meno.
     *
     * @param isWearable true se l'oggetto è indossabile, false altrimenti
     */
    public void setWearable(final boolean isWearable) {
        this.wearable = isWearable;
    }

    /**
     * Verifica se l'oggetto può essere acceso/spento o meno.
     *
     * @return true se l'oggetto può essere acceso/spento, false altrimenti
     */
    public boolean isSwitchable() {
        return switchable;
    }

    /**
     * Imposta se l'oggetto può essere acceso/spento o meno.
     *
     * @param isSwitchable true se l'oggetto può essere acceso/spento, false altrimenti
     */
    public void setSwitchable(final boolean isSwitchable) {
        this.switchable = isSwitchable;
    }

    /**
     * Verifica se l'oggetto è acceso o meno.
     *
     * @return true se l'oggetto è acceso, false altrimenti
     */
    public boolean isStateOn() {
        return stateOn;
    }

    /**
     * Imposta se l'oggetto è acceso o meno.
     *
     * @param isStateOn true se l'oggetto è acceso, false altrimenti
     */
    public void setStateOn(final boolean isStateOn) {
        this.stateOn = isStateOn;
    }

    /**
     * Verifica se l'oggetto è visibile o meno.
     *
     * @return true se l'oggetto è visibile, false altrimenti
     */
     public boolean isVisible() {
         return visible;
     }

    /**
     * Imposta se l'oggetto è visibile o meno.
     *
     * @param newVisible true se l'oggetto è visibile, false altrimenti
     */
     public void setVisible(final boolean newVisible) {
         this.visible = newVisible;
     }

     /**
     *
     * @return alias
     */
    public Set<String> getAlias() {
        return alias;
    }

    /**
     * Imposta un nuovo insieme di alias per l'oggetto.
     *
     * @param newAlias il nuovo insieme di alias
     */
    public void setAlias(final Set<String> newAlias) {
        this.alias = newAlias;
    }

    /**
     * Imposta un nuovo insieme di alias per l'oggetto.
     *
     * @param newAlias il nuovo insieme di alias
     */
    public void setAlias(final String[] newAlias) {
        this.alias = new HashSet<>(Arrays.asList(newAlias));
    }

    /**
     * Restituisce l'ID dell'oggetto.
     *
     * @return l'ID dell'oggetto
     */
    public int getId() {
        return id;
    }

    /**
     * Verifica se l'oggetto è stato utilizzato.
     *
     * @return true se l'oggetto è stato utilizzato, false altrimenti
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * Imposta se l'oggetto è stato utilizzato.
     *
     * @param used true se l'oggetto deve essere considerato utilizzato, false altrimenti
     */
    public void setUsed(boolean used) {
         this.used = used;
     }

    /**
     * Restituisce il codice hash dell'oggetto basato sull'ID.
     *
     * @return il codice hash dell'oggetto
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.id;
        return hash;
    }

    /**
     * Verifica se questo oggetto è uguale a un altro oggetto.
     * Due oggetti sono considerati uguali se hanno lo stesso ID.
     *
     * @param obj l'oggetto da confrontare
     * @return true se gli oggetti sono uguali, false altrimenti
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
        final AdvObject other = (AdvObject) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    /**
     * Restituisce una frase specifica (di dialogo) associata all'oggetto.
     *
     * @param i l'indice della frase
     * @return la frase all'indice specificato
     */
     public String getSentences(int i) {
         return sentences[i];
     }


    /**
     * Imposta un array di frasi associate all'oggetto.
     *
     * @param sentences il nuovo array di frasi
     */
     public void setSentences(String[] sentences) {
         this.sentences = sentences;
     }

}

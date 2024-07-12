/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Classe che rappresenta un comando nel gioco, ogni comando è definito da un tipo, un nome e un insieme di alias.
 * Implementa {@link Serializable} per permettere la serializzazione dei comandi.
 */
public class Command implements Serializable {

    private final CommandType type;

    private final String name;

    private Set<String> alias;

    /**
     * Costruttore che inizializza un comando con un tipo e un nome.
     *
     * @param newType il tipo di comando
     * @param newName il nome del comando
     */
    public Command(final CommandType newType, final String newName) {
        this.type = newType;
        this.name = newName;
    }

    /**
     * Costruttore che inizializza un comando con un tipo, un nome e un insieme di alias.
     *
     * @param newType il tipo di comando
     * @param newName il nome del comando
     * @param newAlias l'insieme di alias del comando
     */
    public Command(final CommandType newType, final String newName, final Set<String> newAlias) {
        this.type = newType;
        this.name = newName;
        this.alias = newAlias;
    }

    /**
     * Restituisce il nome del comando.
     *
     * @return il nome del comando
     */
    public String getName() {
        return name;
    }

    /**
     * Restituisce l'insieme degli alias del comando.
     *
     * @return l'insieme degli alias
     */
    public Set<String> getAlias() {
        return alias;
    }

    /**
     * Imposta un nuovo insieme di alias per il comando.
     *
     * @param newAlias il nuovo insieme di alias
     */
    public void setAlias(final Set<String> newAlias) {
        this.alias = newAlias;
    }

    /**
     * Imposta un nuovo insieme di alias per il comando utilizzando un array di stringhe.
     *
     * @param newAlias l'array di stringhe contenente i nuovi alias
     */
    public void setAlias(final String[] newAlias) {
        this.alias = new HashSet<>(Arrays.asList(newAlias));
    }

    /**
     * Restituisce il tipo del comando.
     *
     * @return il tipo del comando
     */
    public CommandType getType() {
        return type;
    }

    /**
     * Restituisce il codice hash del comando basato sul tipo.
     *
     * @return il codice hash del comando
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.type);
        return hash;
    }

    /**
     * Verifica se questo comando è uguale a un altro oggetto.
     * Due comandi sono considerati uguali se hanno lo stesso tipo.
     *
     * @param obj l'oggetto da confrontare
     * @return true se i comandi sono uguali, false altrimenti
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
        final Command other = (Command) obj;
        if (this.type != other.type) {
            return false;
        }
        return true;
    }

}

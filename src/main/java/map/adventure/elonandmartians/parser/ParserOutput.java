/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.parser;

import map.adventure.elonandmartians.type.AdvObject;
import map.adventure.elonandmartians.type.Command;

import java.io.Serializable;

/**
 * La classe ParserOutput gestisce il risultato dell'analisi di un comando.
 * Contiene il comando e gli oggetti associati al comando analizzato.
 */
public class ParserOutput implements Serializable {

    private Command command;

    private AdvObject object;

    private AdvObject invObject;

    private AdvObject invObject2;

    /**
     * Costruttore che inizializza un ParserOutput con un comando e un oggetto.
     *
     * @param comm il comando analizzato
     * @param obj l'oggetto associato al comando
     */
    public ParserOutput(final Command comm, final AdvObject obj) {
        this.command = comm;
        this.object = obj;
    }

    /**
     * Costruttore che inizializza un ParserOutput con un comando e tre oggetti.
     *
     * @param comm il comando analizzato
     * @param obj l'oggetto associato al comando
     * @param invObj l'oggetto dell'inventario associato al comando
     * @param invObj2 il secondo oggetto dell'inventario associato al comando
     */
    public ParserOutput(final Command comm, final AdvObject obj, final AdvObject invObj, final AdvObject invObj2) {
        this.command = comm;
        this.object = obj;
        this.invObject = invObj;
        this.invObject2 = invObj2;
    }

    /**
     * Restituisce il comando analizzato.
     *
     * @return il comando analizzato
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Imposta il comando analizzato.
     *
     * @param comm il comando da impostare
     */
    public void setCommand(final Command comm) {
        this.command = comm;
    }

    /**
     * Restituisce l'oggetto associato al comando.
     *
     * @return l'oggetto associato al comando
     */
    public AdvObject getObject() {
        return object;
    }

    /**
     * Imposta l'oggetto associato al comando.
     *
     * @param obj l'oggetto da impostare
     */
    public void setObject(final AdvObject obj) {
        this.object = obj;
    }

    /**
     * Restituisce l'oggetto dell'inventario associato al comando.
     *
     * @return l'oggetto dell'inventario associato al comando
     */
    public AdvObject getInvObject() {
        return invObject;
    }

    /**
     * Imposta l'oggetto dell'inventario associato al comando.
     *
     * @param invObj l'oggetto dell'inventario da impostare
     */
    public void setInvObject(final AdvObject invObj) {
        this.invObject = invObj;
    }

    /**
     * Restituisce il secondo oggetto dell'inventario associato al comando.
     *
     * @return il secondo oggetto dell'inventario associato al comando
     */
    public AdvObject getInvObject2() {
        return invObject2;
    }

    /**
     * Imposta il secondo oggetto dell'inventario associato al comando.
     *
     * @param invObj2 il secondo oggetto dell'inventario da impostare
     */
    public void setInvObject2(final AdvObject invObj2) {
        this.invObject2 = invObj2;
    }
}

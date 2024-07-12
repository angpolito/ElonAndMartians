/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.parser;

import map.adventure.elonandmartians.game.Utils;
import map.adventure.elonandmartians.type.AdvObject;
import map.adventure.elonandmartians.type.Command;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * La classe Parser serve ad interpretare i comandi forniti dall'utente.
 */
public class Parser implements Serializable {

    private final Set<String> stopwords;

    /**
     * Costruttore della classe Parser.
     *
     * @param newStopword il set di stopwords da rimuovere durante il parsing
     */
    public Parser(final Set<String> newStopword) {
        this.stopwords = newStopword;
    }

    /**
     * Verifica se un token corrisponde a un comando.
     *
     * @param token il token da verificare
     * @param commands la lista dei comandi disponibili
     * @return l'indice del comando se trovato, altrimenti -1
     */
    private int checkForCommand(final String token, final List<Command> commands) {
        for (int i = 0; i < commands.size(); i++) {
            if (commands.get(i).getName().equals(token) || commands.get(i).getAlias().contains(token)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Verifica se un token corrisponde a un oggetto.
     *
     * @param token il token da verificare
     * @param objects la lista degli oggetti disponibili
     * @return l'indice dell'oggetto se trovato, altrimenti -1
     */
    private int checkForObject(final String token, final List<AdvObject> objects) {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getName().equals(token) || objects.get(i).getAlias().contains(token)) {
                return i;
            }
        }
        return -1;
    }

    /* ATTENZIONE: il parser e' implementato in modo abbastanza indipendete dalla lingua, ma riconosce solo
    * frasi semplici del tipo <azione> <oggetto> <oggetto>. Eventuali articoli o preposizioni vengono semplicemente
    * rimossi.
    */

    /**
     * Il metodo ParserOutput analizza un comando testuale e cerca di abbinarlo a un comando e, opzionalmente,
     * a uno o due oggetti presenti in una lista di oggetti o nell'inventario.
     *
     * @param command la stringa del comando fornito dall'utente
     * @param commands la lista dei comandi validi
     * @param objects la lista degli oggetti presenti nella stanza
     * @param inventory la lista degli oggetti presenti nell'inventario
     * @return ParserOutput o Null
     */
    public ParserOutput parse(final String command, final List<Command> commands, final List<AdvObject> objects,
        final List<AdvObject> inventory) {
        List<String> tokens = Utils.parseString(command, stopwords);
        if (!tokens.isEmpty()) {
            int ic = checkForCommand(tokens.get(0), commands);
            if (ic > -1) {
                if (tokens.size() > 1) { //se ci sono 2 o 3 tokens
                    int io1 = checkForObject(tokens.get(1), objects); //check se il 2o token è oggetto della stanza
                    int ioinv1 = checkForObject(tokens.get(1), inventory); //check se il 2o token è oggetto dell'inv
                    int io2 = tokens.size() > 2 ? checkForObject(tokens.get(2), objects) : -1;
                    //check se il 3o token è oggetto della stanza
                    int ioinv2 = tokens.size() > 2 ? checkForObject(tokens.get(2), inventory) : -1;
                    //check se il 3o token è oggetto dell'inv


                     if (io1 > -1 && ioinv2 > -1) { //Caso: <comando> <oggetto_stanza> <oggetto_inventario>
                        return new ParserOutput(commands.get(ic), objects.get(io1), inventory.get(ioinv2), null);
                    } else if (ioinv1 > -1 && io2 > -1) { //Caso: <comando> <oggetto_inventario> <oggetto_stanza>
                        return new ParserOutput(commands.get(ic), objects.get(io2), inventory.get(ioinv1), null);
                        //Inverte i due oggetti
                    } else if (io1 > -1) {
                        return new ParserOutput(commands.get(ic), objects.get(io1), null, null);
                    } else if (ioinv1 > -1) { //Caso: <comando> <oggetto_inventario>
                        if (ioinv2 > -1) {  //Caso: <comando> <oggetto_inventario> <oggetto_inventario>
                            return new ParserOutput(commands.get(ic), null, inventory.get(ioinv1),
                                    inventory.get(ioinv2));
                        }
                        return new ParserOutput(commands.get(ic), null, inventory.get(ioinv1), null);
                    } else if (io2 > -1) { //Caso: <comando> <non_riconosciuto> <oggetto_stanza>
                        return new ParserOutput(commands.get(ic), objects.get(io2), null, null);
                    } else if (ioinv2 > -1) { //Caso: <comando> <non_riconosciuto> <oggetto_inventario>
                        return new ParserOutput(commands.get(ic), null, inventory.get(ioinv2), null);
                    } else { //Nessun oggetto trovato
                        return new ParserOutput(commands.get(ic), null, null, null);
                    }
                } else { //Caso: <comando>
                    return new ParserOutput(commands.get(ic), null);
                }
            } else { //Token presente ma non riconosciuto
                return new ParserOutput(null, null);
            }
        } else { //Non ci sono token
            return null;
        }
    }
}

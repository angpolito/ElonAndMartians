/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.observer;

import java.io.Serializable;
import java.util.Random;

import map.adventure.elonandmartians.game.GameDescription;
import map.adventure.elonandmartians.game.Utils;
import map.adventure.elonandmartians.parser.ParserOutput;
import map.adventure.elonandmartians.type.CommandType;
import map.adventure.elonandmartians.game.Engine;

/**
 * Questa classe implementa l'interfaccia GameObserver e gestisce i movimenti del giocatore.
 * Quando il giocatore tenta di muoversi, verifica se il movimento e' possibile
 * e aggiorna la stanza corrente del gioco di conseguenza.
 * Se il movimento non e' possibile, restituisce un messaggio di errore.
 */
public class MoveObserver implements GameObserver, Serializable {
    private Random random;

    /**
     * Il costruttore di MoveObserver inizializza la variabile random.
     */
    public MoveObserver() {
        this.random = new Random();
    }

    /**
     * Aggiorna l'osservatore con lo stato del gioco e il comando dell'utente.
     * <p>
     * Questo metodo puo' essere sovrascritto dalle sottoclassi per modificare
     * il comportamento del movimento.
     *
     * @param description  la descrizione aggiornata del gioco
     * @param parserOutput l'output del parser che rappresenta il comando dell'utente
     * @return una stringa che rappresenta la risposta dell'osservatore al cambiamento
     */
    @Override
    public String update(final GameDescription description, final ParserOutput parserOutput) {
        if (parserOutput.getCommand().getType() == CommandType.NORD) {
            return handleMoveNorth(description);
        } else if (parserOutput.getCommand().getType() == CommandType.SOUTH) {
            return handleMoveSouth(description, parserOutput);
        } else if (parserOutput.getCommand().getType() == CommandType.EAST) {
            return handleMoveEast(description);
        } else if (parserOutput.getCommand().getType() == CommandType.WEST) {
            return handleMoveWest(description);
        }
        return "";
    }

    /**
     * Gestisce il movimento in direzione NORD.
     *
     * @param description la descrizione aggiornata del gioco
     * @return il messaggio risultato dell'azione
     */
    private String handleMoveNorth(final GameDescription description) {
        if (description.getCurrentRoom().getNorth() != null) {
            if (description.getCurrentRoom().getId() == 8) {
                if (!description.getCurrentRoom().getObject(17).isOpen()) {
                    Engine.appendToOutput(("La porta della cella e' chiusa.\nDevi trovare un modo per uscire da"
                            + " qui, prova ad interagire con gli oggetti "
                            + "nella stanza e a frugarti nelle tasche, magari qualcosa e' sfruggito"
                            + "alla perquisizione."));

                } else {
                    description.setCurrentRoom(description.getCurrentRoom().getNorth());
                }
            } else {
                description.setCurrentRoom(description.getCurrentRoom().getNorth());
            }
        } else {
            return generateRandErrMsg();
        }
        return "";
    }

    /**
     * Gestisce il movimento in direzione SUD e il caso specifico del ritorno dal laboratorio.
     *
     * @param description la descrizione aggiornata del gioco
     * @param parserOutput l'output del parser che rappresenta il comando dell'utente
     * @return il messaggio risultato dell'azione
     */
    private String handleMoveSouth(final GameDescription description, final ParserOutput parserOutput) {
        if (description.getCurrentRoom().getSouth() != null) {
            if (description.getCurrentRoom().getId() == 4) {
                if (Utils.findObjectIndex(description.getRooms().get(Utils.findRoomIndex(description, 2)),
                        20) != -1) {
                    if (description.getCurrentRoom().getSouth().getObject(20).isVisible()) {
                        if (description.getCharacterLook()) {
                            if (Utils.findInventoryIndex(description, 5) != -1) {
                                description.setCurrentRoom(description.getCurrentRoom().getSouth());
                                System.out.println(Utils.wrapText("\nL'Alieno nel corridoio ti ha individuato e potrebbe"
                                        + " allertare Zylok."));
                            } else {
                                System.out.println(Utils.wrapText("C'e' un alieno di ronda nel Corridoio, potrebbe riconoscerti"
                                        + " e allertare Zylok, a pugni non te la cavi benissimo: meglio indietreggiare."));
                                description.setCurrentRoom(description.getRooms().get(Utils.findRoomIndex(description,
                                        4)));
                                Utils.printCurrentLocation();
                            }
                        } else {
                            handlePacificAlien(description, parserOutput);
                        }
                    } else {
                        description.setCurrentRoom(description.getCurrentRoom().getSouth());
                    }
                } else {
                    description.setCurrentRoom(description.getCurrentRoom().getSouth());
                }
            } else {
                description.setCurrentRoom(description.getCurrentRoom().getSouth());
            }
        } else {
            return generateRandErrMsg();
        }
        return "";
    }


    /**
     * Gestisce il movimento in direzione EST.
     *
     * @param description la descrizione aggiornata del gioco
     * @return il messaggio risultato dell'azione
     */
    private String handleMoveEast(final GameDescription description) {
        if (description.getCurrentRoom().getEast() != null || description.getCurrentRoom().getId() == 1) {
            if (description.getCurrentRoom().getId() == 1) {
                if (!description.getCurrentRoom().getObject(8).isOpen()) {
                    Engine.appendToOutput("La porta e' chiusa... manca qualcosa per poterla aprire.\n");
                } else {
                    description.setCurrentRoom(null);
                }
            } else {
                description.setCurrentRoom(description.getCurrentRoom().getEast());
            }
        } else {
            return generateRandErrMsg();
        }
        return "";
    }

    /**
     * Gestisce il movimento in direzione OVEST.
     *
     * @param description la descrizione aggiornata del gioco
     * @return il messaggio risultato dell'azione
     */
    private String handleMoveWest(final GameDescription description) {
        if (description.getCurrentRoom().getWest() != null) {
            if (description.getCurrentRoom().getId() == 1) {
                if (!description.getCrewIsFree()) {
                    Engine.appendToOutput("Questa cella e' inaccessibile, puoi parlare con loro attraverso"
                            + " i buchi nel vetro antiproiettile.\n");
                    description.setCurrentRoom(description.getCurrentRoom());
                } else {
                    description.setCurrentRoom(description.getCurrentRoom().getWest());
                }
            } else {
                description.setCurrentRoom(description.getCurrentRoom().getWest());
            }
        } else {
            return generateRandErrMsg();
        }
        return "";
    }

    /**
     * Genera un messaggio di errore casuale quando si tenta di andare in una direzione non consentita.
     *
     * @return un messaggio di errore casuale
     */
    private String generateRandErrMsg() {
        String[] wrongDirectionMessage = {
                "Non puoi oltrepassare i muri...\n",
                "Non capisco perche' sbatti la testa contro il muro.\n",
                "Ahi! Bella botta... da quella parte non puoi andare.\n",
                "Imbecille, immagini porte laddove non ce ne sono! E' un muro quello.\n",
                "Da quella parte non puoi andare, cambia direzione.\n"
        };
        int randomIndex = random.nextInt(wrongDirectionMessage.length);
        return wrongDirectionMessage[randomIndex];
    }

    /**
     * Gestisce l'annuncio dello spawn dell'alieno che non riconosce Elon in quanto travestito.
     *
     * @param description la descrizione aggiornata del gioco
     * @param parserOutput l'output del parser che rappresenta il comando dell'utente
     */
    private void handlePacificAlien(final GameDescription description, final ParserOutput parserOutput) {
        Utils.setTabulation(true);
        description.setCurrentRoom(description.getCurrentRoom().getSouth());
        Utils.setTabulation(true);
        System.out.println(Utils.wrapText("Attenzione, c'e' un alieno nel corridoio, fortunatamente non puo' riconoscerti\n"
                + "\t- Alieno: sono Kif Kroker, il guardiano delle prigioni, tu devi essere il nuovo arrivato,"
                + " ti avverto, non fare cavolate, ho il controllo di tutta questa zona."));
        Utils.setTabulation(false);
    }
}

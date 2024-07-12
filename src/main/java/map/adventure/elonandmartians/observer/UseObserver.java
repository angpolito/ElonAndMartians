/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.observer;

import map.adventure.elonandmartians.game.GameDescription;
import map.adventure.elonandmartians.parser.ParserOutput;
import map.adventure.elonandmartians.type.AdvObjectContainer;
import map.adventure.elonandmartians.type.CommandType;
import map.adventure.elonandmartians.game.Engine;
import map.adventure.elonandmartians.game.Utils;
import map.adventure.elonandmartians.type.Room;

import java.io.Serializable;

/**
 * La classe UseObserver implementa GameObserver e gestisce l'utilizzo degli oggetti nel gioco.
 */
public class UseObserver implements GameObserver, Serializable {


    /**
     * Aggiorna lo stato del gioco in base all'uso di un oggetto.
     *
     * @param description  rappresenta la descrizione aggiornata del gioco
     * @param parserOutput l'output del parser che rappresenta il comando dell'utente
     * @return il messaggio risultato dell'azione
     */
    @Override
    public String update(final GameDescription description, final ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.USE) {
            if (parserOutput.getObject() != null) {
                msg.append(handleGeneralObj(description, parserOutput));
            } else if (parserOutput.getInvObject() != null) {
                msg.append(handleInvInv(description, parserOutput));
            } else {
                msg.append("Eh?? Non ho capito cosa vuoi usare.\n");
            }
        }
        return msg.toString();
    }

    /**
     * Chiama i metodi che rispettivamente gestiscono <usa> <obj_inv> <obj_stanza> e <usa> <obj_stanza>.
     *
     * @param description la descrizione aggiornata del gioco
     * @param parserOutput l'output del parser che rappresenta il comando dell'utente
     * @return il messaggio risultato dell'azione
     */
    private String handleGeneralObj(final GameDescription description, final ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();

        if (parserOutput.getInvObject() != null) {
            msg.append(handleInvObj(description, parserOutput));
        } else if (parserOutput.getObject() instanceof AdvObjectContainer && parserOutput.getObject().getId() == 2) {
            msg.append(handleInvContainer(description, parserOutput));
        } else {
            msg.append(handleObj(description, parserOutput));
        }
        return msg.toString();
    }

    /**
     * Chiama i metodi che gestiscono i casi specifici (ex. telescopio), se il parserOutput restituisce un Container
     * chiama il metodo che gestisce i container
     *
     * @param description la descrizione aggiornata del gioco
     * @param parserOutput l'output del parser che rappresenta il comando dell'utente
     * @return il messaggio risultato dell'azione
     */
    private String handleInvObj(final GameDescription description, final ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();

        if (parserOutput.getObject().isPickupable()) {
            msg.append("Non puoi usare " + parserOutput.getObject().getName() + "!\n"
                    + "Devi prima raccoglierlo...\n");
        } else {
            if (parserOutput.getInvObject().getId() == 18 && parserOutput.getObject().getId() == 10) {
                msg.append(handleTelescope(parserOutput) + ("\n"));
            } else if (parserOutput.getObject().getId() == 10 && parserOutput.getInvObject().getId() != 18) {
                msg.append("Accesso negato: non disponi dei permessi necessari ad utilizzarlo e non sara' certo "
                        + Utils.getArticle(parserOutput.getInvObject().getName()) + " a darteli.");

            } else if (parserOutput.getInvObject().getId() == 15 && parserOutput.getObject().getId() == 17) {
                msg.append(handledoorlockElon(description, parserOutput, 1) + ("\n"));
                Room prisonCellElon = description.getRooms().get(Utils.findRoomIndex(description, 8));
                prisonCellElon.getObject(17).setName("serratura forzata");
                prisonCellElon.getObject(17).setDescription("e' una serratura, e' stata forzata... che altro"
                        + " vuoi sapere?");
                prisonCellElon.getObject(17).setAlias(new String[]{""});
            } else if (parserOutput.getInvObject().getId() == 18 && parserOutput.getObject().getId() == 23) {
                setCrewFree(description);
                msg.append("Hai liberato i tuoi compagni di viaggio, per il momento quei pigroni hanno deciso di"
                        + " rimanere a poltrire nella loro cella. Ti seguiranno qualora decidessi di scappare");
            } else if (parserOutput.getInvObject().getId() != 15 && parserOutput.getObject().getId() == 17) {
                msg.append("Non puoi aprire " + parserOutput.getObject().getName() + " con: "
                        + parserOutput.getInvObject().getName() + ("\n"));

            } else if (parserOutput.getInvObject().getId() == 1 && parserOutput.getObject().getId() == 8) {
                msg.append(handledoorlockElon(description, parserOutput, 2) + ("\n"));
            } else if (parserOutput.getInvObject().getId() != 1 && parserOutput.getObject().getId() == 8) {
                msg.append("Non puoi aprire " + parserOutput.getObject().getName() + " con: "
                        + parserOutput.getInvObject().getName() + ("\n"));
            } else if (parserOutput.getObject() instanceof AdvObjectContainer) {
                msg.append(handleInvContainer(description, parserOutput));
            } else if (parserOutput.getObject().getId() == 11) { //usa teletrasporto con tuta
                if (!description.getCharacterLook()) {
                    msg.append("Regola n.1 del teletrasporto: non tenere in mano oggetti mentre si viaggia, potrebbe"
                            + " essere pericoloso.");
                } else {
                    msg.append("Scansione biometrica avviata... umano rilevato, accesso negato.");
                }
            }
        }
        return msg.toString();
    }

    /**
     * Gestisce tutto l'utilizzo del telescopio.
     *
     * @param parserOutput l'output del parser che rappresenta il comando dell'utente
     * @return il messaggio risultato dell'azione
     */
    private String handleTelescope(final ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();

        if (parserOutput.getObject().isUsed()) {
            msg.append("Hai gia' inviato la tua richiesta di aiuto... continua a pregare");
        } else {
            Engine.appendToOutput("Stai usando " + parserOutput.getObject().getName() + "\n");
            Engine.appendToOutput("Ora puoi inviare un messaggio al pianeta Terra! Puoi pronunciare una sola parola..."
                    + " Non perdere tempo, hai solo tre tentativi.\n");
            parserOutput.getObject().setStateOn(true);
            parserOutput.getObject().setUsed(true);
        }
        return msg.toString();
    }

    /**
     * Gestisce l'apertura delle serrature.
     *
     * @param description la descrizione aggiornata del gioco
     * @param parserOutput l'output del parser che rappresenta il comando dell'utente
     * @return il messaggio risultato dell'azione
     */
    private String handledoorlockElon(final GameDescription description, final ParserOutput parserOutput, final int i) {
        StringBuilder msg = new StringBuilder();
        parserOutput.getObject().setOpen(true);
        description.getInventory().remove(parserOutput.getInvObject());
        if (i == 1) {
            msg.append("Hai aperto la porta della cella");
        } else {
            msg.append("Il portone sembra riconoscere la presenza della gemma... l'incavo si illumina di una luce"
                    + " intensa, quasi accecante, mentre le intarsiature blu si trasformano in un vortice di energia.\n"
                    + "Inserendo la gemma nell'incavo, senti una risonanza profonda, una vibrazione che percorre il"
                    + " portone intero, che immediatamente, inizia a scorrere silenziosamente verso l'alto, rivelando"
                    + " un corridoio illuminato dalla luce del sole rosso, conducente verso l'uscita");
        }
        return msg.toString();
    }

    /**
     * Gestisce l'uso degli oggetti container (nello specifico del generatore di ologrammi).
     *
     * @param description la descrizione aggiornata del gioco
     * @param parserOutput l'output del parser che rappresenta il comando dell'utente
     * @return il messaggio risultato dell'azione
     */
    private String handleInvContainer(final GameDescription description, final ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        AdvObjectContainer c = (AdvObjectContainer) parserOutput.getObject();

        if (parserOutput.getInvObject() != null) {
            if (parserOutput.getObject().getId() == 2 && parserOutput.getInvObject().getId() == 12
                    && parserOutput.getInvObject().isVisible()) {
                if (!c.getList().isEmpty()) {
                    msg.append("Dna umano inserito correttamente.\n");
                    msg.append("\nStai usando " + parserOutput.getObject().getName() + ".\n"
                            + "\nOra hai le sembianze di un marziano! I tuoi nemici non ti riconosceranno.\n");
                    parserOutput.getObject().setStateOn(true);
                    description.setCharacterLook(false);
                    description.getInventory().remove(parserOutput.getInvObject());
                } else {
                    c.add(parserOutput.getInvObject());
                    msg.append("Dna umano inserito correttamente. Manca quello alieno ed e' fatta.\n");
                    description.getInventory().remove(parserOutput.getInvObject());
                }
            } else if (parserOutput.getObject().getId() == 2 && parserOutput.getInvObject().getId() == 13
                    && parserOutput.getInvObject().isVisible()) {
                if (!c.getList().isEmpty()) {
                    msg.append("Dna alieno inserito correttamente.\n");
                    msg.append("\nStai usando " + parserOutput.getObject().getName() + "\n"
                            + "\nOra hai le sembianze di un marziano! I tuoi nemici non ti riconosceranno.\n");
                    parserOutput.getObject().setStateOn(true);
                    description.setCharacterLook(false);
                    description.getInventory().remove(parserOutput.getInvObject());
                    Room prisonsRoom = description.getRooms().get(Utils.findRoomIndex(description, 1));
                } else {
                    c.add(parserOutput.getInvObject());
                    msg.append("Dna alieno inserito correttamente. Manca quello umano ed e' fatta.\n");
                    description.getInventory().remove(parserOutput.getInvObject());
                }
            } else if (parserOutput.getInvObject().getId() == 19 && parserOutput.getObject().getId() == 6) {
                if (!parserOutput.getObject().isOpen()) {
                    msg.append("Sembra che la struttura dello scrigno abbia ceduto alla potenza del colpo. "
                            + "\nOra puoi aprirlo per vederne il contenuto.\n");
                    parserOutput.getObject().setOpen(true);
                } else {
                    msg.append("La serratura e' gia' stata rotta...");
                }
            }
        } else if (parserOutput.getObject().getId() == 2) {
            if (parserOutput.getObject().isStateOn()) {
                msg.append("Stai usando " + parserOutput.getObject().getName() + ".\n");
                if (description.getCharacterLook()) {
                    msg.append("Sei tornato ad avere sembianze aliene. Ora i tuoi nemici non ti riconosceranno.\n");
                    description.setCharacterLook(false);
                    Room prisonsRoom = description.getRooms().get(Utils.findRoomIndex(description, 1));
                } else {
                    msg.append("Sei tornato ad avere sembianze umane. Ora i tuoi nemici ti riconosceranno.\n");
                    description.setCharacterLook(true);
                }
            } else {
                msg.append("Manca ancora qualcosa per poter utilizzare il generatore.");
            }
        }
        return msg.toString();
    }

    /**
     * Gestisce il caso: <usa> <oggetto raccoglibile a terra> e verifica il caso specifico "usa teletrasporto"
     * chiama il metodo che gestisce il teletrasporto.
     *
     * @param description la descrizione aggiornata del gioco
     * @param parserOutput l'output del parser che rappresenta il comando dell'utente
     * @return il messaggio risultato dell'azione
     */
    private String handleObj(final GameDescription description, final ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        Room tRoom = description.getRooms().get(Utils.findRoomIndex(description, 4));

        if (parserOutput.getObject().isPickupable()) {
            msg.append("Non puoi usare " + parserOutput.getObject().getName() + "!\n"
                    + "Devi prima raccoglierlo...\n");
        } else if (parserOutput.getObject().getId() == 11) {
            if (parserOutput.getInvObject() == null) {
                if (!description.getCharacterLook()) {
                    msg.append(handleTeleportation(description));
                } else {
                    msg.append("Scansione biometrica avviata... umano rilevato, accesso negato.");
                }
            } else { //usa teletrasporto con tuta
                msg.append("Regola n.1 del teletrasporto: non tenere in mano oggetti mentre si viaggia,"
                        + " potrebbe essere pericoloso.");
            }

        } else if (parserOutput.getObject().getId() == 10) { //usa telescopio
            msg.append("Accesso negato: non disponi dei permessi necessari ad utilizzarlo. Pare serva qualcosa...");
        } else {
            msg.append("Non puoi usare " + parserOutput.getObject().getName() + " in questo modo!\n"
                    + "Ti manca qualcosa...\n");
        }


        return msg.toString();
    }

    /**
     * Gestisce il teletrasporto.
     *
     * @param description la descrizione aggiornata del gioco
     * @return il messaggio risultato dell'azione
     */
    private String handleTeleportation(final GameDescription description) {
        StringBuilder msg = new StringBuilder();
        if (description.getCurrentRoom().getId() == 5) {
            Engine.appendToOutput("Un raggio di luce ti abbaglia...\n");
            description.setCurrentRoom(description.getRooms().get(Utils.findRoomIndex(description, 6)));
        } else {
            Engine.appendToOutput("Un raggio di luce ti abbaglia...\n");
            description.setCurrentRoom(description.getRooms().get(Utils.findRoomIndex(description, 5)));
        }
        return msg.toString();
    }

    /**
     * Gestisce ii casi in cui due oggetti dell'inventario interagisco tra loro e i casi specifici.
     *
     * @param description la descrizione aggiornata del gioco
     * @param parserOutput l'output del parser che rappresenta il comando dell'utente
     * @return il messaggio risultato dell'azione
     */
    private String handleInvInv(final GameDescription description, final ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();

        if (parserOutput.getInvObject2() != null) {
            if (parserOutput.getInvObject().getId() == 4 && parserOutput.getInvObject2().getId() == 3) {
                // usa forbici con tuta
                description.getInventory().get(Utils.findInventoryIndex(description, 13)).setVisible(true);
                msg.append("Con le forbici spaziali, hai tagliato con precisione un pezzo di tuta aliena,"
                        + " ricco di DNA extraterrestre.\n");
                description.getInventory().remove(parserOutput.getInvObject2());
            } else if (parserOutput.getInvObject().getId() == 4 && parserOutput.getInvObject2().getId() == 14) {
                // usa forbici con capelli
                description.getInventory().get(Utils.findInventoryIndex(description, 12)).setVisible(true);
                msg.append("Con le forbici spaziali, ti sei tagliato un capello, raccogliendo un campione di DNA"
                        + " umano.\n");
                description.getInventory().remove(parserOutput.getInvObject2());
            }
        } else {
            msg.append("Non puoi usare " + parserOutput.getInvObject().getName() + " in questo modo!\n"
                    + "Deve essere combinato con qualcosa.\n");
        }
        return msg.toString();
    }

    /**
     * Gestisce la liberazione dell'equipaggio di bordo
     *
     * @param description la descrizione aggiornata del gioco
     * */
    private void setCrewFree(final GameDescription description) {
        description.setCrewIsFree(true);
        Room prisonsRoom = description.getRooms().get(Utils.findRoomIndex(description, 1));
        Room cellCrew = description.getRooms().get(Utils.findRoomIndex(description, 9));
        prisonsRoom.getObject(23).setName("serratura aperta");
        prisonsRoom.getObject(23).setDescription("la serratura e' gia' stata aperta... grazie Kif Kroker");
        prisonsRoom.getObject(23).setAlias(new String[]{""});
        prisonsRoom.getObject(21).setVisible(true);
        prisonsRoom.getObject(22).setVisible(true);
        cellCrew.getObjects().add(prisonsRoom.getObjects().get(Utils.findObjectIndex(prisonsRoom, 21)));
        prisonsRoom.getObjects().remove(prisonsRoom.getObjects().get(Utils.findObjectIndex(prisonsRoom, 21)));
        cellCrew.getObjects().add(prisonsRoom.getObjects().get(Utils.findObjectIndex(prisonsRoom, 22)));
        prisonsRoom.getObjects().remove(prisonsRoom.getObjects().get(Utils.findObjectIndex(prisonsRoom, 22)));
    }
}

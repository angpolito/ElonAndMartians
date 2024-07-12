/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.observer;

import map.adventure.elonandmartians.game.GameDescription;
import map.adventure.elonandmartians.game.Utils;
import map.adventure.elonandmartians.parser.ParserOutput;
import map.adventure.elonandmartians.type.AdvObjectContainer;
import map.adventure.elonandmartians.type.CommandType;

import java.io.Serializable;

/**
 * La classe OpenObserver implementa GameObserver e gestisce gli oggetti contenitori e contenuti.
 */
public class OpenObserver implements GameObserver, Serializable {

    /**
     *
     * @param description rappresenta la descrizione aggiornata del gioco
     * @param parserOutput e' l'output del parser che rappresenta il comando dell'utente
     * @return il messaggio risultato dell'azione
     */
    @Override
    public String update(final GameDescription description, final ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.OPEN) {
            if (parserOutput.getObject() == null && parserOutput.getInvObject() == null) {
                msg.append("Non c'e' niente da aprire qui.\n");
            } else {
                if (parserOutput.getObject() != null) {
                    if (parserOutput.getObject().isOpenable() && parserOutput.getObject().isOpen()) {
                        if (parserOutput.getObject() instanceof AdvObjectContainer) {
                            msg.append("Hai aperto: ").append(parserOutput.getObject().getName()).append("\n");
                            AdvObjectContainer c = (AdvObjectContainer) parserOutput.getObject();
                            if (!c.getList().isEmpty()) {
                                msg.append("Hai raccolto: \n");
                                for (int i = 0; i < c.getList().size(); i++) {
                                    description.getInventory().add(c.getList().get(i));
                                    msg.append("\t- " + c.getList().get(i).getName() + "\n");
                                    c.getList().remove(c.getList().get(i));
                                }
                                msg.append("\nImprovvisamente si sente un allarme squillare. Sei nei guai!" + "\n");
                                description.getRooms().get(Utils.findRoomIndex(description,
                                        2)).getObject(20).setVisible(true);
                                msg.append("\n");
                            } else {
                                msg.append("Lo scrigno e' vuoto. Hai gia' raccolto il suo contenuto.\n");
                            }
                            parserOutput.getObject().setOpen(true);
                        } else {
                            msg.append("Hai aperto: ").append(parserOutput.getObject().getName()).append("\n");
                            parserOutput.getObject().setOpen(true);
                        }
                    } else {
                        msg.append("Non puoi aprire questo oggetto.\n");
                    }
                }
            }
        }
        return msg.toString();
    }
}

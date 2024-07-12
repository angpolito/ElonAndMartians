package map.adventure.elonandmartians.game;

import map.adventure.elonandmartians.observer.GameObserver;

/**
 * L'interfaccia GameObservable rappresenta un oggetto osservabile nel pattern Observer.
 * Le classi che implementano questa interfaccia possono avere uno o più osservatori che
 * vengono notificati ogni volta che c'e' un cambiamento nello stato dell'oggetto osservabile.
 */
public interface GameObservable {
    /**
     * Aggiunge un osservatore alla lista degli osservatori.
     *
     */
    void attach(GameObserver o);

    /**
     * Notifica tutti gli osservatori il registrarsi di un cambiamento.
     */
    void notifyObservers();
}

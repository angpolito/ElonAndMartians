/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.thread;

import java.io.Serializable;

/**
 * La classe Time gestisce un timer che tiene traccia del tempo trascorso e notifica un callback
 * ad intervalli di un secondo.
 */
public class Time implements Runnable, Serializable {

    private TimerCallback callback;
    private boolean running;
    private static long elapsedTime;
    private long currentTime;
    private static Time instance;

    /**
     * Costruttore che inizializza il timer.
     *
     * @param callback che sarà chiamato per aggiornamenti del tempo.
     */
    public Time(TimerCallback callback) {
        this.callback = callback;
        this.running = false;
    }

    /**
     * Costruttore privato per implementare il pattern Singleton.
     */
    private Time() {}

    /**
     * Restituisce l'istanza singleton della classe Time.
     * Se l'istanza non esiste, viene creata.
     *
     * @return instance
     */
    public static Time getInstance() {
        if (instance == null) {
            instance = new Time();
        }
        return instance;
    }

    /**
     * Metodo run che avvia il timer e aggiorna il tempo trascorso ad intervalli di un secondo.
     * Notifica il callback con il tempo formattato.
     */
    @Override
    public void run() {
        running = true;
        long startTime = System.currentTimeMillis();

        while (running) {
            long currentTime = System.currentTimeMillis();
            elapsedTime += currentTime - startTime;
            startTime = currentTime;

            String formattedTime = formatTime(elapsedTime);
            callback.onTimeUpdate(formattedTime);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Resetta il tempo.
     */
    public void reset() {
        elapsedTime = 0;
    }

    /**
     * Ferma il timer.
     */
    public void stop() {
        running = false;
    }

    /**
     * Converte il tempo trascorso in una stringa formattata HH:mm:ss.
     *
     * @param elapsedTime il tempo trascorso in millisecondi.
     * @return una stringa formattata del tempo trascorso.
     */
    public static String formatTime(long elapsedTime) {
        long seconds = (elapsedTime / 1000) % 60;
        long minutes = (elapsedTime / (1000 * 60)) % 60;
        long hours = (elapsedTime / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Interfaccia funzionale per il callback che viene notificato con il tempo formattato.
     */
    @FunctionalInterface
    public interface TimerCallback {
        void onTimeUpdate(String formattedTime);
    }

    /**
     *
     * @return tempo trascorso in millisecondi
     */
    public static long getElapsedTime(){
        return elapsedTime;
    }

    /**
     * Imposta il tempo trascorso.
     *
     * @param elapsedTime il tempo trascorso in millisecondi da impostare.
     */
    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}

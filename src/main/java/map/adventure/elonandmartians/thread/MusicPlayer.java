/**
 * @author APS Dev (Angelo Polito, Paolo Scicutella, Simone Pugliese)
 */
package map.adventure.elonandmartians.thread;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * La classe MusicPlayer gestisce la riproduzione di file musicali in un thread separato.
 * Permette di avviare, mettere in pausa, interrompere e regolare il volume della riproduzione musicale.
 */
public class MusicPlayer implements Runnable {
    private String musicFilePath;
    private Clip clip;
    private boolean isMuted = false;
    private boolean isPlaying = true;
    private long clipTimePosition = 0;
    private FloatControl volumeControl;

    /**
     * Costruisce un oggetto MusicPlayer con il percorso del file musicale specificato.
     *
     * @param musicFilePath Il percorso del file musicale da riprodurre.
     */
    public MusicPlayer(String musicFilePath) {
        this.musicFilePath = musicFilePath;
    }

    /**
     * Esegue il lettore musicale in un thread separato, riproducendo il file audio specificato in loop.
     * Se il file non viene trovato o si verifica un errore durante la riproduzione, stampa un messaggio di errore.
     * Il thread rimane attivo finché non viene interrotto.
     */
    @Override
    public void run() {
        File musicFile = new File(musicFilePath);
        if (!musicFile.exists()) {
            System.err.println("File audio non trovato: " + musicFilePath);
            return;
        }

        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile)) {
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);


            // Ottieni il controllo del volume
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }

            // Thread attivo finché la musica sta suonando
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(1000);
            }
            clip.stop();
            clip.close();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Errore durante la riproduzione del file audio: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Riproduzione musicale interrotta: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Attiva/disattiva lo stato di muto del lettore musicale. Se è in muto, mette in pausa la riproduzione;
     * se non è in muto, riprende la riproduzione dalla posizione in cui è stata messa in pausa.
     */
    public void toggleMute() {
        if (clip != null) {
            if (isMuted) {
                clip.setMicrosecondPosition(clipTimePosition);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                isMuted = false;
                isPlaying = true;
            } else {
                clipTimePosition = clip.getMicrosecondPosition();
                clip.stop();
                isMuted = true;
                isPlaying = false;
            }
        }
    }

    /**
     * Ferma la riproduzione musicale e chiude il clip.
     */
    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    /**
     * Controlla se il lettore musicale sta attualmente riproducendo.
     *
     * @return Un valore booleano che indica se la musica è in riproduzione.
     */
    public boolean isPlaying() {
        return isPlaying;
    }
}

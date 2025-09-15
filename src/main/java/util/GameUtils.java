package util;

import game.GameWorld;
import game.Player;
import game.GameStateManager;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameUtils {
    // Singleton Pattern: LoggerUtil fornisce un'unica istanza globale del logger.
    // Questo garantisce che tutti i componenti del sistema usino lo stesso logger.
    private static final Logger logger = LoggerUtil.getInstance().getGlobalLogger();

    /**
     * Thread Pattern:
     * Il metodo startGameTimer avvia un timer in un thread separato per gestire la durata della partita.
     * Questo pattern permette di eseguire operazioni concorrenti (come il countdown del tempo di gioco)
     * senza bloccare il flusso principale dell'applicazione, migliorando la reattività e la scalabilità.
     *
     * Motivo dell'utilizzo:
     * - Permette di aggiornare il tempo rimanente in modo asincrono e sicuro.
     * - Consente di terminare il gioco automaticamente allo scadere del tempo.
     * - Favorisce la separazione tra logica di gioco e gestione del tempo.
     */
    public static void startGameTimer(GameWorld gameWorld, Player player, AtomicBoolean gameActive, int durationInSeconds) {
        // Usa la durata specificata o un valore di default se non valido, evitando valori negativi
        int duration = durationInSeconds > 0 ? durationInSeconds : GameSettings.getInstance().getGameDurationInSeconds(); // Tempo dal JSON
        // Thread separato per il timer, che aggiorna il tempo rimanente ogni secondo
        // e termina il gioco quando il tempo scade
        new Thread(() -> {
            try {
                for (int i = duration; i > 0; i--) {
                    logger.log(Level.INFO, "Tempo rimanente: " + i + " secondi");
                    Thread.sleep(1000); // 1 secondo
                    // Aggiorna il tempo rimanente nel mondo di gioco
                    gameWorld.setTimeRemaining(i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Timer interrotto: " + e.getMessage());
            } finally {
                gameActive.set(false); // Termina il gioco
                System.out.println("Tempo scaduto. Termina il gioco.");
            }
        }).start();
    }
}
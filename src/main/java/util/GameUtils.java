package util;

import game.GameWorld;
import game.Player;
import game.GameStateManager;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameUtils {
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
        int duration = durationInSeconds > 0 ? durationInSeconds : GameSettings.getInstance().getGameDurationInSeconds(); // Tempo dal JSON
        new Thread(() -> {
            try {
                for (int i = duration; i > 0; i--) {
                    Thread.sleep(1000); // 1 secondo
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
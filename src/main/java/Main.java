import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.GamePhysics;
import game.GameState;
import game.GameWorld;
import game.GameStateManager;
import game.Player;
import model.Item;
import server.ServerManager;
import util.GameUtils;
import util.LoggerUtil;

public class Main {
    // Singleton Pattern: LoggerUtil fornisce un'unica istanza globale del logger.
    // Questo garantisce che tutti i componenti del sistema usino lo stesso logger.
    private static final Logger logger = LoggerUtil.getInstance().getGlobalLogger();
    
    // Costanti di configurazione
    private static final int GAME_DURATION_IN_SECONDS = 60; // 1 minuto
    private static final int DEFAULT_PLAYER_X = 2;
    private static final int DEFAULT_PLAYER_Y = 5;

    // Stato globale del gioco
    private static final AtomicBoolean gameActive = new AtomicBoolean(true);
    private static int timerDuration;
    private static GamePhysics gamePhysics; // Riferimento a GamePhysics

    public static void main(String[] args) {
        logger.info("Inizializzazione del server e della logica di gioco...");

        // Inizializziamo il mondo e il giocatore
        GameWorld gameWorld = new GameWorld();
        Player player = new Player(DEFAULT_PLAYER_X, DEFAULT_PLAYER_Y);

        // Factory/Builder Pattern: GameStateManager gestisce la creazione e il caricamento dello stato del gioco.
        // Permette di astrarre la logica di persistenza e ripristino dello stato.
        // Delegando questa responsabilità a GameStateManager, il codice principale rimane pulito e focalizzato sulla logica di gioco.
        initializeGameState(gameWorld, player);

        // Thread Pattern: Separazione delle responsabilità tramite thread dedicati per fisica e timer.
        // Migliora la reattività e la scalabilità del gioco.
        startGameThreads(gameWorld, player);

        // Facade Pattern: ServerManager incapsula la complessità della gestione server-client.
        // Espone un'interfaccia semplice per avviare il server.
        startServer(gameWorld, player);

        // Hook Pattern: Utilizzo di shutdown hook per gestire il salvataggio dello stato alla chiusura.
        // Garantisce la persistenza dei dati anche in caso di chiusura improvvisa.
        setupShutdownHook(gameWorld, player);
    }

    /**
     * Carica lo stato del gioco o crea uno stato iniziale.
     * Factory/Builder Pattern: GameStateManager gestisce la creazione e il caricamento dello stato.
     * Questo pattern centralizza la logica di costruzione e ripristino dello stato del gioco,
     * rendendo il codice più modulare e facilmente estendibile.
     */
    private static void initializeGameState(GameWorld gameWorld, Player player) {
        GameState gameState = GameStateManager.loadGameStateWithFallback();
        if (gameState != null) {
            logger.info("Stato del gioco trovato. Caricamento in corso...");
            gameWorld.loadGame(player); // Carichiamo i dati del mondo
            timerDuration = gameState.getTimeRemaining(); // Tempo rimanente
        } else {
            logger.info("Nessun stato salvato trovato. Creazione di uno stato iniziale...");
            timerDuration = GAME_DURATION_IN_SECONDS; // Imposta il tempo di default
            createInitialGameState(gameWorld); // Passa il GameWorld
        }
    }

    /**
     * Crea e salva uno stato iniziale del gioco.
     * Factory/Builder Pattern: GameStateManager crea e salva lo stato iniziale.
     * Questo pattern permette di astrarre la logica di creazione e persistenza dello stato,
     * facilitando la manutenzione e l'estensione del sistema.
     */
    private static void createInitialGameState(GameWorld gameWorld) {
        gameWorld.resetGame(); // Resetta il mondo e crea il terreno e un oggetto iniziale
        GameStateManager.saveGameStateDual( // Salva il nuovo stato iniziale
            DEFAULT_PLAYER_X, 
            DEFAULT_PLAYER_Y, 
            0, 
            gameWorld.getWorldState(new Player(5,3)), // Ottiene gli item generati dal GameWorld
            GAME_DURATION_IN_SECONDS 
        );
        logger.info("Stato iniziale creato e salvato con successo tramite GameWorld.");
    }

    /**
     * Avvia i thread principali del gioco come fisica e timer.
     * Thread Pattern: Separazione delle logiche in thread dedicati.
     * Il Thread Pattern consente di gestire la fisica e il timer in modo concorrente,
     * migliorando la reattività e la scalabilità dell'applicazione.
     */
    private static void startGameThreads(GameWorld gameWorld, Player player) {
        // Motore di fisica del gioco
        gamePhysics = new GamePhysics(gameWorld, player);
        Thread physicsThread = new Thread(gamePhysics);
        physicsThread.start();

        // Timer del gioco
        Thread timerThread = new Thread(() -> {
            GameUtils.startGameTimer(gameWorld, player, gameActive, timerDuration);
        });
        timerThread.start();
    }

    /**
     * Configura e avvia il server per la gestione client-server.
     * Facade Pattern: ServerManager semplifica l'interazione con il server.
     * Il Facade Pattern viene utilizzato per nascondere la complessità della logica server-client,
     * offrendo un'interfaccia semplice e chiara per avviare e gestire il server.
     */
    private static void startServer(GameWorld gameWorld, Player player) {
        ServerManager serverManager = new ServerManager(gameWorld, player, gameActive, timerDuration);
        serverManager.startServer();
        logger.info("Server avviato correttamente e pronto a gestire le richieste.");
    }

    /**
     * Configura un hook per salvare automaticamente lo stato del gioco
     * alla chiusura del programma.
     * Hook Pattern: Utilizzo di shutdown hook per gestire la persistenza.
     * L' Hook Pattern permette di eseguire operazioni di cleanup o salvataggio
     * quando l'applicazione viene chiusa, garantendo la persistenza dei dati.
     */
    private static void setupShutdownHook(GameWorld gameWorld, Player player) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            gameWorld.setGameActive(false); // Ferma ogni logica dipendente dallo stato del gioco
            saveGameState(gameWorld, player);
            gamePhysics.stop(); // Ferma il thread della fisica
            logger.info("Il gioco è stato terminato correttamente.");
        }));
    }

    /**
     * Salva lo stato corrente del gioco.
     * Factory/Builder Pattern: GameStateManager gestisce la persistenza dello stato.
     * Questo pattern centralizza la logica di salvataggio, rendendo il codice più robusto e manutenibile.
     */
    private static void saveGameState(GameWorld gameWorld, Player player) {
        try {
            GameStateManager.saveGameStateDual(
                player.getX(),
                player.getY(),
                gameWorld.getScore(),
                gameWorld.getWorldState(player),
                gameWorld.getTimeRemaining()
            );
            logger.info("Stato del gioco salvato correttamente.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore durante il salvataggio dello stato del gioco: {0}", e.getMessage());
        }
    }
}
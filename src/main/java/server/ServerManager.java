package server;

import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;

import exception.EmojiCraftException;
import game.GamePhysics;
import game.GameWorld;
import game.Player;
import response.WorldResponse;
import util.GameUtils;
import util.LoggerUtil;

import java.util.concurrent.atomic.AtomicBoolean;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

public class ServerManager {
    /**
     * Singleton Pattern:
     * LoggerUtil fornisce un'unica istanza globale del logger per tutto il sistema.
     * Questo pattern garantisce che la gestione dei log sia centralizzata, evitando duplicazioni e
     * facilitando la configurazione e la manutenzione. In un'applicazione server, è fondamentale
     * che tutti i componenti condividano lo stesso logger per tracciare eventi e errori in modo coerente.
     */
    private static final Logger logger = LoggerUtil.getInstance().getGlobalLogger();

    private final GameWorld gameWorld;
    private final Player player;
    private final AtomicBoolean gameActive; // Stato del gioco
    private final Gson gson = new Gson();
    private final int timerRemaining;

    /**
     * Facade Pattern:
     * ServerManager incapsula la logica di interazione tra il server web e il mondo di gioco,
     * fornendo un'interfaccia semplificata per gestire richieste HTTP e aggiornare lo stato.
     * Il Facade Pattern viene utilizzato qui per nascondere la complessità delle operazioni
     * (movimento, reset, timer, serializzazione) e offrire un punto di accesso unico e chiaro
     * alle funzionalità principali del backend. Questo favorisce la separazione delle responsabilità
     * e rende il codice più leggibile e manutenibile.
     */
    public ServerManager(GameWorld gameWorld, Player player, AtomicBoolean gameActive, int timerRemaining) {
        this.gameWorld = gameWorld;
        this.player = player;
        this.gameActive = gameActive;
        this.timerRemaining = timerRemaining;
    }

    public void startServer() {
        staticFiles.externalLocation("C:/Users/bruno/Desktop/OOP/EmojiCraftMaven/public");
        port(4567);

        // Rotta per ottenere lo stato del mondo
        get("/world", (req, res) -> {
            res.type("application/json");

            String[][] grid = gameWorld.getWorldState(player);
            int score = gameWorld.getScore();
            int recentScore = gameWorld.getRecentScoreGained();
            boolean collected = gameWorld.isItemCollected();

            // Usa il tempo rimanente aggiornato da GameWorld
            int currentTimeRemaining = gameWorld.getTimeRemaining();

            /**
             * DTO Pattern:
             * WorldResponse incapsula lo stato globale del mondo di gioco in un oggetto trasferibile.
             * Il Data Transfer Object (DTO) permette di raggruppare tutte le informazioni rilevanti
             * per il client in modo strutturato e serializzabile, favorendo la separazione tra logica
             * di business e presentazione. Questo pattern è particolarmente utile nelle comunicazioni
             * tra server e client, dove serve trasferire dati in modo efficiente e sicuro.
             */
            return gson.toJson(new WorldResponse(grid, score, recentScore, collected, gameActive.get(), currentTimeRemaining));
        });

        // Rotta per gestire il movimento del giocatore
        post("/move", (req, res) -> {
            if (!gameActive.get()) {
                return gson.toJson(new WorldResponse(gameWorld.getWorldState(player), gameWorld.getScore(), 0, false, false, timerRemaining));
            }

            String dir = req.queryParams("dir");
            if (dir == null || !dir.matches("[WASD]")) {
                throw new EmojiCraftException("Invalid direction: " + dir);
            }
            /**
             * Command Pattern:
             * Il metodo movePlayer incapsula l'azione di movimento del giocatore come comando.
             * Questo pattern consente di gestire le azioni come oggetti o metodi separati, facilitando
             * l'estensione, la manutenzione e la gestione di operazioni undo/redo. In questo contesto,
             * ogni richiesta di movimento viene trattata come un comando che modifica lo stato del gioco.
             */
            boolean itemCollected = gameWorld.movePlayer(player, dir.toUpperCase(), new GamePhysics(gameWorld, player));
            res.type("application/json");
            // DTO Pattern: MoveResponse incapsula il risultato del movimento
            return gson.toJson(gameWorld.getMoveResponse(player, itemCollected));
        });

        // Nuova rotta per avviare la partita
        post("/start", (req, res) -> {
            if (gameActive.get()) {
                logger.info("Il gioco è già attivo. Ignora la richiesta.");
                return "{\"message\": \"Game is already active\", \"gameActive\": true}";
            }

            gameActive.set(true); // Imposta il gioco come attivo
            logger.info("Gioco e Timer Avviato. gameActive = " + gameActive.get());

            // Reinizializza il giocatore
            player.setPosition(2, 5);
            gameWorld.createGround();
            logger.info("Giocatore reinizializzato alle posizioni iniziali.");

            /**
             * Thread Pattern:
             * Il timer di gioco viene avviato in un thread separato per evitare blocchi e garantire
             * la reattività del server. Il Thread Pattern permette di gestire operazioni concorrenti
             * (come timer e fisica) in modo indipendente dal flusso principale dell'applicazione.
             */
            new Thread(() -> GameUtils.startGameTimer(gameWorld, player, gameActive, 180)).start();

            res.type("application/json");
            return "{\"message\": \"Game started\", \"gameActive\": true}";
        });

        post("/restart", (req, res) -> {
            logger.info("Richiesta di riavvio del gioco ricevuta.");

            // Ripristina lo stato del gioco
            gameWorld.resetGame();
            player.setPosition(2, 5); // Reimposta la posizione del giocatore
            gameActive.set(true); // Riattiva il gioco

            // Thread Pattern: Avvia nuovamente il motore fisico in un thread separato
            Thread physicsThread = new Thread(new GamePhysics(gameWorld, player));
            physicsThread.start();
            logger.info("Motore fisico riavviato.");

            // Thread Pattern: Avvia il timer del gioco in un thread separato
            new Thread(() -> GameUtils.startGameTimer(gameWorld, player, gameActive, gameWorld.getTimeRemaining())).start();
            logger.info("Timer del gioco riavviato.");

            res.type("application/json");
            return "{\"message\": \"Game restarted\", \"gameActive\": true}";
        });

    }
}
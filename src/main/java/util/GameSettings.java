package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class GameSettings {
    // Logger per la classe GameSettings, usiamo il logger globale
    // per mantenere coerenza nei log dell'applicazione, evitando duplicazioni
    // e facilitando la tracciabilità degli eventi.
    // Singleton Pattern: LoggerUtil fornisce un'unica istanza globale del logger.
    private static final Logger logger = LoggerUtil.getInstance().getGlobalLogger();
    private static final String SETTINGS_FILE = "src/main/resources/GameSettings.json"; // Percorso file impostazioni
    /**
     * Singleton Pattern:
     * La variabile instance mantiene l'unica istanza di GameSettings per tutta l'applicazione.
     * Il Singleton Pattern viene utilizzato qui per garantire che la configurazione del gioco sia
     * caricata una sola volta e condivisa da tutti i componenti, evitando duplicazioni e
     * garantendo coerenza nei parametri di configurazione.
     *
     * Motivo dell'utilizzo:
     * - Centralizza la gestione delle impostazioni, semplificando l'accesso e la modifica.
     * - Riduce il rischio di inconsistenze tra diverse parti del sistema.
     * - Migliora la manutenibilità e la scalabilità dell'applicazione.
     */
    // Istanza singleton di GameSettings
    private static GameSettings instance;

    // Parametri di configurazione del gioco
    // Questi valori saranno caricati dal file JSON, con valori di default in caso di errore
    private int spawnItemInterval;
    private int gameDurationInSeconds;
    private int physicsStrength;
    private int gameUpdateFPS;

    // Singleton per ottenere l'istanza caricata
    public static GameSettings getInstance() {
        if (instance == null) {
            instance = loadSettings();
        }
        return instance;
    }

    // Metodo per caricare le impostazioni dal file JSON
    private static GameSettings loadSettings() {
        try (FileReader reader = new FileReader(SETTINGS_FILE)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.fromJson(reader, GameSettings.class);
        } catch (IOException e) {
            logger.severe("Impossibile caricare il file delle impostazioni: " + e.getMessage());
            // Configura valori predefiniti in caso di errore
            GameSettings defaultSettings = new GameSettings();
            defaultSettings.spawnItemInterval = 4000;
            defaultSettings.gameDurationInSeconds = 180;
            defaultSettings.physicsStrength = 200;
            defaultSettings.gameUpdateFPS = 60;
            return defaultSettings;
        }
    }

    // Getter per le impostazioni
    public int getSpawnItemInterval() {
        return spawnItemInterval;
    }

    public int getGameDurationInSeconds() {
        return gameDurationInSeconds;
    }

    public int getPhysicsStrength() {
        return physicsStrength;
    }

    public int getGameUpdateFPS() {
        return gameUpdateFPS;
    }
}

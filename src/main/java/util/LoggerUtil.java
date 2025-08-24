package util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtil {
    /**
     * Singleton Pattern:
     * LoggerUtil implementa il Singleton Pattern per garantire che esista una sola istanza del logger globale
     * in tutta l'applicazione. Questo pattern è fondamentale per centralizzare la gestione dei log,
     * evitare duplicazioni, facilitare la configurazione e mantenere la coerenza nella registrazione degli eventi.
     *
     * Motivo dell'utilizzo:
     * - Permette a tutti i componenti di accedere allo stesso logger, semplificando la tracciabilità.
     * - Riduce il rischio di errori dovuti a logger multipli o configurazioni incoerenti.
     * - Migliora la manutenibilità e la scalabilità del sistema.
     */
    private static LoggerUtil instance; // Istanza Singleton
    private static final Logger logger = Logger.getLogger("GlobalLogger");

    // Costruttore privato
    private LoggerUtil() {
        try {
            FileHandler fileHandler = new FileHandler("game_logs.log", true); // Scrive su file
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            System.err.println("Errore nel logger: " + e.getMessage());
        }
    }

    // Metodo statico per ottenere l'istanza Singleton
    public static synchronized LoggerUtil getInstance() {
        if (instance == null) {
            instance = new LoggerUtil();
        }
        return instance;
    }

    // Metodo per il logger globale
    public Logger getGlobalLogger() {
        return logger;
    }
}
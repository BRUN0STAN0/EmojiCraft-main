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
    // Logger globale
    private static final Logger logger = Logger.getLogger("GlobalLogger");

    // Costruttore privato
    private LoggerUtil() {
        try {
            FileHandler fileHandler = new FileHandler("game_logs.log", true); // Scrive su file
            // Formattatore semplice per i log
            fileHandler.setFormatter(new SimpleFormatter());
            // Aggiunge il FileHandler al logger, evitando di propagare i log alla console
            logger.addHandler(fileHandler);
            // Disabilita i logger dei genitori, per evitare duplicazioni nei log
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            System.err.println("Errore nel logger: " + e.getMessage());
        }
    }

    // Metodo statico per ottenere l'istanza Singleton
    // Thread-safe per evitare problemi in ambienti multi-thread
    // Lazy initialization per creare l'istanza solo quando necessaria, risparmiando risorse
    public static synchronized LoggerUtil getInstance() {
        if (instance == null) {
            instance = new LoggerUtil();
        }
        return instance;
    }

    // Metodo per il logger globale, accessibile da tutta l'applicazione
    public Logger getGlobalLogger() {
        return logger;
    }
}
/*
 * Design Patterns utilizzati in questo file:
 * - Custom Exception (Specialization): 
 *   Questo file implementa una eccezione personalizzata estendendo RuntimeException.
 *   Il pattern di specializzazione permette di gestire in modo chiaro e specifico gli errori
 *   legati al dominio EmojiCraft, migliorando la robustezza e la leggibilità del codice.
 *
 * Motivo dell'utilizzo:
 * - Permette di distinguere facilmente le eccezioni specifiche dell'applicazione EmojiCraft
 *   da quelle generiche Java, facilitando il debugging e la gestione degli errori.
 */

package exception;

public class EmojiCraftException extends RuntimeException {
    /**
     * Specialization Pattern: Costruttore che permette di specificare un messaggio di errore.
     */
    public EmojiCraftException(String message) {
        super(message);
    }

    /**
     * Specialization Pattern: Costruttore che permette di specificare un messaggio e una causa.
     */
    public EmojiCraftException(String message, Throwable cause) {
        super(message, cause);
    }
}

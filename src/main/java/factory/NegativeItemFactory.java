/*
 * Design Patterns utilizzati in questo file:
 * - Factory Pattern:
 *   Questo file applica il Factory Pattern per la creazione di oggetti Item negativi.
 *   Il Factory Pattern consente di centralizzare la logica di istanziazione, rendendo il codice
 *   più modulare e facilitando l'estensione per nuovi tipi di oggetti.
 *
 * Motivo dell'utilizzo:
 * - Permette di gestire facilmente la creazione di oggetti con caratteristiche specifiche (emoji e punteggi negativi).
 * - Consente di modificare la logica di creazione senza impattare il resto dell'applicazione.
 */

package factory;

import java.util.Random;

import model.Item;

public class NegativeItemFactory {
    private static final String[] EMOJIS = {"💩", "👺", "👽", "👻", "☠️"}; // Emoji negative
    private static final int[] SCORES = {-5, -15, -20, -25, -50}; // Punteggi negativi
    private static final Random random = new Random();

    /**
     * Factory Pattern: Metodo per creare un oggetto Item negativo con emoji e punteggio casuali
     * in una posizione specifica.
     */
    public static Item createRandomNegativeItem(int x, int y) {
        int index = random.nextInt(EMOJIS.length);
        String emoji = EMOJIS[index];
        int score = SCORES[index];
        return new Item(x, y, emoji, score);
    }
}
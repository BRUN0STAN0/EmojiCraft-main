/*
 * Design Patterns utilizzati in questo file:
 * - Factory Pattern:
 *   Questo file implementa il Factory Pattern per la creazione di oggetti Item.
 *   Il Factory Pattern permette di centralizzare e astrarre la logica di istanziazione degli oggetti,
 *   rendendo il codice più modulare, flessibile e facilmente estendibile.
 *
 * Motivo dell'utilizzo:
 * - Facilita la gestione della creazione di oggetti complessi o variabili (Item con emoji e punteggio casuali).
 * - Permette di modificare la logica di creazione senza impattare il resto del sistema.
 */

package factory;

import java.util.Random;

import model.Item;

public class ItemFactory {
    private static final String[] EMOJIS = {"🙂", "😄", "😁", "😍", "🤑"};
    private static final int[] SCORES = {5, 10, 20, 25, 50};
    private static final Random random = new Random();

    /**
     * Factory Pattern: Metodo per creare un oggetto Item con emoji e punteggio casuali
     * in una posizione specifica.
     */
    public static Item createRandomItem(int x, int y) {
        int index = random.nextInt(EMOJIS.length);
        String emoji = EMOJIS[index];
        int score = SCORES[index];
        return new Item(x, y, emoji, score);
    }
}

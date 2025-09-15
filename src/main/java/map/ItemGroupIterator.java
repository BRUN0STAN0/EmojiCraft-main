package map;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Iterator Pattern: Il pattern Iterator consente di accedere agli elementi di un oggetto collezione in modo sequenziale senza esporre la sua rappresentazione interna.
 * La classe ItemGroupIterator implementa l'Iterator Pattern per permettere la navigazione sequenziale
 * tra i componenti di un ItemGroup. Questo pattern separa la logica di iterazione dalla struttura dati,
 * rendendo il codice più flessibile e riutilizzabile.
 *
 * Motivo dell'utilizzo:
 * - Permette di scorrere facilmente i componenti di un gruppo senza esporre la struttura interna.
 * - Facilita l'estensione e la manutenzione della logica di iterazione.
 */
public class ItemGroupIterator implements MapIterator {
    private final List<MapComponent> components;
    private int position = 0;

    public ItemGroupIterator(List<MapComponent> components) {
        this.components = components;
    }

    // Implementazione dell'Iterator Pattern, fornendo i metodi hasNext() e next()
    // Questi metodi permettono di controllare se ci sono elementi successivi e di accedere al prossimo elemento.
    @Override
    public boolean hasNext() {
        // Controlla se ci sono ancora componenti da iterare
        // Restituisce true se ci sono elementi successivi, false altrimenti
        return position < components.size();
    }

    @Override
    public MapComponent next() {
        // Restituisce il componente corrente e passa al successivo
        // Lancia un'eccezione se non ci sono più elementi
        if (!hasNext()) {
            throw new NoSuchElementException("No more components.");
        }
        return components.get(position++);
    }
}

package map;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Iterator Pattern:
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

    @Override
    public boolean hasNext() {
        return position < components.size();
    }

    @Override
    public MapComponent next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more components.");
        }
        return components.get(position++);
    }
}

package map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Composite Pattern:  il pattern Composite consente di trattare oggetti singoli e composizioni di oggetti in modo uniforme.
 * La classe ItemGroup implementa il Composite Pattern per gestire un insieme di MapComponent come un'unica entità.
 * Questo pattern permette di trattare gruppi di oggetti e singoli oggetti in modo uniforme,
 * facilitando operazioni come aggiunta, rimozione e renderizzazione sulla mappa.
 *
 * Motivo dell'utilizzo:
 * - Permette di gestire facilmente gruppi di oggetti (es. muri, item) come se fossero un singolo oggetto.
 * - Facilita l'estensione e la manutenzione del codice, rendendo la struttura della mappa più flessibile.
 */
public class ItemGroup implements Serializable {
    private static final long serialVersionUID = 1L; // Aggiungi un serialVersionUID
    private final List<MapComponent> components = new ArrayList<>();

    // Composite Pattern: Aggiungi un componente al gruppo
    public void add(MapComponent component) {
        components.add(component);
    }

    // Composite Pattern: Rimuovi un componente dal gruppo
    public void remove(MapComponent component) {
        components.remove(component);
    }

    // Composite Pattern: Restituisci tutti i componenti del gruppo
    public List<MapComponent> getComponents() {
        return components;
    }

    // Composite Pattern: Metodo per disegnare tutti i componenti sulla mappa
    public void render(String[][] grid) {
        for (MapComponent component : components) {
            int x = component.getX();
            int y = component.getY();

            // Verifica che gli indici siano validi
            if (x >= 0 && x < grid[0].length && y >= 0 && y < grid.length) {
                grid[y][x] = component.getSymbol(); // Usa il simbolo del componente
            } else {
                System.err.println("Errore: componente fuori dai limiti! X=" + x + ", Y=" + y);
            }
        }
    }

    public boolean isCellEmpty(int x, int y) {
        // Controlla se la cella è vuota
        return components.stream()
                .noneMatch(component -> component.getX() == x && component.getY() == y);
    }
}
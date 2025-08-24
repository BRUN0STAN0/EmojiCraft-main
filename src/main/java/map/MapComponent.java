package map;

/**
 * Composite Pattern:
 * L'interfaccia MapComponent definisce il contratto per i componenti della mappa (item, muri, ecc.).
 * Questo pattern permette di trattare oggetti singoli e gruppi di oggetti in modo uniforme,
 * facilitando l'estensione e la gestione della struttura della mappa.
 *
 * Motivo dell'utilizzo:
 * - Consente di implementare una gerarchia flessibile di oggetti mappa.
 * - Permette di aggiungere facilmente nuovi tipi di componenti mantenendo la compatibilità con il resto del sistema.
 */
public interface MapComponent {
    void render(String[][] grid);
    // Define contract for getters
    int getX();
    int getY();
    String getSymbol();
}

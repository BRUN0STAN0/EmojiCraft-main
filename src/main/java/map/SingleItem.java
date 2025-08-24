package map;

/**
 * Composite Pattern:
 * La classe SingleItem implementa MapComponent e rappresenta un singolo elemento della mappa.
 * Questo pattern permette di trattare oggetti singoli e gruppi di oggetti in modo uniforme,
 * facilitando la gestione e la renderizzazione sulla mappa.
 *
 * Motivo dell'utilizzo:
 * - Consente di gestire ogni item come componente della mappa, mantenendo la compatibilità con ItemGroup.
 * - Facilita l'estensione e la manutenzione della struttura della mappa.
 */
public class SingleItem implements MapComponent {
    private final int x;
    private final int y;
    private final String emoji;
    private boolean visible;

    @Override
    public String getSymbol() {
        return emoji;
    }

    public SingleItem(int x, int y, String emoji) {
        this.x = x;
        this.y = y;
        this.emoji = emoji;
        this.visible = true;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean v) {
        visible = v;
    }

    @Override
    public int getX() { return x; }
    @Override
    public int getY() { return y; }
    public String getEmoji() { return emoji; }

    @Override
    public void render(String[][] grid) {
        if (visible) {
            grid[y][x] = emoji;
        }
    }
}

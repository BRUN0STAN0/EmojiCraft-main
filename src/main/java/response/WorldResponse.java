package response;

/**
 * DTO Pattern:
 * La classe WorldResponse incapsula lo stato globale del mondo di gioco dopo un'azione.
 * Questo pattern facilita il trasferimento di dati tra componenti (ad esempio tra logica di gioco e interfaccia),
 * rendendo il codice più leggibile, manutenibile e facilmente serializzabile.
 *
 * Motivo dell'utilizzo:
 * - Permette di raggruppare e trasferire in modo semplice tutte le informazioni rilevanti sullo stato del mondo.
 * - Favorisce la separazione tra logica di business e presentazione.
 */
public class WorldResponse {
    private final String[][] grid;
    private final int score;
    private final int recentScoreGained;
    private final boolean collected;
    private final boolean gameActive;
    private final int timeRemaining;

    public WorldResponse(String[][] grid, int score, int recentScoreGained, boolean collected, boolean gameActive, int timeRemaining) {
        this.grid = grid;
        this.score = score;
        this.recentScoreGained = recentScoreGained;
        this.collected = collected;
        this.gameActive = gameActive;
        this.timeRemaining = timeRemaining;
    }

    public String[][] getGrid() {
        return grid;
    }

    public int getScore() {
        return score;
    }

    public int getRecentScoreGained() {
        return recentScoreGained;
    }

    public boolean isCollected() {
        return collected;
    }

    public boolean isGameActive() { // Getter per gameActive
        return gameActive;
    }
}
package response;

/**
 * DTO Pattern: DTO (Data Transfer Object)
 * La classe MoveResponse incapsula lo stato risultante da un'azione di movimento del giocatore.
 * Questo pattern facilita il trasferimento di dati tra componenti (ad esempio tra logica di gioco e interfaccia),
 * rendendo il codice più leggibile, manutenibile e facilmente serializzabile.
 *
 * Motivo dell'utilizzo:
 * - Permette di raggruppare e trasferire in modo semplice le informazioni rilevanti dopo un movimento.
 * - Favorisce la separazione tra logica di business e presentazione.
 */
public class MoveResponse {
    private final int playerX;
    private final int playerY;
    private final int score;
    private final boolean itemCollected;

    public MoveResponse(int playerX, int playerY, int score, boolean itemCollected) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.score = score;
        this.itemCollected = itemCollected;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public int getScore() {
        return score;
    }

    public boolean isItemCollected() {
        return itemCollected;
    }
}

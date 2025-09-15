package model;

import java.io.Serializable;

import map.MapComponent;

public class Item implements MapComponent, Serializable {

    /**
     * serialVersionUID:
     * È un identificatore univoco per la versione della classe serializzabile.
     * Serve a garantire la compatibilità tra la versione della classe usata per serializzare
     * e quella usata per deserializzare un oggetto. Se le versioni non corrispondono,
     * Java genera un InvalidClassException durante la deserializzazione.
     * 
     * Utilizzare serialVersionUID è importante per evitare problemi quando la classe cambia nel tempo.
     * E' importante anche in ambito di sicurezza, poiché previene attacchi di deserializzazione
     * che potrebbero sfruttare differenze tra versioni della classe.
     * 
     * 1L indica che questa è la prima versione della classe. Ma si poteva anche scegliere un altro numero
     * o una stringa hash per rappresentare la versione della classe.
     * In questo caso, 1L è sufficiente perché la classe è semplice e non
     * contiene logica complessa che potrebbe richiedere versioni successive.
     */
    private static final long serialVersionUID = 1L;

    private int x;
    private int y;
    private String emoji;
    private boolean visible;
    private int score;

    /**
     * Composite Pattern: il composite pattern è utilizzato per trattare oggetti singoli e composizioni di oggetti in modo uniforme.
     * Questo approccio semplifica la gestione di strutture complesse, consentendo
     * di trattare oggetti composti e singoli con la stessa interfaccia.
     * 
     * Il costruttore di Item permette di creare un singolo componente della mappa,
     * gestibile uniformemente insieme ad altri tramite ItemGroup.
     *
     * DTO Pattern: DTO (Data Transfer Object)
     * La classe Item funge da DTO per trasferire informazioni tra il mondo di gioco e l'interfaccia utente.
     * 
     * Incapsula lo stato dell'oggetto (posizione, emoji, punteggio, visibilità) per facilitarne il trasferimento tra componenti.
     */
    public Item(int x, int y, String emoji, int score) {
        this.x = x;
        this.y = y;
        this.emoji = emoji;
        this.score = score;
        this.visible = true;
    }

    // 👇 Vecchio costruttore mantenuto per compatibilità, se ti serve ancora
    public Item(int x, int y, String emoji) {
        this(x, y, emoji, 1); // default score: 1
    }

    @Override
    public String getSymbol() {
        return emoji;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public String getEmoji() {
        return emoji;
    }

    public int getScore() {
        return score;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Composite Pattern:
     * Il metodo render permette di gestire la visualizzazione dell'item come componente della mappa,
     * uniformando la logica di renderizzazione tra singoli oggetti e gruppi.
     */
    @Override
    public void render(String[][] grid) {
        if (visible) {
            grid[y][x] = emoji;
        }
    }
}

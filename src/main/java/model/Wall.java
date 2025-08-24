package model;

import java.io.Serializable;

import map.MapComponent;

public class Wall implements MapComponent, Serializable {
    
    /**
     * serialVersionUID:
     * È un identificatore univoco per la versione della classe serializzabile.
     * Serve a garantire la compatibilità tra la versione della classe usata per serializzare
     * e quella usata per deserializzare un oggetto. Se le versioni non corrispondono,
     * Java genera un InvalidClassException durante la deserializzazione.
     * 
     * Utilizzare serialVersionUID è importante per evitare problemi quando la classe cambia nel tempo.
     */
    private static final long serialVersionUID = 1L;

    private final int x, y;

    /**
     * Composite Pattern:
     * Il costruttore di Wall permette di creare un singolo componente della mappa,
     * gestibile uniformemente insieme ad altri tramite ItemGroup.
     * Questo pattern facilita la gestione e la renderizzazione di oggetti multipli e singoli in modo uniforme.
     */
    public Wall(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    /**
     * Composite Pattern:
     * Il metodo render permette di gestire la visualizzazione del muro come componente della mappa,
     * uniformando la logica di renderizzazione tra singoli oggetti e gruppi.
     */
    @Override
    public void render(String[][] grid) {
        grid[y][x] = "🧱";
    }

    @Override
    public String getSymbol() {
        return "🧱";
    }
}
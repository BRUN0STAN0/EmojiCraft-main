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
     * E' importante anche in ambito di sicurezza, poiché previene attacchi di deserializzazione
     * che potrebbero sfruttare differenze tra versioni della classe.
     * 
     * 1L indica che questa è la prima versione della classe. Ma si poteva anche scegliere un altro numero
     * o una stringa hash per rappresentare la versione della classe.
     * In questo caso, 1L è sufficiente perché la classe è semplice e non
     * contiene logica complessa che potrebbe richiedere versioni successive.
     */
    private static final long serialVersionUID = 1L;

    private final int x, y;

    /**
     * Composite Pattern: Il composite pattern è utilizzato per trattare oggetti singoli e composizioni di oggetti in modo uniforme.
     * Questo approccio semplifica la gestione di strutture complesse, consentendo
     * di trattare oggetti composti e singoli con la stessa interfaccia.
     * Il costruttore di Wall permette di creare un singolo componente della mappa,
     * gestibile uniformemente insieme ad altri tramite ItemGroup.
     * Questo pattern facilita la gestione e la renderizzazione di oggetti multipli e singoli in modo uniforme.
     */
    public Wall(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getter per la coordinata X
    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    /**
     * Composite Pattern: Il composite pattern è utilizzato per trattare oggetti singoli e composizioni di oggetti in modo uniforme.
     * Questo approccio semplifica la gestione di strutture complesse, consentendo di trattare oggetti composti e singoli
     * con la stessa interfaccia.
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
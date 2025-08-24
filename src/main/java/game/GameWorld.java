package game;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import factory.ItemFactory;
import factory.NegativeItemFactory;
import map.ItemGroup;
import map.MapComponent;
import model.Item;
import model.Wall;
import response.MoveResponse;
import util.GameSettings;

public class GameWorld {
    // Singleton Pattern: Utilizzo di GameSettings come configurazione globale condivisa
    private static final util.GameSettings gameSettings = util.GameSettings.getInstance();
    private static final long ITEM_LIFETIME = gameSettings.getSpawnItemInterval(); // Intervallo spawn item

    private static final int WIDTH = 24;
    private static final int HEIGHT = 10;
    private final ItemGroup items = new ItemGroup();
    private List<Item> itemList = new ArrayList<>();
    private int score = 0;
    private int recentScoreGained = 0;
    private boolean itemCollected = false;
    // Thread-safe Collection Pattern: uso di ConcurrentHashMap per gestire i timer degli oggetti in modo sicuro tra thread
    private final Map<Item, Long> itemTimers = new ConcurrentHashMap<>();
    private int timeRemaining;
    private int itemSpawnCounter = 0;
    private boolean gameActive = true;
    private boolean spawnNegativeNext = false;
    private static final int PLAYER_START_X = 5;
    private static final int PLAYER_START_Y = 3;

    public GameWorld() {
        createGround();
        spawnNewItem();
    }

    // Singleton Pattern: GameSettings come configurazione globale
    public synchronized boolean isGameActive() {
        return gameActive;
    }

    public synchronized void setGameActive(boolean isActive) {
        this.gameActive = isActive;
    }

    public synchronized int getTimeRemaining() {
        return timeRemaining;
    }

    public synchronized void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    // Getter per gli oggetti
    public List<Item> getItems() {
        return itemList;
    }

    public void setItems(List<Item> items) {
        this.itemList = items;
    }

    public GameWorld(ItemGroup items) {
        this.items.getComponents().addAll(items.getComponents());
    }

    /**
     * Facade Pattern: Il metodo movePlayer fornisce un'interfaccia semplificata per gestire il movimento del giocatore,
     * nascondendo la complessità della gestione della fisica e delle collisioni.
     */
    public boolean movePlayer(Player player, String direction, GamePhysics gamePhysics) {
        itemCollected = false;

        // Thread Pattern: Notifica che il movimento manuale è in corso
        gamePhysics.startManualMovement();

        int newX = player.getX();
        int newY = player.getY();

        // Calcola la nuova posizione in base alla direzione
        switch (direction) {
            case "W" -> newY -= 1;
            case "S" -> newY += 1;
            case "A" -> newX -= 1;
            case "D" -> newX += 1;
        }

        // Controlla se la nuova posizione è valida
        if (newX >= 0 && newX < WIDTH && newY >= 0 && newY < HEIGHT - 2) {
            player.move(newX - player.getX(), newY - player.getY(), directionToEmoji(direction));
            itemCollected = checkItemCollision(player);
        } else {
            System.out.println("Movimento non valido: il giocatore ha raggiunto il limite della griglia.");
        }

        // Thread Pattern: Fine del movimento manuale, riattiva la fisica
        gamePhysics.endManualMovement();

        System.out.println("Posizione giocatore: X=" + player.getX() + ", Y=" + player.getY());
        return itemCollected;
    }

    // Metodo di supporto per ottenere l'emoji della direzione
    private String directionToEmoji(String direction) {
        return switch (direction) {
            case "W" -> "🤸🏻‍♂️";
            case "S" -> "🧎🏻‍♂️‍➡️";
            case "A" -> "🚶🏻‍♂️";
            case "D" -> "🚶🏻‍♂️‍➡️";
            default -> "🧍‍♂️";
        };
    }

    /**
     * Facade Pattern: checkItemCollision incapsula la logica di raccolta oggetti e aggiornamento punteggio,
     * semplificando l'interazione con il mondo di gioco.
     */
    public synchronized boolean checkItemCollision(Player player) {
        System.out.println("Controllo collisione per il giocatore in X=" + player.getX() + ", Y=" + player.getY());
        System.out.println("Oggetti presenti nel mondo:");
        items.getComponents().forEach(item -> {
            System.out.println("- Oggetto: " + item.getSymbol() + ", X=" + item.getX() + ", Y=" + item.getY());
        });

        boolean collected = items.getComponents().removeIf(component -> {
            if (component instanceof Item item && item.getX() == player.getX() && item.getY() == player.getY()) {
                recentScoreGained = item.getScore();
                score += recentScoreGained;
                System.out.println("Oggetto raccolto: " + item.getSymbol());
                return true;
            }
            return false;
        });

        if (collected) {
            spawnNewItem();
        }

        return collected;
    }

    /**
     * DTO Pattern: getWorldState restituisce una rappresentazione compatta dello stato del mondo,
     * utile per il trasferimento tra componenti e per la serializzazione.
     */
    public String[][] getWorldState(Player player) {
        String[][] grid = new String[HEIGHT][WIDTH];
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                grid[y][x] = " ";
            }
        }

        items.render(grid);

        if (player.getY() >= 0 && player.getY() < HEIGHT && player.getX() >= 0 && player.getX() < WIDTH) {
            grid[player.getY()][player.getX()] = player.getEmoji();
        } else {
            System.err.println("Errore: il giocatore è fuori dai limiti della griglia!");
        }

        return grid;
    }

    public int getScore() {
        return score;
    }

    public int getRecentScoreGained() {
        return recentScoreGained;
    }

    public boolean isItemCollected() {
        return itemCollected;
    }

    /**
     * Composite Pattern: createGround aggiunge oggetti Wall al gruppo di oggetti (ItemGroup),
     * permettendo di gestire oggetti multipli come un'unica entità.
     */
    public void createGround() {
        for (int y = HEIGHT - 2; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Wall wall = new Wall(x, y);
                items.add(wall);
                System.out.println("Muro aggiunto in posizione X=" + x + ", Y=" + y);
            }
        }
    }

    public MoveResponse getMoveResponse(Player player, boolean itemCollected) {
        return new MoveResponse(player.getX(), player.getY(), score, itemCollected);
    }

    public boolean isCellEmpty(int x, int y) {
        return items.getComponents().stream()
                .noneMatch(component -> component.getX() == x && component.getY() == y);
    }

    public int getHeight() {
        return HEIGHT;
    }

    /**
     * Thread-safe Collection Pattern: updateItems gestisce la rimozione e lo spawn degli oggetti
     * in modo sicuro tra thread, grazie all'uso di ConcurrentHashMap.
     */
    public synchronized void updateItems() {
        if (!gameActive) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        Iterator<Map.Entry<Item, Long>> iterator = itemTimers.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Item, Long> entry = iterator.next();
            Item item = entry.getKey();
            long creationTime = entry.getValue();

            if (currentTime - creationTime > ITEM_LIFETIME) {
                items.remove(item);
                iterator.remove();
                System.out.println("Oggetto scaduto rimosso: " + item.getSymbol());

                if (gameActive) {
                    spawnNewItem();
                }
            }
        }
    }

    /**
     * Factory Pattern: spawnNewItem utilizza ItemFactory e NegativeItemFactory per alternare la creazione
     * di oggetti positivi e negativi, centralizzando la logica di istanziazione.
     */
    public synchronized void spawnNewItem() {
        if (!gameActive) return;

        Random random = new Random();
        int x, y;

        do {
            x = random.nextInt(WIDTH);
            y = random.nextInt(HEIGHT - 2);
        } while (!isCellEmpty(x, y));

        Item newItem = spawnNegativeNext
            ? NegativeItemFactory.createRandomNegativeItem(x, y)
            : ItemFactory.createRandomItem(x, y);

        spawnNegativeNext = !spawnNegativeNext;

        items.add(newItem);
        itemTimers.put(newItem, System.currentTimeMillis());
        System.out.println("Oggetto registrato: " + newItem.getSymbol() + " in posizione X=" + x + ", Y=" + y);
    }

    /**
     * Composite Pattern: render delega la visualizzazione degli oggetti al gruppo (ItemGroup),
     * permettendo di gestire la renderizzazione di più oggetti in modo uniforme.
     */
    public void render(String[][] grid) {
        for (MapComponent component : items.getComponents()) {
            int x = component.getX();
            int y = component.getY();

            if (x >= 0 && x < grid[0].length && y >= 0 && y < grid.length) {
                grid[y][x] = component.getSymbol();
                System.out.println("Render Oggetto: " + component.getSymbol() + " in posizione X=" + x + ", Y=" + y);
            } else {
                System.err.println("Errore: componente fuori dai limiti! X=" + x + ", Y=" + y);
            }
        }
    }

    /**
     * Facade Pattern: saveGame incapsula la logica di salvataggio dello stato del gioco,
     * semplificando l'interazione con GameStateManager.
     */
    public void saveGame(Player player) {
        try {
            GameStateManager.saveGameStateDual(
                player.getX(),
                player.getY(),
                score,
                getWorldState(player),
                timeRemaining
            );
            System.out.println("Stato del gioco salvato correttamente.");
        } catch (Exception e) {
            System.err.println("Errore durante il salvataggio dello stato del gioco: " + e.getMessage());
        }
    }

    /**
     * Facade Pattern: loadGame incapsula la logica di caricamento dello stato del gioco,
     * semplificando l'interazione con GameStateManager.
     */
    public void loadGame(Player player) {
        try {
            GameState gameState = GameStateManager.loadGameState();
            if (gameState != null) {
                player.move(gameState.getPlayerX() - player.getX(), gameState.getPlayerY() - player.getY(), "🧍");
                score = gameState.getScore();
                timeRemaining = gameState.getTimeRemaining();
                items.getComponents().clear();

                String[][] loadedGrid = gameState.getGrid();

                if (loadedGrid != null) {
                    for (int y = 0; y < loadedGrid.length; y++) {
                        for (int x = 0; x < loadedGrid[0].length; x++) {
                            String symbol = loadedGrid[y][x];
                            if (symbol != null && !symbol.equals(" ")) {
                                MapComponent mapComponent = new Wall(x,y);
                                if (mapComponent != null) {
                                    items.add(mapComponent);
                                }
                            }
                        }
                    }
                } else {
                    System.err.println("Attenzione: non è stata trovata alcuna griglia salvata!");
                }

                System.out.println("Stato del gioco caricato con successo.");
            } else {
                System.err.println("Errore: impossibile caricare lo stato del gioco!");
            }
        } catch (Exception e) {
            System.err.println("Errore durante il caricamento dello stato del gioco: " + e.getMessage());
        }
    }

    /**
     * Hook Pattern: registerShutdownHook registra un hook per salvare automaticamente lo stato del gioco
     * alla chiusura dell'applicazione.
     */
    public void registerShutdownHook(Player player) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            saveGame(player);
        }));
    }

    public static void validateOrInitializePlayer(GameWorld gameWorld, Player player) {
        if (!gameWorld.isValidPosition(player.getX(), player.getY())) {
            System.out.println("Giocatore fuori dai confini. Reinserito nella posizione iniziale.");
            player.setPosition(5, 3);
        }
    }
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT - 2;
    }

    /**
     * Facade Pattern: resetGame incapsula la logica di reset dell'intero stato del mondo,
     * semplificando la gestione della partita.
     */
    public void resetGame() {
        this.items.getComponents().clear(); 
        this.itemTimers.clear();
        this.score = 0;
        this.timeRemaining = 60;
        this.recentScoreGained = 0;
        this.itemCollected = false;
        createGround(); 
        spawnNewItem(); 
        System.out.println("Gioco completamente ripristinato. Oggetti, timer e punteggio resettati.");
    }

    public ItemGroup getItemsGroup() {
        return this.items;
    }
}
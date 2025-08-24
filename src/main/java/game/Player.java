package game;

public class Player {
    private int x;
    private int y;
    private String emoji = "🧍🏻‍♂️"; // Emoji predefinita

    /**
     * DTO Pattern:
     * La classe Player rappresenta un oggetto di trasferimento dati che incapsula
     * lo stato del giocatore (posizione e emoji) e lo rende facilmente gestibile e serializzabile.
     * Questo pattern facilita la comunicazione tra componenti e la manipolazione dello stato.
     */
    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    // Getter & Setter (DTO Pattern: facilitano la manipolazione dello stato)
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    /**
     * Command Pattern:
     * Il metodo move incapsula l'azione di movimento del giocatore, aggiornando posizione ed emoji.
     * Questo pattern permette di gestire le azioni come comandi, rendendo il codice più flessibile.
     */
    public void move(int deltaX, int deltaY, String newEmoji) {
        this.x += deltaX;
        this.y += deltaY;
        this.emoji = newEmoji; // Aggiorna l'emoji in base alla direzione
    }

    public void setPosition(int x, int y) {

        this.x = x;
        this.y = y;
    }
}
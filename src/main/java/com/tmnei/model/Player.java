package com.tmnei.model;

/**
 * Representa a un jugador en el juego de memoria.
 */
public class Player {
    /** Nombre del jugador. */
    private final String name;
    /** Puntaje acumulado del jugador. */
    private int score;
    /** Total de fallos cometidos por el jugador. */
    private int totalFails;
    /** Tiempo total jugado por el jugador en milisegundos. */
    private long totalTime;
    /** Cantidad de pares encontrados por el jugador. */
    private int matches;

    /**
     * Crea un nuevo jugador con el nombre especificado.
     * @param name nombre del jugador
     */
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.totalFails = 0;
        this.totalTime = 0;
        this.matches = 0;
    }

    /**
     * Obtiene el nombre del jugador.
     * @return nombre del jugador
     */
    public String getName() { return name; }

    /**
     * Obtiene el puntaje acumulado del jugador.
     * @return puntaje del jugador
     */
    public int getScore() { return score; }

    /**
     * Obtiene el total de fallos cometidos por el jugador.
     * @return total de fallos
     */
    public int getTotalFails() { return totalFails; }

    /**
     * Obtiene el tiempo total jugado por el jugador en milisegundos.
     * @return tiempo total en milisegundos
     */
    public long getTotalTime() { return totalTime; }

    /**
     * Obtiene la cantidad de pares encontrados por el jugador.
     * @return cantidad de pares encontrados
     */
    public int getMatches() { return matches; }

    /**
     * Suma puntos al puntaje del jugador.
     * @param points puntos a sumar
     */
    public void addScore(int points) { this.score += points; }

    /**
     * Incrementa en uno el total de fallos del jugador.
     */
    public void addFail() { this.totalFails++; }

    /**
     * Suma tiempo al tiempo total jugado por el jugador.
     * @param time tiempo a sumar en milisegundos
     */
    public void addTime(long time) { this.totalTime += time; }

    /**
     * Incrementa en uno la cantidad de pares encontrados por el jugador.
     */
    public void addMatch() { this.matches++; }

    /**
     * Reinicia las estadísticas del turno actual del jugador.
     * (Método disponible para futuras ampliaciones)
     */
    public void resetTurnStats() {

    }
}

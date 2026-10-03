package com.tmnei.model;

import java.util.*;

/**
 * Modelo principal del juego de memoria.
 * Gestiona el estado del juego, los jugadores, las cartas, los turnos y la lógica de niveles.
 */
public class GameModel {
    /** Números y sus representaciones en inglés y español. */
    private static final String[][] NUMBERS = {
            {"1", "One", "Uno"},
            {"2", "Two", "Dos"},
            {"3", "Three", "Tres"},
            {"4", "Four", "Cuatro"},
            {"5", "Five", "Cinco"},
            {"6", "Six", "Seis"},
            {"7", "Seven", "Siete"},
            {"8", "Eight", "Ocho"},
            {"9", "Nine", "Nueve"},
            {"10", "Ten", "Diez"},
            {"11", "Eleven", "Once"},
            {"12", "Twelve", "Doce"},
            {"13", "Thirteen", "Trece"},
            {"14", "Fourteen", "Catorce"},
            {"15", "Fifteen", "Quince"}
    };

    /** Lista de jugadores. */
    private final List<Player> players;
    /** Índice del jugador actual. */
    private int currentPlayerIndex;
    /** Número de turno actual. */
    private int currentTurn;
    /** Nivel actual del juego. */
    private int currentLevel;
    /** Lista de cartas en juego. */
    private List<Card> cards;
    /** Primera carta seleccionada en el turno. */
    private Card firstCard;
    /** Segunda carta seleccionada en el turno. */
    private Card secondCard;
    /** Tiempo de inicio del turno actual. */
    private long turnStartTime;
    /** Fallos cometidos en el turno actual. */
    private int turnFails;
    /** Indica si el juego ha finalizado. */
    private boolean gameFinished;

    /**
     * Crea un nuevo modelo de juego con la cantidad de jugadores indicada.
     * @param numPlayers número de jugadores
     */
    public GameModel(int numPlayers) {
        this.players = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            players.add(new Player("Jugador " + i));
        }
        this.currentPlayerIndex = 0;
        this.currentTurn = 1;
        this.currentLevel = 1;
        this.gameFinished = false;
        generateCards();
        startTurn();
    }

    /**
     * Genera las cartas para el nivel actual.
     */
    private void generateCards() {
        cards = new ArrayList<>();
        int pairsCount = getCurrentLevelPairs();

        for (int i = 0; i < pairsCount; i++) {
            String[] numberData = NUMBERS[i];
            cards.add(new Card(numberData[0], numberData[1], "English"));
            cards.add(new Card(numberData[0], numberData[2], "Spanish"));
        }

        Collections.shuffle(cards);
    }

    /**
     * Reinicia el estado de las cartas y las mezcla.
     */
    private void resetCards() {
        for (Card card : cards) {
            card.setFlipped(false);
            card.setMatched(false);
        }
        Collections.shuffle(cards);
    }

    /**
     * Obtiene la cantidad de pares para el nivel actual.
     * @return cantidad de pares
     */
    private int getCurrentLevelPairs() {
        return switch (currentLevel) {
            case 2 -> 10;
            case 3 -> 15;
            default -> 5;
        };
    }

    /**
     * Obtiene el tamaño de la grilla para el nivel actual.
     * @return cantidad de cartas en la grilla
     */
    public int getGridSize() {
        return switch (currentLevel) {
            case 2 -> 20;
            case 3 -> 30;
            default -> 10;
        };
    }

    /**
     * Obtiene la cantidad de columnas de la grilla para el nivel actual.
     * @return columnas de la grilla
     */
    public int getGridCols() {
        if (currentLevel == 3) {
            return 6;
        }
        return 5;
    }

    /**
     * Obtiene la cantidad de filas de la grilla para el nivel actual.
     * @return filas de la grilla
     */
    public int getGridRows() {
        return switch (currentLevel) {
            case 2 -> 4;
            case 3 -> 5;
            default -> 2;
        };
    }

    /**
     * Selecciona una carta en el turno actual.
     * @param card carta seleccionada
     * @return true si la selección es válida, false si la carta ya está volteada o emparejada
     */
    public boolean selectCard(Card card) {
        if (card.isFlipped() || card.isMatched()) {
            return false;
        }

        card.setFlipped(true);

        if (firstCard == null) {
            firstCard = card;
            return true;
        } else {
            secondCard = card;
            return checkMatch();
        }
    }

    /**
     * Verifica si las dos cartas seleccionadas forman un par.
     * @return true si hay coincidencia, false en caso contrario
     */
    private boolean checkMatch() {
        if (firstCard.matches(secondCard)) {
            firstCard.setMatched(true);
            secondCard.setMatched(true);
            getCurrentPlayer().addMatch();
            return true;
        } else {
            turnFails++;
            getCurrentPlayer().addFail();
            return false;
        }
    }

    /**
     * Reinicia la selección de cartas, volteando las que no hayan sido emparejadas.
     */
    public void resetSelectedCards() {
        if (firstCard != null && !firstCard.isMatched()) {
            firstCard.setFlipped(false);
        }
        if (secondCard != null && !secondCard.isMatched()) {
            secondCard.setFlipped(false);
        }
        firstCard = null;
        secondCard = null;
    }

    /**
     * Indica si el nivel actual está completo (todas las cartas emparejadas).
     * @return true si el nivel está completo, false en caso contrario
     */
    public boolean isLevelComplete() {
        return cards.stream().allMatch(Card::isMatched);
    }

    /**
     * Avanza al siguiente turno, actualizando puntajes, nivel y jugador actual.
     */
    public void nextTurn() {
        long turnTime = System.currentTimeMillis() - turnStartTime;
        getCurrentPlayer().addTime(turnTime);

        int score = calculateTurnScore(turnTime, turnFails);
        getCurrentPlayer().addScore(score);

        currentTurn++;

        if (currentTurn > 12) {
            gameFinished = true;
            return;
        }

        if (currentTurn % 4 == 1 && currentTurn > 1) {
            currentLevel++;
            generateCards();
        } else {
            resetCards();
        }

        if (players.size() > 1) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }

        startTurn();
    }

    /**
     * Inicia un nuevo turno, reiniciando tiempos y selección de cartas.
     */
    private void startTurn() {
        turnStartTime = System.currentTimeMillis();
        turnFails = 0;
        resetSelectedCards();
    }

    /**
     * Calcula el puntaje obtenido en el turno según el tiempo y los fallos.
     * @param turnTime tiempo del turno en milisegundos
     * @param fails cantidad de fallos en el turno
     * @return puntaje calculado
     */
    private int calculateTurnScore(long turnTime, int fails) {
        int maxScore = 100;
        int idealTime = getIdealTimeForLevel() * 1000;
        int maxTime = idealTime * 3;

        int timeSeconds = (int) (turnTime / 1000);

        int timePenalty = 0;
        if (timeSeconds > idealTime / 1000) {
            timePenalty = Math.min(50, (timeSeconds - idealTime / 1000) * 2);
        }

        int failPenalty = fails * 10;

        return Math.max(0, maxScore - timePenalty - failPenalty);
    }

    /**
     * Obtiene el tiempo ideal para completar el nivel actual.
     * @return tiempo ideal en segundos
     */
    private int getIdealTimeForLevel() {
        return switch (currentLevel) {
            case 2 -> 60;
            case 3 -> 90;
            default -> 30;
        };
    }

    /**
     * Obtiene la lista de jugadores.
     * @return lista de jugadores
     */
    public List<Player> getPlayers() { return players; }

    /**
     * Obtiene el jugador actual.
     * @return jugador actual
     */
    public Player getCurrentPlayer() { return players.get(currentPlayerIndex); }

    /**
     * Obtiene el número de turno actual.
     * @return número de turno
     */
    public int getCurrentTurn() { return currentTurn; }

    /**
     * Obtiene el nivel actual.
     * @return nivel actual
     */
    public int getCurrentLevel() { return currentLevel; }

    /**
     * Obtiene la lista de cartas en juego.
     * @return lista de cartas
     */
    public List<Card> getCards() { return cards; }

    /**
     * Obtiene la primera carta seleccionada en el turno.
     * @return primera carta seleccionada
     */
    public Card getFirstCard() { return firstCard; }

    /**
     * Obtiene la segunda carta seleccionada en el turno.
     * @return segunda carta seleccionada
     */
    public Card getSecondCard() { return secondCard; }

    /**
     * Obtiene la cantidad de fallos en el turno actual.
     * @return cantidad de fallos
     */
    public int getTurnFails() { return turnFails; }

    /**
     * Indica si el juego ha finalizado.
     * @return true si el juego terminó, false en caso contrario
     */
    public boolean isGameFinished() { return gameFinished; }

    /**
     * Obtiene el tiempo transcurrido en el turno actual.
     * @return tiempo en milisegundos
     */
    public long getCurrentTurnTime() {
        return System.currentTimeMillis() - turnStartTime;
    }

    /**
     * Obtiene el ranking final de los jugadores ordenado por puntaje.
     * @return lista de jugadores ordenada por puntaje descendente
     */
    public List<Player> getFinalRanking() {
        List<Player> ranking = new ArrayList<>(players);
        ranking.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));
        return ranking;
    }
}

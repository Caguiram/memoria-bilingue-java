package com.tmnei.controller;

import com.tmnei.model.Card;
import com.tmnei.model.GameModel;
import com.tmnei.view.GameView;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * Controlador principal del juego de memoria.
 * Gestiona la lógica entre el modelo y la vista, incluyendo la selección de cartas,
 * manejo de turnos, animaciones y actualización de la interfaz.
 */
public class GameController {
    /** Modelo del juego que contiene la lógica y el estado. */
    private GameModel model;
    /** Vista del juego encargada de la interfaz gráfica. */
    private final GameView view;
    /** Tarea asíncrona para manejar la comprobación de coincidencias. */
    private Task<Void> matchCheckTask;

    /**
     * Crea un nuevo controlador del juego.
     * @param model Modelo del juego.
     * @param view Vista del juego.
     */
    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        this.view.setController(this);
        view.setModel(model);
        updateView();
    }

    /**
     * Maneja el evento de clic sobre una carta.
     * @param card Carta seleccionada.
     */
    public void handleCardClick(Card card) {
        if (model.getFirstCard() != null && model.getSecondCard() != null) {
            return;
        }

        boolean wasMatch = model.selectCard(card);
        if (!wasMatch && model.getSecondCard() == null) {
            return;
        }

        view.playFlipAnimation(card, () -> {
            view.updateGameInfo();

            if (model.getFirstCard() != null && model.getSecondCard() != null) {
                if (wasMatch) {
                    handleMatch();
                } else {
                    handleMismatch();
                }
            }
        });
    }

    /**
     * Maneja la lógica cuando se encuentra una coincidencia.
     */
    private void handleMatch() {
        Card first = model.getFirstCard();
        Card second = model.getSecondCard();

        view.showMatchAnimation(first, second, () -> {
            Platform.runLater(() -> {
                model.resetSelectedCards();

                if (model.isLevelComplete()) {
                    model.nextTurn();
                    if (model.isGameFinished()) {
                        view.showGameResults();
                    } else {
                        Platform.runLater(() -> {
                            view.setModel(model);
                            view.updateGameBoard();
                            view.updateGameInfo();
                        });
                    }
                } else {
                    view.updateGameInfo();
                }
            });
        });
    }

    /**
     * Maneja la lógica cuando no hay coincidencia entre las cartas seleccionadas.
     */
    private void handleMismatch() {
        if (matchCheckTask != null && matchCheckTask.isRunning()) {
            matchCheckTask.cancel();
        }

        matchCheckTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(1500);
                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    Card first = model.getFirstCard();
                    Card second = model.getSecondCard();
                    model.resetSelectedCards();
                    view.updateCardDisplay(first);
                    view.updateCardDisplay(second);
                });
            }
        };

        new Thread(matchCheckTask).start();
    }

    /**
     * Inicia un nuevo juego con el número de jugadores especificado.
     * @param numPlayers Número de jugadores.
     */
    public void startNewGame(int numPlayers) {
        this.model = new GameModel(numPlayers);
        view.setModel(model);
        view.initializeGameBoard();
        updateView();
    }

    /**
     * Actualiza la información y el tablero de la vista.
     */
    private void updateView() {
        view.updateGameInfo();
        view.updateGameBoard();
    }

    /**
     * Obtiene el modelo actual del juego.
     * @return El modelo del juego.
     */
    public GameModel getModel() {
        return model;
    }
}

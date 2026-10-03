package com.tmnei.view;

import com.tmnei.controller.GameController;
import com.tmnei.model.Card;
import com.tmnei.model.GameModel;
import com.tmnei.model.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Vista principal del juego de memoria T.M.N.E.I.
 * Gestiona la interfaz gráfica, el tablero, la información del juego y las animaciones.
 */
public class GameView {
    /** Ventana principal de la aplicación. */
    private final Stage stage;
    /** Escena del menú principal. */
    private Scene menuScene;
    /** Escena del juego. */
    private Scene gameScene;
    /** Controlador del juego. */
    private GameController controller;
    /** Modelo del juego. */
    private GameModel model;

    /** GridPane que contiene las cartas del tablero. */
    private GridPane gameGrid;
    /** Etiqueta con el nombre del jugador actual. */
    private Label playerLabel;
    /** Etiqueta con el turno actual. */
    private Label turnLabel;
    /** Etiqueta con el nivel actual. */
    private Label levelLabel;
    /** Etiqueta con la puntuación. */
    private Label scoreLabel;
    /** Etiqueta con la cantidad de fallos. */
    private Label failsLabel;
    /** Etiqueta con el tiempo transcurrido. */
    private Label timeLabel;
    /** Contenedor de la información del juego. */
    private VBox gameInfo;

    /** Mapa de cartas a sus vistas correspondientes. */
    private final Map<Card, CardView> cardViews;
    /** Timeline para actualizar el tiempo en pantalla. */
    private Timeline timeUpdater;

    /** Familia de fuente utilizada en la interfaz. */
    private static final String FONT_FAMILY = "Arial Black";

    /**
     * Crea la vista del juego y muestra el menú principal.
     * @param stage ventana principal de la aplicación
     */
    public GameView(Stage stage) {
        this.stage = stage;
        this.cardViews = new HashMap<>();
        setupMenuScene();
        setupGameScene();
        stage.setScene(menuScene);
        stage.show();
    }

    /**
     * Configura la escena del menú principal.
     */
    private void setupMenuScene() {
        VBox menuLayout = new VBox(30);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setBackground(new Background(new BackgroundFill(Color.web("#6DE1D2"), null, null)));
        menuLayout.setPadding(new Insets(50));

        Label title = new Label("T.M.N.E.I.");
        title.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 48));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Juego de Memoria Bilingüe");
        subtitle.setFont(Font.font(FONT_FAMILY, FontWeight.NORMAL, 20));
        subtitle.setTextFill(Color.WHITE);

        VBox playerButtons = new VBox(15);
        playerButtons.setAlignment(Pos.CENTER);

        for (int i = 1; i <= 3; i++) {
            Button playerBtn = createMenuButton(i + " Jugador" + (i > 1 ? "es" : ""));
            final int players = i;
            playerBtn.setOnAction(e -> startGame(players));
            playerButtons.getChildren().add(playerBtn);
        }

        Button exitBtn = createMenuButton("Salir");
        exitBtn.setOnAction(e -> Platform.exit());

        menuLayout.getChildren().addAll(title, subtitle, playerButtons, exitBtn);
        menuScene = new Scene(menuLayout, 800, 600);
    }

    /**
     * Crea un botón estilizado para el menú principal.
     * @param text texto del botón
     * @return botón creado
     */
    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 18));
        btn.setPrefWidth(200);
        btn.setPrefHeight(50);
        btn.setStyle("-fx-background-color: #FFD63A; -fx-text-fill: #333333; -fx-background-radius: 10;");

        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #FFA955; -fx-text-fill: white; -fx-background-radius: 10;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #FFD63A; -fx-text-fill: #333333; -fx-background-radius: 10;"));

        return btn;
    }

    /**
     * Configura la escena principal del juego.
     */
    private void setupGameScene() {
        BorderPane gameLayout = new BorderPane();
        gameLayout.setBackground(new Background(new BackgroundFill(Color.web("#F75A5A"), null, null)));

        setupGameInfo();
        gameLayout.setTop(gameInfo);

        gameGrid = new GridPane();
        gameGrid.setAlignment(Pos.CENTER);
        gameGrid.setHgap(10);
        gameGrid.setVgap(10);
        gameGrid.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(gameGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: #F75A5A; -fx-background-color: #F75A5A;");

        gameLayout.setCenter(scrollPane);

        HBox bottomButtons = new HBox(20);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.setPadding(new Insets(20));

        Button menuBtn = createGameButton();
        menuBtn.setOnAction(e -> {
            stopTimeUpdater();
            stage.setScene(menuScene);
        });

        bottomButtons.getChildren().add(menuBtn);
        gameLayout.setBottom(bottomButtons);

        gameScene = new Scene(gameLayout, 800, 600);
        setupSceneResizing();
    }

    /**
     * Configura el panel de información del juego (jugador, turno, nivel, etc.).
     */
    private void setupGameInfo() {
        gameInfo = new VBox(10);
        gameInfo.setAlignment(Pos.CENTER);
        gameInfo.setPadding(new Insets(20));
        gameInfo.setBackground(new Background(new BackgroundFill(Color.web("#6DE1D2"), null, null)));

        HBox topInfo = new HBox(30);
        topInfo.setAlignment(Pos.CENTER);

        playerLabel = createInfoLabel("Jugador 1");
        turnLabel = createInfoLabel("Turno: 1");
        levelLabel = createInfoLabel("Nivel: 1");

        topInfo.getChildren().addAll(playerLabel, turnLabel, levelLabel);

        HBox bottomInfo = new HBox(30);
        bottomInfo.setAlignment(Pos.CENTER);

        scoreLabel = createInfoLabel("Puntos: 0");
        failsLabel = createInfoLabel("Fallos: 0");
        timeLabel = createInfoLabel("Tiempo: 0s");

        bottomInfo.getChildren().addAll(scoreLabel, failsLabel, timeLabel);

        gameInfo.getChildren().addAll(topInfo, bottomInfo);
    }

    /**
     * Crea una etiqueta estilizada para la información del juego.
     * @param text texto de la etiqueta
     * @return etiqueta creada
     */
    private Label createInfoLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16));
        label.setTextFill(Color.WHITE);
        return label;
    }

    /**
     * Crea un botón estilizado para la escena de juego.
     * @return botón creado
     */
    private Button createGameButton() {
        Button btn = new Button("Menú Principal");
        btn.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 14));
        btn.setPrefWidth(150);
        btn.setPrefHeight(40);
        btn.setStyle("-fx-background-color: #FFD63A; -fx-text-fill: #333333; -fx-background-radius: 10;");

        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #FFA955; -fx-text-fill: white; -fx-background-radius: 10;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #FFD63A; -fx-text-fill: #333333; -fx-background-radius: 10;"));

        return btn;
    }

    /**
     * Configura los listeners para redimensionar los elementos al cambiar el tamaño de la ventana.
     */
    private void setupSceneResizing() {
        gameScene.widthProperty().addListener((obs, oldVal, newVal) -> resizeGameElements());
        gameScene.heightProperty().addListener((obs, oldVal, newVal) -> resizeGameElements());
    }

    /**
     * Redimensiona las cartas y elementos del tablero según el tamaño de la ventana.
     */
    private void resizeGameElements() {
        if (model == null) return;

        double sceneWidth = gameScene.getWidth();
        double sceneHeight = gameScene.getHeight();

        double availableWidth = sceneWidth - 100;
        double availableHeight = sceneHeight - 300;

        int cols = model.getGridCols();
        int rows = model.getGridRows();

        double cardWidth = Math.min(120, (availableWidth - (cols - 1) * 10) / cols);
        double cardHeight = Math.min(160, (availableHeight - (rows - 1) * 10) / rows);

        for (CardView cardView : cardViews.values()) {
            cardView.resize(cardWidth, cardHeight);
        }
    }

    /**
     * Asigna el controlador del juego a la vista.
     * @param controller controlador del juego
     */
    public void setController(GameController controller) {
        this.controller = controller;
    }

    /**
     * Asigna el modelo del juego a la vista y actualiza la información.
     * @param model modelo del juego
     */
    public void setModel(GameModel model) {
        this.model = model;
        updateGameInfo();
    }

    /**
     * Inicia una nueva partida con el número de jugadores especificado.
     * @param numPlayers número de jugadores
     */
    private void startGame(int numPlayers) {
        if (controller != null) {
            controller.startNewGame(numPlayers);
        }
        stage.setScene(gameScene);
        startTimeUpdater();
    }

    /**
     * Inicializa el tablero de juego con las cartas del modelo.
     */
    public void initializeGameBoard() {
        if (model == null) {
            throw new IllegalStateException("El modelo no ha sido asignado a la vista.");
        }
        cardViews.clear();
        gameGrid.getChildren().clear();

        List<Card> cards = model.getCards();
        int cols = model.getGridCols();

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            CardView cardView = new CardView(card);

            cardView.setOnMouseClicked(e -> {
                if (controller != null) {
                    controller.handleCardClick(card);
                }
            });

            cardViews.put(card, cardView);
            gameGrid.add(cardView, i % cols, i / cols);
        }

        resizeGameElements();
    }

    /**
     * Actualiza completamente el tablero de juego.
     */
    public void updateGameBoard() {
        clearGameBoard();
        initializeGameBoard();
    }

    /**
     * Limpia el tablero de juego y las vistas de cartas.
     */
    private void clearGameBoard() {
        cardViews.clear();
        gameGrid.getChildren().clear();
    }

    /**
     * Actualiza la visualización de una carta específica.
     * @param card carta a actualizar
     */
    public void updateCardDisplay(Card card) {
        CardView cardView = cardViews.get(card);
        if (cardView != null) {
            cardView.updateDisplay();
        }
    }

    /**
     * Actualiza la información del juego en pantalla (jugador, turno, nivel, etc.).
     */
    public void updateGameInfo() {
        if (model == null) return;

        Player currentPlayer = model.getCurrentPlayer();
        playerLabel.setText(currentPlayer.getName());
        turnLabel.setText("Turno: " + model.getCurrentTurn() + "/12");
        levelLabel.setText("Nivel: " + model.getCurrentLevel());
        scoreLabel.setText("Puntos: " + currentPlayer.getScore());
        failsLabel.setText("Fallos: " + model.getTurnFails());

        long timeSeconds = model.getCurrentTurnTime() / 1000;
        timeLabel.setText("Tiempo: " + timeSeconds + "s");
    }

    /**
     * Muestra la animación de emparejamiento para dos cartas y ejecuta una acción al finalizar.
     * @param card1 primera carta emparejada
     * @param card2 segunda carta emparejada
     * @param onFinished acción a ejecutar al finalizar la animación
     */
    public void showMatchAnimation(Card card1, Card card2, Runnable onFinished) {
        CardView cv1 = cardViews.get(card1);
        CardView cv2 = cardViews.get(card2);

        if (cv1 != null && cv2 != null) {
            cv1.playMatchAnimation();
            cv2.playMatchAnimation();

            Timeline wait = new Timeline(new KeyFrame(Duration.seconds(1.2), e -> onFinished.run()));
            wait.play();
        } else {
            onFinished.run();
        }
    }

    /**
     * Ejecuta la animación de voltear una carta y ejecuta una acción al finalizar.
     * @param card carta a animar
     * @param onFinished acción a ejecutar al finalizar la animación
     */
    public void playFlipAnimation(Card card, Runnable onFinished) {
        CardView cv = cardViews.get(card);
        if (cv == null) {
            onFinished.run();
            return;
        }
        cv.playFlipAnimation(onFinished);
    }

    /**
     * Muestra la pantalla de resultados finales del juego.
     */
    public void showGameResults() {
        stopTimeUpdater();

        VBox resultsLayout = new VBox(30);
        resultsLayout.setAlignment(Pos.CENTER);
        resultsLayout.setBackground(new Background(new BackgroundFill(Color.web("#6DE1D2"), null, null)));
        resultsLayout.setPadding(new Insets(50));

        Label title = new Label("¡Juego Terminado!");
        title.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 36));
        title.setTextFill(Color.WHITE);

        VBox results = new VBox(15);
        results.setAlignment(Pos.CENTER);

        List<Player> ranking = model.getFinalRanking();

        if (ranking.size() == 1) {
            Player player = ranking.get(0);
            results.getChildren().addAll(
                    createResultLabel("Puntuación Final: " + player.getScore()),
                    createResultLabel("Tiempo Total: " + (player.getTotalTime() / 1000) + "s"),
                    createResultLabel("Fallos Totales: " + player.getTotalFails()),
                    createResultLabel("Parejas Encontradas: " + player.getMatches())
            );
        } else {
            Label rankingTitle = createResultLabel("Ranking Final:");
            results.getChildren().add(rankingTitle);

            for (int i = 0; i < ranking.size(); i++) {
                Player player = ranking.get(i);
                String position = (i + 1) + "º lugar - " + player.getName();
                String info = "Puntos: " + player.getScore() + " | Fallos: " + player.getTotalFails();

                results.getChildren().addAll(
                        createResultLabel(position),
                        createResultLabel(info)
                );
            }
        }

        Button menuBtn = createMenuButton("Menú Principal");
        menuBtn.setOnAction(e -> stage.setScene(menuScene));

        Button newGameBtn = createMenuButton("Nuevo Juego");
        newGameBtn.setOnAction(e -> {
            if (controller != null) {
                controller.startNewGame(model.getPlayers().size());
            }
            stage.setScene(gameScene);
            startTimeUpdater();
        });

        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(newGameBtn, menuBtn);

        resultsLayout.getChildren().addAll(title, results, buttons);

        Scene resultsScene = new Scene(resultsLayout, 800, 600);
        stage.setScene(resultsScene);
    }

    /**
     * Crea una etiqueta estilizada para mostrar resultados.
     * @param text texto de la etiqueta
     * @return etiqueta creada
     */
    private Label createResultLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 18));
        label.setTextFill(Color.WHITE);
        return label;
    }

    /**
     * Inicia el temporizador que actualiza el tiempo en pantalla.
     */
    private void startTimeUpdater() {
        timeUpdater = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateGameInfo()));
        timeUpdater.setCycleCount(Timeline.INDEFINITE);
        timeUpdater.play();
    }

    /**
     * Detiene el temporizador de actualización de tiempo.
     */
    private void stopTimeUpdater() {
        if (timeUpdater != null) {
            timeUpdater.stop();
        }
    }
}

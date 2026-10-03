package com.tmnei.view;

import com.tmnei.model.Card;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * Vista gráfica de una carta en el juego de memoria.
 * Muestra el estado visual de la carta y gestiona animaciones de interacción.
 */
public class CardView extends StackPane {
    /** Modelo de la carta asociada a esta vista. */
    private Card card;
    /** Fondo rectangular de la carta. */
    private Rectangle background;
    /** Etiqueta para mostrar el texto de la carta. */
    private Label text;
    /** Familia de fuente utilizada para el texto. */
    private static final String FONT_FAMILY = "Arial Black";

    /** Ancho original de la carta. */
    private double originalWidth = 100;
    /** Alto original de la carta. */
    private double originalHeight = 140;

    /**
     * Crea una nueva vista para la carta especificada.
     * @param card carta a representar
     */
    public CardView(Card card) {
        this.card = card;
        setupCard();
    }

    /**
     * Configura los componentes visuales y eventos de la carta.
     */
    private void setupCard() {
        background = new Rectangle(originalWidth, originalHeight);
        background.setArcWidth(10);
        background.setArcHeight(10);
        background.setStroke(Color.web("#333333"));
        background.setStrokeWidth(2);

        text = new Label();
        text.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 12));
        text.setAlignment(Pos.CENTER);
        text.setTextAlignment(TextAlignment.CENTER);

        // CLAVE: Configurar el Label para que no se trunque
        text.setWrapText(true);
        text.setEllipsisString(""); // Eliminar los "..."
        text.setMaxWidth(originalWidth - 10); // Dejar margen
        text.setMaxHeight(originalHeight - 10);
        text.setPrefWidth(originalWidth - 10);
        text.setPrefHeight(originalHeight - 10);
        text.setMinWidth(originalWidth - 10);
        text.setMinHeight(originalHeight - 10);

        // Configurar el StackPane
        this.setMaxWidth(originalWidth);
        this.setMaxHeight(originalHeight);
        this.setPrefWidth(originalWidth);
        this.setPrefHeight(originalHeight);
        this.setMinWidth(originalWidth);
        this.setMinHeight(originalHeight);

        getChildren().addAll(background, text);
        setAlignment(Pos.CENTER);

        updateDisplay();

        setOnMouseEntered(e -> {
            if (!card.isMatched() && !card.isFlipped()) {
                playHoverAnimation(true);
            }
        });

        setOnMouseExited(e -> {
            if (!card.isMatched() && !card.isFlipped()) {
                playHoverAnimation(false);
            }
        });
    }

    /**
     * Actualiza la apariencia visual de la carta según su estado.
     */
    public void updateDisplay() {
        if (card.isMatched()) {
            background.setFill(Color.web("#6DE1D2"));
            text.setText(card.getDisplayText());
            text.setTextFill(Color.WHITE);
        } else if (card.isFlipped()) {
            background.setFill(Color.web("#FFD63A"));
            text.setText(card.getDisplayText());
            text.setTextFill(Color.web("#333333"));
        } else {
            // Estado inicial: carta volteada hacia abajo
            background.setFill(Color.web("#FFA955"));
            text.setText("?");
            text.setTextFill(Color.BLACK); // Cambiar a negro para que sea consistente
        }
    }

    /**
     * Ejecuta la animación de voltear la carta.
     * @param onFinished acción a ejecutar al finalizar la animación
     */
    public void playFlipAnimation(Runnable onFinished) {
        // Usar ScaleTransition en lugar de RotateTransition para evitar problemas 3D
        ScaleTransition shrink = new ScaleTransition(Duration.millis(150), this);
        shrink.setFromX(1.0);
        shrink.setToX(0.0); // Encoge horizontalmente hasta desaparecer
        shrink.setFromY(1.0);
        shrink.setToY(1.0); // Mantiene la altura

        ScaleTransition expand = new ScaleTransition(Duration.millis(150), this);
        expand.setFromX(0.0);
        expand.setToX(1.0); // Expande horizontalmente desde 0
        expand.setFromY(1.0);
        expand.setToY(1.0);

        shrink.setOnFinished(e -> {
            updateDisplay(); // Cambiar contenido cuando está "invisible"
            expand.play();
        });

        expand.setOnFinished(e -> {
            // Asegurar que vuelva al tamaño exacto original
            forceResetSize();
            if (onFinished != null) onFinished.run();
        });

        shrink.play();
    }

    /**
     * Ejecuta la animación de hover (al pasar el mouse).
     * @param enter true si el mouse entra, false si sale
     */
    private void playHoverAnimation(boolean enter) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(200), this);

        if (enter) {
            scale.setToX(1.05);
            scale.setToY(1.05);
        } else {
            scale.setToX(1.0);
            scale.setToY(1.0);
        }

        scale.setOnFinished(e -> {
            if (!enter) {
                forceResetSize();
            }
        });

        scale.play();
    }

    /**
     * Ejecuta la animación cuando la carta es emparejada correctamente.
     */
    public void playMatchAnimation() {
        background.setFill(Color.web("#6DE1D2"));
        text.setTextFill(Color.WHITE);

        ScaleTransition scale = new ScaleTransition(Duration.millis(500), this);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(1.2);
        scale.setToY(1.2);
        scale.setCycleCount(2);
        scale.setAutoReverse(true);

        scale.setOnFinished(e -> forceResetSize());

        scale.play();
    }

    /**
     * Obtiene la carta asociada a esta vista.
     * @return carta asociada
     */
    public Card getCard() {
        return card;
    }

    /**
     * Redimensiona la carta y sus componentes internos.
     * @param width nuevo ancho
     * @param height nueva altura
     */
    public void resize(double width, double height) {
        // Actualizar las dimensiones guardadas
        originalWidth = width;
        originalHeight = height;

        // Redimensionar todos los componentes
        background.setWidth(width);
        background.setHeight(height);

        // Redimensionar el text con márgenes
        double textWidth = width - 10;
        double textHeight = height - 10;

        text.setMaxWidth(textWidth);
        text.setMaxHeight(textHeight);
        text.setPrefWidth(textWidth);
        text.setPrefHeight(textHeight);
        text.setMinWidth(textWidth);
        text.setMinHeight(textHeight);

        // Redimensionar el contenedor
        this.setMaxWidth(width);
        this.setMaxHeight(height);
        this.setPrefWidth(width);
        this.setPrefHeight(height);
        this.setMinWidth(width);
        this.setMinHeight(height);

        double fontSize = Math.min(width / 6, height / 8);
        text.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, fontSize));

        // Forzar actualización del layout
        requestLayout();
    }

    /**
     * Fuerza el reset completo del tamaño y transformaciones a las dimensiones originales.
     */
    private void forceResetSize() {
        // Limpiar todas las transformaciones
        getTransforms().clear();

        // Resetear todas las propiedades de transformación
        setScaleX(1.0);
        setScaleY(1.0);
        setScaleZ(1.0);
        setRotate(0.0);
        setTranslateX(0.0);
        setTranslateY(0.0);
        setTranslateZ(0.0);
        setOpacity(1.0);

        // Forzar el tamaño exacto
        resize(originalWidth, originalHeight);

        // Solicitar actualización del layout
        requestLayout();

        // Si el padre existe, solicitar también su layout
        if (getParent() != null) {
            getParent().requestLayout();
        }
    }
}

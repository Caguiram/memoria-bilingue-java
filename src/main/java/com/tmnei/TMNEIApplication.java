package com.tmnei;

import com.tmnei.controller.GameController;
import com.tmnei.model.GameModel;
import com.tmnei.view.GameView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación T.M.N.E.I. - Juego de Memoria Bilingüe.
 * Inicia la interfaz gráfica y configura el modelo, la vista y el controlador del juego.
 */
public class TMNEIApplication extends Application {

    /**
     * Método de inicio de JavaFX. Configura la ventana principal, el modelo, la vista y el controlador.
     *
     * @param primaryStage el escenario principal de la aplicación
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("T.M.N.E.I. - Juego de Memoria Bilingüe");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        GameView view = new GameView(primaryStage);
        GameModel model = new GameModel(1);
        GameController controller = new GameController(model, view);

        primaryStage.setOnCloseRequest(e -> {
            javafx.application.Platform.exit();
            System.exit(0);
        });
    }

    /**
     * Método principal. Lanza la aplicación JavaFX.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }
}

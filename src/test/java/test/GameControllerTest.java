package test;

import com.tmnei.controller.GameController;
import com.tmnei.model.Card;
import com.tmnei.model.GameModel;
import com.tmnei.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link GameController}.
 */
@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    /**
     * Vista simulada para pruebas.
     */
    @Mock
    private GameView mockView;

    /**
     * Controlador bajo prueba.
     */
    private GameController controller;

    /**
     * Modelo del juego utilizado en las pruebas.
     */
    private GameModel model;

    /**
     * Inicializa el modelo y el controlador antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        model = new GameModel(1);
        controller = new GameController(model, mockView);
    }

    /**
     * Verifica la inicialización correcta del controlador y la vinculación con la vista y el modelo.
     */
    @Test
    void testControllerInitialization() {
        verify(mockView).setController(controller);
        verify(mockView).setModel(model);
        assertEquals(model, controller.getModel());
    }

    /**
     * Verifica que al hacer clic en una carta válida se invoque la animación de volteo.
     */
    @Test
    void testHandleCardClickFirstCard() {
        Card card = model.getCards().get(0);

        controller.handleCardClick(card);

        // Verificar que se llame la animación de volteo
        verify(mockView).playFlipAnimation(eq(card), any(Runnable.class));
    }

    /**
     * Verifica que al hacer clic en una carta ya volteada no se realice ninguna acción.
     */
    @Test
    void testHandleCardClickAlreadyFlipped() {
        Card card = model.getCards().get(0);
        card.setFlipped(true);

        controller.handleCardClick(card);

        // Si la carta ya está volteada, no debería hacer nada
        verify(mockView, never()).playFlipAnimation(eq(card), any(Runnable.class));
    }

    /**
     * Verifica que al hacer clic en una carta ya emparejada no se realice ninguna acción.
     */
    @Test
    void testHandleCardClickMatched() {
        Card card = model.getCards().get(0);
        card.setMatched(true);

        controller.handleCardClick(card);

        // Si la carta ya está emparejada, no debería hacer nada
        verify(mockView, never()).playFlipAnimation(eq(card), any(Runnable.class));
    }

    /**
     * Verifica que al intentar seleccionar una tercera carta cuando ya hay dos seleccionadas no se realice ninguna acción.
     */
    @Test
    void testHandleCardClickWithBothCardsSelected() {
        // Simular que ya hay dos cartas seleccionadas
        Card card1 = model.getCards().get(0);
        Card card2 = model.getCards().get(1);
        Card card3 = model.getCards().get(2);

        // Seleccionar las primeras dos cartas
        model.selectCard(card1);
        model.selectCard(card2);

        // Intentar seleccionar una tercera carta
        controller.handleCardClick(card3);

        // No debería hacer nada porque ya hay dos cartas seleccionadas
        verify(mockView, never()).playFlipAnimation(eq(card3), any(Runnable.class));
    }
}

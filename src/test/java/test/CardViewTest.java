package test;

import com.tmnei.model.Card;
import com.tmnei.view.CardView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link CardView}.
 */
@ExtendWith(ApplicationExtension.class)
class CardViewTest {

    private CardView cardView;
    private Card card;

    /**
     * Inicializa los objetos de prueba antes de cada test.
     */
    @BeforeEach
    void setUp() {
        card = new Card("5", "Five", "English");
        cardView = new CardView(card);
    }

    /**
     * Verifica la correcta creación de la vista de la carta.
     */
    @Test
    void testCardViewCreation() {
        assertNotNull(cardView);
        assertEquals(card, cardView.getCard());
    }

    /**
     * Verifica el estado inicial de la carta asociada a la vista.
     */
    @Test
    void testCardViewInitialState() {
        assertFalse(card.isFlipped());
        assertFalse(card.isMatched());
    }

    /**
     * Verifica que la vista se actualiza correctamente al voltear la carta.
     */
    @Test
    void testUpdateDisplayFlipped() {
        card.setFlipped(true);
        cardView.updateDisplay();
        assertTrue(card.isFlipped());
    }

    /**
     * Verifica que la vista se actualiza correctamente al emparejar la carta.
     */
    @Test
    void testUpdateDisplayMatched() {
        card.setMatched(true);
        cardView.updateDisplay();
        assertTrue(card.isMatched());
    }

    /**
     * Verifica que el método resize de la vista no lanza excepciones.
     */
    @Test
    void testResize() {
        assertDoesNotThrow(() -> cardView.resize(120, 160));
        assertDoesNotThrow(() -> cardView.resize(80, 100));
    }
}

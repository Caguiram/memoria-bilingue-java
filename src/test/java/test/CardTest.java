package test;

import com.tmnei.model.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link Card}.
 */
class CardTest {

    private Card englishCard;
    private Card spanishCard;
    private Card differentCard;

    /**
     * Inicializa las cartas de prueba antes de cada test.
     */
    @BeforeEach
    void setUp() {
        englishCard = new Card("5","Five", "English");
        spanishCard = new Card("5","Cinco", "Spanish");
        differentCard = new Card("3","Three", "English");
    }

    /**
     * Verifica la correcta creación de una carta.
     */
    @Test
    void testCardCreation() {
        assertEquals("Five", englishCard.getNumber());
        assertEquals("English", englishCard.getLanguage());
        assertFalse(englishCard.isFlipped());
        assertFalse(englishCard.isMatched());
    }

    /**
     * Verifica el cambio de estado al voltear una carta.
     */
    @Test
    void testCardFlipping() {
        assertFalse(englishCard.isFlipped());
        englishCard.setFlipped(true);
        assertTrue(englishCard.isFlipped());
    }

    /**
     * Verifica el cambio de estado al emparejar una carta.
     */
    @Test
    void testCardMatching() {
        assertFalse(englishCard.isMatched());
        englishCard.setMatched(true);
        assertTrue(englishCard.isMatched());
    }

    /**
     * Verifica el texto mostrado por la carta.
     */
    @Test
    void testGetDisplayText() {
        assertEquals("Five", englishCard.getDisplayText());
        assertEquals("Cinco", spanishCard.getDisplayText());
    }

    /**
     * Verifica la lógica de coincidencia entre cartas de diferentes idiomas y números.
     */
    @Test
    void testMatches() {
        Card englishFive = new Card("5","Five", "English");
        Card spanishFive = new Card("5","Five", "Spanish");
        Card englishThree = new Card("3", "Three", "English");
        Card spanishThree = new Card("3", "Three", "Spanish");

        assertTrue(englishFive.matches(spanishFive));
        assertTrue(spanishFive.matches(englishFive));
        assertFalse(englishFive.matches(englishThree));
        assertFalse(englishFive.matches(englishFive));
        assertFalse(spanishFive.matches(spanishThree));
    }

    /**
     * Verifica que dos cartas con el mismo idioma no coincidan.
     */
    @Test
    void testMatchesSameLanguage() {
        Card anotherEnglishCard = new Card("5", "Five", "English");
        assertFalse(englishCard.matches(anotherEnglishCard));
    }

    /**
     * Verifica que cartas con diferentes números no coincidan.
     */
    @Test
    void testMatchesDifferentNumbers() {
        assertFalse(englishCard.matches(differentCard));
    }
}

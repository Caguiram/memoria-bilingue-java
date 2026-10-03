package test;

import com.tmnei.model.Card;
import com.tmnei.model.GameModel;
import com.tmnei.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Pruebas unitarias para la clase {@link GameModel}.
 */
class GameModelTest {

    /** Instancia de juego para pruebas de un solo jugador. */
    private GameModel singlePlayerGame;
    /** Instancia de juego para pruebas multijugador. */
    private GameModel multiPlayerGame;

    /**
     * Inicializa las instancias de juego antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        singlePlayerGame = new GameModel(1);
        multiPlayerGame = new GameModel(3);
    }

    /**
     * Verifica la inicialización correcta del juego.
     */
    @Test
    void testGameInitialization() {
        assertEquals(1, singlePlayerGame.getPlayers().size());
        assertEquals(3, multiPlayerGame.getPlayers().size());
        assertEquals(1, singlePlayerGame.getCurrentTurn());
        assertEquals(1, singlePlayerGame.getCurrentLevel());
        assertFalse(singlePlayerGame.isGameFinished());
    }

    /**
     * Verifica que los nombres de los jugadores sean correctos en multijugador.
     */
    @Test
    void testPlayerNames() {
        List<Player> players = multiPlayerGame.getPlayers();
        assertEquals("Jugador 1", players.get(0).getName());
        assertEquals("Jugador 2", players.get(1).getName());
        assertEquals("Jugador 3", players.get(2).getName());
    }

    /**
     * Verifica que el jugador actual sea el esperado al iniciar el juego.
     */
    @Test
    void testCurrentPlayer() {
        assertEquals("Jugador 1", singlePlayerGame.getCurrentPlayer().getName());
        assertEquals("Jugador 1", multiPlayerGame.getCurrentPlayer().getName());
    }

    /**
     * Verifica el tamaño de la cuadrícula en el nivel 1.
     */
    @Test
    void testGridSizeLevel1() {
        assertEquals(10, singlePlayerGame.getGridSize());
        assertEquals(5, singlePlayerGame.getGridCols());
        assertEquals(2, singlePlayerGame.getGridRows());
    }

    /**
     * Verifica la generación de cartas y su distribución por idioma.
     */
    @Test
    void testCardsGeneration() {
        List<Card> cards = singlePlayerGame.getCards();
        assertEquals(10, cards.size());

        long englishCards = cards.stream().filter(card -> card.getLanguage().equals("English")).count();
        long spanishCards = cards.stream().filter(card -> card.getLanguage().equals("Spanish")).count();

        assertEquals(5, englishCards);
        assertEquals(5, spanishCards);
    }

    /**
     * Verifica la selección de cartas y el estado de las cartas seleccionadas.
     */
    @Test
    void testCardSelection() {
        List<Card> cards = singlePlayerGame.getCards();
        Card firstCard = cards.get(0);
        Card secondCard = cards.get(1);

        assertTrue(singlePlayerGame.selectCard(firstCard));
        assertTrue(firstCard.isFlipped());
        assertEquals(firstCard, singlePlayerGame.getFirstCard());

        singlePlayerGame.selectCard(secondCard);
        assertTrue(secondCard.isFlipped());
        assertEquals(secondCard, singlePlayerGame.getSecondCard());
    }

    /**
     * Verifica que no se pueda seleccionar una carta que ya está volteada.
     */
    @Test
    void testCardSelectionAlreadyFlipped() {
        List<Card> cards = singlePlayerGame.getCards();
        Card card = cards.get(0);

        singlePlayerGame.selectCard(card);
        assertFalse(singlePlayerGame.selectCard(card));
    }

    /**
     * Verifica que no se pueda seleccionar una carta que ya está emparejada.
     */
    @Test
    void testCardSelectionMatched() {
        List<Card> cards = singlePlayerGame.getCards();
        Card card = cards.get(0);
        card.setMatched(true);

        assertFalse(singlePlayerGame.selectCard(card));
    }

    /**
     * Verifica el reseteo de las cartas seleccionadas.
     */
    @Test
    void testResetSelectedCards() {
        List<Card> cards = singlePlayerGame.getCards();
        Card firstCard = cards.get(0);
        Card secondCard = cards.get(1);

        singlePlayerGame.selectCard(firstCard);
        singlePlayerGame.selectCard(secondCard);

        singlePlayerGame.resetSelectedCards();

        assertFalse(firstCard.isFlipped());
        assertFalse(secondCard.isFlipped());
        assertNull(singlePlayerGame.getFirstCard());
        assertNull(singlePlayerGame.getSecondCard());
    }

    /**
     * Verifica si el nivel se considera completo cuando todas las cartas están emparejadas.
     */
    @Test
    void testIsLevelComplete() {
        assertFalse(singlePlayerGame.isLevelComplete());

        List<Card> cards = singlePlayerGame.getCards();
        cards.forEach(card -> card.setMatched(true));

        assertTrue(singlePlayerGame.isLevelComplete());
    }

    /**
     * Verifica la progresión del turno.
     */
    @Test
    void testTurnProgression() {
        int initialTurn = singlePlayerGame.getCurrentTurn();
        singlePlayerGame.nextTurn();
        assertEquals(initialTurn + 1, singlePlayerGame.getCurrentTurn());
    }

    /**
     * Verifica la progresión de nivel y el tamaño de la cuadrícula en cada nivel.
     */
    @Test
    void testLevelProgression() {
        GameModel game = new GameModel(1);
        assertEquals(1, game.getCurrentLevel());

        for (int i = 0; i < 4; i++) {
            game.nextTurn();
        }
        assertEquals(2, game.getCurrentLevel());
        assertEquals(20, game.getGridSize());

        for (int i = 0; i < 4; i++) {
            game.nextTurn();
        }
        assertEquals(3, game.getCurrentLevel());
        assertEquals(30, game.getGridSize());
    }

    /**
     * Verifica si el juego se marca como finalizado tras suficientes turnos.
     */
    @Test
    void testGameFinished() {
        GameModel game = new GameModel(1);
        assertFalse(game.isGameFinished());

        for (int i = 0; i < 12; i++) {
            game.nextTurn();
        }
        assertTrue(game.isGameFinished());
    }

    /**
     * Verifica la rotación de turnos entre jugadores en modo multijugador.
     */
    @Test
    void testMultiPlayerTurnRotation() {
        assertEquals(0, multiPlayerGame.getCurrentPlayer().getName().indexOf("Jugador 1"));

        multiPlayerGame.nextTurn();
        assertEquals(0, multiPlayerGame.getCurrentPlayer().getName().indexOf("Jugador 2"));

        multiPlayerGame.nextTurn();
        assertEquals(0, multiPlayerGame.getCurrentPlayer().getName().indexOf("Jugador 3"));

        multiPlayerGame.nextTurn();
        assertEquals(0, multiPlayerGame.getCurrentPlayer().getName().indexOf("Jugador 1"));
    }

    /**
     * Verifica que en modo un jugador no haya rotación de turnos.
     */
    @Test
    void testSinglePlayerNoTurnRotation() {
        Player initialPlayer = singlePlayerGame.getCurrentPlayer();
        singlePlayerGame.nextTurn();
        assertEquals(initialPlayer, singlePlayerGame.getCurrentPlayer());
    }

    /**
     * Verifica que el tiempo del turno actual sea mayor o igual a cero.
     */
    @Test
    void testGetCurrentTurnTime() {
        assertTrue(singlePlayerGame.getCurrentTurnTime() >= 0);
    }

    /**
     * Verifica el ranking final de los jugadores según su puntaje.
     */
    @Test
    void testFinalRanking() {
        List<Player> players = multiPlayerGame.getPlayers();
        players.get(0).addScore(100);
        players.get(1).addScore(150);
        players.get(2).addScore(75);

        List<Player> ranking = multiPlayerGame.getFinalRanking();

        assertEquals(150, ranking.get(0).getScore());
        assertEquals(100, ranking.get(1).getScore());
        assertEquals(75, ranking.get(2).getScore());
    }
}

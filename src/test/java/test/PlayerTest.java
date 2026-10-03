package test;

import com.tmnei.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link Player}.
 */
class PlayerTest {

    /**
     * Instancia de jugador utilizada en las pruebas.
     */
    private Player player;

    /**
     * Inicializa el jugador antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        player = new Player("Test Player");
    }

    /**
     * Verifica la creación correcta del jugador y sus valores iniciales.
     */
    @Test
    void testPlayerCreation() {
        assertEquals("Test Player", player.getName());
        assertEquals(0, player.getScore());
        assertEquals(0, player.getTotalFails());
        assertEquals(0, player.getTotalTime());
        assertEquals(0, player.getMatches());
    }

    /**
     * Verifica que el puntaje se incremente correctamente.
     */
    @Test
    void testAddScore() {
        player.addScore(50);
        assertEquals(50, player.getScore());

        player.addScore(30);
        assertEquals(80, player.getScore());
    }

    /**
     * Verifica que el contador de fallos se incremente correctamente.
     */
    @Test
    void testAddFail() {
        player.addFail();
        assertEquals(1, player.getTotalFails());

        player.addFail();
        assertEquals(2, player.getTotalFails());
    }

    /**
     * Verifica que el tiempo total se incremente correctamente.
     */
    @Test
    void testAddTime() {
        player.addTime(1500);
        assertEquals(1500, player.getTotalTime());

        player.addTime(2000);
        assertEquals(3500, player.getTotalTime());
    }

    /**
     * Verifica que el contador de coincidencias se incremente correctamente.
     */
    @Test
    void testAddMatch() {
        player.addMatch();
        assertEquals(1, player.getMatches());

        player.addMatch();
        assertEquals(2, player.getMatches());
    }

    /**
     * Verifica el funcionamiento combinado de los métodos de la clase Player.
     */
    @Test
    void testMultipleOperations() {
        player.addScore(100);
        player.addFail();
        player.addTime(5000);
        player.addMatch();

        assertEquals(100, player.getScore());
        assertEquals(1, player.getTotalFails());
        assertEquals(5000, player.getTotalTime());
        assertEquals(1, player.getMatches());
    }
}

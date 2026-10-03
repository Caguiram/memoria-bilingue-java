package test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


@Suite
@SelectClasses({
        CardTest.class,
        PlayerTest.class,
        GameModelTest.class,
        GameControllerTest.class,
        CardViewTest.class
})

/**
 * Clase de prueba que agrupa todas las pruebas unitarias del proyecto.
 * Utiliza JUnit 5 para ejecutar las pruebas de forma organizada.
 */
public class TestSuite {
}

# Memoria bilingüe — T.M.N.E.I.

Juego de escritorio que permite emparejar números en español e inglés. Organiza la aplicación con el patrón **Modelo–Vista–Controlador** y separa las reglas del juego de la interfaz JavaFX.

**Tecnologías:** Java 17, JavaFX 21, Maven, JUnit 5, Mockito y TestFX.

## Qué incluye

- Cartas equivalentes en español e inglés.
- Tres niveles, con 5, 10 y 15 parejas.
- Puntuación según el tiempo y los fallos.
- Seguimiento de jugadores, turnos y clasificación final.
- Pruebas del modelo, cartas, jugadores y componentes de la aplicación.

## Ejecutar

Necesitas **JDK 17 o superior** y **Maven 3.9 o superior**.

```bash
mvn compile
mvn javafx:run
```

Maven descarga las dependencias de JavaFX; no necesitas copiar un SDK al repositorio.

## Pruebas

```bash
mvn test
```

Las pruebas de interfaz necesitan una sesión gráfica. Para ejecutar solo las pruebas del modelo:

```bash
mvn -Dtest=CardTest,PlayerTest,GameModelTest test
```

## Estructura

```text
src/main/java/com/tmnei/
  model/        Cartas, jugadores y reglas del juego
  view/         Ventanas y representación visual
  controller/   Coordinación de acciones
src/test/java/  Pruebas de la entrega
pom.xml         Dependencias y ejecución con Maven
```

## Contexto académico

Versión organizada de un proyecto universitario conservado por Cristian Aguirre Ramirez. Se mantienen los archivos fuente y las pruebas originales; la configuración Maven se ajustó para ejecutarlo sin un módulo Java inexistente y con el JDK 17 disponible. Esta publicación no atribuye exclusivamente a una persona las contribuciones de posibles compañeros de equipo.

package com.tmnei.model;

import java.util.Objects;

/**
 * Representa una carta en el juego de memoria.
 * Cada carta tiene un identificador, un número, un idioma, y estados de volteo y coincidencia.
 */
public class Card {
    /** Identificador único de la carta. */
    private final String number;
    /** Idioma de la carta. */
    private final String language;
    /** Indica si la carta está volteada. */
    private boolean isFlipped;
    /** Indica si la carta ya fue emparejada. */
    private boolean isMatched;
    /** Identificador único para emparejamiento. */
    private final String id;

    /**
     * Crea una nueva carta.
     * @param id Identificador único de la carta.
     * @param number Número o valor de la carta.
     * @param language Idioma de la carta.
     */
    public Card(String id, String number, String language) {
        this.id = id;
        this.number = number;
        this.language = language;
        this.isFlipped = false;
        this.isMatched = false;
    }

    /**
     * Obtiene el identificador de la carta.
     * @return id de la carta.
     */
    public String getId() {return id;}

    /**
     * Obtiene el número o valor de la carta.
     * @return número de la carta.
     */
    public String getNumber() { return number; }

    /**
     * Obtiene el idioma de la carta.
     * @return idioma de la carta.
     */
    public String getLanguage() { return language; }

    /**
     * Indica si la carta está volteada.
     * @return true si está volteada, false en caso contrario.
     */
    public boolean isFlipped() { return isFlipped; }

    /**
     * Indica si la carta ya fue emparejada.
     * @return true si está emparejada, false en caso contrario.
     */
    public boolean isMatched() { return isMatched; }

    /**
     * Establece el estado de volteo de la carta.
     * @param flipped true para voltear la carta, false para desvoltearla.
     */
    public void setFlipped(boolean flipped) { this.isFlipped = flipped; }

    /**
     * Establece el estado de coincidencia de la carta.
     * @param matched true si la carta fue emparejada, false en caso contrario.
     */
    public void setMatched(boolean matched) { this.isMatched = matched; }

    /**
     * Obtiene el texto a mostrar en la carta.
     * @return texto de la carta.
     */
    public String getDisplayText() {
        return number;
    }

    /**
     * Compara si dos cartas son iguales según su id y lenguaje.
     * @param obj Objeto a comparar.
     * @return true si son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Card other)) return false;
        return id.equals(other.id)
                && language.equals(other.language);
    }

    /**
     * Calcula el hashCode de la carta.
     * @return hashCode basado en id y lenguaje.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, language);
    }

    /**
     * Determina si dos cartas forman un par válido.
     * @param other Otra carta a comparar.
     * @return true si tienen el mismo id y diferente idioma.
     */
    public boolean matches(Card other) {
        return this.id.equals(other.id) && !this.language.equals(other.language);
    }
}

package de.platen.clapsesy.htmlrenderer.core;

import de.platen.syntaxparser.Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParsererzeugungTest {

    @Test
    void testErzeugeParser() {
        Parsererzeugung parsererzeugung = new Parsererzeugung();
        assertInstanceOf(Parser.class, parsererzeugung.erzeugeParser());
    }
}

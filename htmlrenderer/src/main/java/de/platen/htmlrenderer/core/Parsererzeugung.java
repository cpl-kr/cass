package de.platen.htmlrenderer.core;

import de.platen.syntaxparser.Parser;
import de.platen.syntaxparser.grammatik.Grammatik;
import de.platen.syntaxparser.grammatik.GrammatikAufbau;
import de.platen.syntaxparser.grammatik.GrammatikLesen;

public class Parsererzeugung {

    private static final String GRAMMATIK = "S { Version Masse Daten }\n" +
            "Version { WortVersion Versionsangabe }\n" +
            "Masse { Breite Höhe }\n" +
            "Masse { Höhe Breite }\n" +
            "Breite { WortBreite Zahl }\n" +
            "Höhe { WortHöhe Zahl }\n" +
            "Daten { WortHtml Base64 }\n" +
            "Daten { WortUrl Adresse }\n" +
            "WortVersion \"Version\"\n" +
            "Versionsangabe <[0-9]+.[]0-9]+.[0-9]+>\n" +
            "WortBreite \"Breite\"\n" +
            "WortHöhe \"Höhe\"\n" +
            "WortHtml \"Html\"\n" +
            "WortUrl \"Url\"\n" +
            "Zahl [09]\n" +
            "Base64 <[A-Za-z0-9+=]+>\n" +
            "Adresse <(http|https)://[-\\w]+(\\.\\w[-\\w]*)+>\n";

    public Parser erzeugeParser() {
        final GrammatikAufbau grammatikAufbau = new GrammatikAufbau();
        GrammatikLesen grammatikLesen = new GrammatikLesen(grammatikAufbau);
        for (char c : GRAMMATIK.toCharArray()) {
            grammatikLesen.verarbeiteZeichen(c);
        }
        grammatikLesen.checkGrammatik();
        Grammatik grammatik = grammatikLesen.getGrammatik();
        return new Parser(grammatik);
    }
}

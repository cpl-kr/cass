package de.platen.lib.zahl;

import de.platen.lib.objekt.Objekt;

public class GanzzahlPositiv extends Objekt<Integer> {

    private static String GANZZAHL_POSITIV_FEHLER_PARAMETER = "Parameter darf nicht kleiner 1 sein.";

    public GanzzahlPositiv(final Integer wert) {
        super(wert);
        if (wert < 1) {
            throw new IllegalArgumentException(GANZZAHL_POSITIV_FEHLER_PARAMETER);
        }
    }
}

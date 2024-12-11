package de.platen.lib.objekt;

public abstract class Objekt<T> {

    private static String FEHLER_PARAMETER = "Parameter darf nicht null sein.";

    private final T wert;

    public Objekt(final T wert) {
        this.wert = wert;
        if (wert == null) {
            throw new IllegalArgumentException(FEHLER_PARAMETER);
        }
    }

    public T get() {
        return wert;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (wert == null ? 0 : wert.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Objekt other = (Objekt) obj;
        if (wert == null) {
            if (other.wert != null) {
                return false;
            }
        } else if (!wert.equals(other.wert)) {
            return false;
        }
        return true;
    }

    public static void checkNull(final Object object, final RuntimeException e) {
        if (object == null) {
            throw e;
        }
    }
}

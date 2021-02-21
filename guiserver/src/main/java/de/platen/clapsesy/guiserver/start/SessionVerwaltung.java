package de.platen.clapsesy.guiserver.start;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SessionVerwaltung {

    private final Set<UUID> angefordert = new HashSet<>();
    private final Set<UUID> benutzt = new HashSet<>();
    private final Map<UUID, String> pfade = new HashMap<>();

    public UUID requestSession() {
        final UUID session = UUID.randomUUID();
        angefordert.add(session);
        return session;
    }

    public boolean useSession(final UUID session) {
        if (angefordert.contains(session)) {
            benutzt.add(session);
            angefordert.remove(session);
            return true;
        }
        return false;
    }

    public boolean destroySession(final UUID session) {
        if (benutzt.contains(session)) {
            benutzt.remove(session);
            return true;
        }
        return false;
    }

    public boolean isUsed(final UUID session) {
        return benutzt.contains(session);
    }

    public boolean setPath(final UUID sessionId, final String path) {
        if (benutzt.contains(sessionId) || angefordert.contains(sessionId)) {
            pfade.put(sessionId, path);
            return true;
        }
        return false;
    }

    public String getPath(final UUID sessionId) {
        return pfade.get(sessionId);
    }
}

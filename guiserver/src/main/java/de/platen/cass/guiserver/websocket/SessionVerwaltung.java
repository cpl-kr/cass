package de.platen.cass.guiserver.websocket;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SessionVerwaltung {

	private final Set<UUID> angefordert = new HashSet<>();
	private final Set<UUID> benutzt = new HashSet<>();
	
	public UUID requestSession() {
		UUID session = UUID.randomUUID();
		this.angefordert.add(session);
		return session;
	}
	
	public boolean useSession(UUID session) {
		if (this.angefordert.contains(session)) {
			this.benutzt.add(session);
			this.angefordert.remove(session);
			return true;
		}
		return false;
	}
	
	public boolean destroySession(UUID session) {
		if (this.benutzt.contains(session)) {
			this.benutzt.remove(session);
			return true;
		}
		return false;
	}
	
	public boolean isUsed (UUID session) {
        return this.benutzt.contains(session);
    }
}

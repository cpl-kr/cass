package de.platen.cass.guiserver.exception;

public class GuiServerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GuiServerException(Throwable throwable) {
		super(throwable);
	}
	
	public GuiServerException(String message) {
		super(message);
	}
}

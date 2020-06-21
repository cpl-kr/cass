package de.platen.clapsesy.guiserver.frontend.guielement;

public class TextInputHtmlInput {

	private final HtmlInput htmlActive;
	private final HtmlInput htmlInactive;
	private final HtmlInput htmlFocus;
	private final HtmlInput htmlMouseOverActive;
	private final HtmlInput htmlMouseOverFocus;

	public TextInputHtmlInput(HtmlInput htmlActive, HtmlInput htmlInactive, HtmlInput htmlFocus, HtmlInput htmlMouseOverActive, HtmlInput htmlMouseOverFocus) {
		this.htmlActive = htmlActive;
		this.htmlInactive = htmlInactive;
		this.htmlFocus = htmlFocus;
		this.htmlMouseOverActive = htmlMouseOverActive;
		this.htmlMouseOverFocus = htmlMouseOverFocus;
	}

	public HtmlInput getHtmlActive() {
		return htmlActive;
	}

	public HtmlInput getHtmlInactive() {
		return htmlInactive;
	}

	public HtmlInput getHtmlFocus() {
		return htmlFocus;
	}

	public HtmlInput getHtmlMouseOverActive() {
		return htmlMouseOverActive;
	}

	public HtmlInput getHtmlMouseOverFocus() {
		return htmlMouseOverFocus;
	}
}

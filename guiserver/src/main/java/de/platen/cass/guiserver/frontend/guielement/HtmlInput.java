package de.platen.cass.guiserver.frontend.guielement;

public class HtmlInput {

	private final String htmlBefore;
	private final String htmlAfter;
	
	public HtmlInput(String htmlBefore, String htmlAfter) {
		this.htmlBefore = htmlBefore;
		this.htmlAfter = htmlAfter;
	}

	public String getHtmlBefore() {
		return htmlBefore;
	}

	public String getHtmlAfter() {
		return htmlAfter;
	}
}

package com.trinary.parse.xml;

public class XmlTextElement extends XmlElement {
	protected String text;
	
	public XmlTextElement(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public String toString() {
		return "TEXT: " + text;
	}
}

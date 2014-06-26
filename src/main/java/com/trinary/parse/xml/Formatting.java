package com.trinary.parse.xml;

import java.util.Map;

public class Formatting {
	protected FormattingType type;
	protected Map<String, String> attributes;
	
	public Formatting(FormattingType type) {
		this.type = type;
	}
	
	public Formatting(FormattingType type, Map<String, String> attributes) {
		this.type = type;
		this.attributes = attributes;
	}

	public FormattingType getType() {
		return type;
	}

	public void setType(FormattingType type) {
		this.type = type;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	
	public String toString() {
		return String.format("%s => %s", type, attributes);
	}
}

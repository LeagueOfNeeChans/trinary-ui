package com.trinary.parse.xml;

import java.util.HashMap;
import java.util.Map;

public class Formatting {
	protected FormattingType type;
	protected HashMap<String, String> attributes = new HashMap<String, String>();
	
	public Formatting(FormattingType type) {
		this.type = type;
	}
	
	public Formatting(FormattingType type, Map<String, String> attributes) {
		this.type = type;
		this.attributes.putAll(attributes);
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
		this.attributes.putAll(attributes);
	}
	
	public String toString() {
		return String.format("%s => %s", type, attributes);
	}
}

package com.trinary.parse.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlClosingTag extends XmlTag {
	protected Pattern closeTagRegex = Pattern.compile("<\\/(\\S+)>");
	
	public XmlClosingTag(String tagData) {
		Matcher match = closeTagRegex.matcher(tagData);
		
		if (match.matches()) {
			label = match.group(1);
		}
	}
	
	public String toString() {
		return "LABEL: " + label;
	}
}

package com.trinary.parse.xml;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlTagElement extends XmlTag {
	protected HashMap<String, String> attributes = new HashMap<String, String>();
	protected XmlBlock children = null;
	
	protected static Pattern openTagRegex = Pattern.compile("<(\\S+)\\s*(.*)>");
	protected static Pattern elementRegex = Pattern.compile("(\\w+)\\s*=\\s*['\"]*([^'\"]+)['\"]*");
	protected static Pattern attributeRegex = Pattern.compile("([a-zA-Z0-9\\-]+)\\s*=\\s*['\"]([a-zA-Z0-9\\- ,;:\\.#]+)['\"]");
		
	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(HashMap<String, String> attributes) {
		this.attributes = attributes;
	}

	public XmlBlock getChildren() {
		return children;
	}

	public void setChildren(XmlBlock children) {
		this.children = children;
	}

	public XmlTagElement(String tagString) {
		Matcher match = openTagRegex.matcher(tagString);
		
		if (match.matches() && match.groupCount() > 0) {
			this.label = match.group(1);
			if (match.group(2) != null && match.group(2) != "") {
				Matcher attrMatch = attributeRegex.matcher(match.group(2));
				
				while (attrMatch.find()) {
					attributes.put(attrMatch.group(1), attrMatch.group(2));
				}
			}
		}
	}
	
	public String toString() {
		String ret = String.format(""
				+ "LABEL: %s\n"
				+ "ATTRIBUTES:", label);
		
		for (Entry<String, String> attribute : attributes.entrySet()) {
			ret += String.format("\n\t%s => %s", attribute.getKey(), attribute.getValue());
		}
		
		ret += "\nCHILDREN:";
		
		if (children != null) {
			for (XmlElement element : children.getElements()) {
				ret += String.format("\n%s", element);
			}
		}
		
		ret += "\n";
		
		return ret;
	}
}

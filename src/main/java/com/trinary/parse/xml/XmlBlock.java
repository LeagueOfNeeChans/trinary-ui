package com.trinary.parse.xml;

import java.util.ArrayList;

public class XmlBlock {
	protected ArrayList<XmlElement> elements = new ArrayList<XmlElement>();
		
	protected enum ParserState {
		START,
		DONE,
		READING_TEXT,
		READING_OPEN_TAG,
		READING_CLOSE_TAG,
		VERIFY_CLOSE_TAG,
		SEARCH_CLOSE_TAG
	}
	
	public ArrayList<XmlElement> getElements() {
		return elements;
	}

	public XmlBlock(String xmlData) {
		String data = "";
		String textData = "";
		String openTagData = "";
		XmlTagElement openTag = null;
		String closeTagData = "";
		ParserState state = ParserState.START;
		
		for (Character b : xmlData.toCharArray()) {
			switch(state) {
			case START:
				data = "";
				openTagData = "";
				closeTagData = "";
				
				if (b == '<') {
					openTagData += b;
					state = ParserState.READING_OPEN_TAG;
				} else if (b != '<' && b != '>') {
					textData += b;
					state = ParserState.READING_TEXT;
				}
				break;
			case READING_TEXT:
				if (b == '<') {
					XmlTextElement textElement = new XmlTextElement(textData);
					elements.add(textElement);
					
					textData = "";
					
					openTagData += b;
					state = ParserState.READING_OPEN_TAG;
				} else {
					textData += b;
				}
				break;
			case READING_OPEN_TAG:
				if (b == '>') {
					openTagData += b;
					openTag = new XmlTagElement(openTagData);
					
					state = ParserState.SEARCH_CLOSE_TAG;
				} else if (b == '/') {
					openTag = new XmlTagElement(openTagData + ">");
					
					elements.add(openTag);
					
					state = ParserState.START;
				} else {
					openTagData += b;
				}
				
				break;
			case SEARCH_CLOSE_TAG:
				closeTagData = "";
				
				if (b == '<') {
					closeTagData += b;
					
					state = ParserState.VERIFY_CLOSE_TAG;
				} else {
					data += b;
				}
				break;
			case VERIFY_CLOSE_TAG:
				closeTagData += b;
				if (b != '/') {
					data += closeTagData;
					
					state = ParserState.SEARCH_CLOSE_TAG;
				} else {
					state = ParserState.READING_CLOSE_TAG;
				}
				break;
			case READING_CLOSE_TAG:
				closeTagData += b;
				if (b == '>') {
					XmlClosingTag closeTag = new XmlClosingTag(closeTagData);
					if (!closeTag.getLabel().equals(openTag.getLabel())) {
						data += closeTagData;
						state = ParserState.SEARCH_CLOSE_TAG;
					} else {
						openTag.setChildren(new XmlBlock(data));
						
						elements.add(openTag);
						
						state = ParserState.START;
					}
				}
				break;
			default:
				break;
			}
		}
		
		switch(state) {
		case READING_TEXT:
			XmlTextElement textElement = new XmlTextElement(textData);
			elements.add(textElement);
			break;
		case READING_OPEN_TAG:
			break;
		case SEARCH_CLOSE_TAG:
			break;
		case VERIFY_CLOSE_TAG:
			break;
		case READING_CLOSE_TAG:
			break;
		default:
			break;
		}
	}
	
	public String toString() {
		String ret = "";
		for (XmlElement element : elements) {
			ret += element;
		}
		
		return ret;
	}
}
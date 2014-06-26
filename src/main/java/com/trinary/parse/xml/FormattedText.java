package com.trinary.parse.xml;

import java.util.Stack;

public class FormattedText {
	private XmlBlock block;
	private Stack<Formatting> formatStack = new Stack<>();
	
	FormattedText(String text) {
		block = new XmlBlock(text);
	}
	
	public static String getTabs(int tabs) {
		String ret = "";
		
		for (int i = 0; i < tabs; i++) {
			ret += "\t";
		}
		
		return ret;
	}
	
	private void renderElement(XmlElement element) {
		if (element instanceof XmlTextElement) {
			System.out.println(String.format("RENDERING '%s' with formats %s", element.getText(), formatStack));
		} else if (element instanceof XmlTagElement) {
			XmlTagElement tag = (XmlTagElement)element;
			switch(tag.getLabel()) {
			case "img":
				System.out.println("DISPLAYING PICTURE INLINE: " + tag.getAttributes().get("src"));
				return;
			case "i":
				formatStack.push(new Formatting(FormattingType.ITALIC, tag.getAttributes()));
				break;
			case "b":
				formatStack.push(new Formatting(FormattingType.BOLD, tag.getAttributes()));
				break;
			case "span":
			default:
				formatStack.push(new Formatting(FormattingType.SPAN, tag.getAttributes()));
				break;
			}
			
			if (tag.getChildren() != null) {
				for(XmlElement child : tag.getChildren().getElements()) {
					renderElement(child);
				}
			}
			
			formatStack.pop();
		}
	}
	
	public void render() {
		for (XmlElement element : block.elements) {
			renderElement(element);
		}
	}
}

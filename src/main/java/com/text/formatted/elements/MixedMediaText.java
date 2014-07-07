/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.text.formatted.elements;

import com.tcg.generator.layouts.elements.GenericElement;
import com.trinary.parse.xml.Formatting;
import com.trinary.parse.xml.FormattingType;
import com.trinary.parse.xml.XmlBlock;
import com.trinary.parse.xml.XmlElement;
import com.trinary.parse.xml.XmlTagElement;
import com.trinary.parse.xml.XmlTextElement;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author mmain
 */
public class MixedMediaText {
    protected ArrayList<MarkupElement> elements = new ArrayList<>();
    protected int iter = 0;
    protected int width = 0;
    protected Stack<Formatting> formatStack = new Stack<Formatting>();
    
    public MixedMediaText() {}
    
    public MixedMediaText(String text) {
    	XmlBlock block = new XmlBlock(text);
    	for (XmlElement element : block.getElements()) {
			renderElement(element);
		}
    }
    
    public MixedMediaText substring(Integer endIndex) {
		Integer t = 0;
		MixedMediaText modded = new MixedMediaText();
		for (MarkupElement element : elements) {
			Integer length = 1;
			
			if (element instanceof TextInsert) {
				length = element.getText().length();
			}
			
			t += length;
			
			if (element instanceof TextInsert) {
				String text = "";
				if (t >= endIndex) {
					text = element.getText().substring(0, element.getText().length() - (t - endIndex));
					break;
				} else {
					text = element.getText();
				}
				 
				modded.addElement(new TextInsert(text, ((TextInsert)element).getFormatStack()));
			} else if (element instanceof ImageInsert) {
				modded.addElement(element);
			}
		}
		
		return modded;
    }
    
    public Integer length() {
    	Integer t = 0;
		for (MarkupElement element : elements) {
			Integer length = 1;
			
			if (element instanceof TextInsert) {
				length = element.getText().length();
			}
			
			t += length;
		}
		
		return t;
    }
    
    public void addElement(MarkupElement me) {
        elements.add(me);
    }
    
    public MarkupElement popElement() {
        if (elements.isEmpty()) {
            return null;
        }
        return elements.remove(elements.size() - 1);
    }
    
    public MarkupElement peekElement() {
        if (elements.isEmpty()) {
            return null;
        }
        return elements.get(elements.size() - 1);
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public String toString() {
        String s = "";
        for (MarkupElement element : elements) {
            s += element;
        }
        return s;
    }
    
    public MarkupElement next() {
        if (iter < elements.size()) {
            return elements.get(iter++);
        } else {
            return null;
        }
    }
    
    public void reset() {
        iter = 0;
    }
    
    public void drawTo(BufferedImage bi, GenericElement layout) {
        
    }
    
	private void renderElement(XmlElement element) {
		if (element instanceof XmlTextElement) {
			for (String text : element.getText().split(" ")) {
				elements.add(new TextInsert(text, formatStack));
			}
		} else if (element instanceof XmlTagElement) {
			XmlTagElement tag = (XmlTagElement)element;
			switch(tag.getLabel()) {
			case "img":
				elements.add(new ImageInsert(tag.getAttributes().get("src")));
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
	
	public ArrayList<MarkupElement> getElements() {
		return elements;
	}
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.text.formatted.elements;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Stack;

import com.trinary.parse.xml.Formatting;

/**
 *
 * @author mmain
 */
public class TextInsert extends MarkupElement {
    protected String text;
    protected Stack<Formatting> formatStack = new Stack<Formatting>();
    
    TextInsert(String text, Stack<Formatting> formatStack, Point origin, Point relative, int width, int height) {
        super(origin, relative, width, height);
    	this.text = text;
    	
    	if (!formatStack.isEmpty()) {
	    	Formatting thisElement = formatStack.peek();
	    	
	    	if (thisElement.getAttributes().get("id") != null) {
	    		this.mark(thisElement.getAttributes().get("id"));
	    	}
    	}
        
        for (Formatting formatting : formatStack) {
        	this.formatStack.add(formatting);
        }
    }
    
    TextInsert(String text, Stack<Formatting> formatStack) {
    	this.text = text;
    	
    	if (!formatStack.isEmpty()) {
	    	Formatting thisElement = formatStack.peek();
	    	
	    	if (thisElement.getAttributes().get("id") != null) {
	    		this.mark(thisElement.getAttributes().get("id"));
	    	}
    	}
        
        for (Formatting formatting : formatStack) {
        	this.formatStack.add(formatting);
        }
    }
    
    public Color getFontColor() {
    	Color color = null;
    	
    	for (Formatting formatting : formatStack) {
    		String hexString = formatting.getAttributes().get("color");
    		
    		if (hexString == null) {
    			return null;
    		}
    		
    		
    		if (hexString.toCharArray()[0] == '#') {
    			hexString = hexString.substring(1);
    			
    			if (hexString.length() != 6) {
    				return null;
    			}
    		
	    		String red   = hexString.substring(0, 2);
	    		String green = hexString.substring(2, 4);
	    		String blue  = hexString.substring(4, 6);
	    		
	    		Integer r = Integer.decode("0x" + red);
	    		Integer g = Integer.decode("0x" + green);
	    		Integer b = Integer.decode("0x" + blue);
	    		
	    		color = new Color(r, g, b);
    		}
    	}
    	
    	return color;
    }
    
    public int getFontWeight() {
    	int font = 0;
    	
    	for (Formatting formatting : formatStack) {
    		switch (formatting.getType()) {
			case BOLD:
				font += Font.BOLD;
				break;
			case ITALIC:
				font += Font.ITALIC;
				break;
			case SPAN:
				break;
			default:
				break;
    		}
    	}
    	
    	return font;
    }

    public void drawTo(BufferedImage bi, int x, int y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public String getText() {
        return text;
    }
    
    public String toString() {
        return "[WORD: " + text + "]";
    }

	public Stack<Formatting> getFormatStack() {
		return formatStack;
	}
}

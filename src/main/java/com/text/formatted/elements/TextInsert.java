/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.text.formatted.elements;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Stack;

import com.trinary.parse.xml.Formatting;

/**
 *
 * @author mmain
 */
public class TextInsert extends MarkupElement {
    protected String text;
    protected Stack<Formatting> formatStack = new Stack<Formatting>();
    
    TextInsert(String text, Stack<Formatting> formatStack) {
        this.text = text;
        
        for (Formatting formatting : formatStack) {
        	this.formatStack.add(formatting);
        }
    }
    
    public int getFontWeight() {
    	int font = 0;
    	
    	for (Formatting formatting : formatStack) {
    		System.out.println("FONT WEIGHT: " + formatting);
    		
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
    	
    	System.out.println("FONT ======> " + font);
    	
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

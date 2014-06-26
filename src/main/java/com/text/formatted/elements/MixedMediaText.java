/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.text.formatted.elements;

import com.tcg.generator.layouts.ElementLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author mmain
 */
public class MixedMediaText {
    protected ArrayList<MarkupElement> elements = new ArrayList<>();
    protected int iter = 0;
    protected int width = 0;
    
    public MixedMediaText() {}
    
    public MixedMediaText(String text) {
        try {
            int start = 0, end = 0;
            
            while (text.length() > 0 && start >= 0 && end >= 0) {
                start = text.indexOf("[");
                end = text.indexOf("]");
                
                if (start == -1 || end == -1) {
                    String[] words = text.split(" ");
                    for (String word : words) {
                        elements.add(new TextInsert(word));
                    }
                    break;
                }
                
                if (start > 0) {
                    String s = text.substring(0, start);
                    String[] words = s.split(" ");
                    if (words.length == 0) {
                        elements.add(new TextInsert(" "));
                    } else {
                        for (String word : words) {
                            elements.add(new TextInsert(word));
                        }
                    }
                }
                
                String s = text.substring(start + 1, end);
                
                //Parse the insert element
                String[] kp = s.split(":");
                String[] words = kp[1].split(" ");
                
                for (String word : words) {
                    switch(kp[0]) {
                        case "icon":
                            elements.add(new ImageInsert(word));
                            break;
                        case "bold":
                            elements.add(new BoldTextInsert(word));
                            break;
                        case "italic":
                            elements.add(new ItalicTextInsert(word));
                            break;
                        default:
                            elements.add(new TextInsert(word));
                            break;
                    }
                }
                
                text = text.substring(end + 1);
            }
        } catch (Exception e) {
            elements = new ArrayList<>();
            System.out.println("INVALID FORMAT!");
        }
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
    
    public void drawTo(BufferedImage bi, ElementLayout layout) {
        
    }
}
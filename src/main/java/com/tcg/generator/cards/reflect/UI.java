/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcg.generator.cards.reflect;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.tcg.generator.layouts.elements.ElementMapping;
import com.tcg.generator.layouts.elements.GenericElement;
import com.tcg.generator.ui.GenericUI;
import com.text.formatted.elements.MixedMediaText;
import com.text.formatted.elements.MixedMediaTextBlock;

/**
 *
 * @author mmain
 */
public class UI extends GenericUI {
	protected HashMap<String, MixedMediaTextBlock> cache = new HashMap<String, MixedMediaTextBlock>();
	
	public void clearCache(String key) {
		this.cache.remove(key);
	}
	
    @SuppressWarnings("unchecked")
	@Override
    public final BufferedImage render() {
        BufferedImage bi = new BufferedImage(uiLayout.getWidth(), uiLayout.getHeight(), BufferedImage.TYPE_INT_ARGB);
        drawLayer(background, bi);
        
        uiLayout.sort();
        
        ArrayList<MixedMediaText> mmtl;
        for (GenericElement element: uiLayout.getElements()) {
            System.out.println("Mapping element: " + element.getName());
            
            // Test to see if layer should be drawn
            if (!element.shouldDraw(this)) {
                continue;
            }
            
            drawLayer(element, bi);
            
            if (element.getMappings() != null) {
            	ArrayList<String> lines = new ArrayList<>();
            	if (!cache.containsKey(element.getName())) {
	                for (Entry<String, ElementMapping> entry : element.getMappings().entrySet()) {
	                    Field field;
	                    try {
	                        field = this.getClass().getField(entry.getKey());
	                    } catch (NoSuchFieldException e) {
	                        System.out.println("\tUnable to find field with name \"" + entry.getKey() + "\"");
	                        continue;
	                    }
	
	                    ElementMapping mapping =  entry.getValue();
	
	                    System.out.println("\tFIELD: " +  field.getName() + " (" + field.getType().getSimpleName() + ")");
	                    if (mapping.getLabel() != null) {
	                        System.out.println("\t\tLABEL: " + mapping.getLabel());
	                    }
	                    if (mapping.getType() != null) {
	                        System.out.println("\t\tTYPE:  " + mapping.getType());
	                    }
	                    
	                    ArrayList<String> list;
	                    Integer intValue;
	                    Float floatValue;
	                    String stringValue;
	                    
	                    String line = "";
	                    Object value = new Object();
	                    if (mapping.getLabel() != null) {
	                        line += mapping.getLabel() + ": ";
	                    }
	                    
	                    try {
	                        value = field.get(this);
	                    } catch (IllegalArgumentException | IllegalAccessException ex) {
	                        System.out.println("\t\tUnable to retrieve value");
	                    }
	                    
	                    if (value != null) { 
	                        switch (field.getType().getSimpleName()) {
	                            case "String":
	                                stringValue = (String)value;
	                                line += stringValue.toString();
	                                lines.add(line);
	                                break;
	                            case "Integer":
	                                intValue = (Integer)value;
	                                line += intValue.toString();
	                                lines.add(line);
	                                break;
	                            case "Float":
	                                floatValue = (Float)value;
	                                line += floatValue.toString();
	                                lines.add(line);
	                                break;
	                            case "ArrayList":
	                                list = (ArrayList<String>)value;
	                                for (String aLine : list) {
	                                    lines.add(aLine);
	                                }
	                                break;
	                            default:
	                                System.out.println("\t\tInvalid field type!");
	                                break;
	                        }
	                    }
	                }
            	}
 
                switch(element.getType()) {
                case "animated-text-box":
                	if (!cache.containsKey(element.getName())) {
                		mmtl = splitAndFitMixedText(bi, lines, element);
                		cache.put(element.getName(), new MixedMediaTextBlock(mmtl, true));
                	}
                	
                	System.out.println("DRAWING " + element.getName());
                	drawMixedMediaText(bi, cache.get(element.getName()).getLines(), element);
                    break;
                case "text-box":
                	if (!cache.containsKey(element.getName())) {
                		mmtl = splitAndFitMixedText(bi, lines, element);
                		cache.put(element.getName(), new MixedMediaTextBlock(mmtl, false));
                	}
                	drawMixedMediaText(bi, cache.get(element.getName()).getLines(), element);
                    break;
                case "table":
                    drawTableCols(bi, lines, 2, element);
                    break;
                case "image":
                case "layer":
                    break;
                default:
                    break;
                }
            }
        }
        
        return bi;
    }
}

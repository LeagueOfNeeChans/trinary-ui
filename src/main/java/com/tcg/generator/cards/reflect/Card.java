/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcg.generator.cards.reflect;

import com.tcg.generator.cards.GenericCard;
import com.tcg.generator.layouts.ElementLayout;
import com.tcg.generator.layouts.ElementMapping;
import com.text.formatted.elements.MixedMediaText;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map.Entry;
import javax.imageio.ImageIO;

/**
 *
 * @author mmain
 */
public class Card extends GenericCard {
    @Override
    public final void draw(String outputDirectory) {
        BufferedImage bi = new BufferedImage(cardLayout.getWidth(), cardLayout.getHeight(), BufferedImage.TYPE_INT_ARGB);
        drawLayer(artwork, bi);
        
        ArrayList<MixedMediaText> mmtl;
        for (ElementLayout element: cardLayout.getElements()) {
            System.out.println("Mapping element: " + element.getName());
            
            // Test to see if layer should be drawn
            if (!element.shouldDraw(this)) {
                continue;
            }
            
            drawLayer(element, bi);
            if (element.getMappings() != null) {
                ArrayList<String> lines = new ArrayList<>();
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
                System.out.println("\tLINES: " + lines);
                
                switch(element.getType()) {
                    case "text-box":
                        mmtl = splitAndFitMixedText(bi, lines, element);
                        drawMixedMediaText(bi, mmtl, element);
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
        
        try {
            ImageIO.write(bi, "png", new File(outputDirectory + cardName + ".png"));
        } catch (IOException ex) {
            System.out.println("Unable to write image!");
        }
    }
}

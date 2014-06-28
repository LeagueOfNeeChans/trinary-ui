/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcg.generator.layouts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.awt.Color;

/**
 *
 * @author mmain
 */
public class UIFont {
    private String family;
    private String weight;
    private int size;
    private UIColor color;
    
    @JsonCreator
    public UIFont(
            @JsonProperty("family") String family,
            @JsonProperty("weight") String weight,
            @JsonProperty("size")   int size,
            @JsonProperty("color")  UIColor color
            ) {
        this.family = family;
        this.weight = weight;
        this.size = size;
        this.color = color;
    }
    
    public String getFamily() {
        return family;
    }
    
    public String getWeight() {
        return weight;
    }
    
    public int getSize() {
        return size;
    }
    
    public Color getColor() {
        return color.getColor();
    }
    
    public String toString() {
        return  "\t\tfamily: " + family + "\n" +
                "\t\tweight: " + weight + "\n" +
                "\t\tsize:   " + size + "\n";
    }
}

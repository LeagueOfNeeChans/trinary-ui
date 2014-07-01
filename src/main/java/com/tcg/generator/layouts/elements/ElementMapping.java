/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcg.generator.layouts.elements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Michael
 */
public class ElementMapping {
    protected String label;
    protected String type;
    
    @JsonCreator
    public ElementMapping(
            @JsonProperty("label")        String label,
            @JsonProperty("type")         String type
            ) {
        this.label = label;
        this.type = type == null ? "text" : type;
    }
        
    public String getLabel() {
        return label;
    }
    
    public String getType() {
        return type;
    }
}

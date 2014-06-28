/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcg.generator.layouts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

/**
 *
 * @author mmain
 */
public class ElementLayout {
    private String name, type;
    private String inherits;
    private Integer x, y;
    private Integer width, height;
    private Integer marginX, marginY;
    private Boolean wordWrap;
    private String align, vAlign;
    private UIFont font;
    private Double transparency;
    private UILayer layer;
    private Integer columns;
    private Condition condition;
    private LinkedHashMap<String, ElementMapping> mappings;
    
    @JsonCreator
    public ElementLayout(
            @JsonProperty("name")         String name,
            @JsonProperty("type")         String type,
            @JsonProperty("inherits")     String inherits,
            @JsonProperty("x")            Integer x,
            @JsonProperty("y")            Integer y,
            @JsonProperty("width")        Integer width,
            @JsonProperty("height")       Integer height,
            @JsonProperty("margin-x")     Integer marginX,
            @JsonProperty("margin-y")     Integer marginY,
            @JsonProperty("word-wrap")    Boolean wordWrap,
            @JsonProperty("h-align")      String align,
            @JsonProperty("v-align")      String vAlign,
            @JsonProperty("columns")      Integer columns,
            @JsonProperty("transparency") Double transparency,
            @JsonProperty("font")         UIFont font,
            @JsonProperty("layer")        UILayer layer,
            @JsonProperty("mappings")     LinkedHashMap<String, ElementMapping> mappings,
            @JsonProperty("condition")    String condition
            ) {
        this.name = name;
        this.type = type;
        this.inherits = inherits;
        
        if (this.inherits != null) {
            
        }
        
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.marginX = marginX;
        this.marginY = marginY;
        this.wordWrap = wordWrap;
        this.align = align;
        this.vAlign = vAlign;
        this.columns = columns;
        this.font = font;
        this.transparency = transparency;
        this.mappings = mappings;
        this.layer = layer;
        
        if (marginX == null) {
            this.marginX = 0;
        }
        if (marginY == null) {
            this.marginY = 0;
        }

        if (wordWrap == null) {
            this.wordWrap = true;
        }
        
        if (align == null) {
            this.align = "left";
        }
        
        if (vAlign == null) {
            this.vAlign = "top";
        }
        
        if (layer != null) {
            if (this.height == null) {
                this.height = this.layer.getHeight();
            }
            if (this.width == null) {
                this.width = this.layer.getWidth();
            }
        }
        if (condition != null) {
            this.condition = new Condition(condition);
        }
    }
    
    public String getName() {
        return name;
    }
        
    public String getType() {
        return type;
    }
    
    public Integer getX() {
        return x;
    }
    
    public Integer getY() {
        return y;
    }
        
    public Integer getWidth() {
        return width;
    }
    
    public Integer getHeight() {
        return height;
    }
    
    public Integer getMarginX() {
        return marginX;
    }
    
    public Integer getMarginY() {
        return marginY;
    }
    
    public Integer getColumns() {
        return columns;
    }
    
    public Double getTransparency() {
        return transparency;
    }
    
    public UIFont getCardFont() {
        return font;
    }
    
    public Condition getCondition() {
        return condition;
    }
    
    public Boolean hasWordWrap() {
        return wordWrap;
    }
    
    public String getAlign() {
        return align;
    }
    
    public String getVAlign() {
        return vAlign;
    }
    
    public boolean shouldDraw(LinkedHashMap<String, Object> object) {
        if (condition != null) {
            return condition.test(object);
        } else {
            return true;
        }
    }
    
    public boolean shouldDraw(Object object) {
        if (condition != null) {
            return condition.test(object);
        } else {
            return true;
        }
    }
    
    public Font getFont() {
        if (font == null) {
            return null;
        }
        
        int fontWeight = 0;
        
        switch (font.getWeight()) {
            case "bold":
                fontWeight = Font.BOLD;
                break;
            case "italic":
                fontWeight = Font.ITALIC;
                break;
            case "bold-italic":
                fontWeight = Font.BOLD | Font.ITALIC;
                break;
            default:
            case "normal":
                fontWeight = Font.PLAIN;
                break;
        }
        
        return new Font(font.getFamily(), fontWeight, font.getSize());
    }
    
    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
    
    public BufferedImage getLayer() {
        if (layer == null) {
            return null;
        }
        return layer.getImage();
    }
    
    public LinkedHashMap<String, ElementMapping> getMappings() {
        return mappings;
    }
    
    public ElementMapping getMapping(String fieldName) {
        return mappings.get(fieldName);
    }
    
    public String toString() {
        return  "\tname:         " + name + "\n" +
                "\ttype:         " + type + "\n" +
                "\tx:            " + x + "\n" +
                "\ty:            " + y + "\n" +
                "\twidth:        " + width + "\n" +
                "\theight:       " + height +  "\n" +
                "\tmargin-x:     " + marginX + "\n" +
                "\tmargin-y:     " + marginY + "\n" +
                "\tcolumns:      " + columns + "\n" +
                "\ttransparency: " + transparency + "\n" +
                "\tfont:         " + "\n" + font;
    }    
}
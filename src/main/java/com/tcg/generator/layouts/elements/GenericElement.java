/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcg.generator.layouts.elements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcg.generator.layouts.Condition;
import com.tcg.generator.layouts.UIColor;
import com.tcg.generator.layouts.UIFont;
import com.tcg.generator.layouts.UILayer;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

/**
 *
 * @author mmain
 */
public class GenericElement implements Comparable<GenericElement> {
	protected String name = "";
	protected String type = "";
	protected String inherits = "";
	protected Integer x = 0, y = 0;
	protected Integer width = 0, height = 0;
	protected Integer marginX = 0, marginY = 0;
	protected Boolean wordWrap = false;
	protected String align = "left", vAlign = "top";
	protected UIFont font = new UIFont("Optima", "normal", 12, new UIColor(0, 0, 0));
	protected Double transparency = 1.0;
	protected UILayer layer = null;
	protected Integer columns = null;
	protected Condition condition = null;
	protected LinkedHashMap<String, ElementMapping> mappings = new LinkedHashMap<>();
	protected Integer zIndex = 0;
	
	public GenericElement() {
		
	}
    
    @JsonCreator
    public GenericElement(
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
    
    public UIFont getUiFont() {
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
    
    public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setInherits(String inherits) {
		this.inherits = inherits;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public void setMarginX(Integer marginX) {
		this.marginX = marginX;
	}

	public void setMarginY(Integer marginY) {
		this.marginY = marginY;
	}

	public void setWordWrap(Boolean wordWrap) {
		this.wordWrap = wordWrap;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public void setvAlign(String vAlign) {
		this.vAlign = vAlign;
	}

	public void setFont(UIFont font) {
		this.font = font;
	}

	public void setTransparency(Double transparency) {
		this.transparency = transparency;
	}

	public void setLayer(UILayer layer) {
		this.layer = layer;
	}

	public void setColumns(Integer columns) {
		this.columns = columns;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public void setMappings(LinkedHashMap<String, ElementMapping> mappings) {
		this.mappings = mappings;
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
                fontWeight = Font.BOLD + Font.ITALIC;
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
    
    public void setMapping(String fieldName, ElementMapping mapping) {
    	mappings.put(fieldName, mapping);
    }
    
    public void setMapping(String fieldName) {
    	mappings.put(fieldName, new ElementMapping(null, null));
    }

    public Integer getzIndex() {
		return zIndex;
	}

	public void setzIndex(Integer zIndex) {
		this.zIndex = zIndex;
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

	@Override
	public int compareTo(GenericElement o) {
		if (this.zIndex < o.zIndex) {
			return -1;
		} else if (this.zIndex == o.zIndex) {
			return 0;
		} else {
			return 1;
		}
	}    
}
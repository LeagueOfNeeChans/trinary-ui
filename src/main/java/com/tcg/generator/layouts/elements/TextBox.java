package com.tcg.generator.layouts.elements;

import java.util.LinkedHashMap;

import com.tcg.generator.layouts.UIFont;
import com.tcg.generator.layouts.UILayer;

public class TextBox extends GenericElement {
	public TextBox() {
		super();
		this.type = "text-box";
		this.wordWrap = true;
	}
	
	public TextBox(Integer x, Integer y, Integer width, Integer height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public TextBox(String name, String inherits, Integer x,
			Integer y, Integer width, Integer height, Integer marginX,
			Integer marginY, Boolean wordWrap, String align, String vAlign,
			Integer columns, Double transparency, UIFont font, UILayer layer,
			LinkedHashMap<String, ElementMapping> mappings, String condition) {
		
		super(name, "text-box", inherits, x, y, width, height, marginX, marginY, wordWrap,
				align, vAlign, columns, transparency, font, layer, mappings, condition);
	}
}

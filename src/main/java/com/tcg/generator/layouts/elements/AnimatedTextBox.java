package com.tcg.generator.layouts.elements;

import java.util.LinkedHashMap;

import com.tcg.generator.layouts.UIFont;
import com.tcg.generator.layouts.UILayer;

public class AnimatedTextBox extends TextBox {
	protected Integer drawingIndex = 0;
	
	public AnimatedTextBox() {
		super();
		this.type = "animated-text-box";
		this.wordWrap = true;
	}
	
	public AnimatedTextBox(Integer x, Integer y, Integer width, Integer height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public AnimatedTextBox(String name, String inherits, Integer x,
			Integer y, Integer width, Integer height, Integer marginX,
			Integer marginY, Boolean wordWrap, String align, String vAlign,
			Integer columns, Double transparency, UIFont font, UILayer layer,
			LinkedHashMap<String, ElementMapping> mappings, String condition) {
		
		super(name, "animated-text-box", x, y, width, height, marginX, marginY, wordWrap,
				align, vAlign, columns, transparency, font, layer, mappings, condition);
	}

	public Integer getDrawingIndex() {
		return drawingIndex;
	}

	public void setDrawingIndex(Integer drawingIndex) {
		this.drawingIndex = drawingIndex;
	}
	
	public void incrementDrawingIndex() {
		this.drawingIndex++;
	}
	
	public void resetDrawingIndex() {
		this.drawingIndex = 0;
	}
}

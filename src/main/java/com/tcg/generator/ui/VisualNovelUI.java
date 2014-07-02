package com.tcg.generator.ui;

import com.tcg.generator.cards.reflect.UI;
import com.tcg.generator.layouts.Resource;
import com.tcg.generator.layouts.UIColor;
import com.tcg.generator.layouts.UIFont;
import com.tcg.generator.layouts.UILayer;
import com.tcg.generator.layouts.UILayout;
import com.tcg.generator.layouts.elements.GenericElement;
import com.tcg.generator.layouts.elements.TextBox;

public class VisualNovelUI extends UI {
	public String text;
	
	public VisualNovelUI(Integer width, Integer height) {
		TextBox textBox = new TextBox();
		GenericElement scene = new GenericElement();
		
		Integer boxWidth  = new Integer((int) Math.round(0.8 * width));
		Integer boxHeight = new Integer((int) Math.round(0.25 * height));
		Integer boxX = (width - boxWidth) / 2;
		
		Resource sword = new Resource("resources/vanguard/sword.png", "sword");
		
		scene.setzIndex(0);
		scene.setX(0);
		scene.setY(0);
		scene.setWidth(width);
		scene.setHeight(height);
		scene.setLayer(new UILayer("art/miku.jpg", width, height));
		scene.setTransparency(1.0);
		
		textBox.setName("text");
		textBox.setType("text-box");
		textBox.setzIndex(2);
		textBox.setX(boxX);
		textBox.setY(400);
		textBox.setMarginX(20);
		textBox.setMarginY(20);
		textBox.setWidth(boxWidth);
		textBox.setHeight(boxHeight);
		//textBox.setLayer(new UILayer(new UIColor(255, 255, 255), boxWidth, boxHeight));
		textBox.setLayer(new UILayer("layers/vanguard/effect-box.png", boxWidth, boxHeight));
		textBox.setTransparency(0.50);
		textBox.setFont(new UIFont("Optima", "normal", 12, new UIColor(0, 0, 0)));
		textBox.setMapping("text");
		
		uiLayout = new UILayout();
		uiLayout.setWidth(width);
		uiLayout.setHeight(width);
		uiLayout.addElement(scene);
		uiLayout.addElement(textBox);
		uiLayout.addResource(sword);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
}

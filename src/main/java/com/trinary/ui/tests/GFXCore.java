package com.trinary.ui.tests;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import com.trinary.ui.config.ConfigHolder;
import com.trinary.ui.config.ResourceStore;
import com.trinary.ui.elements.FormattedTextElement;
import com.trinary.ui.elements.GraphicElement;
import com.trinary.ui.elements.ResourceElement;

public class GFXCore {
	private JFrame container;
	private Canvas canvas;
	private Boolean running = true;
	
	BufferStrategy strategy;
	
	public GFXCore() {		
		container = new JFrame("League of Nee-chans");
		container.setPreferredSize(new Dimension(800, 600));
		
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(800, 600));
		canvas.setVisible(true);
		
		container.setResizable(false);
		container.add(canvas);
		container.pack();
		container.setVisible(true);
		
		canvas.createBufferStrategy(2);
		strategy = canvas.getBufferStrategy();
	}
	
	public void mainLoop() {
		ConfigHolder.setConfig("rootDirectory", "src/main/resources/");
		ResourceStore.addFolder("resources/vanguard");
		ResourceStore.addFolder("vn/actors");
		
		GraphicElement container = new GraphicElement(0, 0, 800, 600, "vn/scenes/hallway.jpg");
		container.setzIndex(0);
		
		ResourceElement actor1 = new ResourceElement();
		actor1.changeResource("actor_neutral");
		actor1.move(400, 0);
		actor1.setzIndex(1);
		
		FormattedTextElement textBox = new FormattedTextElement((800 - (int)(.8 * 800))/2, 350, (int)(.8 * 800), 200);
		textBox.setLayer("layers/vanguard/effect-box.png");
		textBox.setMarginX(20);
		textBox.setMarginY(20);
		textBox.setTransparency(0.75f);
		textBox.setzIndex(2);
		textBox.setText("This is <b>test</b> text.  This <i><b>is</b></i> also a test.  "
				+ "My anus is bleeding please help me why won't <b>anyone</b> help me oh "
				+ "gosh there's <i color='#FF0000'>blood</i> everywhere help me why isn't anyone helping me "
				+ "oh god I can see Jesus's eyes!  Why have you forsaken me oh lord?  Give me a "
				+ "<img src='sword' /> I will do it myself!", true);
		
		container.addChild(textBox);
		container.addChild(actor1);
		
		while (running) {
			Graphics2D g = (Graphics2D)strategy.getDrawGraphics();
			g.drawImage(container.render(), null, 0, 0);
			
			strategy.show();
			try { Thread.sleep(10); } catch (Exception e) {}
		}
	}
}
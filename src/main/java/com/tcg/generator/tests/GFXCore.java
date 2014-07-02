package com.tcg.generator.tests;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import com.tcg.generator.config.ConfigHolder;
import com.tcg.generator.ui.VisualNovelUI;

public class GFXCore {
	private JFrame container;
	private Canvas canvas;
	private Boolean running = true;
	private Integer x = 0;
	private Integer y = 0;
	
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
		VisualNovelUI ui = new VisualNovelUI(800, 600);
		ui.setText("This is <b>test</b> text.  This <i><b>is</b></i> also a test.  "
				+ "My anus is bleeding please help me why won't <b>anyone</b> help me oh "
				+ "gosh there's <i color='#FF0000'>blood</i> everywhere help me why isn't anyone helping me "
				+ "oh god I can see Jesus's eyes!  Why have you forsaken me oh lord?  Give me a "
				+ "<img src='sword' /> I will do it myself!");
		
		while (running) {
			Graphics2D g = (Graphics2D)strategy.getDrawGraphics();
			g.drawImage(ui.render(), null, x, y);
			
			strategy.show();
			try { Thread.sleep(20); } catch (Exception e) {}
		}
	}
}
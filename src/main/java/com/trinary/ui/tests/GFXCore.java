package com.trinary.ui.tests;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.trinary.ui.config.ConfigHolder;
import com.trinary.ui.config.ResourceStore;
import com.trinary.ui.elements.FormattedTextElement;
import com.trinary.ui.elements.GraphicElement;
import com.trinary.ui.elements.ResourceElement;
import com.trinary.util.Location;

public class GFXCore {
	private JFrame container;
	private Canvas canvas;
	private Boolean running = true;
	
	private BufferStrategy strategy;
	
	private GraphicElement gfxContainer;
	private ArrayList<ResourceElement> actors = new ArrayList<>();
	private FormattedTextElement textBox;
	
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
		
		ConfigHolder.setConfig("rootDirectory", "src/main/resources/");
		ResourceStore.addFolder("resources/vanguard");
		ResourceStore.addFolder("vn/actors");
		
		gfxContainer = new GraphicElement(0, 0, 800, 600, "vn/scenes/hallway.jpg");
		gfxContainer.setzIndex(0);
		
		actors.add(new ResourceElement());
		actors.get(0).changeResource("actor_neutral");
		actors.get(0).move(new Location(null, 0, 800, null));
		actors.get(0).setzIndex(1);
		
		textBox = new FormattedTextElement((800 - (int)(.8 * 800))/2, 350, (int)(.8 * 800), 200);
		textBox.setLayer("layers/vanguard/effect-box.png");
		textBox.setMarginX(20);
		textBox.setMarginY(20);
		textBox.setTransparency(0.75f);
		textBox.setzIndex(2);
		
		gfxContainer.addChild(textBox);
		
		for (ResourceElement actor : actors) {
			gfxContainer.addChild(actor);
		}
	}
	
	public void setText(String text) {
		textBox.setText(text);
	}
	
	public void changeActorMood(int actor, String mood) {
		actors.get(actor).changeResource(mood);
	}
	
	public void moveActor(int actor, int x, int y) {
		actors.get(actor).move(x, y);
	}
	
	public void mainLoop() {
		int pageCount = 0;

		textBox.setText("So anon...why did you want to see <i>me</i> after <span color='#FF0000'>class</span>?  "
				+ "You seemed <b>insistent</b>...what's up?  In fact you have been acting weird all week."
				+ "First you couldn't stop staring at Miku, and now this...I don't know what to think of all this."
				+ "Wh...what is going on?", true);
		
		while (running) {
			if (textBox.isDone() && pageCount == 0) {
				textBox.setText("Why are <b>you</b> looking at me like <i>that</i>?", true);
				actors.get(0).changeResource("actor_embarrassed");
				actors.get(0).move(new Location(null, 0, 800, null));
				pageCount++;
			}
			
			Graphics2D g = (Graphics2D)strategy.getDrawGraphics();
			g.drawImage(gfxContainer.render(), null, 0, 0);
			
			strategy.show();
			try { Thread.sleep(10); } catch (Exception e) {}
		}
	}
}
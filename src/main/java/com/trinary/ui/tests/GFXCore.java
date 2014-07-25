package com.trinary.ui.tests;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.HashMap;

import javax.swing.JFrame;

import com.trinary.ui.config.ConfigHolder;
import com.trinary.ui.config.ResourceStore;
import com.trinary.ui.elements.FormattedTextElement;
import com.trinary.ui.elements.ResourceElement;
import com.trinary.util.Location;

public class GFXCore {
	private JFrame container;
	private Canvas canvas;
	private Boolean running = true;
	
	private BufferStrategy strategy;
	
	private ResourceElement gfxContainer;
	private FormattedTextElement textBox;
	
	private HashMap<String, ResourceElement> actors = new HashMap<>();
	
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
		ResourceStore.addFolder("vn/scenes");
		
		//gfxContainer = new GraphicElement(0, 0, 800, 600, "vn/scenes/hallway.jpg");
		gfxContainer = new ResourceElement();
		gfxContainer.changeResource("hallway");
		gfxContainer.setWidth(800);
		gfxContainer.setHeight(600);
		gfxContainer.setzIndex(0);
		
		textBox = gfxContainer.addChild(FormattedTextElement.class);
		
		actors.put("girl-chan", gfxContainer.addChild(ResourceElement.class));
		actors.get("girl-chan").changeResource("actor_neutral");
		actors.get("girl-chan").move(new Location(null, 0, 800, null));
		actors.get("girl-chan").setzIndex(1);
		
		textBox.setWidthP(.80);
		textBox.setHeightP(.30);
		textBox.move(new Location(0, null, null, 550));
		textBox.center();
		textBox.setMarginX(20);
		textBox.setMarginY(20);
		textBox.setTransparency(0.75f);
		textBox.setzIndex(2);
		textBox.setLayer("layers/vanguard/effect-box.png");
		
		for (ResourceElement actor : actors.values()) {
			gfxContainer.addChild(actor);
		}
	}
	
	public void changeScene(String sceneName) {
		gfxContainer.changeResource(sceneName);
	}
	
	public void addActor(String actor, Integer position) {
		actors.put(actor, gfxContainer.addChild(ResourceElement.class));
	}
	
	public void setText(String text) {
		textBox.setText(text, true);
	}
	
	public void changeActorMood(String actor, String mood) {
		actors.get(actor).changeResource(mood);
	}
	
	public void moveActor(String actor, int x, int y) {
		actors.get(actor).move(x, y);
	}
	
	public void mainLoop() {
		int pageCount = 0;

		this.setText("So anon...why did you want to see <i>me</i> after <span color='#FF0000'>class</span>?  "
				+ "You seemed <b>insistent</b>...what's up?  In fact you have been acting weird all week.  "
				+ "First you couldn't stop staring at Miku, and now this...I don't know what to think of all this.  "
				+ "Wh...what is going on?");
		
		while (running) {
			if (textBox.isDone() && pageCount == 0) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.setText("Why are <b>you</b> looking at me like <i>that</i>?  And how did we teleport to "
						+ "this classroom in completely different country?!");
				this.changeActorMood("girl-chan", "actor_embarrassed");
				this.changeScene("classroom");
				pageCount++;
			}
			
			Graphics2D g = (Graphics2D)strategy.getDrawGraphics();
			g.drawImage(gfxContainer.render(), null, 0, 0);
			
			strategy.show();
			try { Thread.sleep(10); } catch (Exception e) {}
		}
	}
}
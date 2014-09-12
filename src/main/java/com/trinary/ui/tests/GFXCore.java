package com.trinary.ui.tests;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.text.formatted.elements.PositionedElement;
import com.trinary.ui.config.ConfigHolder;
import com.trinary.ui.config.ResourceStore;
import com.trinary.ui.elements.AnimatedElement;
import com.trinary.ui.elements.ChoiceBox;
import com.trinary.ui.elements.FormattedTextElement;
import com.trinary.ui.elements.ResourceElement;
import com.trinary.ui.transitions.FadeOutIn;
import com.trinary.util.Location;

public class GFXCore implements KeyListener, MouseListener, MouseMotionListener {
	private JFrame container;
	private Canvas canvas;
	private Boolean running = true;
	private Boolean paused = false;
	
	private BufferStrategy strategy;
	
	private ResourceElement curtain;
	private ResourceElement gfxContainer;
	private FormattedTextElement textBox;
	private ChoiceBox choiceBox;
	private HashMap<String, AnimatedElement> actors = new HashMap<>();
	
	public GFXCore() {	
		container = new JFrame("League of Nee-chans");
		container.setPreferredSize(new Dimension(800, 600));
		container.addKeyListener(this);
		container.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(container.getWidth(), container.getHeight()));
		canvas.setVisible(true);
		canvas.setFocusable(false);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		
		container.setResizable(false);
		container.add(canvas);
		container.pack();
		container.setVisible(true);
		
		canvas.createBufferStrategy(2);
		strategy = canvas.getBufferStrategy();
		
		ConfigHolder.setConfig("rootDirectory", "src/main/resources/");
		ResourceStore.addFolder("vn");
		
		gfxContainer = new ResourceElement();
		gfxContainer.setWidth(container.getWidth());
		gfxContainer.setHeight(container.getHeight());
		
		gfxContainer.setzIndex(0);
		
		curtain = gfxContainer.addChild(ResourceElement.class);
		curtain.setWidthP(1.0);
		curtain.setHeightP(1.0);
		curtain.setzIndex(3);
		curtain.setTransparency(0.0f);
		
		choiceBox = gfxContainer.addChild(ChoiceBox.class);
		choiceBox.changeResource("vn.skin.textbox", false);
		choiceBox.setWidthP(0.60);
		choiceBox.setHeightP(0.60);
		choiceBox.sethAlign("center");
		choiceBox.setvAlign("center");
		choiceBox.setMarginX(20);
		choiceBox.setMarginY(40);
		choiceBox.setTransparency(0.0f);
		choiceBox.setzIndex(9999);
		
		textBox = gfxContainer.addChild(FormattedTextElement.class);
		textBox.changeResource("vn.skin.textbox", false);
		textBox.setWidthP(.80);
		textBox.setHeightP(.30);
		textBox.move(new Location("bottom: 90%"));
		textBox.sethAlign("center");
		textBox.setMarginX(20);
		textBox.setMarginY(20);
		textBox.setTransparency(0.75f);
		textBox.setzIndex(2);
	}
	
	public void changeScene(String sceneName) {
		gfxContainer.changeResource(sceneName, false);
	}
	
	public void addActor(String actor, Integer position) {
		actors.put(actor, gfxContainer.addChild(AnimatedElement.class));
	}
	
	public void setText(String text) {
		textBox.setText(text, true);
	}
	
	public void changeActorMood(String actor, String mood) {
		actors.get(actor).setTransition(new FadeOutIn(mood));
	}
	
	public void moveActor(String actor, int x, int y) {
		actors.get(actor).move(x, y);
	}
	
	public void skip() {
		if (textBox.isSkipped()) {
			textBox.setDone();
			return;
		}
		textBox.setSkipped();
	}
	
	public void pause() {
		textBox.togglePause();
		paused = !paused;
		
		if (paused) {
			darken();
		} else {
			undarken();
		}
	}
	
	public void darken() {
		curtain.setTransparency(0.5f);
	}
	
	public void undarken() {
		curtain.setTransparency(0.0f);
	}
	
	public void mainLoop() {
		// Run the main loop
		while (running) {
			Graphics2D g = (Graphics2D)strategy.getDrawGraphics();
			g.drawImage(gfxContainer.render(), null, 0, 0);
			
			strategy.show();
			try { Thread.sleep(10); } catch (Exception e) {}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyChar()) {
		case KeyEvent.VK_ESCAPE:
			this.pause();
			break;
		case KeyEvent.VK_ENTER:
		default:
			this.skip();
			break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("CLICKED AT: " + e.getX() + ", " + e.getY());
		
		for (PositionedElement element : PositionedElement.getMarked()) {
			if (element.rectangleContains(e.getX(), e.getY())) {
				System.out.println(String.format("\t\tCLICKED ON MONITORED ELEMENT %s!", element.getId()));
				
				// Send choice_made message with id back to SE core
			}
		}
	}
	
	@Override
	// This is built in to our system.  This simply highlights marked text when a mouse runs over it.
	// Currently it only highlights the elements in our choice box.
	public void mouseMoved(MouseEvent e) {
		for (PositionedElement element : PositionedElement.getMarked()) {
			if (element.rectangleContains(e.getX(), e.getY())) {
				synchronized(choiceBox) {
					choiceBox.highlight(element.getId());
				}
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}
	
	public BufferedImage createBlackBufferedImage(int width, int height) {
		BufferedImage b_img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = b_img.createGraphics();

		graphics.setPaint(Color.black);
		graphics.fillRect(0, 0, b_img.getWidth(), b_img.getHeight());
		
		return b_img;
	}
}
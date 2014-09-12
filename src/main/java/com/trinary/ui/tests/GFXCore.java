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
import java.util.LinkedHashSet;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.text.formatted.elements.PositionedElement;
import com.trinary.ui.config.ConfigHolder;
import com.trinary.ui.config.ResourceStore;
import com.trinary.ui.elements.AnimatedElement;
import com.trinary.ui.elements.ChoiceBox;
import com.trinary.ui.elements.ContainerElement;
import com.trinary.ui.elements.FormattedTextElement;
import com.trinary.ui.transitions.FadeOutIn;
import com.trinary.util.Location;
import com.trinary.vn.screen.ActorPosition;

public class GFXCore implements KeyListener, MouseListener, MouseMotionListener {
	private JFrame frame;
	private Canvas canvas;
	private Boolean running = true;
	private Boolean paused = false;
	
	private BufferStrategy strategy;
	
	private ContainerElement container;
	private AnimatedElement curtain, scene;
	private FormattedTextElement textBox;
	private ChoiceBox choiceBox;
	private HashMap<String, AnimatedElement> actors = new HashMap<>();
	
	private HashMap<String, ActorPosition> actorPositions = new HashMap<>();
	
	private Integer moodIndex = 0;
	private String[] moods = {"default", "annoyed", "embarrassed", "wishful"};
	
	private Integer sceneIndex = 0;
	private String[] scenes = {"hallway", "classroom"};
	
	public GFXCore() {
		// Set up Java container
		frame = new JFrame("League of Nee-chans");
		frame.setPreferredSize(new Dimension(800, 600));
		frame.addKeyListener(this);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		// Set up drawing surface
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));
		canvas.setVisible(true);
		canvas.setFocusable(false);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		
		// Add drawing surface to Java container
		frame.setResizable(false);
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
		
		// Create buffer strategy
		canvas.createBufferStrategy(2);
		strategy = canvas.getBufferStrategy();
		
		// Configure UI and add resources
		ConfigHolder.setConfig("rootDirectory", "src/main/resources/");
		ResourceStore.addFolder("vn");
		
		// Create our UI container
		container = new ContainerElement(frame);
		
		// Create background (scene)
		scene = container.addChild(AnimatedElement.class);
		scene.changeResource("_black", false);
		scene.setWidthP(1.0);
		scene.setHeightP(1.0);
		
		// Create text box
		textBox = container.addChild(FormattedTextElement.class);
		textBox.changeResource("vn.skin.textbox", false);
		textBox.setWidthP(.80);
		textBox.setHeightP(.30);
		textBox.move(new Location("bottom: 90%"));
		textBox.sethAlign("center");
		textBox.setMarginX(20);
		textBox.setMarginY(20);
		textBox.setTransparency(0.75f);
		textBox.setzIndex(5);
		
		// Create curtain for darkening/lightening screen
		curtain = container.addChild(AnimatedElement.class);
		curtain.setWidthP(1.0);
		curtain.setHeightP(1.0);
		curtain.setTransparency(0.0f);
		curtain.setzIndex(10);
		
		// Create choice box
		choiceBox = container.addChild(ChoiceBox.class);
		choiceBox.changeResource("vn.skin.textbox", false);
		choiceBox.setWidthP(0.60);
		choiceBox.setHeightP(0.60);
		choiceBox.sethAlign("center");
		choiceBox.setvAlign("center");
		choiceBox.setMarginX(20);
		choiceBox.setMarginY(40);
		choiceBox.setTransparency(0.0f);
		choiceBox.setzIndex(9999);
		
		// Set up actor positions
		actorPositions.put("right", new ActorPosition("right: 100%, bottom: 100%", 1, 1.0));
		actorPositions.put("left",  new ActorPosition("left:  0%, bottom: 100%", 1, 1.0));
	}
	
	public void changeScene(String sceneName) {
		String resName = String.format("vn.scenes.%s", sceneName);
		scene.setTransition(new FadeOutIn(resName));
	}
	
	public void addActor(String name, String position) {
		ActorPosition pos = actorPositions.get(position);
		if (pos == null) {
			return;
		}
		
		AnimatedElement actor = container.addChild(AnimatedElement.class);
		actor.move(pos.getPosition());
		actor.setzIndex(pos.getzIndex());
		//actor.scale(new Float(pos.getScale()));
		
		actors.put(name, actor);
	}
	
	public void addActor(String name, String mood, String position) {
		addActor(name, position);
		
		AnimatedElement actor = container.addChild(AnimatedElement.class);
		String resName = String.format("vn.actors.%s.%s", name, mood);
		actor.changeResource(resName, true);
		
		actors.put(name, actor);
	}
	
	public void setText(String text) {
		textBox.setText(text, true);
	}
	
	public void changeActorMood(String actor, String mood) {
		String resName = String.format("vn.actors.%s.%s", actor, mood);
		actors.get(actor).setTransition(new FadeOutIn(resName));
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
		changeScene("hallway");
		addActor("girlchan", "right");
		changeActorMood("girlchan", "default");
		
		setText("[HELP]: Press 'a' to change actor mood and 's' to change the scene.");
		
		// Run the main loop
		while (running) {
			Graphics2D g = (Graphics2D)strategy.getDrawGraphics();
			g.drawImage(container.render(), null, 0, 0);
			
			strategy.show();
			try { Thread.sleep(10); } catch (Exception e) {}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyChar()) {
		case KeyEvent.VK_ESCAPE:
			System.out.println("ESCAPE PRESSED!");
			this.pause();
			break;
		case KeyEvent.VK_ENTER:
			System.out.println("ENTER PRESSED!");
			this.skip();
			break;
		case 'a':
			// Test code
			System.out.println("INDEX: " + moodIndex);
			System.out.println("MOOD:  " + moods[moodIndex]);
			
			moodIndex++;
			if (moodIndex >= moods.length) {
				moodIndex = 0;
			}
			changeActorMood("girlchan", moods[moodIndex]);
			setText(String.format("I am <b>%s</b>!", moods[moodIndex]));
			break;
		case 's':
			// Test code
			System.out.println("INDEX: " + moodIndex);
			System.out.println("MOOD:  " + moods[moodIndex]);
			
			sceneIndex++;
			if (sceneIndex >= scenes.length) {
				sceneIndex = 0;
			}
			changeScene(scenes[sceneIndex]);
			changeActorMood("girlchan", "embarrassed");
			setText(String.format("How did we just move to the <b>%s</b>?", scenes[sceneIndex]));
			break;
		case 'h':
			setText("[HELP]: Press 'a' to change actor mood and 's' to change the scene.");
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
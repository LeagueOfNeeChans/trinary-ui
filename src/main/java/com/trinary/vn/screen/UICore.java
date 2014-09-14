package com.trinary.vn.screen;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.text.formatted.elements.PositionedElement;
import com.trinary.ui.config.ConfigHolder;
import com.trinary.ui.config.ResourceStore;
import com.trinary.ui.elements.AnimatedElement;
import com.trinary.ui.elements.ChoiceBox;
import com.trinary.ui.elements.ContainerElement;
import com.trinary.ui.elements.FormattedTextElement;
import com.trinary.ui.elements.UIElement;
import com.trinary.ui.transitions.FadeOutIn;
import com.trinary.util.EventCallback;
import com.trinary.util.Location;

public class UICore implements KeyListener, MouseListener, MouseMotionListener {
	// Java UI Elements
	private JFrame frame;
	private Canvas canvas;
	private Boolean running = true;
	private Boolean paused = false;
	
	// Strategy for double buffering
	private BufferStrategy strategy;
	
	// Trinary UI Elements
	private ContainerElement container;
	private AnimatedElement curtain, scene;
	private FormattedTextElement textBox;
	private ChoiceBox choiceBox;
	private HashMap<String, AnimatedElement> actors = new HashMap<>();
	
	// Actor positions
	private HashMap<String, ActorPosition> actorPositions = new HashMap<>();
	
	// Callback for marked functions
	private EventCallback callback = null;
	
	// Test variables
	/*
	private Integer moodIndex = 0;
	private String[] moods = {"default", "annoyed", "embarrassed", "wishful"};
	private Integer sceneIndex = 0;
	private String[] scenes = {"hallway", "classroom"};
	*/
	
	/**
	 * Setup our Visual Novel UI
	 */
	public UICore() {
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
		ConfigHolder.setConfig("rootDirectory", "resources/");
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
		
		container.sortChildrenByZIndex();
		
		for (UIElement e : container.getChildren()) {
			System.out.println("Z-INDEX: " + e.getzIndex());
		}
		
		// Set up actor positions
		actorPositions.put("right", new ActorPosition("right: 100%, bottom: 100%", 1, 1.0));
		actorPositions.put("left",  new ActorPosition("left:  0%, bottom: 100%", 1, 1.0));
	}
	
	/**
	 * Change the scene to the named scene
	 * 
	 * @param sceneName
	 */
	public void changeScene(String sceneName) {
		String resName = String.format("vn.scenes.%s", sceneName);
		scene.setTransition(new FadeOutIn(resName));
	}
	
	/**
	 * Add a named actor in the named position
	 * 
	 * @param name
	 * @param position
	 */
	public void addActor(String name, String position) {
		ActorPosition pos = actorPositions.get(position);
		if (pos == null) {
			return;
		}
		
		AnimatedElement actor = scene.addChild(AnimatedElement.class);
		actor.move(pos.getPosition());
		actor.setzIndex(pos.getzIndex());
		//actor.scale(new Float(pos.getScale()));
		
		actors.put(name, actor);
	}
	
	/**
	 * Add a named actor with the specified mood in the named position
	 * 
	 * @param name
	 * @param mood
	 * @param position
	 */
	public void addActor(String name, String mood, String position) {
		addActor(name, position);
		
		AnimatedElement actor = container.addChild(AnimatedElement.class);
		String resName = String.format("vn.actors.%s.%s", name, mood);
		actor.changeResource(resName, true);
		
		actors.put(name, actor);
	}
	
	/**
	 * Set the text in the text box
	 * 
	 * @param text
	 */
	public void setText(String actor, String text) {
		textBox.setText(String.format("<b>%s</b>: %s", capitalize(actor), text), true);
	}
	
	/**
	 * Change the mood of the named actor
	 * 
	 * @param actor
	 * @param mood
	 */
	public void changeActorMood(String actor, String mood) {
		String resName = String.format("vn.actors.%s.%s", actor, mood);
		actors.get(actor).setTransition(new FadeOutIn(resName));
	}
	
	/**
	 * Move the named actor to this x, y coordinate
	 * 
	 * @param actor
	 * @param x
	 * @param y
	 */
	public void moveActor(String actor, int x, int y) {
		actors.get(actor).move(x, y);
	}
	
	/**
	 * Move the named actor to this relative location
	 * 
	 * @param actor
	 * @param location
	 */
	public void moveActor(String actor, String location) {
		actors.get(actor).move(new Location(location));
	}
	
	/**
	 * Add a choice to the choice box
	 */
	public void addChoice(String id, String text) {
		choiceBox.addChoice(id, "<img src='vn.skin.black-icon'/>" + text);
	}
	
	/**
	 * Show the choice box
	 */
	public void showChoices() {
		choiceBox.finish();
		darken();
		choiceBox.setTransparency(1.0f);
	}
	
	/**
	 * Skips by pulling the next UI message from the bus
	 */
	public void skip() {
		if (textBox.isSkipped()) { // If the textBox is skipped...
			
			// Set the textBox to done
			textBox.setDone();
					
			return;
		}
		
		// Set the textBox as skipped
		textBox.setSkipped();
	}
	
	/**
	 * Pause the UI
	 */
	public void pause() {
		textBox.togglePause();
		paused = !paused;
		
		if (paused) {
			darken();
		} else {
			undarken();
		}
	}
	
	/**
	 * Kill rendering loop
	 */
	public void stop() {
		running = false;
	}
	
	/**
	 * Pause the UI and open the menu
	 */
	public void openMenu() {
		pause();
		
		// TODO write menu UI
	}
	
	/**
	 * Darken the screen
	 */
	public void darken() {
		curtain.setTransparency(0.5f);
	}
	
	/**
	 * Undarken the screen
	 */
	public void undarken() {
		curtain.setTransparency(0.0f);
	}
	
	/**
	 * Check the status of the last command
	 * @return
	 */
	public boolean isDrawing() {
		return !textBox.isDone();
	}
	
	/**
	 * Set the callback function for mouse click events
	 */
	public void setCallback(EventCallback c) {
		callback = c;
	}
	
	/**
	 * Render each frame of the game here
	 */
	public void mainLoop() {
		/**
		 * Test code
		 * Remove from final version
		 */
		/*
		changeScene("hallway");
		addActor("girlchan", "right");
		changeActorMood("girlchan", "default");
		setText("HELP","Press 'a' to change actor mood and 's' to change the scene.");
		*/
		
		// Run the main loop
		while (running) {
			
			if (textBox.isSkipped()) { // If the textBox is done rendering...
				
				// Skip to the next screen of dialog if auto play is enabled
				if (ConfigHolder.getConfig("autoPlay") != null) {
					// TODO attempt to put a delay here
					skip();
				}
			}
			
			Graphics2D g = (Graphics2D)strategy.getDrawGraphics();
			g.drawImage(container.render(), null, 0, 0);
			
			strategy.show();
			try { Thread.sleep(10); } catch (Exception e) {}
		}
	}

	/**
	 * Monitor the keyboard for activity
	 */
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
		/*
		case 'a':
			// Test code
			System.out.println("INDEX: " + moodIndex);
			System.out.println("MOOD:  " + moods[moodIndex]);
			
			moodIndex++;
			if (moodIndex >= moods.length) {
				moodIndex = 0;
			}
			changeActorMood("girlchan", moods[moodIndex]);
			setText("girl-chan: ", String.format("I am <b>%s</b>!", moods[moodIndex]));
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
			setText("girl-chan: ", String.format("How did we just move to the <b>%s</b>?", scenes[sceneIndex]));
			break;
		case 'h':
			setText("HELP", "Press 'a' to change actor mood and 's' to change the scene.");
			break;
		*/
		}
	}

	/**
	 * Monitor for clicks on marked elements
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("CLICKED AT: " + e.getX() + ", " + e.getY());
		
		CopyOnWriteArrayList<PositionedElement> marked = PositionedElement.getMarked();
		
		synchronized(marked) {
			for (PositionedElement element : marked) {
				if (element.rectangleContains(e.getX(), e.getY())) {
					System.out.println(String.format("\t\tCLICKED ON MONITORED ELEMENT %s!", element.getId()));
					
					choiceBox.setTransparency(0.0f);
					choiceBox.clear();
					undarken();
					
					// Send choice_made message with id back to SE core
					if (callback != null) {
						callback.run(element.getId());
					}
				}
			}
		}
	}
	
	/**
	 * Highlight marked elements when the mouse hovers over them
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		CopyOnWriteArrayList<PositionedElement> marked = PositionedElement.getMarked();
		synchronized(marked) {
			for (PositionedElement element : marked) {
				if (element.rectangleContains(e.getX(), e.getY())) {
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
	
	private String capitalize(String line) {
	  return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}
}
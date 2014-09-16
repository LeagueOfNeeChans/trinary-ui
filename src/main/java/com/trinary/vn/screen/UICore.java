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
import javax.xml.bind.JAXBException;

import com.text.formatted.elements.PositionedElement;
import com.trinary.ui.config.ConfigHolder;
import com.trinary.ui.config.ResourceStore;
import com.trinary.ui.elements.AnimatedElement;
import com.trinary.ui.elements.ChoiceBox;
import com.trinary.ui.elements.ContainerElement;
import com.trinary.ui.elements.FormattedTextElement;
import com.trinary.ui.elements.Monitorable;
import com.trinary.ui.elements.ResourceElement;
import com.trinary.ui.transitions.FadeOutIn;
import com.trinary.util.EventCallback;
import com.trinary.util.LayoutLoader;
import com.trinary.util.Location;

@SuppressWarnings("restriction")
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
	private ResourceElement curtain;
	private AnimatedElement scene;
	private FormattedTextElement textBox;
	private ChoiceBox choiceBox;
	private HashMap<String, AnimatedElement> actors = new HashMap<>();
	
	// Actor positions
	private HashMap<String, ActorPosition> actorPositions = new HashMap<>();
	
	// Last monitorable
	private Monitorable lastMonitorable;
	
	// Callback for marked functions
	private EventCallback callback = null;
	
	// Rendering thread
	private Thread renderThread = new Thread() {
		@Override
		public void run() {
			mainLoop();
		}
	};
	
	/**
	 * Setup our Visual Novel UI
	 */
	public UICore() {		
		this("layout.xml");
	}
	
	public UICore(String layoutFile) {
		// Configure UI and add resources
		ConfigHolder.setConfig("rootDirectory", "resources/");
		ResourceStore.addFolder("vn");
		
		try {
			container = LayoutLoader.processLayout(layoutFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		container.sortChildrenByZIndex();
		
		scene     = (AnimatedElement)ContainerElement.getElementByName("scene");
		curtain   = (ResourceElement)ContainerElement.getElementByName("curtain");
		textBox   = (FormattedTextElement)ContainerElement.getElementByName("textBox");
		choiceBox = (ChoiceBox)ContainerElement.getElementByName("choiceBox");
		
		// Set up actor positions
		actorPositions.put("right", new ActorPosition("right: 100%, bottom: 100%", 1, 1.0));
		actorPositions.put("left",  new ActorPosition("left:  0%, bottom: 100%", 1, 1.0));
		
		// Set up Java container
		frame = new JFrame("League of Nee-chans");
		frame.setPreferredSize(new Dimension(container.getWidth(), container.getHeight()));
		frame.addKeyListener(this);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		// Set up drawing surface
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(container.getWidth(), container.getHeight()));
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
		
		// Start the thread
		renderThread.start();
	}
	
	/**
	 * Render each frame of the game here
	 */
	protected void mainLoop() {
		Graphics2D g = (Graphics2D)strategy.getDrawGraphics();
		
		// Run the main loop
		while (running) {
			paint(g);
		}
	}
	
	protected void paint(Graphics2D g) {
		g.drawImage(container.render(), null, 0, 0);
		
		strategy.show();
		try { Thread.sleep(10); } catch (Exception e) {}
	}
	
	/**
	 * Change the scene to the named scene
	 * 
	 * @param sceneName
	 */
	public void changeScene(String sceneName) {
		String resName = String.format("vn.scenes.%s", sceneName);
		scene.setTransition(new FadeOutIn(resName, false));
		lastMonitorable = scene;
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
		lastMonitorable = textBox;
	}
	
	/**
	 * Change the mood of the named actor
	 * 
	 * @param actor
	 * @param mood
	 */
	public void changeActorMood(String actor, String mood) {
		String resName = String.format("vn.actors.%s.%s", actor, mood);
		AnimatedElement e = actors.get(actor);
		e.setTransition(new FadeOutIn(resName, true));
		lastMonitorable = e;
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
		System.out.println("CHOICEBOX IS: " + choiceBox);
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
		return lastMonitorable.isBusy();
	}
	
	/**
	 * Set the callback function for mouse click events
	 */
	public void setCallback(EventCallback c) {
		callback = c;
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
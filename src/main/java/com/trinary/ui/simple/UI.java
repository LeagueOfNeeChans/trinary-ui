package com.trinary.ui.simple;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.xml.bind.JAXBException;

import com.trinary.ui.config.ConfigHolder;
import com.trinary.ui.config.ResourceStore;
import com.trinary.ui.elements.ContainerElement;
import com.trinary.ui.elements.Monitorable;
import com.trinary.util.EventCallback;
import com.trinary.util.LayoutLoader;

@SuppressWarnings("restriction")
public abstract class UI implements KeyListener, MouseListener, MouseMotionListener {
	// Java UI Elements
	protected JFrame frame;
	protected Canvas canvas;
	protected Boolean running = true;
	
	// Strategy for double buffering
	protected BufferStrategy strategy;
	
	// Trinary UI Elements
	protected ContainerElement container;
	
	// Last monitorable
	protected Monitorable lastMonitorable;
	
	// Callback for marked functions
	protected EventCallback callback = null;
	
	/**
	 * The main thread the UI runs in.
	 */
	final protected Thread renderThread = new Thread() {
		@Override
		public void run() {
			mainLoop();
		}
	};
	
	/**
	 * Refresh the size based on the container element size
	 */
	final protected void refreshSwing() {
		resizeSwing(container.getWidth(), container.getHeight());
	}
	
	/**
	 * Resize swing component
	 * @param width
	 * @param height
	 */
	final protected void resizeSwing(int width, int height) {
		System.out.println("CHANGING FRAME AND CANVAS SIZE TO: " + width + "X" + height);
		Dimension frameSize = new Dimension(width, height);
		frame.setPreferredSize(frameSize);
		frame.setSize(frameSize);
		canvas.setPreferredSize(frameSize);
		canvas.setSize(frameSize);
	}
	
	/**
	 * Setup initial containers
	 */
	final protected void setupSwing() {
		// Set up Java container
		frame = new JFrame("League of Nee-chans");
		
		frame.addKeyListener(this);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		// Set up drawing surface
		canvas = new Canvas();
		canvas.setVisible(true);
		canvas.setFocusable(false);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		
		// Add drawing surface to Java container
		frame.setResizable(false);
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Complete the swing setup once sizes are computed
	 */
	final protected void completeSwing() {
		// Create buffer strategy
		canvas.createBufferStrategy(2);
		strategy = canvas.getBufferStrategy();
	}
	
	/**
	 * Load resources and set root directory
	 */
	final protected void loadResources() {
		// Configure UI and add resources
		ConfigHolder.setConfig("rootDirectory", "resources/");
		ResourceStore.addFolder("vn");
	}
	
	/**
	 * Override and place all custom logic here
	 */
	protected abstract void customSetup();
	
	public UI() {
		this("layout.xml");
	}
	
	public UI(int width, int height) {		
		loadResources();
		
		container = new ContainerElement(0, 0, width, height);
		
		setupSwing();
		refreshSwing();
		customSetup();
		completeSwing();
		
		renderThread.start();
	}
	
	public UI(String layoutFile) {
		loadResources();
		
		try {
			container = LayoutLoader.processLayout(layoutFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		container.sortChildrenByZIndex();
		
		setupSwing();
		refreshSwing();
		customSetup();
		completeSwing();
		
		// Start the thread
		renderThread.start();
	}
	
	/**
	 * Render each frame of the game here.
	 */
	final protected void mainLoop() {
		Graphics2D g = (Graphics2D)strategy.getDrawGraphics();
		
		// Run the main loop
		while (running) {
			paint(g);
			strategy.show();
			try { throttle();} catch (Exception e) {}
		}
	}
	
	/**
	 * Specifically how to render a single frame of the UI.
	 * @param g
	 */
	protected void paint(Graphics2D g) {
		g.drawImage(container.render(), null, 0, 0);
	}
	
	/**
	 * How to throttle the frame rate
	 * 
	 * @throws Exception
	 */
	protected void throttle() throws Exception {
		Thread.sleep(10);
	}
	
	/**
	 * Check the status of the last command
	 * @return
	 */
	public boolean isDrawing() {
		if (lastMonitorable != null) {
			return lastMonitorable.isBusy();
		} else {
			return false;
		}
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
	public void keyPressed(KeyEvent e) {}

	/**
	 * Monitor for clicks on marked elements
	 */
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	/**
	 * Highlight marked elements when the mouse hovers over them
	 */
	@Override
	public void mouseMoved(MouseEvent e) {}
	
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
}
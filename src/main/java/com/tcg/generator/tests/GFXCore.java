package com.tcg.generator.tests;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.File;

import javax.swing.JFrame;

import com.tcg.generator.config.ConfigHolder;
import com.tcg.generator.ui.GenericUI;

public class GFXCore implements KeyListener {
	private JFrame container;
	private Canvas canvas;
	private Boolean running = true;
	private Integer x = 0;
	private Integer y = 0;
	
	private GenericUI ui;
	
	BufferStrategy strategy;
	
	public GFXCore() {		
		container = new JFrame("League of Nee-chans");
		container.setPreferredSize(new Dimension(800, 600));
		container.addKeyListener(this);
		
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
		// Draw to JPanel
		ConfigHolder.setConfig("rootDirectory", "src/main/resources/");
        GenericUI ui = new GenericUI()
                .setLayout(new File("src/main/resources/layouts/vanguard.layout"))
                .setBackground(new File("src/main/resources/art/fluttershy-art.png"))
                .setUiData(
                "{" +
                	"\"cardName\": \"Fluttershy\"," +
                    "\"grade\": 0," +
                    "\"trigger\": \"heal\"," +
                    "\"shield\": 10000," +
                    "\"power\": 10000," +
                    "\"clan\": \"Equestria\"," +
                    "\"race\": \"Pony\"," +
                    "\"effects\": [" +
                        "\"<img src='cont' /> If you have a unit named <b>Twilight Sparkle</b> on <img src='vanguard' />, then <b>Fluttershy</b> gains <img src='sword' />+5000 until end of turn.\"" +
                    "]" +
                "}");
       		
		while (running) {
			Graphics2D g = (Graphics2D)strategy.getDrawGraphics();
			g.drawImage(ui.render(), null, x, y);
			
			strategy.show();
			try { Thread.sleep(20); } catch (Exception e) {}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch( keyCode ) { 
		    case KeyEvent.VK_UP:
		        // handle up
		    	y -= 10;
		        break;
		    case KeyEvent.VK_DOWN:
		        // handle down
		    	y += 10;
		        break;
		    case KeyEvent.VK_LEFT:
		        // handle left
		    	x -= 10;
		        break;
		    case KeyEvent.VK_RIGHT :
		        // handle right
		    	x += 10;
		        break;
		}
	    
	    Graphics2D g = (Graphics2D)strategy.getDrawGraphics();
	    g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
package com.trinary.ui.elements;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.trinary.ui.config.ConfigHolder;

public class ContainerElement extends UIElement {
	protected static HashMap<String, ContainerElement> childElements = new HashMap<String, ContainerElement>();
	
	public static ContainerElement getElementByName(String name) {
		return ContainerElement.childElements.get(name);
	}
	
	public ContainerElement() {
		this(0, 0, 0, 0);
	}
	
	public ContainerElement(Container container) {
		this(0, 0, container.getWidth(), container.getHeight());
	}
	
	public ContainerElement(int x, int y, int width, int height) {
		super(x, y, width, height);
		if (width != 0 && height != 0) {
			this.surface = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = this.surface.createGraphics();
			g.setComposite(AlphaComposite.Clear);
			g.fillRect(0, 0, width, height);
		}
	}
	
	public ContainerElement(int x, int y, int width, int height, Color color) {
		super(x, y, width, height);
		
		this.surface = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = this.surface.createGraphics();
		g.setComposite(AlphaComposite.Clear);
		g.fillRect(0, 0, width, height);
		
		this.bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g = this.bi.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, this.width, this.height);
	}
	
	public ContainerElement(int x, int y, int width, int height, String filename) {
		super(x, y, width, height);
		try {
			BufferedImage bi = ImageIO.read(new File(ConfigHolder.getConfig("rootDirectory") + filename));
			this.bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			
			this.surface = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = this.surface.createGraphics();
			g.setComposite(AlphaComposite.Clear);
			g.fillRect(0, 0, width, height);
			
			g = this.bi.createGraphics();
	        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			
			g.drawImage(bi, 0, 0, width, height, null);
		} catch (IOException e) {
			System.out.println("UNABLE TO OPEN " + filename);
		}
	}
	
	public ContainerElement(int x, int y, int width, int height, BufferedImage bi) {
		super(x, y, width, height);
		this.bi = bi;
	}
	
	public void refreshLayer() {
		BufferedImage bi = this.bi;
		
		if (width != 0 && height != 0) {			
			this.bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			this.surface = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			
			Graphics2D g = this.bi.createGraphics();
	        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			
			g.drawImage(bi, 0, 0, width, height, null);
		}
	}
	
	public void setLayer(String filename) {
		BufferedImage bi;
		try {
			bi = ImageIO.read(new File(ConfigHolder.getConfig("rootDirectory") + filename));
			
			this.bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			this.surface = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			
			Graphics2D g = this.bi.createGraphics();
	        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			
			g.drawImage(bi, 0, 0, width, height, null);
		} catch (IOException e) {

		}
	}
	
	public void setLayer(BufferedImage layer) {
		this.bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		this.surface = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = this.bi.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		
		g.drawImage(layer, 0, 0, width, height, null);
	}
	
	@Override
	public void setWidth(int width) {
		setWidth(width, false);
	}
	
	@Override
	public void setHeight(int height) {
		setHeight(height, false);
	}
	
	public void setWidth(int width, boolean porportional) {
		this.width = width;
		
		if (porportional) {
			this.height = (int) (this.width / getRatio());
		}
		
		refreshLayer();
	}
	
	public void setHeight(int height, boolean porportional) {
		this.height = height;
		
		if (porportional) {
			this.width = (int) (getRatio() * this.height);
		}
		
		refreshLayer();
	}
	
	@Override
	public void setWidthP(double p) {
		super.setWidthP(p);
		refreshLayer();
	}
	
	@Override
	public void setHeightP(double p) {
		super.setHeightP(p);
		refreshLayer();
	}
	
	public double getRatio() {
		return this.bi.getWidth() / this.bi.getHeight();
	}
	
	public void renderChildren() {
		synchronized(children) {
			for (UIElement element : children) {
				BufferedImage ebi = element.render();
				
				Graphics2D g = surface.createGraphics();
		        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		        
		        Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, new Float(element.transparency));
		        g.setComposite(comp);
		        g.drawImage(ebi, null, element.getX(), element.getY());
			}
		}
	}
	
	public BufferedImage render() {
		Graphics2D g = surface.createGraphics();
		BufferedImage adjusted = bi;
		
		// Apply post processing
		if (brightness != 1.0f) {
			RescaleOp op = new RescaleOp(new Float(brightness), 0, null);
			adjusted = op.filter(bi, null);
		}
		
		g.drawImage(adjusted, null, 0, 0);
		
        this.renderChildren();
        
        return this.surface;
    }
	
	public void setId(String id) {
		System.out.println("IN SET ID!");
		
		if (this.id != null) {
			childElements.remove(id);
			System.out.println("REMOVING " + id);
		}
		
		this.id = id;
		childElements.put(id, this);
	}
}

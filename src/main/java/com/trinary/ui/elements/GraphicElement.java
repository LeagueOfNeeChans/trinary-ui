package com.trinary.ui.elements;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

import javax.imageio.ImageIO;

import com.trinary.ui.config.ConfigHolder;

public class GraphicElement extends UIElement {
	public GraphicElement() {
		super(0, 0, 1, 1);
	}
	
	public GraphicElement(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.surface = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = this.surface.createGraphics();
		g.setComposite(AlphaComposite.Clear);
		g.fillRect(0, 0, width, height);
	}
	
	public GraphicElement(int x, int y, int width, int height, Color color) {
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
	
	public GraphicElement(int x, int y, int width, int height, String filename) {
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
	
	public GraphicElement(int x, int y, int width, int height, BufferedImage bi) {
		super(x, y, width, height);
		this.bi = bi;
	}
	
	public void refreshLayer() {
		BufferedImage bi = this.bi;
		
		this.bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		this.surface = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = this.bi.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		
		g.drawImage(bi, 0, 0, width, height, null);
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
	
	public void renderChildren() {
		Collections.sort(children);
		for (UIElement element : children) {
			BufferedImage ebi = element.render();
			
			Graphics2D g = surface.createGraphics();
	        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
	        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	        
	        System.out.println("TRANSPARENCY: " + element.transparency);
	        
	        Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, element.transparency);
	        g.setComposite(comp);
	        g.drawImage(ebi, null, element.getX(), element.getY());
		}
	}
	
	public BufferedImage render() {
		Graphics2D g = surface.createGraphics();
		g.drawImage(bi, null, 0, 0);
		
        this.renderChildren();
        return this.surface;
    }
}

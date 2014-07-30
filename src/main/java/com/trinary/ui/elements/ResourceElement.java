package com.trinary.ui.elements;

import java.awt.image.BufferedImage;

import com.trinary.ui.config.ResourceStore;

public class ResourceElement extends GraphicElement {
	public ResourceElement() {
		super();
	}
	
	public void changeResource(String name, Boolean perserveDimensions) {
		Resource r = ResourceStore.getResource(name);
		
		if (r == null) {
			return;
		}
		
		this.bi = r.getImage();
		
		if (perserveDimensions) {
			this.width = r.getWidth();
			this.height = r.getHeight();
		}
		
		this.surface = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public double getRatio() {
		return this.bi.getWidth() / this.bi.getHeight();
	}
	
	public void scale(float percent) {
		this.width = (int) (width * percent);
		this.height = (int) (height * percent);
		refreshLayer();
	}
	
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		refreshLayer();
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
}

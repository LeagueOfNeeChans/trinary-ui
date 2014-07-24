package com.trinary.ui.elements;

import java.awt.image.BufferedImage;

import com.trinary.ui.config.ResourceStore;
import com.trinary.util.Location;

public class ResourceElement extends GraphicElement {
	public void changeResource(String name) {
		Resource r = ResourceStore.getResource(name);
		
		if (r == null) {
			return;
		}
		
		this.bi = r.getImage();
		this.width = r.getWidth();
		this.height = r.getHeight();
		
		this.surface = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public double getRatio() {
		return this.bi.getWidth() / this.bi.getHeight();
	}
	
	public void move(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void move(Location location) {
		if (location.getLeft() != null) {
			this.x = location.getLeft();
		} else if (location.getRight() != null) {
			this.x = location.getRight() - this.width;
		}
		
		if (location.getTop() != null) {
			this.y = location.getTop();
		} else if (location.getBottom() != null) {
			this.y = location.getBottom() - this.height;
		}
	}
	
	public void moveAndScale(Location location) {
		this.x = location.getLeft();
		this.y = location.getTop();
		
		this.width = location.getRight() - location.getLeft();
		this.height = location.getBottom() - location.getTop();
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

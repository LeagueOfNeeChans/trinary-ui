package com.trinary.ui.elements;

import java.awt.image.BufferedImage;

import com.trinary.ui.config.ResourceStore;
import com.trinary.ui.transitions.Transition;

public class ResourceElement extends ContainerElement {
	protected Transition transition;
	
	public ResourceElement() {
		super();
		changeResource("_transparent", false);
	}
	
	public ResourceElement(String resourceName) {
		super();
		changeResource(resourceName, false);
	}
	
	public ResourceElement(int x, int y, int width, int height) {
		super(x, y, width, height);
		changeResource("_transparent", false);
	}
	
	public ResourceElement(int x, int y, int width, int height, String resourceName) {
		super(x, y, width, height);
		changeResource(resourceName, false);
	}

	public void changeResource(String name, Boolean perserveDimensions) {
		Resource r = ResourceStore.getResource(name);
		if (r == null) {
			return;
		}
		
		this.bi = r.getImage();
		
		if (perserveDimensions || this.width == 0 || this.height == 0) {
			this.width = r.getWidth();
			this.height = r.getHeight();
		}
		
		refreshLayer();
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

	public Transition getTransition() {
		return transition;
	}

	public void setTransition(Transition transition) {
		this.transition = transition;
	}
	
	@Override
	public BufferedImage render() {
		super.render();
		
		if (transition != null) {
			transition.step(this);
		}
		
		return this.surface;
	}
}

package com.trinary.ui.elements;

import java.awt.image.BufferedImage;

import com.trinary.ui.config.ResourceStore;
import com.trinary.ui.transitions.Transition;

public class ResourceElement extends ContainerElement {
	protected Transition transition;
	
	public ResourceElement() {
		super();
		changeResource("_black", false);
	}
	
	public ResourceElement(int x, int y, int width, int height) {
		super(x, y, width, height);
		changeResource("_black", false);
	}

	public void changeResource(String name, Boolean perserveDimensions) {
		Resource r = ResourceStore.getResource(name);
		
		System.out.println("CHANGING RESOURCE TO " + name);
		
		if (r == null) {
			System.out.println("CAN'T FIND RESOURCE WITH NAME " + name);
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

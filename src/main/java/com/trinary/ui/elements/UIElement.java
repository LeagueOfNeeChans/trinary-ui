package com.trinary.ui.elements;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class UIElement {
	protected int x, y;
	protected int width, height;
	protected float transparency = 1.0f;
	
	protected BufferedImage bi;
	protected BufferedImage surface;
	
	protected UIElement parent = null;
	protected ArrayList<UIElement> children = new ArrayList<>();
	
	public UIElement() {
		// TODO Auto-generated constructor stub
	}
	
	public UIElement(int x, int y, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}

	public UIElement getParent() {
		return parent;
	}

	public void setParent(UIElement parent) {
		this.parent = parent;
	}
	
	public ArrayList<UIElement> getChildren() {
		return children;
	}

	public void addChild(UIElement child) {
		child.parent = this;
		this.children.add(child);
	}
	
	public BufferedImage getLayer() {
		return bi;
	}
	
	public float getTransparency() {
		return transparency;
	}

	public void setTransparency(float transparency) {
		this.transparency = transparency;
	}

	public abstract BufferedImage render();
}

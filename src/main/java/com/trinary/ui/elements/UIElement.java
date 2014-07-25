package com.trinary.ui.elements;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.trinary.util.Location;

public abstract class UIElement implements Comparable<UIElement> {
	protected Location pos = new Location();
	protected int width, height;
	protected float transparency = 1.0f;
	protected int zIndex = 0;
	
	protected BufferedImage bi;
	protected BufferedImage surface;
	
	protected UIElement parent = null;
	protected ArrayList<UIElement> children = new ArrayList<>();
	
	public UIElement() {
		// TODO Auto-generated constructor stub
	}
	
	public UIElement(int x, int y, int width, int height) {
		super();
		this.pos.setLeft(x);
		this.pos.setTop(y);
		this.width = width;
		this.height = height;
	}
	
	public <T extends UIElement> T addChild(Class<T> clazz) {
		try {
			T child = clazz.newInstance();
			this.addChild(child);
			
			return child;
		} catch (Exception e) {
			return null;
		}
	}
	
	public void setWidthP(double p) {
		if (parent != null) {
			this.width = (int) (parent.width * p);
		}
	}
	
	public void setHeightP(double p) {
		if (parent != null) {
			this.height = (int) (parent.height * p);
		}
	}
	
	public void center() {
		if (parent != null) {
			int x = (parent.width - this.width)/2;
			this.pos.setLeft(x);
		}
	}
	
	public int getX() {
		if (this.pos.getLeft() != null) {
			return this.pos.getLeft();
		} else if (this.pos.getRight() != null) {
			return this.pos.getRight() - this.width;
		} else {
			return 0;
		}
	}
	
	public int getY() {
		if (this.pos.getTop() != null) {
			return this.pos.getTop();
		} else if (this.pos.getBottom() != null) {
			return this.pos.getBottom() - this.height;
		} else {
			return 0;
		}
	}
	
	public void setX(int x) {
		this.pos.setLeft(x);
	}
	
	public void setY(int y) {
		this.pos.setTop(y);
	}
	
	public void move(int x, int y) {
		this.setX(x);
		this.setY(y);
	}
	
	public void move(Location pos) {
		this.pos = pos;
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
	
	public int getzIndex() {
		return zIndex;
	}

	public void setzIndex(int zIndex) {
		this.zIndex = zIndex;
	}
	
	@Override
	public int compareTo(UIElement o) {
		return new Integer(zIndex).compareTo(new Integer(o.zIndex));
	}

	public abstract BufferedImage render();
}

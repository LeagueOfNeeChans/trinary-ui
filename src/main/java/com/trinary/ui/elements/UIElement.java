package com.trinary.ui.elements;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import com.trinary.util.Location;

public abstract class UIElement implements Comparable<UIElement> {
	protected Location pos = new Location();
	protected int width, height;
	protected int zIndex = -1;
	
	protected float brightness = 1.0f;
	protected float transparency = 1.0f;
	protected float lastTransparency = 1.0f;
	protected String hAlign = "none";
	protected String vAlign = "none";
	
	protected BufferedImage bi;
	protected BufferedImage surface;
	
	protected UIElement parent = null;
	protected ArrayList<UIElement> children = new ArrayList<>();
	
	public UIElement() {
		// TODO Auto-generated constructor stub
	}
	
	public UIElement(int x, int y, int width, int height) {
		super();
		this.pos = new Location(String.format("left: %d, top: %d", x, y));
		this.width = width;
		this.height = height;
	}
	
	public <T extends UIElement> T addChild(Class<T> clazz) {
		try {
			T child = clazz.newInstance();
			this.addChild(child);
			
			return child;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public void addChild(UIElement child) {
		child.parent = this;
		this.children.add(child);
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
	
	public void sethAlign(String hAlign) {
		this.hAlign = hAlign;
	}

	public void setvAlign(String vAlign) {
		this.vAlign = vAlign;
	}

	public int getX() {
		switch (this.hAlign) {
		case "center":
			return (parent.width - this.width)/2;
		case "right":
			return parent.width - this.width;
		case "left":
			return 0;
		case "none":
		default:
			break;
		}
		
		if (this.pos.getLeft() != null) {
			if (this.pos.getLeft().isPercentage()) {
				return (int) (this.pos.getLeft().getPos()/100.0f * this.parent.getWidth());
			} else {
				return this.pos.getLeft().getPos();
			}
		} else if (this.pos.getRight() != null) {
			if (this.pos.getRight().isPercentage()) {
				return (int) ((this.pos.getRight().getPos()/100.0f * this.parent.getWidth()) - this.width);
			} else {
				return this.pos.getRight().getPos() - this.width;
			}
		} else {
			return 0;
		}
	}
	
	public int getY() {
		switch (this.vAlign) {
		case "center":
			return (parent.height - this.height)/2;
		case "top":
			return 0;
		case "bottom":
			return parent.height - this.height;
		case "none":
		default:
			break;
		}
		
		if (this.pos.getTop() != null) {
			if (this.pos.getTop().isPercentage()) {
				return (int) (this.pos.getTop().getPos()/100.0f * this.parent.getHeight());
			} else {
				return this.pos.getTop().getPos();
			}
		} else if (this.pos.getBottom() != null) {
			if (this.pos.getBottom().isPercentage()) {
				return (int) (this.pos.getBottom().getPos()/100.0f * this.parent.getHeight() - this.height);
			} else {
				return this.pos.getBottom().getPos() - this.height;
			}
		} else {
			return 0;
		}
	}
	
	public void setX(int x) {
		this.pos.getLeft().setPos(x);
	}
	
	public void setY(int y) {
		this.pos.getTop().setPos(y);
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
	
	public BufferedImage getLayer() {
		return bi;
	}
	
	public float getTransparency() {
		return transparency;
	}

	public void setTransparency(float transparency) {
		this.transparency = transparency;
	}
	
	public void hide() {
		this.lastTransparency = this.transparency;
		this.transparency = 0.0f;
	}
	
	public void show() {
		this.transparency = this.lastTransparency;
	}
	
	public int getzIndex() {
		return zIndex;
	}

	public void setzIndex(int zIndex) {
		this.zIndex = zIndex;
	}
	
	public float getBrightness() {
		return brightness;
	}

	public void setBrightness(float brightness) {
		setBrightness(brightness, false);
	}
	
	public void setBrightness(float brightness, boolean recursive) {
		this.brightness = brightness;
		
		if (recursive) {
			for(UIElement child : this.children) {
				child.setBrightness(brightness, recursive);
			}
		}
	}
	
	public void sortChildrenByZIndex() {
		Collections.sort(children);
		for (UIElement element : children) {
			element.sortChildrenByZIndex();
		}
	}

	@Override
	public int compareTo(UIElement o) {
		return new Integer(zIndex).compareTo(new Integer(o.zIndex));
	}
	
	public Point getAbsolute() {
		UIElement ptr = this.parent;
		Point abs = new Point(this.getX(), this.getY());
		
		while (ptr != null) {
			abs.x += ptr.getX();
			abs.y += ptr.getY();
			ptr = ptr.getParent();
		}
		
		return abs;
	}

	public boolean rectangleContains(int x, int y) {
		return  x > this.getAbsolute().x &&
				y > this.getAbsolute().y &&
				x < this.getAbsolute().x + this.width &&
				y < this.getAbsolute().y + this.height;
	}

	public abstract BufferedImage render();
}

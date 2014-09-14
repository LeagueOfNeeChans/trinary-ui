package com.text.formatted.elements;

import java.awt.Point;
import java.util.concurrent.CopyOnWriteArrayList;

import com.trinary.ui.elements.ChildElement;
import com.trinary.ui.elements.UIElement;

public class PositionedElement implements ChildElement {
	protected static CopyOnWriteArrayList<PositionedElement> marked = new CopyOnWriteArrayList<>();
	protected Point relative;
	protected int width, height;
	
	protected String id;

	protected UIElement parent;
	
	public PositionedElement() {
		relative = new Point(0, 0);
		width = 0;
		height = 0;
	}
	
	public PositionedElement(Point origin, Point relative, int width, int height) {
		super();
		this.relative = relative;
		this.width = width;
		this.height = height;
	}
	
	public static CopyOnWriteArrayList<PositionedElement> getMarked() {
		return marked;
	}

	public Point getRelative() {
		return relative;
	}

	public void setRelative(Point relative) {
		this.relative = relative;
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

	public void setParent(UIElement parentElement) {
		this.parent = parentElement;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void mark(String name) {
		System.out.println("MARKED " + this);
		
		this.id = name;
		marked.add(this);
	}
	
	public String toString() {
		return String.format("%d, %d (%d, %d)", this.relative.x, this.relative.y, this.width, this.height);
	}
	
	public Point getAbsolute() {
		UIElement ptr = this.parent;
		Point abs = new Point(this.relative);
		
		while (ptr != null) {
			abs.x += ptr.getX();
			abs.y += ptr.getY();
			ptr = ptr.getParent();
		}
		
		return abs;
	}
	
	public boolean rectangleContains(int x, int y) {		
		return  x >= this.getAbsolute().x &&
				y >= this.getAbsolute().y &&
				x <= this.getAbsolute().x + this.width &&
				y <= this.getAbsolute().y + this.height;
	}
}

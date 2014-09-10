package com.text.formatted.elements;

import java.awt.Point;
import java.util.ArrayList;

import com.trinary.ui.elements.ChildElement;
import com.trinary.ui.elements.UIElement;

public class PositionedElement implements ChildElement {
	protected static ArrayList<PositionedElement> marked = new ArrayList<>();
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
	
	public static ArrayList<PositionedElement> getMarked() {
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
		/*
		System.out.println("ID: " + id);
		
		System.out.println(String.format("X: %d <= %d <= %d? %s", 
				this.getAbsolute().x, 
				x, 
				this.getAbsolute().x + this.width,
				x > this.getAbsolute().x && x < this.getAbsolute().x + this.width));
		System.out.println(String.format("Y: %d <= %d <= %d? %s", 
				this.getAbsolute().y, 
				y, 
				this.getAbsolute().y + this.height,
				y >= this.getAbsolute().y && y <= this.getAbsolute().y + this.height));
		
		System.out.println(String.format("CONTAINS? %s", 
				(x >= this.getAbsolute().x &&
				y >= this.getAbsolute().y) &&
				(x <= this.getAbsolute().x + this.width &&
				y <= this.getAbsolute().y + this.height)));
		*/
		
		return  x >= this.getAbsolute().x &&
				y >= this.getAbsolute().y &&
				x <= this.getAbsolute().x + this.width &&
				y <= this.getAbsolute().y + this.height;
	}
}

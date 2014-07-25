package com.trinary.util;

public class Location {
	protected Integer 
				top = null, 
				left = null, 
				bottom = null, 
				right = null;
	
	public Location() {
		// TODO Auto-generated constructor stub
	}
	
	public Location(Integer left, Integer top, Integer right, Integer bottom) {
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public Integer getLeft() {
		return left;
	}

	public void setLeft(Integer left) {
		this.left = left;
	}

	public Integer getBottom() {
		return bottom;
	}

	public void setBottom(Integer bottom) {
		this.bottom = bottom;
	}

	public Integer getRight() {
		return right;
	}

	public void setRight(Integer right) {
		this.right = right;
	}
}

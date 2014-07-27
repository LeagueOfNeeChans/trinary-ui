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
	
	public Location(String locString) {
		String[] pairs = locString.split(",");
		
		for (String pair : pairs) {
			pair = pair.trim();
			String[] kv = pair.split(":");
			
			if (kv.length < 2) {
				continue;
			}
			
			kv[0] = kv[0].trim();
			kv[1] = kv[1].trim();
			
			try {
				switch(kv[0]) {
				case "top":
					top = Integer.parseInt(kv[1]);
					break;
				case "left":
					left = Integer.parseInt(kv[1]);
					break;
				case "bottom":
					bottom = Integer.parseInt(kv[1]);
					break;
				case "right":
					right = Integer.parseInt(kv[1]);
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("Unable to parse " + kv[1] + " to int!");
				continue;
			}
		}
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
	
	@Override
	public String toString() {
		return String.format(""
				+ "left:    %s\n"
				+ "top:     %s\n"
				+ "right:   %s\n"
				+ "bottom:  %s",
				left, top, right, bottom);
	}
}

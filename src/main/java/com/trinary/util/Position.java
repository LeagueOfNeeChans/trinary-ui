package com.trinary.util;

public class Position {
	private Integer pos;
	private Boolean percentage;
	
	public Position(Integer pos, Boolean percentage) {
		super();
		this.pos = pos;
		this.percentage = percentage;
	}

	public Integer getPos() {
		return pos;
	}
	
	public void setPos(Integer pos) {
		this.pos = pos;
	}
	
	public Boolean isPercentage() {
		return percentage;
	}
	
	public String toString() {
		return String.format("%s%s", pos, percentage ? "%" : "px");
	}
}

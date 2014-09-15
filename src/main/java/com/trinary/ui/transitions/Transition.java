package com.trinary.ui.transitions;

import com.trinary.ui.elements.ResourceElement;

public abstract class Transition {
	protected ResourceElement e;
	protected String to;
	protected Boolean perserveDimensions;
	protected Double speed = 1.0;
	protected String state = "idle";
	
	public Transition(String to, Boolean perserveDimensions) {
		this.to = to;
		this.perserveDimensions = perserveDimensions;
	}
	
	public Transition(String to, Boolean perserveDimensions, Double speed) {
		this.to = to;
		this.speed = speed;
		this.perserveDimensions = perserveDimensions;
	}
	
	public abstract void step(ResourceElement e);
}

package com.trinary.ui.transitions;

import com.trinary.ui.elements.ResourceElement;

public abstract class Transition {
	protected ResourceElement e;
	protected String to;
	protected Double speed = 1.0;
	protected String state = "idle";
	
	public Transition(String to) {
		this.to = to;
	}
	
	public Transition(String to, Double speed) {
		this.to = to;
		this.speed = speed;
	}
	
	public abstract void step(ResourceElement e);
}

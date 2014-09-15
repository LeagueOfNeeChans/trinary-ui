package com.trinary.ui.transitions;

import com.trinary.ui.elements.AnimatedElement;
import com.trinary.ui.elements.Monitorable;

public abstract class Transition implements Monitorable {
	protected AnimatedElement e;
	protected String to;
	protected Boolean perserveDimensions;
	protected Double speed = 1.0;
	protected String state = "idle";
	
	protected Boolean done = false;
	
	public Transition(String to, Boolean perserveDimensions) {
		this.to = to;
		this.perserveDimensions = perserveDimensions;
	}
	
	public Transition(String to, Boolean perserveDimensions, Double speed) {
		this.to = to;
		this.speed = speed;
		this.perserveDimensions = perserveDimensions;
	}
	
	@Override
	public boolean isBusy() {
		// TODO Auto-generated method stub
		return !done;
	}

	public abstract void step(AnimatedElement e);
}

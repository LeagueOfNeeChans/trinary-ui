package com.trinary.ui.elements;

import java.awt.image.BufferedImage;

import com.trinary.ui.transitions.Transition;

public class AnimatedElement extends ResourceElement implements Monitorable {
	protected Transition transition;
	
	@Override
	public BufferedImage render() {
		surface = super.render();

		if (transition != null) {
			transition.step(this);
		}
		
		return this.surface;
	}
	
	public Transition getTransition() {
		return transition;
	}

	public void setTransition(Transition transition) {
		this.transition = transition;
	}

	@Override
	public boolean isBusy() {
		return transition != null;
	}
}

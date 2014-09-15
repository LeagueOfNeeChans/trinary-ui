package com.trinary.ui.transitions;

import com.trinary.ui.elements.AnimatedElement;

public class FadeOutIn extends Transition {

	public FadeOutIn(String to, Boolean perserveDimensions) {
		super(to, perserveDimensions);
		// TODO Auto-generated constructor stub
	}
	
	public FadeOutIn(String to, Boolean perserveDimensions, Double speed) {
		super(to, perserveDimensions, speed);
	}

	@Override
	public void step(AnimatedElement e) {
		if (state == "idle") {
			state = "fadingOut";
		}
		
		switch (state) {
		case "fadingIn":
			fadingIn(e);
			break;
		case "fadingOut":
			fadingOut(e);
			break;
		}
	}

	protected void fadingIn(AnimatedElement e) {
		if (e.getTransparency() == 1.0f) {
			state = "done";
			e.setTransition(null);
		} else {
			if (e.getTransparency() + 0.1f > 1) {
				e.setTransparency(1.0f);
			} else {
				e.setTransparency(e.getTransparency() + 0.1f);
			}
		}
	}
	
	protected void fadingOut(AnimatedElement e) {
		if (e.getTransparency() == 0.0f) {
			state = "fadingIn";
			e.changeResource(to, perserveDimensions);
		} else {
			if (e.getTransparency() - 0.1f < 0) {
				e.setTransparency(0.0f);
			} else {
				e.setTransparency(e.getTransparency() - 0.1f);
			}
		}
	}
}

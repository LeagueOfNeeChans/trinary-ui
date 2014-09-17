package com.trinary.ui.transitions;

import com.trinary.ui.elements.AnimatedElement;

public class FadeOut extends Transition {

	public FadeOut(Boolean perserveDimensions) {
		super(null, perserveDimensions);
		// TODO Auto-generated constructor stub
	}
	
	public FadeOut(Boolean perserveDimensions, Double speed) {
		super(null, perserveDimensions, speed);
	}

	@Override
	public void step(AnimatedElement e) {
		if (state == "idle") {
			state = "fadingOut";
		}
		
		switch (state) {
		case "fadingOut":
			fadingOut(e);
			break;
		}
	}
	
	protected void fadingOut(AnimatedElement e) {
		if (e.getTransparency() == 0.0f) {
			state = "done";
			e.setTransition(null);
		} else {
			if (e.getTransparency() - 0.1f < 0) {
				e.setTransparency(0.0f);
			} else {
				e.setTransparency(e.getTransparency() - 0.1f);
			}
		}
	}
}

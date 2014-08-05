package com.trinary.ui.transitions;

import com.trinary.ui.elements.ResourceElement;

public class FadeOutIn extends Transition {

	public FadeOutIn(String to) {
		super(to);
		// TODO Auto-generated constructor stub
	}
	
	public FadeOutIn(String to, Double speed) {
		super(to, speed);
	}

	@Override
	public void step(ResourceElement e) {
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

	protected void fadingIn(ResourceElement e) {
		if (e.getTransparency() == 1.0f) {
			state = "idle";
			e.setTransition(null);
		} else {
			if (e.getTransparency() + 0.1f > 1) {
				e.setTransparency(1.0f);
			} else {
				e.setTransparency(e.getTransparency() + 0.1f);
			}
		}
	}
	
	protected void fadingOut(ResourceElement e) {
		if (e.getTransparency() == 0.0f) {
			state = "fadingIn";
			e.changeResource(to, true);
		} else {
			if (e.getTransparency() - 0.1f < 0) {
				e.setTransparency(0.0f);
			} else {
				e.setTransparency(e.getTransparency() - 0.1f);
			}
		}
	}
}

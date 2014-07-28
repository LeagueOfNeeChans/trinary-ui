package com.trinary.ui.elements;

import java.awt.image.BufferedImage;

public class AnimatedElement extends ResourceElement {
	protected String transitionEffect = "none";
	protected String transitioningInto;
	protected String state = "idle";
	
	protected void fadingIn() {
		if (this.transparency == 1.0f) {
			state = "idle";
			transitionEffect = "none";
		} else {
			if (transparency + 0.1f > 1) {
				transparency = 1.0f;
			} else {
				transparency += 0.1f;
			}
		}
	}
	
	protected void fadingOut() {
		if (this.transparency == 0.0f) {
			state = "fadingIn";
			this.changeResource(transitioningInto);
		} else {
			if (transparency - 0.1f < 0) {
				transparency = 0.0f;
			} else {
				transparency -= 0.1f;
			}
		}
	}
	
	protected void fadeInto() {
		if (state == "idle") {
			state = "fadingOut";
		}
		
		switch (state) {
		case "fadingIn":
			fadingIn();
			break;
		case "fadingOut":
			fadingOut();
			break;
		}
	}
	
	@Override
	public BufferedImage render() {
		switch (transitionEffect) {
		case "fadeInto":
			fadeInto();
			break;
		}
		
		super.render();
		
		return this.surface;
	}
	
	public void transitionInto(String resourceName, String effectName) {
		this.transitioningInto = resourceName;
		this.transitionEffect = effectName;
	}
}

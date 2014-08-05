package com.trinary.ui.elements;

import java.awt.image.BufferedImage;

public class AnimatedElement extends ResourceElement {
	@Override
	public BufferedImage render() {
		super.render();
		
		return this.surface;
	}
}

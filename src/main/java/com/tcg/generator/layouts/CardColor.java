package com.tcg.generator.layouts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.awt.Color;

public class CardColor {
	protected Integer r, g, b;

	@JsonCreator
	CardColor(
			@JsonProperty("r") Integer r,
			@JsonProperty("g") Integer g,
			@JsonProperty("b")Integer b
			) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Color getColor() {
		return new Color(r, g, b);
	}
}

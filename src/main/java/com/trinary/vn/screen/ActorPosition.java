package com.trinary.vn.screen;

import com.trinary.util.Location;

public class ActorPosition {
	protected Location position;
	protected Integer zIndex;
	protected Double scale;
	
	public ActorPosition(Location position, Integer zIndex, Double scale) {
		super();
		this.position = position;
		this.zIndex = zIndex;
		this.scale = scale;
	}
	
	public ActorPosition(String position, Integer zIndex, Double scale) {
		super();
		this.position = new Location(position);
		this.zIndex = zIndex;
		this.scale = scale;
	}

	public Location getPosition() {
		return position;
	}

	public void setPosition(Location position) {
		this.position = position;
	}

	public Integer getzIndex() {
		return zIndex;
	}

	public void setzIndex(Integer zIndex) {
		this.zIndex = zIndex;
	}

	public Double getScale() {
		return scale;
	}

	public void setScale(Double scale) {
		this.scale = scale;
	}
}

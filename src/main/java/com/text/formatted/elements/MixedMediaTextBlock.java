package com.text.formatted.elements;

import java.util.ArrayList;

public class MixedMediaTextBlock {
	protected ArrayList<MixedMediaText> lines = new ArrayList<MixedMediaText>();
	protected Integer dIndex = 0;
	protected Boolean animated = false;
	protected Integer speed = 500;
	protected Boolean done = false;
	
	public MixedMediaTextBlock(ArrayList<MixedMediaText> lines, Boolean animated) {
		this.animated = animated;
		
		for (MixedMediaText line : lines) {
			this.lines.add(line);
		}
		
		if (!this.animated) {
			this.done = true;
		}
	}
	
	public Integer length() {
		Integer t = 0;
		for (MixedMediaText element : lines) {
			t += element.length();
		}
		
		return t;
	}
	
	public ArrayList<MixedMediaText> getLines() {
		if (!this.done) {
			System.out.println(String.format("%d/%d", this.dIndex, this.length()));
			
			if (this.dIndex < this.length()) {
				return this.substring(dIndex++);
			} else {
				this.done = true;
			}
		}
		
		return lines;
	}
	
    public ArrayList<MixedMediaText> substring(Integer endIndex) {
    	Integer t = 0;
    	ArrayList<MixedMediaText> subList = new ArrayList<MixedMediaText>();
    	for (MixedMediaText element: lines) {
    		t += element.length();
    		
			if (t >= endIndex) {
				subList.add(element.substring(element.length() - (t - endIndex)));
				break;
			} else {
				subList.add(element);
			}
    	}
    	
    	return subList;
    }

	public Boolean getDone() {
		return done;
	}
}
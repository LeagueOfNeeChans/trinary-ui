/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcg.generator.layouts;

import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcg.generator.layouts.elements.GenericElement;

/**
 *
 * @author mmain
 */
public class UILayout {
    protected ArrayList<GenericElement> elements;
    protected ArrayList<Resource>resources;
    protected String name;
    protected Integer width, height;
    protected Boolean sorted = false;
    protected ObjectMapper mapper = new ObjectMapper();
    
    public UILayout() {
    	this.elements = new ArrayList<GenericElement>();
    	this.resources = new ArrayList<Resource>();
    }
    
    @JsonCreator
    public UILayout(
            @JsonProperty("name")      String name,
            @JsonProperty("elements")  ArrayList<GenericElement> elements,
            @JsonProperty("resources") ArrayList<Resource> resources,
            @JsonProperty("width")     Integer width,
            @JsonProperty("height")    Integer height
            ) {
        this.name = name;
        this.elements = elements;
        this.width = width;
        this.height = height;
        this.resources = resources;
    }
    
    public void addElement(GenericElement element) {
    	elements.add(element);
    	sorted = false;
    }
    
    public void addResource(Resource resource) {
    	resources.add(resource);
    }
    
    public String getName() {
        return name;
    }
    
    public ArrayList<GenericElement> getElements() {
        return elements;
    }
    
    public GenericElement getElementByMapping(String fieldName) {
        for (GenericElement element : elements) {
            if (element.getMappings() != null && element.getMappings().get(fieldName) != null) {
                return element;
            }
        }
        return null;
    }
    
    public ArrayList<GenericElement> getElementsByType(String type) {
        ArrayList<GenericElement> found = new ArrayList<>();
        for (GenericElement element : elements) {
            if (element.getName().equals(type)) {
                found.add(element);
            }
        }
        return found;
    }
    
    public GenericElement getElementByName(String elementName) {
        for (GenericElement element : elements) {
            if (element.getName().equals(elementName)) {
                return element;
            }
        }
        return null;
    }
    
    public void setWidth(Integer width) {
		this.width = width;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
        return width;
    }
    
    public Integer getHeight() {
        return height;
    }
    
    public Integer getResourceMaxHeight() {
        Integer max = 0;
        for (Resource resource : resources) {
            if (max < resource.getHeight()) {
                max = resource.getHeight();
            }
        }
        
        return max;
    }
    
    public Resource getResource(String resourceName) {
        for (Resource resource : resources) {
            if (resource.getResourceName().equals(resourceName)) {
                return resource;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        String s = "";
        
        for (GenericElement element : elements) {
            s += element.toString() + "\n";
        }
        
        return s;
    }
    
	public void sort() {
    	if (!sorted) {
    		Collections.sort(elements);
    		sorted = true;
    	}
    }
}

package com.trinary.ui.config;

import java.util.HashMap;

import com.trinary.ui.elements.Resource;

public class ResourceStore {
	protected static HashMap<String, Resource> resources = new HashMap<>();

	public static void addResource(String name, String filename) {
		resources.put(name, new Resource(filename, name));
	}
	
	public static Resource getResource(String name) {
		return resources.get(name);
	}
}

package com.trinary.ui.config;

import java.io.File;
import java.util.HashMap;

import com.trinary.ui.elements.Resource;

public class ResourceStore {
	protected static HashMap<String, Resource> resources = new HashMap<>();

	public static void addResource(String name, String filename) {
		resources.put(name, new Resource(filename, name));
	}
	
	public static void addFolder(String directory) {
		File f = new File(ConfigHolder.getConfig("rootDirectory") + directory);
		
		for (String file : f.list()) {
			String filename = file.substring(0, file.lastIndexOf('.'));
			addResource(filename, directory + "/" + file);
		}
	}
	
	public static Resource getResource(String name) {
		return resources.get(name);
	}
}

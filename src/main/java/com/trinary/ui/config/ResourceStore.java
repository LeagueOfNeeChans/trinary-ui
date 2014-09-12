package com.trinary.ui.config;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import com.trinary.ui.elements.Resource;

public class ResourceStore {
	protected static HashMap<String, Resource> resources = new HashMap<>();
	
	static {
		resources.put("_black", new Resource(createBlackBufferedImage(800, 600)));
	}

	public static void addResource(String name, String filename) {
		resources.put(name, new Resource(filename, name));
		System.out.println("ADDED KEY: " + name);
	}
	
	public static void addFolder(String directory) {
		File f = new File(ConfigHolder.getConfig("rootDirectory") + directory);
		
		for (File node : f.listFiles()) {
			String file = node.getName();
			if (node.isDirectory()) {
				addFolder(directory + "/" + node.getName());
			} else if (node.isFile()) {
				String filename = file.substring(0, file.lastIndexOf('.'));
				if (filename != null && !filename.isEmpty()) {
					String key = (directory + "/" + filename).replaceAll("(\\\\|\\/|_)", ".");
					addResource(key, directory + "/" + file);
				}
			}
		}
	}
	
	public static Resource getResource(String name) {
		return resources.get(name);
	}
	
	public static BufferedImage createBlackBufferedImage(int width, int height) {
		BufferedImage b_img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = b_img.createGraphics();

		graphics.setPaint(Color.black);
		graphics.fillRect(0, 0, b_img.getWidth(), b_img.getHeight());
		
		return b_img;
	}
}

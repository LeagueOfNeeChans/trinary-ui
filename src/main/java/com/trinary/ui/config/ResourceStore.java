package com.trinary.ui.config;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import com.trinary.parse.xml.Formatting;
import com.trinary.ui.elements.Resource;

public class ResourceStore {
	protected static HashMap<String, Resource> resources = new HashMap<>();
	
	static {
		resources.put("_black", new Resource(createColoredBufferedImage(800, 600, Color.black)));
		resources.put("_white", new Resource(createColoredBufferedImage(800, 600, Color.white)));
		resources.put("_transparent", new Resource(createColoredBufferedImage(800, 600, null)));
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
		Resource r = resources.get(name);
		
		if (r == null && name.startsWith("#")) {
			resources.put(name, new Resource(createColoredBufferedImage(800, 600, getColor(name))));
		}
		
		return r;
	}
	
	protected static BufferedImage createColoredBufferedImage(int width, int height, Color color) {
		BufferedImage b_img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = b_img.createGraphics();

		if (color != null) {
			graphics.setPaint(color);
		}
		graphics.fillRect(0, 0, b_img.getWidth(), b_img.getHeight());
		
		return b_img;
	}
	
	protected static Color getColor(String hexString) {
    	Color color = null;
    		
		if (hexString == null) {
			return Color.black;
		}
		
		if (hexString.toCharArray()[0] == '#') {
			hexString = hexString.substring(1);
			
			if (hexString.length() != 6) {
				return null;
			}
		
    		String red   = hexString.substring(0, 2);
    		String green = hexString.substring(2, 4);
    		String blue  = hexString.substring(4, 6);
    		
    		Integer r = Integer.decode("0x" + red);
    		Integer g = Integer.decode("0x" + green);
    		Integer b = Integer.decode("0x" + blue);
    		
    		color = new Color(r, g, b);
		}
    	
    	return color;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trinary.ui.elements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcg.generator.config.ConfigHolder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author mmain
 */
public class Resource {
    protected String resourceName;
    protected String resourcePath;
    protected BufferedImage resourceImage;
    protected Integer width, height;
    
    @JsonCreator
    public Resource(@JsonProperty("file") String resourcePath, @JsonProperty("name") String resourceName) {
        this.resourcePath = resourcePath;
        this.resourceName = resourceName;
        String filePath = ConfigHolder.getConfig("rootDirectory") + resourcePath;
        try {
            this.resourceImage = ImageIO.read(new File(filePath));
            System.out.println("Successfully opened: " + filePath);
        } catch (IOException ex) {
            System.out.println("Failed to open:      " + filePath);
        }
    }
    
    public BufferedImage getImage() {
        return resourceImage;
    }
    
    public String getResourceName() {
        return resourceName;
    }
    
    public int getWidth() {
        return resourceImage.getWidth();
    }
    
    public int getHeight() {
        return resourceImage.getHeight();
    }
    
    public String toString() {
        return "\t\tresourcePath: " + resourcePath;
    }
}

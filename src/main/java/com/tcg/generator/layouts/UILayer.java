/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcg.generator.layouts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcg.generator.config.ConfigHolder;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Michael
 */
public class UILayer {
    protected String type;
    protected String file;
    protected UIColor color;
    protected Integer width, height;
    protected BufferedImage image;
    
    @JsonCreator
    public UILayer(
            @JsonProperty("type")   String type,
            @JsonProperty("file")   String file,
            @JsonProperty("color")  UIColor color,
            @JsonProperty("width")  Integer width,
            @JsonProperty("height") Integer height
            ) {
        this.type = type;
        this.file = file;
        this.color = color;
        
        if (file != null) {
            try {
                this.image = ImageIO.read(new File(ConfigHolder.getConfig("rootDirectory") + file));
            } catch (IOException ex) {
                System.out.println("Unable to open: " + ConfigHolder.getConfig("rootDirectory") + file);
                return;
            }
            System.out.println("Successfully opened: " + ConfigHolder.getConfig("rootDirectory") + file);
        } else if (color != null) {
            this.width = width;
            this.height = height;
            this.image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
            
            Graphics graphics = this.image.getGraphics();
            graphics.setColor(color.getColor());
            graphics.fillRect(0, 0, this.width, this.height);
        }
    }
    
    public Integer getWidth() {
        return image.getWidth();
    }
    
    public Integer getHeight() {
        return image.getHeight();
    }
    
    public BufferedImage getImage() {
        return image;
    }
}
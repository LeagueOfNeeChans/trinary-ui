/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.text.formatted.elements;

import java.awt.image.BufferedImage;

/**
 *
 * @author mmain
 */
public abstract class MarkupElement {
    public abstract void drawTo(BufferedImage bi, int x, int y);
    
    public abstract String getText();
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcg.generator.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcg.generator.cards.reflect.UI;
import com.tcg.generator.config.ConfigHolder;
import com.tcg.generator.layouts.Resource;
import com.tcg.generator.layouts.UIFont;
import com.tcg.generator.layouts.UILayout;
import com.tcg.generator.layouts.elements.ElementMapping;
import com.tcg.generator.layouts.elements.GenericElement;
import com.text.formatted.elements.ImageInsert;
import com.text.formatted.elements.MarkupElement;
import com.text.formatted.elements.MixedMediaText;
import com.text.formatted.elements.TextInsert;

/**
 *
 * @author Michael
 */
public class GenericUI {
    private LinkedHashMap<String, Object> uiData;
    protected UILayout uiLayout;
    protected BufferedImage background;
    protected ObjectMapper mapper = new ObjectMapper();
    
    public GenericUI() {}
    
    public GenericUI(String background, UILayout layout, LinkedHashMap<String, Object> uiData) {
        this.uiData = uiData;

        setLayout(layout);
        setBackground(new File(background));
    }
    
    public GenericUI(String background, String layoutFile, LinkedHashMap<String, Object> uiData) {
        this.uiData = uiData;
        setLayout(layoutFile);
        setBackground(new File(background));
    }
    
    public GenericUI(BufferedImage background, String layoutFile, LinkedHashMap<String, Object> uiData) {
    	this.uiData = uiData;
    	this.background = background;
    	setLayout(layoutFile);
    }
    
    public final UILayout getLayout() {
    	return this.uiLayout;
    }
    
    public final GenericUI setLayout(UILayout layout) {
        this.uiLayout = layout;
        return this;
    }
    
    public final GenericUI setLayout(File layoutFile) {
        try {
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            this.uiLayout = mapper.readValue(layoutFile, UILayout.class);
        } catch (IOException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }
    
    public final GenericUI setLayout(String jsonData) {
        try {
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            this.uiLayout = mapper.readValue(jsonData, UILayout.class);
        } catch (IOException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }
    
    public final GenericUI setBackground(File artworkFile) {
        try {
            this.background = ImageIO.read(artworkFile);
           System.out.println("Successfully opened: " + artworkFile.getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Failed to open:      " + artworkFile.getAbsolutePath());
        }
        return this;
    }
    
    public final GenericUI setUiData(String jsonData) {
        try {
            this.uiData = mapper.readValue(jsonData, LinkedHashMap.class);
        } catch (IOException ex) {
            Logger.getLogger(GenericUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }
    
    public final GenericUI setUiData(LinkedHashMap<String, Object> cardData) {
        this.uiData = cardData;
        return this;
    }
    
    @SuppressWarnings("unchecked")
	public BufferedImage render() {
    	System.out.println("LAYOUT: " + uiLayout);
    	
        BufferedImage bi = new BufferedImage(uiLayout.getWidth(), uiLayout.getHeight(), BufferedImage.TYPE_INT_ARGB);
        drawLayer(background, bi);
        
        uiLayout.sort();
        
        ArrayList<MixedMediaText> mmtl;
        for (GenericElement element: uiLayout.getElements()) {
            
            // Test to see if layer should be drawn
            if (!element.shouldDraw(uiData)) {
                continue;
            }
            
            System.out.println("Drawing element: " + element.getName());
            
            drawLayer(element, bi);
            if (element.getMappings() != null) {
                ArrayList<String> lines = new ArrayList<>();
                for (Entry<String, ElementMapping> entry : element.getMappings().entrySet()) {
                    ElementMapping mapping =  entry.getValue();
                    String field = entry.getKey();
                    Object value = uiData.get(field);

                    if (value != null) {
                        System.out.println("\tFIELD: " +  field + " (" + value.getClass().getSimpleName() + ")");

                        if (mapping.getLabel() != null) {
                            System.out.println("\t\tLABEL: " + mapping.getLabel());
                        }
                        if (mapping.getType() != null) {
                            System.out.println("\t\tTYPE:  " + mapping.getType());
                        }
                        
                        ArrayList<String> list;
                        Integer intValue;
                        Float floatValue;
                        String stringValue;

                        String line = "";
                        
                        if (mapping.getLabel() != null) {
                            line += mapping.getLabel() + ": ";
                        }
                        
                        switch (value.getClass().getSimpleName()) {
                            case "String":
                                stringValue = (String)value;
                                line += stringValue.toString();
                                lines.add(line);
                                break;
                            case "Integer":
                                intValue = (Integer)value;
                                line += intValue.toString();
                                lines.add(line);
                                break;
                            case "Float":
                                floatValue = (Float)value;
                                line += floatValue.toString();
                                lines.add(line);
                                break;
                            case "ArrayList":
                                list = (ArrayList<String>)value;
                                for (String aLine : list) {
                                    lines.add(aLine);
                                }
                                break;
                            default:
                                //System.out.println("\t\tInvalid field type!");
                            	continue;
                        }
                    }
                }
                
                System.out.println("\tLINES: " + lines);
                
                switch(element.getType()) {
                    case "text-box":
                        if (element.hasWordWrap()) {
                            mmtl = splitAndFitMixedText(bi, lines, element);
                            drawMixedMediaText(bi, mmtl, element);
                        } else {
                            drawText(bi, lines, element);
                        }
                        break;
                    case "table":
                        drawTableCols(bi, lines, 2, element);
                        break;
                    case "image":
                    case "layer":
                        break;
                    default:
                        break;
                }
            }
        }
        
        return bi;
    }
    
    protected static BufferedImage roundCorners(BufferedImage image, int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();

        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();

        return output;
    }
    
    // ImageWriter stuff
    protected ArrayList<String> splitAndFitText(BufferedImage target, ArrayList<String> lines, GenericElement elementLayout) {
        ArrayList<String> returnLines = new ArrayList<>();
        
        for (String line : lines) {
            ArrayList<String> moreLines = splitAndFitText(target, line, elementLayout);
            returnLines.addAll(moreLines);
        }
        
        return returnLines;
    }
    
    protected ArrayList<String> splitAndFitText(BufferedImage target, String text, GenericElement elementLayout) {
        Graphics2D g = (Graphics2D) target.getGraphics();
        g.setFont(elementLayout.getFont());
        
        FontMetrics fm = g.getFontMetrics();
        
        String[] words = text.split(" ");
        
        String line = "";
        String lastLine;
        String lastWord;
        ArrayList<String> lines = new ArrayList<>();
        
        boolean first = true;
        for (String word : words) {
            lastLine = line;
            lastWord = word;
            if (first) {
                line += word;
                first = false;
            } else {
                line += " " + word;
            }
            
            int width = fm.getStringBounds(line, g).getBounds().width;
            
            if (width > elementLayout.getWidth() - elementLayout.getMarginX() * 2) {
                lines.add(lastLine);
                line = lastWord;
            }
        }
        
        lines.add(line);
        
        for (String element : lines) {
            System.out.println("LINE: " + element);
        }
        
        return lines;
    }
    
    protected ArrayList<MixedMediaText> splitAndFitMixedText(BufferedImage target, ArrayList<String> lines, GenericElement elementLayout) {
        ArrayList<MixedMediaText> returnLines = new ArrayList<>();
        
        for (String line : lines) {
            ArrayList<MixedMediaText> moreLines = splitAndFitMixedText(target, new MixedMediaText(line), elementLayout);
            returnLines.addAll(moreLines);
        }
        
        return returnLines;
    }
    
    protected ArrayList<MixedMediaText> splitAndFitMixedText(BufferedImage target, MixedMediaText text, GenericElement elementLayout) {
        Graphics2D g = target.createGraphics();
        g.setFont(elementLayout.getFont());
        
        MixedMediaText line = new MixedMediaText();
        MarkupElement lastElement;
        MarkupElement lastIcon = null;
        int iconWidth = 0;
        int width = 0;
        int lastWidth = 0;
        int formattedTextWidth = 0;
        
        ArrayList<MixedMediaText> lines = new ArrayList<>();
        
        MarkupElement element;
        int maxWidth = (elementLayout.getWidth() - elementLayout.getMarginX() * 2);
        
        while((element = text.next()) != null) {
            if (element instanceof ImageInsert) {
                iconWidth += uiLayout.getResource(element.getText()).getWidth();
            } else if (element instanceof TextInsert) {
            	TextInsert ti = (TextInsert)element;
            	
                formattedTextWidth += g.getFontMetrics(new Font(elementLayout.getUiFont().getFamily(), ti.getFontWeight(), elementLayout.getUiFont().getSize())).getStringBounds(" " + element.getText(), g).getBounds().width;
            }
            
            line.addElement(element);
            
            /*
            System.out.println(String.format(""
            		+ "FM   WIDTH:  %d\n"
            		+ "IMAGE WIDTH: %d\n"
            		+ "---------------\n"
            		+ "TOTAL:       %d\n"
            		+ "MAX:         %d\n\n",
            		formattedTextWidth,
            		iconWidth,
            		width,
            		maxWidth));
            */
            
            lastWidth = width;
            width = iconWidth + formattedTextWidth;
            
            if (width >= maxWidth) {
                lastElement = line.popElement();
                
                if (line.peekElement() instanceof ImageInsert) {
                    lastIcon = line.popElement();
                }
                
                System.out.println("LINE: " + line);
                
                line.setWidth(lastWidth);
                lines.add(line);
                
                line = new MixedMediaText();
                iconWidth = 0;
                formattedTextWidth = 0;
                                
                if (lastIcon != null) {
                    iconWidth += uiLayout.getResource(lastIcon.getText()).getWidth();
                    line.addElement(lastIcon);
                    lastIcon = null;
                }
                line.addElement(lastElement);
                
                if (lastElement instanceof ImageInsert) {
                    iconWidth += uiLayout.getResource(lastElement.getText()).getWidth();
                } else if (lastElement instanceof TextInsert) {
                	TextInsert ti = (TextInsert)element;
                	
                    formattedTextWidth = g.getFontMetrics(new Font(elementLayout.getUiFont().getFamily(), ti.getFontWeight(), elementLayout.getUiFont().getSize())).getStringBounds(" " + lastElement.getText(), g).getBounds().width;
                }
            }
        }
        System.out.println("LINE: " + line);
        lines.add(line);
        
        return lines;
    }
    
    protected void drawLayer(GenericElement elementLayout, BufferedImage target) {
        if (elementLayout.getLayer() == null) {
            return;
        }
        
        Graphics2D g = (Graphics2D) target.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        Float alpha = elementLayout.getTransparency().floatValue();
        
        int rule = AlphaComposite.SRC_OVER;
        Composite comp = AlphaComposite.getInstance(rule, alpha);
        g.setComposite(comp);
        
        g.drawImage(elementLayout.getLayer(), null, elementLayout.getX(), elementLayout.getY());
    }
    
    protected void drawLayer(BufferedImage layerImage, BufferedImage target) {
        if (layerImage == null) {
            return;
        }
        
        Graphics2D g = (Graphics2D) target.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        g.drawImage(layerImage, null, 0, 0);
    }
    
    protected void drawText(BufferedImage target, ArrayList<String> lines, GenericElement elementLayout) {
        Graphics2D g = (Graphics2D) target.getGraphics();
        g.setFont(elementLayout.getFont());
        g.setColor(elementLayout.getUiFont().getColor());
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        FontMetrics fm = g.getFontMetrics();
        
        int index = 0;
        int actualHeight = fm.getAscent() - fm.getDescent();
        int lineHeight = fm.getHeight();
        int totalHeight = lines.size() * actualHeight;
        for (String line: lines) {
            int lineWidth = (int)fm.getStringBounds(line, g).getBounds().getWidth();
            int startX, startY;
            
            switch (elementLayout.getAlign()) {
            case "right":
                startX = elementLayout.getWidth() - elementLayout.getMarginX() - lineWidth;
                break;
            case "center":
                startX = (elementLayout.getWidth() - lineWidth)/2;
                break;
            case "left":
            default:
                startX = elementLayout.getMarginX();
                break;
            }
            
            switch (elementLayout.getVAlign()) {
            case "center":
                startY = (elementLayout.getHeight() - totalHeight)/2;
                break;
            case "bottom":
                startY = elementLayout.getHeight() - elementLayout.getMarginY() - totalHeight;
                break;
            case "top":
            default:
                startY = elementLayout.getMarginY();
                break;
            }

            g.drawString(line, elementLayout.getX() + startX, (elementLayout.getY() + startY + actualHeight ) + (index * lineHeight));
            
            index++;
        }
    }
    
    protected void drawText(BufferedImage target, String text, GenericElement elementLayout) {
        Graphics2D g = (Graphics2D) target.getGraphics();
        g.setFont(elementLayout.getFont());
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        FontMetrics fm = g.getFontMetrics();
        
        int actualHeight = fm.getAscent() - fm.getDescent();
        int lineWidth = (int)fm.getStringBounds(text, g).getBounds().getWidth();
        int startX, startY;

        switch (elementLayout.getAlign()) {
        case "right":
            startX = (elementLayout.getWidth() - elementLayout.getMarginX() - lineWidth);
            break;
        case "center":
            startX = elementLayout.getMarginX() + (elementLayout.getWidth() - lineWidth)/2;
            break;
        case "left":
        default:
            startX = elementLayout.getMarginX();
            break;
        }
        
        switch (elementLayout.getVAlign()) {
        case "center":
            startY = (elementLayout.getHeight() - actualHeight)/2;
            break;
        case "bottom":
            startY = elementLayout.getHeight() - elementLayout.getMarginY() - actualHeight;
            break;
        case "top":
        default:
            startY = elementLayout.getMarginY();
            break;
        }
        
        g.setColor(elementLayout.getUiFont().getColor());
        g.drawString(text, elementLayout.getX() + startX, elementLayout.getY() + startY + actualHeight);
    }
    
    protected void drawMixedMediaText(BufferedImage target, ArrayList<MixedMediaText> lines, GenericElement elementLayout) {
        Graphics2D g = (Graphics2D) target.getGraphics();
        g.setFont(elementLayout.getFont());
        g.setColor(elementLayout.getUiFont().getColor());
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        FontMetrics fm = g.getFontMetrics();
        int actualHeight = fm.getAscent() - fm.getDescent();
        int lineHeight = fm.getHeight();
        int index = 0;
        
        System.out.println("****LINES: " + lines);
        
        for (MixedMediaText mmt: lines) {
            int offset = 0;
            int spaceWidth = fm.getStringBounds(" ", g).getBounds().width;
            int textBottom = (elementLayout.getY() + elementLayout.getMarginY() + actualHeight) + (index * lineHeight);
            
            // Debugging lines
            if (ConfigHolder.getConfig("debug") != null) {
                g.setColor(Color.GREEN);
                g.drawLine(elementLayout.getX(), textBottom, elementLayout.getWidth(), textBottom);
                g.setColor(Color.BLUE);
                g.drawLine(elementLayout.getX(), textBottom - fm.getAscent(), elementLayout.getWidth(), textBottom - fm.getAscent());
                g.setColor(Color.RED);
                g.drawLine(elementLayout.getX(), textBottom - (fm.getAscent() - fm.getDescent()), elementLayout.getWidth(), textBottom - (fm.getAscent() - fm.getDescent()));
            }
            
            g.setColor(elementLayout.getUiFont().getColor());
            
            for (MarkupElement me : mmt.getElements()) {
                int startX;
                
                switch (elementLayout.getAlign()) {
                    case "right":
                        startX = (elementLayout.getWidth() - elementLayout.getMarginX() - mmt.getWidth());
                        break;
                    case "center":
                        startX = elementLayout.getMarginX() + (elementLayout.getWidth() - mmt.getWidth())/2;
                        break;
                    case "left":
                    default:
                        startX = elementLayout.getMarginX();
                        break;
                }
                
                if (me instanceof TextInsert) {
                	TextInsert ti = (TextInsert)me;
                	
                	UIFont cf = elementLayout.getUiFont();
                	Color c = ti.getFontColor();
                	
                	if (c == null) {
                		c = cf.getColor();
                	}
                	
                    Font f = new Font(cf.getFamily(), ti.getFontWeight(), cf.getSize());
                    g.setFont(f);
                    g.setColor(c);
                    fm = g.getFontMetrics();
                    
                    g.drawString(me.getText(), elementLayout.getX() + startX + offset, textBottom);
                    offset += fm.getStringBounds(me.getText(), g).getBounds().width + spaceWidth;
                } else if (me instanceof ImageInsert) {
                    Resource r = uiLayout.getResource(me.getText());
                    Integer larger = Math.max(r.getHeight(), actualHeight);
                    Integer diff = Math.abs(r.getHeight() - actualHeight);
                    Integer correction = (int)Math.round((double)diff/2.0);
                    Integer imageDelta = larger - correction;
                    g.drawImage(r.getImage(), null, elementLayout.getX() + startX + offset, (textBottom - imageDelta));
                    offset += r.getWidth() + spaceWidth;
                }
            }
            index++;
        }
    }
    
    protected void drawTableCols(BufferedImage target, ArrayList<String> cells, int nCols, GenericElement elementLayout) {
        Graphics2D g = target.createGraphics();
        g.setFont(elementLayout.getFont());
        g.setColor(elementLayout.getUiFont().getColor());
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        FontMetrics fm = g.getFontMetrics();
        
        //Get max width
        int maxWidth = 0;
        for (String cell: cells) {
            int width = fm.getStringBounds(cell, g).getBounds().width;
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        
        int nRows = (int)Math.ceil((double)cells.size()/(double)nCols);
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                String cell = cells.get((row * nCols) + col);
                
                int lineHeight = fm.getHeight();

                g.drawString(cell, (elementLayout.getX() + elementLayout.getMarginX()) + ((maxWidth + elementLayout.getMarginX()) * col), (elementLayout.getY() + elementLayout.getMarginY() + lineHeight) + (row * lineHeight));
            }
        }
    }
}

package com.trinary.ui.elements;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.text.formatted.elements.ImageInsert;
import com.text.formatted.elements.MarkupElement;
import com.text.formatted.elements.MixedMediaText;
import com.text.formatted.elements.MixedMediaTextBlock;
import com.text.formatted.elements.TextInsert;
import com.trinary.ui.config.ConfigHolder;
import com.trinary.ui.config.ResourceStore;

public class FormattedTextElement extends GraphicElement {
	protected MixedMediaTextBlock mmtb;
	protected Font defaultFont = new Font("optima", Font.PLAIN, 16);
	protected Color defaultFontColor = Color.black;
	
	protected String alignment = "left";
	protected int marginX = 0, marginY = 0;

	public FormattedTextElement(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public void setDefaultFont(Font font) {
		this.defaultFont = font;
	}
	
	public void setText(String text) {
		this.setText(text, false);
	}
	
	public void setText(String text, Boolean animated) {
		MixedMediaText mmt = new MixedMediaText(text);
		this.mmtb = new MixedMediaTextBlock(splitAndFitMixedText(mmt), animated);
		//this.renderText();
	}
	
    public int getMarginX() {
		return marginX;
	}

	public void setMarginX(int marginX) {
		this.marginX = marginX;
	}

	public void setAlignment(String alignment) {
		this.alignment = alignment;
	}

	public int getMarginY() {
		return marginY;
	}

	public void setMarginY(int marginY) {
		this.marginY = marginY;
	}

	protected ArrayList<MixedMediaText> splitAndFitMixedText(ArrayList<String> lines) {
        ArrayList<MixedMediaText> returnLines = new ArrayList<>();
        
        for (String line : lines) {
            ArrayList<MixedMediaText> moreLines = splitAndFitMixedText(new MixedMediaText(line));
            returnLines.addAll(moreLines);
        }
        
        return returnLines;
    }
    
    protected ArrayList<MixedMediaText> splitAndFitMixedText(MixedMediaText text) {
        Graphics2D g = this.surface.createGraphics();
        g.setFont(defaultFont);
        
        MixedMediaText line = new MixedMediaText();
        MarkupElement lastElement;
        MarkupElement lastIcon = null;
        int iconWidth = 0;
        int width = 0;
        int lastWidth = 0;
        int formattedTextWidth = 0;
        
        ArrayList<MixedMediaText> lines = new ArrayList<>();
        
        int maxWidth = (this.width - marginX * 2);
        
        for (MarkupElement element : text.getElements()) {
            if (element instanceof ImageInsert) {
                iconWidth += ResourceStore.getResource(element.getText()).getWidth();
            } else if (element instanceof TextInsert) {
            	TextInsert ti = (TextInsert)element;
            	
            	Font cf = this.defaultFont;
            	Color c = ti.getFontColor();
            	
            	if (c == null) {
            		c = this.defaultFontColor;
            	}
            	
                Font f = new Font(cf.getFamily(), ti.getFontWeight(), cf.getSize());
                g.setFont(f);
                g.setColor(c);
                formattedTextWidth += g.getFontMetrics(cf).getStringBounds(" " + element.getText(), g).getBounds().width;
            }
            
            line.addElement(element);
            
            lastWidth = width;
            width = iconWidth + formattedTextWidth;
            
            if (width >= maxWidth) {
                lastElement = line.popElement();
                
                if (line.peekElement() instanceof ImageInsert) {
                    lastIcon = line.popElement();
                }
                
                line.setWidth(lastWidth);
                lines.add(line);
                
                line = new MixedMediaText();
                iconWidth = 0;
                formattedTextWidth = 0;
                                
                if (lastIcon != null) {
                    iconWidth += ResourceStore.getResource(lastIcon.getText()).getWidth();
                    line.addElement(lastIcon);
                    lastIcon = null;
                }
                line.addElement(lastElement);
                
                if (lastElement instanceof ImageInsert) {
                    iconWidth += ResourceStore.getResource(lastElement.getText()).getWidth();
                } else if (lastElement instanceof TextInsert) {
                	TextInsert ti = (TextInsert)element;
                	
                	Font cf = this.defaultFont;
                	Color c = ti.getFontColor();
                	
                	if (c == null) {
                		c = this.defaultFontColor;
                	}
                	
                    Font f = new Font(cf.getFamily(), ti.getFontWeight(), cf.getSize());
                    g.setFont(f);
                    g.setColor(c);
                	
                    formattedTextWidth = g.getFontMetrics(cf).getStringBounds(" " + lastElement.getText(), g).getBounds().width;
                }
            }
        }
        System.out.println("LINE: " + line);
        lines.add(line);
        
        return lines;
    }
    
    public void renderText() {
    	System.out.println("RENDER- FORMATTEDTEXTELEMENT");
    	
        Graphics2D g = this.surface.createGraphics();
        g.setFont(this.defaultFont);
        g.setColor(this.defaultFontColor);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        FontMetrics fm = g.getFontMetrics();
        int actualHeight = fm.getAscent() - fm.getDescent();
        int lineHeight = fm.getHeight();
        int index = 0;
        
        for (MixedMediaText mmt: mmtb.getLines()) {
            int offset = 0;
            int spaceWidth = fm.getStringBounds(" ", g).getBounds().width;
            int textBottom = (0 + marginY + actualHeight) + (index * lineHeight);
            
            // Debugging lines
            if (ConfigHolder.getConfig("debug") != null) {
                g.setColor(Color.GREEN);
                g.drawLine(0, textBottom, this.width, textBottom);
                g.setColor(Color.BLUE);
                g.drawLine(0, textBottom - fm.getAscent(), this.width, textBottom - fm.getAscent());
                g.setColor(Color.RED);
                g.drawLine(0, textBottom - (fm.getAscent() - fm.getDescent()), this.width, textBottom - (fm.getAscent() - fm.getDescent()));
            }
            
            g.setColor(this.defaultFontColor);
            
            for (MarkupElement me : mmt.getElements()) {
                int startX;
                
                switch (this.alignment) {
                    case "right":
                        startX = (this.width - this.marginX - mmt.getWidth());
                        break;
                    case "center":
                        startX = this.marginY + (this.width - mmt.getWidth())/2;
                        break;
                    case "left":
                    default:
                        startX = this.marginX;
                        break;
                }
                
                if (me instanceof TextInsert) {
                	TextInsert ti = (TextInsert)me;
                	
                	Font cf = this.defaultFont;
                	Color c = ti.getFontColor();
                	
                	if (c == null) {
                		c = this.defaultFontColor;
                	}
                	
                    Font f = new Font(cf.getFamily(), ti.getFontWeight(), cf.getSize());
                    g.setFont(f);
                    g.setColor(c);
                    fm = g.getFontMetrics();
                    
                    g.drawString(me.getText(), 0 + startX + offset, textBottom);
                    offset += fm.getStringBounds(me.getText(), g).getBounds().width + spaceWidth;
                } else if (me instanceof ImageInsert) {
                    Resource r = ResourceStore.getResource(me.getText());
                    Integer larger = Math.max(r.getHeight(), actualHeight);
                    Integer diff = Math.abs(r.getHeight() - actualHeight);
                    Integer correction = (int)Math.round((double)diff/2.0);
                    Integer imageDelta = larger - correction;
                    g.drawImage(r.getImage(), null, 0 + startX + offset, (textBottom - imageDelta));
                    offset += r.getWidth() + spaceWidth;
                }
            }
            index++;
        }
    }
    
    public BufferedImage render() {
    	super.render();
    	this.renderText();
    	
    	return this.surface;
    }
}

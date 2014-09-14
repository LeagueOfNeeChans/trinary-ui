package com.trinary.ui.elements;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;

import com.text.formatted.elements.ImageInsert;
import com.text.formatted.elements.MarkupElement;
import com.text.formatted.elements.MixedMediaText;
import com.text.formatted.elements.MixedMediaTextBlock;
import com.text.formatted.elements.PositionedElement;
import com.text.formatted.elements.TextInsert;
import com.trinary.parse.xml.Formatting;
import com.trinary.parse.xml.FormattingType;
import com.trinary.ui.config.ConfigHolder;
import com.trinary.ui.config.ResourceStore;

public class ChoiceBox extends FormattedTextElement {
	protected class Choice {
		protected String id;
		protected String text;
		protected MixedMediaTextBlock mmtb;
		
		public Choice(String id, String text) {
			super();
			this.id = id;
			this.text = text;
		}
		
		public String getId() {
			return id;
		}
		
		public void setId(String id) {
			this.id = id;
		}
		
		public String getText() {
			return text;
		}
		
		public void setText(String text) {
			this.text = text;
		}

		public MixedMediaTextBlock getMmtb() {
			return mmtb;
		}

		public void setMmtb(MixedMediaTextBlock mmtb) {
			this.mmtb = mmtb;
		}
		
		public void highlight() {
			for (MixedMediaText mmt : this.mmtb.getLines()) {
				for (MarkupElement me : mmt.getElements()) {
					if (me instanceof TextInsert) {
						TextInsert ti = (TextInsert)me;
						ti.getFormatStack().push(new Formatting(FormattingType.BOLD));
					}
				}
			}
		}
		
		public void unhighlight() {
			for (MixedMediaText mmt : this.mmtb.getLines()) {
				for (MarkupElement me : mmt.getElements()) {
					if (me instanceof TextInsert) {
						TextInsert ti = (TextInsert)me;
						ti.getFormatStack().pop();
					}
				}
			}
		}
	}
	
	ArrayList<Choice> choices = new ArrayList<>();
	Choice lastHighlighted = null;
	
	public void addChoice(String id, String text) {
		choices.add(new Choice(id, text));
	}
	
	public void clear() {
		for (Choice o : choices) {
			PositionedElement.getMarked().remove(o.getMmtb());
		}
		choices = new ArrayList<Choice>();
		lastHighlighted = null;
	}
	
	public void highlight(String id) {
		unhighlight();
		for (Choice choice : choices) {
			if (choice.getId().equals(id)) {
				lastHighlighted = choice;
				choice.highlight();
			}
		}
	}
	
	public void unhighlight() {
		if (lastHighlighted != null) {
			lastHighlighted.unhighlight();
			lastHighlighted = null;
		}
	}
	
	public void finish() {
		splitAndFitMixedText();
	}

	protected void splitAndFitMixedText() {
        for (Choice choice : choices) {
            ArrayList<MixedMediaText> moreLines = splitAndFitMixedText(new MixedMediaText(choice.getText()));
            
            MixedMediaTextBlock choiceBlock = new MixedMediaTextBlock(moreLines, false);
            choiceBlock.mark(choice.getId());
            choice.setMmtb(choiceBlock);
        }
	}

	@Override
	public void renderText() {
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
        
        for (Choice choice : choices) {
        	if (choice.mmtb == null) {
        		continue;
        	}
        	boolean setCoord = false;
	        for (MixedMediaText mmt: choice.mmtb.getLines()) {
	            int offset = 0;
	            int spaceWidth = fm.getStringBounds(" ", g).getBounds().width;
	            int textBottom = (marginY + actualHeight) + (index * lineHeight);
	            
	            // Tracking of each element to allow for mouse listeners
	            int w = 0; 
	            //int h = actualHeight;
	            
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
	            
	            // Set start X for this line of text
	            int startX;
	            switch (this.alignment) {
	                case "right":
	                    startX = (this.width - this.marginX - mmt.getWidth());
	                    break;
	                case "center":
	                    startX = this.marginX + (this.width - mmt.getWidth())/2;
	                    break;
	                case "left":
	                default:
	                    startX = this.marginX;
	                    break;
	            }
	            
	            // Set the relative position of this line of text
	            if (!setCoord) {
	            	choice.getMmtb().setRelative(new Point(startX, textBottom - fm.getAscent()));
	            	setCoord = true;
	            }
	            
	            for (MarkupElement me : mmt.getElements()) {
	                
	                // Set the relative x coordinate of this element
	                //relative.x = startX + offset;
	                
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
	                    
	                    // Set the width of this element
	                    w = fm.getStringBounds(me.getText(), g).getBounds().width;
	                    
	                    g.drawString(me.getText(), startX + offset, textBottom);
	                    offset += w + spaceWidth;
	                } else if (me instanceof ImageInsert) {
	                    Resource r = ResourceStore.getResource(me.getText());
	                    Integer larger = Math.max(r.getHeight(), actualHeight);
	                    Integer diff = Math.abs(r.getHeight() - actualHeight);
	                    Integer correction = (int)Math.round((double)diff/2.0);
	                    Integer imageDelta = larger - correction;
	                    
	                    // Set the width of this element
	                    w = r.getWidth();
	                    
	                    g.drawImage(r.getImage(), null, startX + offset, (textBottom - imageDelta));
	                    offset += w + spaceWidth;
	                }
	                
	                //me.setRelative(relative);
	                //me.setWidth(w);
	                //me.setHeight(h);
	            }
	            index++;
	        }
        }
	}
}

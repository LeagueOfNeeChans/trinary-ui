package com.trinary.util;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.trinary.ui.elements.ContainerElement;
import com.trinary.ui.elements.FormattedTextElement;
import com.trinary.ui.elements.ResourceElement;
import com.trinary.ui.layout.ElementLayout;
import com.trinary.ui.layout.PositionValue;

@SuppressWarnings("restriction")
public class LayoutLoader {
	public static ContainerElement processLayout(String filename) throws JAXBException {
		return processLayout(new File(filename));
	}
	
	public static ContainerElement processLayout(File layoutFile) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(ElementLayout.class);
		 
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		ElementLayout layout = (ElementLayout)jaxbUnmarshaller.unmarshal(layoutFile);
		
		return processLayout(layout); 
	}
	
	public static ContainerElement processLayout(ElementLayout layout) {
		return processLayout(layout, null);
	}
	
	@SuppressWarnings("unchecked")
	public static ContainerElement processLayout(ElementLayout layout, ContainerElement parent) {
		PositionValue v;
		
		Class<? extends ContainerElement> clazz = null;
		try {
			clazz = (Class<? extends ContainerElement>)Class.forName(layout.getType());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		if (clazz == null) {
			return null;
		}

		
		ContainerElement ce = null;
		if (parent == null) {
			try {
				ce = clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		} else {
			ce = parent.addChild(clazz);
		}
		
		if (ce == null) {
			return null;
		}
			
		// Set resource
		if (layout.getResource() != null && ResourceElement.class.isAssignableFrom(ce.getClass())) {
			ResourceElement re = (ResourceElement)ce;
			Boolean perserveDimensions = (layout.getDimensions() == null);
			re.changeResource(layout.getResource(), perserveDimensions);
			System.out.println(String.format("%s.changeResource(%s, %s)", layout.getId(), layout.getResource(), perserveDimensions));
		}
		
		// Set dimensions
		if (layout.getDimensions() != null) {
			v = new PositionValue(layout.getDimensions().getWidth());
			if (v.getUnit().equals("%")) {
				ce.setWidthP(v.getNumber()/100.0);
				System.out.println(String.format("%s.setWidthP(%s)", layout.getId(), v.getNumber()/100.0));
			} else if (v.getUnit().equals("px")) {
				ce.setWidth(v.getNumber());
				System.out.println(String.format("%s.setWidth(%s)", layout.getId(), v.getNumber()));
			}
			
			v = new PositionValue(layout.getDimensions().getHeight());
			if (v.getUnit().equals("%")) {
				ce.setHeightP(v.getNumber()/100.0);
				System.out.println(String.format("%s.setHeightP(%s)", layout.getId(), v.getNumber()/100.0));
			} else if (v.getUnit().equals("px")) {
				ce.setHeight(v.getNumber());
				System.out.println(String.format("%s.setHeight(%s)", layout.getId(), v.getNumber()));
			}
		}
		
		// Set location
		if (layout.getLocation() != null && layout.getLocation().getLocationString() != null) {
			ce.move(new com.trinary.util.Location(layout.getLocation().getLocationString()));
			System.out.println(String.format("%s.move(%s)", layout.getId(), layout.getLocation().getLocationString()));
		}
		
		// Set alignment
		if (layout.getAlignment() != null && layout.getAlignment().getX() != null) {
			ce.sethAlign(layout.getAlignment().getX());
			System.out.println(String.format("%s.sethAlign(%s)", layout.getId(), layout.getAlignment().getX()));
		}
		if (layout.getAlignment() != null && layout.getAlignment().getY() != null) {
			ce.setvAlign(layout.getAlignment().getY());
			System.out.println(String.format("%s.setvAlign(%s)", layout.getId(), layout.getAlignment().getY()));
		}
		
		// Set margin
		if (layout.getMargins() != null && FormattedTextElement.class.isAssignableFrom(ce.getClass())) {
			FormattedTextElement fte = (FormattedTextElement)ce;
			if (layout.getMargins().getX() != null) {
				v = new PositionValue(layout.getMargins().getX());
				fte.setMarginX(v.getNumber());
				System.out.println(String.format("%s.setMarginX(%d)", layout.getId(), v.getNumber()));
			}
			if (layout.getMargins().getY() != null) {
				v = new PositionValue(layout.getMargins().getY());
				fte.setMarginY(v.getNumber());
				System.out.println(String.format("%s.setMarginY(%d)", layout.getId(), v.getNumber()));
			}
		}
		
		// Set zIndex
		if (layout.getzIndex() != null) {
			ce.setzIndex(layout.getzIndex());
			System.out.println(String.format("%s.setzIndex(%d)", layout.getId(), ce.getzIndex()));
		}

		// Set transparency
		if (layout.getTransparency() != null) {
			v = new PositionValue(layout.getTransparency());
			ce.setTransparency((float)(v.getNumber() / 100.0f));
			System.out.println(String.format("%s.setTransparency(%f)", layout.getId(), (float)(v.getNumber() / 100.0f)));
		}
		
		System.out.println();
		
		// Set children
		if (layout.getChildren() != null) {
			for (ElementLayout child : layout.getChildren()) {
				processLayout(child, ce);
			}
		}
		
		return ce;
	} 
}

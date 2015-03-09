package de.samdev.absgdx.framework.util;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;

import de.samdev.absgdx.framework.menu.agdxml.AgdxmlGridDefinitions;
import de.samdev.absgdx.framework.menu.agdxml.AgdxmlGridDefinitionsUnit;
import de.samdev.absgdx.framework.menu.agdxml.AgdxmlGridDefinitionsValue;
import de.samdev.absgdx.framework.util.exceptions.AgdxmlParsingException;
import de.samdev.absgdx.framework.util.exceptions.NonInstantiableException;

public class AgdxmlParserHelper {

	private AgdxmlParserHelper() throws NonInstantiableException { throw new NonInstantiableException(); }

	public static Vector2 parseVectorPosition(Element element, String parameter, Rectangle parentBound) throws AgdxmlParsingException {
		String[] parts = parameter.split(",");
		
		if (parts.length != 2) throw new AgdxmlParsingException(element.getName() + " must have the for of \"a,b\"");

		String p1 = parts[0].trim();
		String p2 = parts[1].trim();

		return new Vector2(parseNumber(element, "position[0]", parts[0].trim(), parentBound) + parentBound.x, parseNumber(element, "position[1]", parts[1].trim(), parentBound) + parentBound.y);
	}

	public static float parseNumberMax(Element element, String parametername, String parameter, Rectangle parentBound, float max) throws AgdxmlParsingException {
		if (max < 0) return 0;
		
		return Math.min(max, parseNumber(element, parametername, parameter, parentBound));
	}
	
	public static float parseNumber(Element element, String parametername, String parameter, Rectangle parentBound) throws AgdxmlParsingException {
		parameter = parameter.trim();
		
		if (parameter.endsWith("%")) {
			try {
				float val = Float.parseFloat(parameter.substring(0, parameter.length() - 1));
				if (parametername.equals("width") || parametername.equals("position[0]"))
					return parentBound.width * (val / 100f);
				else if (parametername.equals("height") || parametername.equals("position[1]"))
					return parentBound.height * (val / 100f);
				else
					throw new AgdxmlParsingException("Percentage value is invalid for attribute " + parametername);
			} catch (NumberFormatException e) {
				throw new AgdxmlParsingException("In element" + element.getName() + " "+  parameter + " is not a number");
			}
		} else {
			try {
				return Float.parseFloat(parameter);
			} catch (NumberFormatException e) {
				throw new AgdxmlParsingException("In element" + element.getName() + " "+  parameter + " is not a number");
			}
		}
	}
	
	public static AgdxmlGridDefinitions parseGridDefinitions(Element element) throws AgdxmlParsingException {
		Element element_cdef = element.getChildByName("grid.columndefinitions");
		Element element_rdef = element.getChildByName("grid.rowdefinitions");
		
		List<AgdxmlGridDefinitionsValue> cdef = new ArrayList<AgdxmlGridDefinitionsValue>();
		List<AgdxmlGridDefinitionsValue> rdef = new ArrayList<AgdxmlGridDefinitionsValue>();
		
		if (element_cdef == null || element_cdef.getChildrenByName("columndefinition").size == 0) {
			cdef.add(new AgdxmlGridDefinitionsValue(1, AgdxmlGridDefinitionsUnit.WEIGHT));
		} else {
			for (Element child : element_cdef.getChildrenByName("columndefinition")) {
				cdef.add(parseSingleGridDefinition(child, "width"));
			}
		}
		
		if (element_rdef == null || element_rdef.getChildrenByName("rowdefinition").size == 0) {
			rdef.add(new AgdxmlGridDefinitionsValue(1, AgdxmlGridDefinitionsUnit.WEIGHT));
		} else {
			for (Element child : element_rdef.getChildrenByName("rowdefinition")) {
				rdef.add(parseSingleGridDefinition(child, "height"));
			}
		}
		
		return new AgdxmlGridDefinitions(rdef, cdef);
	}

	private static AgdxmlGridDefinitionsValue parseSingleGridDefinition(Element element, String parametername) throws AgdxmlParsingException {
		String value = element.getAttribute(parametername, "");
		
		if (value.isEmpty()) throw new AgdxmlParsingException("Element " + element.getName() + " need the attribute " + parametername);
		
		if (value.endsWith("%")) {
			try {
				return new AgdxmlGridDefinitionsValue(Float.parseFloat(value.substring(0, value.length() - 1)), AgdxmlGridDefinitionsUnit.PERCENTAGE);
			} catch (NumberFormatException e) {
				throw new AgdxmlParsingException("In element" + element.getName() + " "+  parametername + " is not a number");
			}
		} else if (value.endsWith("*")) {
			try {
				return new AgdxmlGridDefinitionsValue(Float.parseFloat(value.substring(0, value.length() - 1)), AgdxmlGridDefinitionsUnit.WEIGHT);
			} catch (NumberFormatException e) {
				throw new AgdxmlParsingException("In element" + element.getName() + " "+  parametername + " is not a number");
			}
		} else {
			try {
				return new AgdxmlGridDefinitionsValue(Float.parseFloat(value), AgdxmlGridDefinitionsUnit.PIXEL);
			} catch (NumberFormatException e) {
				throw new AgdxmlParsingException("In element" + element.getName() + " "+  parametername + " is not a number");
			}
		}
	}
}

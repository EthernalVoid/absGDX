package de.samdev.absgdx.framework.layer;

import java.util.HashMap;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import de.samdev.absgdx.framework.AgdxGame;
import de.samdev.absgdx.framework.menu.GUITextureProvider;
import de.samdev.absgdx.framework.menu.agdxml.AgdxmlGridDefinitionsUnit;
import de.samdev.absgdx.framework.menu.agdxml.AgdxmlLayerBoundaryElement;
import de.samdev.absgdx.framework.menu.agdxml.AgdxmlValue;
import de.samdev.absgdx.framework.menu.agdxml.AgdxmlVectorValue;
import de.samdev.absgdx.framework.menu.elements.*;
import de.samdev.absgdx.framework.util.AgdxmlParserHelper;
import de.samdev.absgdx.framework.util.exceptions.AgdxmlParsingException;

public abstract class AgdxmlLayer extends MenuLayer {
	private final AgdxmlLayerBoundaryElement boundaryRootElement;
	
	private HashMap<String, GUITextureProvider> map_provider = new HashMap<String, GUITextureProvider>();
	private HashMap<String, TextureRegion[]> map_imagetextures = new HashMap<String, TextureRegion[]>();
	
	public AgdxmlLayer(AgdxGame owner, BitmapFont bmpfont, FileHandle agdxmlFile) throws AgdxmlParsingException {
		super(owner, bmpfont);
		
		try {
			boundaryRootElement = calculate(new XmlReader().parse(agdxmlFile));	
		} catch (Exception e) {
			throw new AgdxmlParsingException(e);
		}
	}
	
	public AgdxmlLayer(AgdxGame owner, BitmapFont bmpfont, String agdxmlFileContent) throws AgdxmlParsingException {
		super(owner, bmpfont);

		try {
			boundaryRootElement = calculate(new XmlReader().parse(agdxmlFileContent));
		} catch (Exception e) {
			throw new AgdxmlParsingException(e);
		}
	}

	@Override
	public void onResize() {
		try {
			recalculate();
		} catch (AgdxmlParsingException e) {
			// Can not happen - because this XML element was already parsed in constructor
			e.printStackTrace();
		}
	}

	private void recalculate() throws AgdxmlParsingException {
		getRoot().setBoundaries(0, 0, owner.getScreenWidth(), owner.getScreenHeight());
		
		boundaryRootElement.position = new AgdxmlVectorValue(new AgdxmlValue(0, AgdxmlGridDefinitionsUnit.PIXEL), new AgdxmlValue(0, AgdxmlGridDefinitionsUnit.PIXEL));
		boundaryRootElement.width = new AgdxmlValue(owner.getScreenWidth(), AgdxmlGridDefinitionsUnit.PIXEL);
		boundaryRootElement.height = new AgdxmlValue(owner.getScreenHeight(), AgdxmlGridDefinitionsUnit.PIXEL);
		
		boundaryRootElement.updateRoot();
	}

	public void addAgdxmlGuiTextureProvider(String key, GUITextureProvider value) {
		map_provider.put(key, value);
	}
	
	private GUITextureProvider getTextureProviderFromMap(Element xmlElement) {
		GUITextureProvider result = map_provider.get(xmlElement.getAttribute("textures", null));
		if (result == null) result = new GUITextureProvider();
		return result;
	}

	public void addAgdxmlImageTexture(String key, TextureRegion value) {
		map_imagetextures.put(key, new TextureRegion[]{value});
	}

	public void addAgdxmlImageTexture(String key, TextureRegion[] value) {
		map_imagetextures.put(key, value);
	}
	
	private TextureRegion getSingleImageTextureFromMap(Element xmlElement) {
		TextureRegion[] result = map_imagetextures.get(xmlElement.getAttribute("texture", null));
		if (result == null) return new TextureRegion();
		if (result.length < 1) return new TextureRegion();
		return result[0];
	}
	
	private TextureRegion[] getMultiImageTextureFromMap(Element xmlElement) {
		TextureRegion[] result = map_imagetextures.get(xmlElement.getAttribute("texture", null));
		if (result == null) result = new TextureRegion[]{ new TextureRegion() };
		return result;
	}
	
	private AgdxmlLayerBoundaryElement calculate(Element xmlRootElement) throws AgdxmlParsingException {
		try {
			if (xmlRootElement == null ) throw new AgdxmlParsingException("root element not found");
			if (! xmlRootElement.getName().equals("frame")) throw new AgdxmlParsingException("root element must be <frame>");
			
			AgdxmlLayerBoundaryElement result = new AgdxmlLayerBoundaryElement(getRoot());
			result.position = new AgdxmlVectorValue(new AgdxmlValue(0, AgdxmlGridDefinitionsUnit.PIXEL), new AgdxmlValue(0, AgdxmlGridDefinitionsUnit.PIXEL));
			result.width = new AgdxmlValue(owner.getScreenWidth(), AgdxmlGridDefinitionsUnit.PIXEL);
			result.height = new AgdxmlValue(owner.getScreenHeight(), AgdxmlGridDefinitionsUnit.PIXEL);
			
			getRoot().setBoundaries(0, 0, owner.getScreenWidth(), owner.getScreenHeight());
			
			for (int i = 0; i < xmlRootElement.getChildCount(); i++) {
				Element child = xmlRootElement.getChild(i);
				
				MenuElement mchild = calculateGeneric(getRoot().getBoundaries(), child, result);
				if (mchild != null) getRoot().addChildren(mchild);
			}
			
			return result;
		} catch (Exception e) {
			throw new AgdxmlParsingException(e);
		}
	}
	
	private MenuElement calculateGeneric(Rectangle boundaries, Element xmlElement, AgdxmlLayerBoundaryElement parent) throws AgdxmlParsingException {
		if (xmlElement.getName().equals("grid.columndefinitions")) return null;
		if (xmlElement.getName().equals("grid.rowdefinitions")) return null;
		
		if (xmlElement.getName().equals("panel"))        return calculatePanel(boundaries, xmlElement, parent);
		if (xmlElement.getName().equals("grid"))         return calculateGrid(boundaries, xmlElement, parent);
		if (xmlElement.getName().equals("button"))       return calculateButton(boundaries, xmlElement, parent);
		if (xmlElement.getName().equals("image"))        return calculateImage(boundaries, xmlElement, parent);
		if (xmlElement.getName().equals("checkbox"))     return calculateCheckBox(boundaries, xmlElement, parent);
		if (xmlElement.getName().equals("radiobutton"))  return calculateRadioButton(boundaries, xmlElement, parent);
		if (xmlElement.getName().equals("label"))        return calculateLabel(boundaries, xmlElement, parent);
		if (xmlElement.getName().equals("settingstree")) return calculateSettingsTree(boundaries, xmlElement, parent);
		
		throw new AgdxmlParsingException("Unknown element <" + xmlElement.getName() + ">");
	}

	private MenuElement calculateSettingsTree(Rectangle boundaries, Element xmlElement, AgdxmlLayerBoundaryElement parent) throws AgdxmlParsingException {
		String id = xmlElement.getAttribute("id", "{" + java.util.UUID.randomUUID().toString() + "}");
		GUITextureProvider tprox = getTextureProviderFromMap(xmlElement);
		
		MenuSettingsTree elem = new MenuSettingsTree(id, tprox, owner.settings.root);
		AgdxmlLayerBoundaryElement boundelem = new AgdxmlLayerBoundaryElement(elem);
		
		boundelem.set(xmlElement);
		boundelem.update(boundaries);
		
		parent.children.add(boundelem);
		
		return elem;
	}

	private MenuElement calculateLabel(Rectangle boundaries, Element xmlElement, AgdxmlLayerBoundaryElement parent) throws AgdxmlParsingException {
		String id = xmlElement.getAttribute("id", "{" + java.util.UUID.randomUUID().toString() + "}");
		
		MenuLabel elem = new MenuLabel(id);
		AgdxmlLayerBoundaryElement boundelem = new AgdxmlLayerBoundaryElement(elem);
		
		boundelem.set(xmlElement);
		boundelem.update(boundaries);
		
		parent.children.add(boundelem);
		
		return elem;
	}

	private MenuElement calculateRadioButton(Rectangle boundaries, Element xmlElement, AgdxmlLayerBoundaryElement parent) throws AgdxmlParsingException {
		String id = xmlElement.getAttribute("id", "{" + java.util.UUID.randomUUID().toString() + "}");
		GUITextureProvider tprox = getTextureProviderFromMap(xmlElement);
		
		MenuRadioButton elem = new MenuRadioButton(id, tprox);
		AgdxmlLayerBoundaryElement boundelem = new AgdxmlLayerBoundaryElement(elem);
		
		boundelem.set(xmlElement);
		boundelem.update(boundaries);
		
		parent.children.add(boundelem);
		
		return elem;
	}

	private MenuElement calculateCheckBox(Rectangle boundaries, Element xmlElement, AgdxmlLayerBoundaryElement parent) throws AgdxmlParsingException {
		String id = xmlElement.getAttribute("id", "{" + java.util.UUID.randomUUID().toString() + "}");
		GUITextureProvider tprox = getTextureProviderFromMap(xmlElement);
		
		MenuCheckBox elem = new MenuCheckBox(id, tprox);
		AgdxmlLayerBoundaryElement boundelem = new AgdxmlLayerBoundaryElement(elem);
		
		boundelem.set(xmlElement);
		boundelem.update(boundaries);
		
		parent.children.add(boundelem);
		
		return elem;
	}

	private MenuElement calculateButton(Rectangle boundaries, Element xmlElement, AgdxmlLayerBoundaryElement parent) throws AgdxmlParsingException {
		String id = xmlElement.getAttribute("id", "{" + java.util.UUID.randomUUID().toString() + "}");
		GUITextureProvider tprox = getTextureProviderFromMap(xmlElement);
		
		MenuButton elem = new MenuButton(id, tprox);
		AgdxmlLayerBoundaryElement boundelem = new AgdxmlLayerBoundaryElement(elem);
		
		boundelem.set(xmlElement);
		boundelem.update(boundaries);
		
		parent.children.add(boundelem);
		
		return elem;
	}

	private MenuElement calculateImage(Rectangle boundaries, Element xmlElement, AgdxmlLayerBoundaryElement parent) throws AgdxmlParsingException {
		String id = xmlElement.getAttribute("id", "{" + java.util.UUID.randomUUID().toString() + "}");
		
		MenuImage elem = new MenuImage(id);
		AgdxmlLayerBoundaryElement boundelem = new AgdxmlLayerBoundaryElement(elem);
		
		boundelem.set(xmlElement);
		boundelem.update(boundaries);
		
		if (xmlElement.getAttribute("texture", null) != null) {
			if (xmlElement.getIntAttribute("animation", 0) > 0) {
				elem.setImage(getMultiImageTextureFromMap(xmlElement), xmlElement.getIntAttribute("animation", 0));
			} else {
				elem.setImage(getSingleImageTextureFromMap(xmlElement));
			}
		}
		
		parent.children.add(boundelem);
		
		return elem;
	}

	private MenuElement calculatePanel(Rectangle boundaries, Element xmlElement, AgdxmlLayerBoundaryElement parent) throws AgdxmlParsingException {
		String id = xmlElement.getAttribute("id", "{" + java.util.UUID.randomUUID().toString() + "}");
		GUITextureProvider tprox = getTextureProviderFromMap(xmlElement);

		boolean iscontainer = xmlElement.getAttribute("container", "false").toLowerCase().equals("true");
		MenuContainer elem;
		if (iscontainer) 
			elem = new MenuContainer(id, tprox);
		else  
			elem = new MenuPanel(id, tprox);
		
		AgdxmlLayerBoundaryElement boundelem = new AgdxmlLayerBoundaryElement(elem);
		
		boundelem.set(xmlElement);
		boundelem.update(boundaries);
		
		parent.children.add(boundelem);
		
		Rectangle bd = new Rectangle(0, 0, elem.getWidth(), elem.getHeight());
		
		for (int i = 0; i < xmlElement.getChildCount(); i++) {
			Element child = xmlElement.getChild(i);
			
			MenuElement mchild = calculateGeneric(new Rectangle(bd), child, boundelem);
			if (mchild != null) elem.addChildren(mchild);
		}
		
		return elem;
	}

	private MenuElement calculateGrid(Rectangle boundaries, Element xmlElement, AgdxmlLayerBoundaryElement parent) throws AgdxmlParsingException {
		String id = xmlElement.getAttribute("id", "{" + java.util.UUID.randomUUID().toString() + "}");
		GUITextureProvider tprox = getTextureProviderFromMap(xmlElement);

		boolean iscontainer = xmlElement.getAttribute("container", "false").toLowerCase().equals("true");
		MenuContainer elem;
		if (iscontainer) 
			elem = new MenuContainer(id, tprox);
		else  
			elem = new MenuPanel(id, tprox);
		
		AgdxmlLayerBoundaryElement boundelem = new AgdxmlLayerBoundaryElement(elem);
		
		boundelem.set(xmlElement);
		boundelem.update(boundaries);
		
		parent.children.add(boundelem);
		
		boundelem.gridDefinitions = AgdxmlParserHelper.parseGridDefinitions(xmlElement);
		
		for (int i = 0; i < xmlElement.getChildCount(); i++) {
			Element child = xmlElement.getChild(i);
			
			int childGrid_x = child.getInt("grid.column", 0);
			int childGrid_y = child.getInt("grid.row", 0);
			
			MenuElement mchild = calculateGeneric(boundelem.gridDefinitions.getBoundaries(childGrid_x, childGrid_y, elem.getBoundaries()), child, boundelem);
			if (mchild != null) elem.addChildren(mchild);
		}
		
		return elem;
	}

	public AgdxmlLayerBoundaryElement getBoundaryRootElement() {
		return boundaryRootElement;
	}
}

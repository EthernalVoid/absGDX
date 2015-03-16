package de.samdev.absgdx.framework.layer;

import java.util.HashMap;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.samdev.absgdx.framework.AgdxGame;
import de.samdev.absgdx.framework.menu.GUITextureProvider;
import de.samdev.absgdx.framework.menu.agdxml.AgdxmlLayerBoundaryElement;
import de.samdev.absgdx.framework.menu.elements.MenuButton;
import de.samdev.absgdx.framework.menu.elements.MenuCheckBox;
import de.samdev.absgdx.framework.menu.elements.MenuContainer;
import de.samdev.absgdx.framework.menu.elements.MenuElement;
import de.samdev.absgdx.framework.menu.elements.MenuImage;
import de.samdev.absgdx.framework.menu.elements.MenuLabel;
import de.samdev.absgdx.framework.menu.elements.MenuPanel;
import de.samdev.absgdx.framework.menu.elements.MenuRadioButton;
import de.samdev.absgdx.framework.menu.elements.MenuSettingsTree;
import de.samdev.absgdx.framework.menu.events.MenuButtonListener;
import de.samdev.absgdx.framework.menu.events.MenuCheckboxListener;
import de.samdev.absgdx.framework.menu.events.MenuContainerListener;
import de.samdev.absgdx.framework.menu.events.MenuImageListener;
import de.samdev.absgdx.framework.menu.events.MenuLabelListener;
import de.samdev.absgdx.framework.menu.events.MenuRadioButtonListener;
import de.samdev.absgdx.framework.menu.events.MenuSettingsTreeListener;
import de.samdev.absgdx.framework.util.AgdxmlParserHelper;
import de.samdev.absgdx.framework.util.dependentProperties.DependentProperty;
import de.samdev.absgdx.framework.util.exceptions.AgdxmlParsingException;

/**
 * A MenuLayer that loads the format from an AGDXML file
 * Can dynamically react on resize Events an different screen sizes
 *
 */
public abstract class AgdxmlLayer extends MenuLayer {
	private final AgdxmlLayerBoundaryElement boundaryRootElement;
	
	private HashMap<String, GUITextureProvider> map_provider = new HashMap<String, GUITextureProvider>();
	private HashMap<String, TextureRegion[]> map_imagetextures = new HashMap<String, TextureRegion[]>();
	
	/**
	 * Create a new AgdxmlLayer
	 * 
	 * @param owner the owner of the layer
	 * @param bmpfont The standard font to use
	 * @param agdxmlFile the file with the AGDXML description 
	 * @throws AgdxmlParsingException if the agdxmlFile has Errors
	 */
	public AgdxmlLayer(AgdxGame owner, BitmapFont bmpfont, FileHandle agdxmlFile) throws AgdxmlParsingException {
		super(owner, bmpfont);

		initialize();
		
		try {
			boundaryRootElement = calculate(new XmlReader().parse(agdxmlFile));	
		} catch (Exception e) {
			throw new AgdxmlParsingException(e);
		}
	}
	
	/**
	 * Creates a new AgdxmlLayer
	 * 
	 * @param owner the owner of the layer
	 * @param bmpfont The standard font to use
	 * @param agdxmlFileContent an XML file content with the AGDXML description 
	 * @throws AgdxmlParsingException if the agdxmlFile has Errors
	 */
	public AgdxmlLayer(AgdxGame owner, BitmapFont bmpfont, String agdxmlFileContent) throws AgdxmlParsingException {
		super(owner, bmpfont);

		initialize();
		
		try {
			boundaryRootElement = calculate(new XmlReader().parse(agdxmlFileContent));
		} catch (Exception e) {
			throw new AgdxmlParsingException(e);
		}
	}
	
	/**
	 * Is called before the AGDXML file is parsed
	 * you can use this method to initialize  the different maps
	 */
	public abstract void initialize();

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
		boundaryRootElement.updateRoot(owner.getScreenWidth(), owner.getScreenHeight());
	}

	/**
	 * add a GUITextureProvider to the map (can be access with the AGDXML-property [textures] )
	 * 
	 * @param key the HashMap Key
	 * @param value the provider
	 */
	public void addAgdxmlGuiTextureProvider(String key, GUITextureProvider value) {
		map_provider.put(key, value);
	}
	
	private GUITextureProvider getTextureProviderFromMap(Element xmlElement, GUITextureProvider defaultProvider) {
		String attr = xmlElement.getAttribute("textures", null);
		if (attr == null) return defaultProvider;
		
		GUITextureProvider result = map_provider.get(attr);
		if (result == null) return new GUITextureProvider();
		
		return result;
	}

	/**
	 * Add a Texture to the map (for &lt;image&gt; elements) (can can be acessed with the AGDXML-property [texture])
	 * 
	 * @param key the HashMap Key
	 * @param value the texture
	 */
	public void addAgdxmlImageTexture(String key, TextureRegion value) {
		map_imagetextures.put(key, new TextureRegion[]{value});
	}

	/**
	 * Add a Texture to the map (for &lt;image&gt; elements) (can can be accessed with the AGDXML-property [texture])
	 * (this is the method needed for animated image tags - because you can supply multiple frames)
	 * 
	 * @param key the HashMap Key
	 * @param value the texture
	 */
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
			
			getRoot().setBoundaries(0, 0, owner.getScreenWidth(), owner.getScreenHeight());
			
			GUITextureProvider rootProvider = getTextureProviderFromMap(xmlRootElement, new GUITextureProvider());
			
			for (int i = 0; i < xmlRootElement.getChildCount(); i++) {
				Element child = xmlRootElement.getChild(i);
				
				MenuElement mchild = calculateGeneric(getRoot().getBoundaries(), child, result, rootProvider);
				if (mchild != null) getRoot().addChildren(mchild);
			}
			
			return result;
		} catch (Exception e) {
			throw new AgdxmlParsingException(e);
		}
	}
	
	private MenuElement calculateGeneric(Rectangle boundaries, Element xmlElement, AgdxmlLayerBoundaryElement parent, GUITextureProvider rootProvider) throws AgdxmlParsingException {
		if (xmlElement.getName().equals("grid.columndefinitions")) return null;
		if (xmlElement.getName().equals("grid.rowdefinitions")) return null;
		
		try {
			if (xmlElement.getName().equals("panel"))        return calculatePanel(boundaries, xmlElement, parent, rootProvider);
			if (xmlElement.getName().equals("grid"))         return calculateGrid(boundaries, xmlElement, parent, rootProvider);
			if (xmlElement.getName().equals("button"))       return calculateButton(boundaries, xmlElement, parent, rootProvider);
			if (xmlElement.getName().equals("image"))        return calculateImage(boundaries, xmlElement, parent, rootProvider);
			if (xmlElement.getName().equals("checkbox"))     return calculateCheckBox(boundaries, xmlElement, parent, rootProvider);
			if (xmlElement.getName().equals("radiobutton"))  return calculateRadioButton(boundaries, xmlElement, parent, rootProvider);
			if (xmlElement.getName().equals("label"))        return calculateLabel(boundaries, xmlElement, parent, rootProvider);
			if (xmlElement.getName().equals("settingstree")) return calculateSettingsTree(boundaries, xmlElement, parent, rootProvider);
		} catch (ReflectionException e) {
			throw new AgdxmlParsingException(e);
		}
		
		throw new AgdxmlParsingException("Unknown element <" + xmlElement.getName() + ">");
	}

	private MenuElement calculateSettingsTree(Rectangle boundaries, Element xmlElement, AgdxmlLayerBoundaryElement parent, GUITextureProvider rootProvider) throws AgdxmlParsingException, ReflectionException {
		String id = xmlElement.getAttribute("id", "{" + java.util.UUID.randomUUID().toString() + "}");
		GUITextureProvider tprox = getTextureProviderFromMap(xmlElement, rootProvider);
		
		MenuSettingsTree elem = new MenuSettingsTree(id, tprox, owner.settings.root);
		AgdxmlLayerBoundaryElement boundelem = new AgdxmlLayerBoundaryElement(elem, xmlElement);
		boundelem.update(boundaries);
		
		if (xmlElement.getAttribute("visible", null) != null)        elem.setVisible(xmlElement.getAttribute("visible").toLowerCase().equals("true"));
		if (xmlElement.getAttribute("rowGap", null) != null)         elem.setRowGap(xmlElement.getIntAttribute("rowGap"));
		if (xmlElement.getAttribute("rowHeight", null) != null)      elem.setRowHeight(xmlElement.getIntAttribute("rowHeight"));
		if (xmlElement.getAttribute("fontColor", null) != null)      elem.setColor(AgdxmlParserHelper.parseColor(xmlElement.getAttribute("fontColor")));
		if (xmlElement.getAttribute("scrollbarColor", null) != null) elem.setScrollbarColor(AgdxmlParserHelper.parseColor(xmlElement.getAttribute("scrollbarColor")));
		if (xmlElement.getAttribute("scrollbarWidth",null) != null)  elem.setRowGap(xmlElement.getIntAttribute("scrollbarWidth"));
		
		final HashMap<String, Method> events = new HashMap<String, Method>();
		
		if (xmlElement.getAttribute("onPointerUp", null) != null)   events.put("onPointerUp",   ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onPointerUp"),   MenuElement.class, String.class));
		if (xmlElement.getAttribute("onPointerDown", null) != null) events.put("onPointerDown", ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onPointerDown"), MenuElement.class, String.class));
		if (xmlElement.getAttribute("onHoverEnd", null) != null)    events.put("onHoverEnd",    ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onHoverEnd"),    MenuElement.class, String.class));
		if (xmlElement.getAttribute("onHover", null) != null)       events.put("onHover",       ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onHover"),       MenuElement.class, String.class));
		if (xmlElement.getAttribute("onFocusLost", null) != null)   events.put("onFocusLost",   ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onFocusLost"),   MenuElement.class, String.class));
		if (xmlElement.getAttribute("onFocus", null) != null)       events.put("onFocus",       ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onFocus"),       MenuElement.class, String.class));
		if (xmlElement.getAttribute("onClicked", null) != null)     events.put("onClicked",     ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onClicked"),     MenuElement.class, String.class));
		
		if (xmlElement.getAttribute("onPropertyChanged", null) != null) events.put("onPropertyChanged", ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onPropertyChanged"), MenuElement.class, String.class, DependentProperty.class));
		
		elem.addSettingsTreeListener(new MenuSettingsTreeListener() {
			@Override
			public void onPointerUp(MenuElement element, String identifier) {
				if (events.containsKey("onPointerUp")) try { events.get("onPointerUp").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onPointerDown(MenuElement element, String identifier) {
				if (events.containsKey("onPointerDown")) try { events.get("onPointerDown").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onHoverEnd(MenuElement element, String identifier) {
				if (events.containsKey("onHoverEnd")) try { events.get("onHoverEnd").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onHover(MenuElement element, String identifier) {
				if (events.containsKey("onHover")) try { events.get("onHover").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onFocusLost(MenuElement element, String identifier) {
				if (events.containsKey("onFocusLost")) try { events.get("onFocusLost").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onFocus(MenuElement element, String identifier) {
				if (events.containsKey("onFocus")) try { events.get("onFocus").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/* ignore */}
			}
			@Override
			public void onClicked(MenuElement element, String identifier) {
				if (events.containsKey("onClicked")) try { events.get("onClicked").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/* ignore */}
			}
			@Override
			public void onPropertyChanged(MenuElement element, String identifier, DependentProperty property) {
				if (events.containsKey("onPropertyChanged")) try { events.get("onPropertyChanged").invoke(AgdxmlLayer.this, element, identifier, property); } catch (ReflectionException e) {/* ignore */}
			}
		});
		
		parent.children.add(boundelem);
		
		return elem;
	}

	private MenuElement calculateLabel(Rectangle boundaries, Element xmlElement, AgdxmlLayerBoundaryElement parent, GUITextureProvider rootProvider) throws AgdxmlParsingException, ReflectionException {
		String id = xmlElement.getAttribute("id", "{" + java.util.UUID.randomUUID().toString() + "}");
		GUITextureProvider tprox = getTextureProviderFromMap(xmlElement, rootProvider);
		
		MenuLabel elem = new MenuLabel(id, tprox);
		AgdxmlLayerBoundaryElement boundelem = new AgdxmlLayerBoundaryElement(elem, xmlElement);
		boundelem.update(boundaries);
		
		if (xmlElement.getAttribute("visible", null) != null)   elem.setVisible(xmlElement.getAttribute("visible").toLowerCase().equals("true"));
		if (xmlElement.getAttribute("content", null) != null)   elem.setContent(xmlElement.getAttribute("content"));
		if (xmlElement.getAttribute("halign", null) != null)    elem.setHorizontalAlign(AgdxmlParserHelper.parseHorizontalAlign(xmlElement.getAttribute("halign")));
		if (xmlElement.getAttribute("valign", null) != null)    elem.setVerticalAlign(AgdxmlParserHelper.parseVerticalAlign(xmlElement.getAttribute("valign")));
		if (xmlElement.getAttribute("fontScale", null) != null) elem.setFontScale(xmlElement.getFloat("fontScale"));
		if (xmlElement.getAttribute("fontColor", null) != null) elem.setColor(AgdxmlParserHelper.parseColor(xmlElement.getAttribute("fontColor")));
		if (xmlElement.getAttribute("autoScale", null) != null) elem.setAutoScale(AgdxmlParserHelper.parseTextAutoScaleMode(xmlElement.getAttribute("autoScale")));
		
		final HashMap<String, Method> events = new HashMap<String, Method>();
		
		if (xmlElement.getAttribute("onPointerUp", null) != null)   events.put("onPointerUp",   ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onPointerUp"),   MenuElement.class, String.class));
		if (xmlElement.getAttribute("onPointerDown", null) != null) events.put("onPointerDown", ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onPointerDown"), MenuElement.class, String.class));
		if (xmlElement.getAttribute("onHoverEnd", null) != null)    events.put("onHoverEnd",    ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onHoverEnd"),    MenuElement.class, String.class));
		if (xmlElement.getAttribute("onHover", null) != null)       events.put("onHover",       ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onHover"),       MenuElement.class, String.class));
		if (xmlElement.getAttribute("onFocusLost", null) != null)   events.put("onFocusLost",   ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onFocusLost"),   MenuElement.class, String.class));
		if (xmlElement.getAttribute("onFocus", null) != null)       events.put("onFocus",       ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onFocus"),       MenuElement.class, String.class));
		if (xmlElement.getAttribute("onClicked", null) != null)     events.put("onClicked",     ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onClicked"),     MenuElement.class, String.class));
		
		if (xmlElement.getAttribute("onContentChanged", null) != null) events.put("onContentChanged", ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onContentChanged"), MenuElement.class, String.class, String.class));
		
		elem.addLabelListener(new MenuLabelListener() {
			@Override
			public void onPointerUp(MenuElement element, String identifier) {
				if (events.containsKey("onPointerUp")) try { events.get("onPointerUp").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onPointerDown(MenuElement element, String identifier) {
				if (events.containsKey("onPointerDown")) try { events.get("onPointerDown").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onHoverEnd(MenuElement element, String identifier) {
				if (events.containsKey("onHoverEnd")) try { events.get("onHoverEnd").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onHover(MenuElement element, String identifier) {
				if (events.containsKey("onHover")) try { events.get("onHover").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onFocusLost(MenuElement element, String identifier) {
				if (events.containsKey("onFocusLost")) try { events.get("onFocusLost").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onFocus(MenuElement element, String identifier) {
				if (events.containsKey("onFocus")) try { events.get("onFocus").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/* ignore */}
			}
			@Override
			public void onClicked(MenuElement element, String identifier) {
				if (events.containsKey("onClicked")) try { events.get("onClicked").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/* ignore */}
			}
			@Override
			public void onContentChanged(MenuElement element, String identifier, String newtext) {
				if (events.containsKey("onContentChanged")) try { events.get("onContentChanged").invoke(AgdxmlLayer.this, element, identifier, newtext); } catch (ReflectionException e) {/* ignore */}
			}
		});
		
		parent.children.add(boundelem);
		
		return elem;
	}

	private MenuElement calculateRadioButton(Rectangle boundaries, Element xmlElement, AgdxmlLayerBoundaryElement parent, GUITextureProvider rootProvider) throws AgdxmlParsingException, ReflectionException {
		String id = xmlElement.getAttribute("id", "{" + java.util.UUID.randomUUID().toString() + "}");
		GUITextureProvider tprox = getTextureProviderFromMap(xmlElement, rootProvider);
		
		MenuRadioButton elem = new MenuRadioButton(id, tprox);
		AgdxmlLayerBoundaryElement boundelem = new AgdxmlLayerBoundaryElement(elem, xmlElement);
		boundelem.update(boundaries);
		
		if (xmlElement.getAttribute("visible", null) != null)      elem.setVisible(xmlElement.getAttribute("visible").toLowerCase().equals("true"));
		if (xmlElement.getAttribute("labelPadding", null) != null) elem.setLabelPadding(AgdxmlParserHelper.parsePadding(xmlElement.getAttribute("labelPadding")));
		if (xmlElement.getAttribute("itemPadding", null) != null)  elem.setLabelPadding(AgdxmlParserHelper.parsePadding(xmlElement.getAttribute("itemPadding")));
		if (xmlElement.getAttribute("content", null) != null)      elem.setContent(xmlElement.getAttribute("content"));
		if (xmlElement.getAttribute("halign", null) != null)       elem.setHorizontalAlign(AgdxmlParserHelper.parseHorizontalAlign(xmlElement.getAttribute("halign")));
		if (xmlElement.getAttribute("valign", null) != null)       elem.setVerticalAlign(AgdxmlParserHelper.parseVerticalAlign(xmlElement.getAttribute("valign")));
		if (xmlElement.getAttribute("fontScale", null) != null)    elem.setFontScale(xmlElement.getFloat("fontScale"));
		if (xmlElement.getAttribute("fontColor", null) != null)    elem.setColor(AgdxmlParserHelper.parseColor(xmlElement.getAttribute("fontColor")));
		if (xmlElement.getAttribute("autoScale", null) != null)    elem.setAutoScale(AgdxmlParserHelper.parseTextAutoScaleMode(xmlElement.getAttribute("autoScale")));
		if (xmlElement.getAttribute("imageVisible", null) != null) elem.setVisible(xmlElement.getAttribute("imageVisible").toLowerCase().equals("true"));
		if (xmlElement.getAttribute("labelVisible", null) != null) elem.setVisible(xmlElement.getAttribute("labelVisible").toLowerCase().equals("true"));
		
		final HashMap<String, Method> events = new HashMap<String, Method>();
		
		if (xmlElement.getAttribute("onPointerUp", null) != null)   events.put("onPointerUp",   ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onPointerUp"),   MenuElement.class, String.class));
		if (xmlElement.getAttribute("onPointerDown", null) != null) events.put("onPointerDown", ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onPointerDown"), MenuElement.class, String.class));
		if (xmlElement.getAttribute("onHoverEnd", null) != null)    events.put("onHoverEnd",    ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onHoverEnd"),    MenuElement.class, String.class));
		if (xmlElement.getAttribute("onHover", null) != null)       events.put("onHover",       ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onHover"),       MenuElement.class, String.class));
		if (xmlElement.getAttribute("onFocusLost", null) != null)   events.put("onFocusLost",   ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onFocusLost"),   MenuElement.class, String.class));
		if (xmlElement.getAttribute("onFocus", null) != null)       events.put("onFocus",       ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onFocus"),       MenuElement.class, String.class));
		if (xmlElement.getAttribute("onClicked", null) != null)     events.put("onClicked",     ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onClicked"),     MenuElement.class, String.class));
		
		if (xmlElement.getAttribute("onChecked", null) != null) events.put("onChecked", ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onChecked"), MenuElement.class, String.class, Boolean.class));
		
		elem.addRadiobuttonListener(new MenuRadioButtonListener() {
			@Override
			public void onPointerUp(MenuElement element, String identifier) {
				if (events.containsKey("onPointerUp")) try { events.get("onPointerUp").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onPointerDown(MenuElement element, String identifier) {
				if (events.containsKey("onPointerDown")) try { events.get("onPointerDown").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onHoverEnd(MenuElement element, String identifier) {
				if (events.containsKey("onHoverEnd")) try { events.get("onHoverEnd").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onHover(MenuElement element, String identifier) {
				if (events.containsKey("onHover")) try { events.get("onHover").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onFocusLost(MenuElement element, String identifier) {
				if (events.containsKey("onFocusLost")) try { events.get("onFocusLost").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onFocus(MenuElement element, String identifier) {
				if (events.containsKey("onFocus")) try { events.get("onFocus").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/* ignore */}
			}
			@Override
			public void onClicked(MenuElement element, String identifier) {
				if (events.containsKey("onClicked")) try { events.get("onClicked").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/* ignore */}
			}
			@Override
			public void onChecked(MenuElement element, String identifier, boolean checked) {
				if (events.containsKey("onChecked")) try { events.get("onChecked").invoke(AgdxmlLayer.this, element, identifier, checked); } catch (ReflectionException e) {/* ignore */}
			}
		});
		
		parent.children.add(boundelem);
		
		return elem;
	}

	private MenuElement calculateCheckBox(Rectangle boundaries, Element xmlElement, AgdxmlLayerBoundaryElement parent, GUITextureProvider rootProvider) throws AgdxmlParsingException, ReflectionException {
		String id = xmlElement.getAttribute("id", "{" + java.util.UUID.randomUUID().toString() + "}");
		GUITextureProvider tprox = getTextureProviderFromMap(xmlElement, rootProvider);
		
		MenuCheckBox elem = new MenuCheckBox(id, tprox);
		AgdxmlLayerBoundaryElement boundelem = new AgdxmlLayerBoundaryElement(elem, xmlElement);
		boundelem.update(boundaries);
		
		if (xmlElement.getAttribute("visible", null) != null)      elem.setVisible(xmlElement.getAttribute("visible").toLowerCase().equals("true"));
		if (xmlElement.getAttribute("labelPadding", null) != null) elem.setLabelPadding(AgdxmlParserHelper.parsePadding(xmlElement.getAttribute("labelPadding")));
		if (xmlElement.getAttribute("itemPadding", null) != null)  elem.setLabelPadding(AgdxmlParserHelper.parsePadding(xmlElement.getAttribute("itemPadding")));
		if (xmlElement.getAttribute("content", null) != null)      elem.setContent(xmlElement.getAttribute("content"));
		if (xmlElement.getAttribute("halign", null) != null)       elem.setHorizontalAlign(AgdxmlParserHelper.parseHorizontalAlign(xmlElement.getAttribute("halign")));
		if (xmlElement.getAttribute("valign", null) != null)       elem.setVerticalAlign(AgdxmlParserHelper.parseVerticalAlign(xmlElement.getAttribute("valign")));
		if (xmlElement.getAttribute("fontScale", null) != null)    elem.setFontScale(xmlElement.getFloat("fontScale"));
		if (xmlElement.getAttribute("fontColor", null) != null)    elem.setColor(AgdxmlParserHelper.parseColor(xmlElement.getAttribute("fontColor")));
		if (xmlElement.getAttribute("autoScale", null) != null)    elem.setAutoScale(AgdxmlParserHelper.parseTextAutoScaleMode(xmlElement.getAttribute("autoScale")));
		if (xmlElement.getAttribute("imageVisible", null) != null) elem.setRenderImage(xmlElement.getAttribute("imageVisible").toLowerCase().equals("true"));
		if (xmlElement.getAttribute("labelVisible", null) != null) elem.setRenderLabel(xmlElement.getAttribute("labelVisible").toLowerCase().equals("true"));
		
		final HashMap<String, Method> events = new HashMap<String, Method>();
		
		if (xmlElement.getAttribute("onPointerUp", null) != null)   events.put("onPointerUp",   ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onPointerUp"),   MenuElement.class, String.class));
		if (xmlElement.getAttribute("onPointerDown", null) != null) events.put("onPointerDown", ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onPointerDown"), MenuElement.class, String.class));
		if (xmlElement.getAttribute("onHoverEnd", null) != null)    events.put("onHoverEnd",    ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onHoverEnd"),    MenuElement.class, String.class));
		if (xmlElement.getAttribute("onHover", null) != null)       events.put("onHover",       ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onHover"),       MenuElement.class, String.class));
		if (xmlElement.getAttribute("onFocusLost", null) != null)   events.put("onFocusLost",   ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onFocusLost"),   MenuElement.class, String.class));
		if (xmlElement.getAttribute("onFocus", null) != null)       events.put("onFocus",       ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onFocus"),       MenuElement.class, String.class));
		if (xmlElement.getAttribute("onClicked", null) != null)     events.put("onClicked",     ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onClicked"),     MenuElement.class, String.class));
		
		if (xmlElement.getAttribute("onChecked", null) != null) events.put("onChecked", ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onChecked"), MenuElement.class, String.class, Boolean.class));
		
		elem.addCheckboxListener(new MenuCheckboxListener() {
			@Override
			public void onPointerUp(MenuElement element, String identifier) {
				if (events.containsKey("onPointerUp")) try { events.get("onPointerUp").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onPointerDown(MenuElement element, String identifier) {
				if (events.containsKey("onPointerDown")) try { events.get("onPointerDown").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onHoverEnd(MenuElement element, String identifier) {
				if (events.containsKey("onHoverEnd")) try { events.get("onHoverEnd").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onHover(MenuElement element, String identifier) {
				if (events.containsKey("onHover")) try { events.get("onHover").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onFocusLost(MenuElement element, String identifier) {
				if (events.containsKey("onFocusLost")) try { events.get("onFocusLost").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onFocus(MenuElement element, String identifier) {
				if (events.containsKey("onFocus")) try { events.get("onFocus").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/* ignore */}
			}
			@Override
			public void onClicked(MenuElement element, String identifier) {
				if (events.containsKey("onClicked")) try { events.get("onClicked").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/* ignore */}
			}
			@Override
			public void onChecked(MenuElement element, String identifier, boolean checked) {
				if (events.containsKey("onChecked")) try { events.get("onChecked").invoke(AgdxmlLayer.this, element, identifier, checked); } catch (ReflectionException e) {/* ignore */}
			}
		});
		
		parent.children.add(boundelem);
		
		return elem;
	}

	private MenuElement calculateButton(Rectangle boundaries, Element xmlElement, AgdxmlLayerBoundaryElement parent, GUITextureProvider rootProvider) throws AgdxmlParsingException, ReflectionException {
		String id = xmlElement.getAttribute("id", "{" + java.util.UUID.randomUUID().toString() + "}");
		GUITextureProvider tprox = getTextureProviderFromMap(xmlElement, rootProvider);
		
		MenuButton elem = new MenuButton(id, tprox);
		AgdxmlLayerBoundaryElement boundelem = new AgdxmlLayerBoundaryElement(elem, xmlElement);
		boundelem.update(boundaries);

		if (xmlElement.getAttribute("visible", null) != null)      elem.setVisible(xmlElement.getAttribute("visible").toLowerCase().equals("true"));
		if (xmlElement.getAttribute("padding", null) != null)      elem.setPadding(AgdxmlParserHelper.parsePadding(xmlElement.getAttribute("padding")));
		if (xmlElement.getAttribute("content", null) != null)      elem.setContent(xmlElement.getAttribute("content"));
		if (xmlElement.getAttribute("halign", null) != null)       elem.setHorizontalAlign(AgdxmlParserHelper.parseHorizontalAlign(xmlElement.getAttribute("halign")));
		if (xmlElement.getAttribute("valign", null) != null)       elem.setVerticalAlign(AgdxmlParserHelper.parseVerticalAlign(xmlElement.getAttribute("valign")));
		if (xmlElement.getAttribute("fontScale", null) != null)    elem.setFontScale(xmlElement.getFloat("fontScale"));
		if (xmlElement.getAttribute("fontColor", null) != null)    elem.setColor(AgdxmlParserHelper.parseColor(xmlElement.getAttribute("fontColor")));
		if (xmlElement.getAttribute("autoScale", null) != null)    elem.setAutoScale(AgdxmlParserHelper.parseTextAutoScaleMode(xmlElement.getAttribute("autoScale")));
		
		final HashMap<String, Method> events = new HashMap<String, Method>();
		
		if (xmlElement.getAttribute("onPointerUp", null) != null)   events.put("onPointerUp",   ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onPointerUp"),   MenuElement.class, String.class));
		if (xmlElement.getAttribute("onPointerDown", null) != null) events.put("onPointerDown", ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onPointerDown"), MenuElement.class, String.class));
		if (xmlElement.getAttribute("onHoverEnd", null) != null)    events.put("onHoverEnd",    ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onHoverEnd"),    MenuElement.class, String.class));
		if (xmlElement.getAttribute("onHover", null) != null)       events.put("onHover",       ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onHover"),       MenuElement.class, String.class));
		if (xmlElement.getAttribute("onFocusLost", null) != null)   events.put("onFocusLost",   ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onFocusLost"),   MenuElement.class, String.class));
		if (xmlElement.getAttribute("onFocus", null) != null)       events.put("onFocus",       ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onFocus"),       MenuElement.class, String.class));
		if (xmlElement.getAttribute("onClicked", null) != null)     events.put("onClicked",     ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onClicked"),     MenuElement.class, String.class));
		
		elem.addButtonListener(new MenuButtonListener() {
			@Override
			public void onPointerUp(MenuElement element, String identifier) {
				if (events.containsKey("onPointerUp")) try { events.get("onPointerUp").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onPointerDown(MenuElement element, String identifier) {
				if (events.containsKey("onPointerDown")) try { events.get("onPointerDown").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onHoverEnd(MenuElement element, String identifier) {
				if (events.containsKey("onHoverEnd")) try { events.get("onHoverEnd").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onHover(MenuElement element, String identifier) {
				if (events.containsKey("onHover")) try { events.get("onHover").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onFocusLost(MenuElement element, String identifier) {
				if (events.containsKey("onFocusLost")) try { events.get("onFocusLost").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onFocus(MenuElement element, String identifier) {
				if (events.containsKey("onFocus")) try { events.get("onFocus").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/* ignore */}
			}
			@Override
			public void onClicked(MenuElement element, String identifier) {
				if (events.containsKey("onClicked")) try { events.get("onClicked").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/* ignore */}
			}
		});
		
		parent.children.add(boundelem);
		
		return elem;
	}

	private MenuElement calculateImage(Rectangle boundaries, Element xmlElement, AgdxmlLayerBoundaryElement parent, GUITextureProvider rootProvider) throws AgdxmlParsingException, ReflectionException {
		String id = xmlElement.getAttribute("id", "{" + java.util.UUID.randomUUID().toString() + "}");
		GUITextureProvider tprox = getTextureProviderFromMap(xmlElement, rootProvider);
		
		MenuImage elem = new MenuImage(id, tprox);
		AgdxmlLayerBoundaryElement boundelem = new AgdxmlLayerBoundaryElement(elem, xmlElement);
		boundelem.update(boundaries);
		
		if (xmlElement.getAttribute("visible", null) != null) elem.setVisible(xmlElement.getAttribute("visible").toLowerCase().equals("true"));
		if (xmlElement.getAttribute("texture", null) != null) {
			if (xmlElement.getIntAttribute("animation", 0) > 0) {
				elem.setImage(getMultiImageTextureFromMap(xmlElement), xmlElement.getIntAttribute("animation", 0));
			} else {
				elem.setImage(getSingleImageTextureFromMap(xmlElement));
			}
		}
		if (xmlElement.getAttribute("behavior", null) != null)  elem.setBehavior(AgdxmlParserHelper.parseImageBehavior(xmlElement.getAttribute("behavior")));
		
		final HashMap<String, Method> events = new HashMap<String, Method>();
		
		if (xmlElement.getAttribute("onPointerUp", null) != null)   events.put("onPointerUp",   ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onPointerUp"),   MenuElement.class, String.class));
		if (xmlElement.getAttribute("onPointerDown", null) != null) events.put("onPointerDown", ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onPointerDown"), MenuElement.class, String.class));
		if (xmlElement.getAttribute("onHoverEnd", null) != null)    events.put("onHoverEnd",    ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onHoverEnd"),    MenuElement.class, String.class));
		if (xmlElement.getAttribute("onHover", null) != null)       events.put("onHover",       ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onHover"),       MenuElement.class, String.class));
		if (xmlElement.getAttribute("onFocusLost", null) != null)   events.put("onFocusLost",   ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onFocusLost"),   MenuElement.class, String.class));
		if (xmlElement.getAttribute("onFocus", null) != null)       events.put("onFocus",       ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onFocus"),       MenuElement.class, String.class));
		if (xmlElement.getAttribute("onClicked", null) != null)     events.put("onClicked",     ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onClicked"),     MenuElement.class, String.class));
		
		elem.addImageListener(new MenuImageListener() {
			@Override
			public void onPointerUp(MenuElement element, String identifier) {
				if (events.containsKey("onPointerUp")) try { events.get("onPointerUp").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onPointerDown(MenuElement element, String identifier) {
				if (events.containsKey("onPointerDown")) try { events.get("onPointerDown").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onHoverEnd(MenuElement element, String identifier) {
				if (events.containsKey("onHoverEnd")) try { events.get("onHoverEnd").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onHover(MenuElement element, String identifier) {
				if (events.containsKey("onHover")) try { events.get("onHover").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onFocusLost(MenuElement element, String identifier) {
				if (events.containsKey("onFocusLost")) try { events.get("onFocusLost").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onFocus(MenuElement element, String identifier) {
				if (events.containsKey("onFocus")) try { events.get("onFocus").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/* ignore */}
			}
			@Override
			public void onClicked(MenuElement element, String identifier) {
				if (events.containsKey("onClicked")) try { events.get("onClicked").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/* ignore */}
			}
		});
		
		parent.children.add(boundelem);
		
		return elem;
	}

	private MenuElement calculatePanel(Rectangle boundaries, Element xmlElement, AgdxmlLayerBoundaryElement parent, GUITextureProvider rootProvider) throws AgdxmlParsingException, ReflectionException {
		String id = xmlElement.getAttribute("id", "{" + java.util.UUID.randomUUID().toString() + "}");
		GUITextureProvider tprox = getTextureProviderFromMap(xmlElement, rootProvider);

		boolean iscontainer = xmlElement.getAttribute("container", "false").toLowerCase().equals("true");
		MenuContainer elem;
		if (iscontainer) 
			elem = new MenuContainer(id, tprox);
		else  
			elem = new MenuPanel(id, tprox);
		
		AgdxmlLayerBoundaryElement boundelem = new AgdxmlLayerBoundaryElement(elem, xmlElement);
		boundelem.update(boundaries);
		
		if (xmlElement.getAttribute("visible", null) != null) elem.setVisible(xmlElement.getAttribute("visible").toLowerCase().equals("true"));
		
		final HashMap<String, Method> events = new HashMap<String, Method>();
		
		if (xmlElement.getAttribute("onPointerUp", null) != null)   events.put("onPointerUp",   ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onPointerUp"),   MenuElement.class, String.class));
		if (xmlElement.getAttribute("onPointerDown", null) != null) events.put("onPointerDown", ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onPointerDown"), MenuElement.class, String.class));
		if (xmlElement.getAttribute("onHoverEnd", null) != null)    events.put("onHoverEnd",    ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onHoverEnd"),    MenuElement.class, String.class));
		if (xmlElement.getAttribute("onHover", null) != null)       events.put("onHover",       ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onHover"),       MenuElement.class, String.class));
		if (xmlElement.getAttribute("onFocusLost", null) != null)   events.put("onFocusLost",   ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onFocusLost"),   MenuElement.class, String.class));
		if (xmlElement.getAttribute("onFocus", null) != null)       events.put("onFocus",       ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onFocus"),       MenuElement.class, String.class));
		if (xmlElement.getAttribute("onClicked", null) != null)     events.put("onClicked",     ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onClicked"),     MenuElement.class, String.class));
		
		elem.addContainerListener(new MenuContainerListener() {
			@Override
			public void onPointerUp(MenuElement element, String identifier) {
				if (events.containsKey("onPointerUp")) try { events.get("onPointerUp").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onPointerDown(MenuElement element, String identifier) {
				if (events.containsKey("onPointerDown")) try { events.get("onPointerDown").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onHoverEnd(MenuElement element, String identifier) {
				if (events.containsKey("onHoverEnd")) try { events.get("onHoverEnd").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onHover(MenuElement element, String identifier) {
				if (events.containsKey("onHover")) try { events.get("onHover").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onFocusLost(MenuElement element, String identifier) {
				if (events.containsKey("onFocusLost")) try { events.get("onFocusLost").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onFocus(MenuElement element, String identifier) {
				if (events.containsKey("onFocus")) try { events.get("onFocus").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/* ignore */}
			}
			@Override
			public void onClicked(MenuElement element, String identifier) {
				if (events.containsKey("onClicked")) try { events.get("onClicked").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/* ignore */}
			}
		});
		
		parent.children.add(boundelem);
		
		Rectangle bd = new Rectangle(0, 0, elem.getWidth(), elem.getHeight());
		for (int i = 0; i < xmlElement.getChildCount(); i++) {
			Element child = xmlElement.getChild(i);
			
			MenuElement mchild = calculateGeneric(new Rectangle(bd), child, boundelem, tprox);
			if (mchild != null) elem.addChildren(mchild);
		}
		
		return elem;
	}

	private MenuElement calculateGrid(Rectangle boundaries, Element xmlElement, AgdxmlLayerBoundaryElement parent, GUITextureProvider rootProvider) throws AgdxmlParsingException, ReflectionException {
		String id = xmlElement.getAttribute("id", "{" + java.util.UUID.randomUUID().toString() + "}");
		GUITextureProvider tprox = getTextureProviderFromMap(xmlElement, rootProvider);

		boolean iscontainer = xmlElement.getAttribute("container", "false").toLowerCase().equals("true");
		MenuContainer elem;
		if (iscontainer) 
			elem = new MenuContainer(id, tprox);
		else  
			elem = new MenuPanel(id, tprox);
		
		AgdxmlLayerBoundaryElement boundelem = new AgdxmlLayerBoundaryElement(elem, xmlElement);
		boundelem.update(boundaries);
		
		if (xmlElement.getAttribute("visible", null) != null) elem.setVisible(xmlElement.getAttribute("visible").toLowerCase().equals("true"));
		
		final HashMap<String, Method> events = new HashMap<String, Method>();
		
		if (xmlElement.getAttribute("onPointerUp", null) != null)   events.put("onPointerUp",   ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onPointerUp"),   MenuElement.class, String.class));
		if (xmlElement.getAttribute("onPointerDown", null) != null) events.put("onPointerDown", ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onPointerDown"), MenuElement.class, String.class));
		if (xmlElement.getAttribute("onHoverEnd", null) != null)    events.put("onHoverEnd",    ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onHoverEnd"),    MenuElement.class, String.class));
		if (xmlElement.getAttribute("onHover", null) != null)       events.put("onHover",       ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onHover"),       MenuElement.class, String.class));
		if (xmlElement.getAttribute("onFocusLost", null) != null)   events.put("onFocusLost",   ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onFocusLost"),   MenuElement.class, String.class));
		if (xmlElement.getAttribute("onFocus", null) != null)       events.put("onFocus",       ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onFocus"),       MenuElement.class, String.class));
		if (xmlElement.getAttribute("onClicked", null) != null)     events.put("onClicked",     ClassReflection.getDeclaredMethod(this.getClass(), xmlElement.getAttribute("onClicked"),     MenuElement.class, String.class));
		
		elem.addContainerListener(new MenuContainerListener() {
			@Override
			public void onPointerUp(MenuElement element, String identifier) {
				if (events.containsKey("onPointerUp")) try { events.get("onPointerUp").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onPointerDown(MenuElement element, String identifier) {
				if (events.containsKey("onPointerDown")) try { events.get("onPointerDown").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onHoverEnd(MenuElement element, String identifier) {
				if (events.containsKey("onHoverEnd")) try { events.get("onHoverEnd").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onHover(MenuElement element, String identifier) {
				if (events.containsKey("onHover")) try { events.get("onHover").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onFocusLost(MenuElement element, String identifier) {
				if (events.containsKey("onFocusLost")) try { events.get("onFocusLost").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/*ignore*/}
			}
			@Override
			public void onFocus(MenuElement element, String identifier) {
				if (events.containsKey("onFocus")) try { events.get("onFocus").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/* ignore */}
			}
			@Override
			public void onClicked(MenuElement element, String identifier) {
				if (events.containsKey("onClicked")) try { events.get("onClicked").invoke(AgdxmlLayer.this, element, identifier); } catch (ReflectionException e) {/* ignore */}
			}
		});
		
		parent.children.add(boundelem);

		for (int i = 0; i < xmlElement.getChildCount(); i++) {
			Element child = xmlElement.getChild(i);
			
			int childGrid_x = child.getInt("grid.column", 0);
			int childGrid_y = child.getInt("grid.row", 0);
			
			MenuElement mchild = calculateGeneric(boundelem.gridDefinitions.getBoundaries(childGrid_x, childGrid_y, elem.getBoundaries()), child, boundelem, tprox);
			if (mchild != null) elem.addChildren(mchild);
		}
		
		return elem;
	}

	/**
	 * get the boundary root element of the boundary tree
	 * in this all relative size information are stores
	 * 
	 * @return the root element
	 */
	public AgdxmlLayerBoundaryElement getBoundaryRootElement() {
		return boundaryRootElement;
	}
}
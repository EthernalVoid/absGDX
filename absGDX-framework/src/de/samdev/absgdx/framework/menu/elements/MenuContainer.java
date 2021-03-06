package de.samdev.absgdx.framework.menu.elements;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.samdev.absgdx.framework.GameSettings;
import de.samdev.absgdx.framework.menu.GUITextureProvider;
import de.samdev.absgdx.framework.menu.MenuOwner;
import de.samdev.absgdx.framework.menu.events.MenuContainerListener;

/**
 * A container containing multiple other elements (but has no visual style)
 */
public class MenuContainer extends MenuBaseElement {
	private List<MenuBaseElement> elements;
	
	/**
	 * Creates a new container
	 * 
	 * @param children the children elements
	 */
	public MenuContainer(List<MenuBaseElement> children) {
		super();
		
		this.elements = children;
	}

	/**
	 * Creates a new container (with no children)
	 */
	public MenuContainer() {
		super();
		
		this.elements = new ArrayList<MenuBaseElement>();
	}

	/**
	 * Creates a new container (with no children)
	 * 
	 * @param ident the unique identifier
	 * @param texprovider the texture provider for this element
	 */
	public MenuContainer(String ident, GUITextureProvider texprovider) {
		super(ident, texprovider);
		
		this.elements = new ArrayList<MenuBaseElement>();
	}

	/**
	 * Creates a new Panel (with no children)
	 * 
	 * @param texprovider the texture provider for this element
	 */
	public MenuContainer(GUITextureProvider texprovider) {
		super(texprovider);
		
		this.elements = new ArrayList<MenuBaseElement>();
	}
	
	@Override
	public void render(SpriteBatch sbatch, BitmapFont font, int offX, int offY) {
		renderChildren(sbatch, font, offX, offY);
	}

	protected void renderChildren(SpriteBatch sbatch, BitmapFont font, int offX, int offY) {
		for (MenuBaseElement element : elements) {
			element.renderElement(sbatch, font, owner, offX + getPositionX(), offY + getPositionY());
		}
	}

	@Override
	public void renderCustom(SpriteBatch sbatch, ShapeRenderer srenderer, BitmapFont font, int offX, int offY) {
		for (MenuBaseElement element : elements) {
			element.renderElementCustom(sbatch, srenderer, font, owner, offX + getPositionX(), offY + getPositionY());
		}
	}
	
	@Override
	public void renderDebug(ShapeRenderer srenderer, GameSettings settings, int offX, int offY) {
		renderChildrenDebug(srenderer, settings, offX, offY);
	}

	protected void renderChildrenDebug(ShapeRenderer srenderer, GameSettings settings, int offX, int offY) {
		for (MenuBaseElement element : elements) {
			element.renderElementDebug(srenderer, owner, settings, offX + getPositionX(), offY + getPositionY());
		}
	}

	@Override
	public void update(float delta) {
		for (MenuBaseElement element : elements) {
			element.update(delta);
		}
	}
	
	/**
	 * Adds a new listener
	 * 
	 * @param l the new listener
	 */
	public void addContainerListener(MenuContainerListener l) {
		super.addElementListener(l);
	}

	@Override
	public void setDepth(int elementdepth) {
		super.setDepth(elementdepth);
		
		for (MenuBaseElement element : elements) {
			element.setDepth(elementdepth + 1);
		}
	}
	
	@Override
	public void pack(MenuOwner owner, MenuBaseElement parent) {
		super.pack(owner, parent);
		
		setDepth(getDepth());
	}
	
	/**
	 * Adds a new child to the panel
	 * ( pack() is called )
	 * 
	 * @param element the new child
	 */
	public void addChildren(MenuBaseElement element) {
		elements.add(element);
		
		pack(this.owner, this.parent);
	}
	
	/**
	 * Removes a child from the panel
	 * ( pack() is called )
	 * 
	 * @param element the child to remove
	 * @return true if the child was a child and was removed
	 */
	public boolean removeChildren(MenuBaseElement element) {
		boolean success = elements.remove(element);

		pack(this.owner, this.parent);
		
		return success;
	}

	@Override
	public MenuBaseElement getElementAt(int x, int y) {
		for (MenuBaseElement element : elements) {
			if (element.getBoundaries().contains(x, y) && element.isVisible())
				return element.getElementAt(x - element.getPositionX(), y - element.getPositionY());
		}
		
		return this;
	}

	@Override
	public List<MenuBaseElement> getDirectInnerElements() {
		return elements;
	}
	
	@Override
	public List<MenuBaseElement> getDirectChildElements() {
		return elements;
	}

	@Override
	public MenuBaseElement getElementByID(String id) {
		if (identifier.equals(id)) return this;
		
		for (MenuBaseElement MenuBaseElement : elements) {
			MenuBaseElement result = MenuBaseElement.getElementByID(id);
			if (result != null) return result;
		}
		
		return null;
	}
}

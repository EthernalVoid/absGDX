package de.samdev.absgdx.example.mainmenu;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.samdev.absgdx.framework.menu.GUITextureProvider;
import de.samdev.absgdx.framework.menu.attributes.HorzAlign;
import de.samdev.absgdx.framework.menu.attributes.RectangleRadius;
import de.samdev.absgdx.framework.menu.attributes.TextAutoScaleMode;
import de.samdev.absgdx.framework.menu.attributes.VertAlign;
import de.samdev.absgdx.framework.menu.elements.MenuElement;
import de.samdev.absgdx.framework.menu.elements.MenuLabel;
import de.samdev.absgdx.framework.util.MenuRenderHelper;

public class MenuProgressbar extends MenuElement {

	private MenuLabel innerLabel = new MenuLabel();
	
	public float perc = 0.65f;
	
	public MenuProgressbar() {
		super();
	}

	public MenuProgressbar(GUITextureProvider texprovider) {
		super(texprovider);
	}

	public MenuProgressbar(String ident) {
		super(ident);
	}

	public MenuProgressbar(String ident, GUITextureProvider texprovider) {
		super(ident, texprovider);
	}

	@Override
	public void render(SpriteBatch sbatch, ShapeRenderer srenderer, BitmapFont font) {
		
		TextureRegion tex0 = getTextureProvider().get(getClass(), "0");
		float tex0_w = getHeight() * (tex0.getRegionWidth() * 1f/ tex0.getRegionHeight());
		
		TextureRegion tex1 = getTextureProvider().get(getClass(), "1");
		float tex1_w = getHeight() * (tex1.getRegionWidth() * 1f/ tex1.getRegionHeight());
		
		TextureRegion tex2 = getTextureProvider().get(getClass(), "2");
		TextureRegion tex3 = getTextureProvider().get(getClass(), "3");
		TextureRegion tex4 = getTextureProvider().get(getClass(), "4");
		
		float percpos = (getWidth() - tex4.getRegionWidth()) * perc + tex4.getRegionWidth()/2;
		RectangleRadius pad = new RectangleRadius(10, 10, 10, 10);
		
		innerLabel.setBoundaries((int)(getPositionX() + percpos - tex4.getRegionWidth()/2f) + pad.left, getPositionY() + pad.top, tex4.getRegionWidth() - pad.getHorizontalSum(), tex4.getRegionHeight() - pad.getVerticalSum());
		innerLabel.setContent((int)(perc*100) + "%");
		innerLabel.setColor(Color.WHITE);
		innerLabel.setAutoScale(TextAutoScaleMode.BOTH);
		innerLabel.setAlign(HorzAlign.CENTER, VertAlign.CENTER);
		
		sbatch.getTransformMatrix().translate(getPositionX(), getPositionY(), 0);
		sbatch.begin();
		{
			MenuRenderHelper.drawTextureStretched(sbatch, tex0, 0, 0, tex0_w, getHeight());
			MenuRenderHelper.drawTextureStretched(sbatch, tex1, getWidth() - tex1_w, 0, tex1_w, getHeight());

			if (percpos > tex0_w)
				MenuRenderHelper.drawTextureStretched(sbatch, tex2, tex0_w, 0, percpos - tex0_w, getHeight());

			if (percpos > tex0_w)
				MenuRenderHelper.drawTextureStretched(sbatch, tex3, percpos, 0, getWidth() - percpos - tex1_w, getHeight());
			
			MenuRenderHelper.drawTexture(sbatch, tex4, percpos - tex4.getRegionWidth()/2, 0);
		}
		sbatch.end();
		sbatch.getTransformMatrix().translate(-getPositionX(), -getPositionY(), 0);
		
		innerLabel.render(sbatch, srenderer, font);
	}

	public int direction = 1;
	@Override
	public void update(float delta) {
		perc += (delta/7500)*direction;
		
		if (perc < 0) {
			perc = 0;
			direction *= -1;
		}
		if (perc > 1) {
			perc = 1;
			direction *= -1;
		}
	}

	@Override
	public MenuElement getElementAt(int x, int y) {
		return this;
	}

	@Override
	public List<MenuElement> getDirectInnerElements() {
		List<MenuElement> result = new ArrayList<MenuElement>();
		result.add(innerLabel);
		return result;
	}

}

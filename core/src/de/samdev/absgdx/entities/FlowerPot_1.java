package de.samdev.absgdx.entities;

import de.samdev.absgdx.DemoGameLayer;
import de.samdev.absgdx.Textures;
import de.samdev.absgdx.framework.entities.Entity;
import de.samdev.absgdx.framework.entities.colliosiondetection.CollisionCircle;
import de.samdev.absgdx.framework.layer.GameLayer;

public class FlowerPot_1 extends Entity {

	public DemoGameLayer owner;
	
	public int tick = 0;
	
	public FlowerPot_1() {
		super(Textures.tex_Flowers_empty, 2, 2);		
	}

	@Override
	public void onLayerAdd(GameLayer layer) {
		setPosition(0.0f, 00.0f);
		
		addCollisionGeo(1, 1, new CollisionCircle(1f));
	}
	
	@Override
	public void beforeUpdate(float delta) {
		tick++;
		
		if (getPositionY() < 5) {
			acceleration.y =  0.00003f;
		} else {
			acceleration.y = -0.00003f;
		}
		
		speed.x = 0.0025f;
		
		if (tick > 60*22)
			this.alive = false;
	}

}

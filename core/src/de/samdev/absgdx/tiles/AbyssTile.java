package de.samdev.absgdx.tiles;

import de.samdev.absgdx.Textures;
import de.samdev.absgdx.framework.entities.colliosiondetection.CollisionGeometryOwner;
import de.samdev.absgdx.framework.map.Tile;

public class AbyssTile extends Tile {
	public AbyssTile() {
		super(Textures.tex_AbyssTile);
	}

	@Override
	public void update(float delta) {
		// NOP
	}

	@Override
	public boolean canMoveCollide(CollisionGeometryOwner other) {
		return true;
	}
}

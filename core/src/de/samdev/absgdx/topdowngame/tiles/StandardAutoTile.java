package de.samdev.absgdx.topdowngame.tiles;

import java.util.HashMap;

import de.samdev.absgdx.Textures;
import de.samdev.absgdx.framework.map.AutoTile;

public class StandardAutoTile extends AutoTile {
	public StandardAutoTile(HashMap<String, String> properties) {
		super(Textures.texmap, 16, 16, properties);
	}
}
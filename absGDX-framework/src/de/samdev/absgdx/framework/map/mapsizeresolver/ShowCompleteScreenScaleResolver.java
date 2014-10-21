package de.samdev.absgdx.framework.map.mapsizeresolver;

import com.badlogic.gdx.math.Vector2;

public class ShowCompleteScreenScaleResolver implements AbstractMapScaleResolver {
	
	public ShowCompleteScreenScaleResolver() {
		// NOP
	}

	@Override
	public Vector2 getTileSize(int screenWidth, int screenHeight, int mapHeight, int mapWidth) {
		return (new MaximumBoundaryScreenScaleResolver(mapHeight, mapWidth)).getTileSize(screenWidth, screenHeight, mapHeight, mapWidth);
	}
}

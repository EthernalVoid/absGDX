package de.samdev.absgdx.framework.map.mapscaleresolver;

public class LimitedMinimumBoundaryMapScaleResolver extends MinimumBoundaryMapScaleResolver {

	private final float maximumCut;
	
	/**
	 * @param visibleTilesX the (optimal / minimal) amount of  
	 * @param visibleTilesY
	 * @param maximumCutPrecentage
	 */
	public LimitedMinimumBoundaryMapScaleResolver(int visibleTilesX, int visibleTilesY, float maximumCutPrecentage) {
		super(visibleTilesX, visibleTilesY);
		
		this.maximumCut = maximumCutPrecentage;
	}

	@Override
	public float getTileSize(int screenWidth, int screenHeight, int mapWidth, int mapHeight) {
		float tx = Math.min(tilesX, mapWidth);
		float ty = Math.min(tilesY, mapHeight);
		
		float scale = super.getTileSize(screenWidth, screenHeight, mapWidth, mapHeight);
		float cut = 1 - ((screenWidth * screenHeight) / (tx * scale * ty * scale));
		
		if (cut > maximumCut)
		{
			if ((screenWidth * 1f / screenHeight) < (tx / ty)) {
				scale = ((screenWidth * ty) / (1 - maximumCut)) / (tx * ty);
			}
			else if ((screenWidth * 1f / screenHeight) > (tx / ty)) {
				scale = ((tx * screenHeight) / (1 - maximumCut)) / (tx * ty);
			}
		}
		
		return scale;
	}

}
package de.samdev.absgdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class Textures {
	public static Texture texmap;
	
	public static TextureRegion tex_dirt;
	
	public static TextureRegion tex_GroundTile_TL;
	public static TextureRegion tex_GroundTile_TR;
	public static TextureRegion tex_GroundTile_BL;
	public static TextureRegion tex_GroundTile_BR;
	
	public static TextureRegion tex_Bucket_empty;
	public static TextureRegion tex_Bucket_full;
	public static TextureRegion tex_Bucket_hay;

	public static TextureRegion tex_Flowers_empty;

	public static TextureRegion tex_Bush_empty;
	public static TextureRegion tex_Bush_full;

	public static TextureRegion tex_Anchorpoint_empty;
	public static TextureRegion tex_Anchorpoint_full;

	public static TextureRegion tex_Angel;

	public static void init() {
		texmap = new Texture("map.png");
		
		tex_dirt = new TextureRegion(texmap, 9*16, 9*16, 16, 16);

		tex_GroundTile_TL = new TextureRegion(texmap, 0*16, 6*16, 16, 16);
		tex_GroundTile_TR = new TextureRegion(texmap, 1*16, 6*16, 16, 16);
		tex_GroundTile_BL = new TextureRegion(texmap, 0*16, 7*16, 16, 16);
		tex_GroundTile_BR = new TextureRegion(texmap, 1*16, 7*16, 16, 16);
		
		tex_Bucket_empty = new TextureRegion(texmap, 10*32, 24*32, 32, 32);
		tex_Bucket_full = new TextureRegion(texmap, 11*32, 24*32, 32, 32);
		tex_Bucket_hay = new TextureRegion(texmap, 12*32, 24*32, 32, 32);
		
		tex_Flowers_empty = new TextureRegion(texmap, 8*32, 25*32, 32, 32);

		tex_Bush_empty = new TextureRegion(texmap, 10*32, 26*32, 32, 32);
		tex_Bush_full = new TextureRegion(texmap, 10*32, 27*32, 32, 32);

		tex_Angel = new TextureRegion(texmap, 8*32, 26*32, 32, 64);
		
		tex_Anchorpoint_empty = new TextureRegion(texmap, 8*32, 24*32, 32, 32);
		tex_Anchorpoint_full = new TextureRegion(texmap, 9*32, 24*32, 32, 32);
	}
}

package de.samdev.absgdx.framework.layer;

import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import de.samdev.absgdx.framework.AgdxGame;
import de.samdev.absgdx.framework.entities.Entity;
import de.samdev.absgdx.framework.entities.colliosiondetection.CollisionBox;
import de.samdev.absgdx.framework.entities.colliosiondetection.CollisionCircle;
import de.samdev.absgdx.framework.entities.colliosiondetection.CollisionGeometry;
import de.samdev.absgdx.framework.entities.colliosiondetection.CollisionMap;
import de.samdev.absgdx.framework.map.TileMap;
import de.samdev.absgdx.framework.map.mapscaleresolver.AbstractMapScaleResolver;
import de.samdev.absgdx.framework.map.mapscaleresolver.ShowCompleteMapScaleResolver;
import de.samdev.absgdx.framework.math.SortedLinkedEntityList;

/**
 * Game layer represents a level or the whole game.
 * It contains the TileMap and the Entities
 */
public abstract class GameLayer extends AgdxLayer {
	
	//######## MAP ########
	
	protected final TileMap map;
	protected Vector2 map_offset = new Vector2(0, 0);

	private AbstractMapScaleResolver mapScaleResolver = new ShowCompleteMapScaleResolver();

	//######## ENTITIES ########
	
	protected final SortedLinkedEntityList entities = new SortedLinkedEntityList();
	protected final CollisionMap collisionMap;
	
	/**
	 * Creates a new GameLayer
	 * 
	 * @param owner
	 * @param map 
	 */
	public GameLayer(AgdxGame owner, TileMap map) {
		super(owner);

		this.map = map;
		this.collisionMap = new CollisionMap(map.width, map.height);
	}

	@Override
	public void render(SpriteBatch sbatch, ShapeRenderer srenderer) {
		float tilesize = mapScaleResolver.getTileSize(owner.getScreenWidth(), owner.getScreenHeight(), map.height, map.width);

		Rectangle visible = getVisibleMapBox();
		
		srenderer.identity();
		srenderer.scale(tilesize, tilesize, 1);
		srenderer.translate(-map_offset.x, -map_offset.y, 0);
		
		sbatch.getTransformMatrix().idt();
		sbatch.getTransformMatrix().scale(tilesize, tilesize, 1);
		sbatch.getTransformMatrix().translate(-map_offset.x, -map_offset.y, 0);
		
		sbatch.disableBlending();
		sbatch.begin();
		for (int y = (int) visible.y; y < Math.min(map.height, (int)(visible.y + visible.height + 1)); y++) {
			for (int x = (int) visible.x; x < Math.min(map.width, (int)(visible.x + visible.width + 1)); x++) {
				TextureRegion r = map.getTile(x, y).getTexture();
				
				if (r != null)
					sbatch.draw(r, x, y, 1, 1);
			}
		}
		sbatch.end();
		
		if (owner.settings.debugMapGridLines.isActive()) {
			srenderer.begin(ShapeType.Line);
			srenderer.setColor(owner.settings.debugMapGridLinesColor.get());
			
			for (int y = (int) visible.y; y < Math.min(map.height, (int)(visible.y + visible.height + 1)); y++) {
				for (int x = (int) visible.x; x < Math.min(map.width, (int)(visible.x + visible.width + 1)); x++) {
					srenderer.rect(x, y, 1, 1);
					
					if (owner.settings.debugCollisionMapMarkers.isActive()) {
						if (! collisionMap.map[x][y].geometries.isEmpty()) {
							srenderer.line(x, y, x+1, y+1);
							srenderer.line(x+1, y, x, y+1);
						}
					}
				}
			}
			srenderer.end();
		}
		
		sbatch.enableBlending();
		sbatch.begin();
		for( Iterator<Entity> it = entities.descendingIterator(); it.hasNext();) { // Iterate reverse (so z order is correct)
		    Entity entity = it.next();
		    
//			if (visible.contains(entity.getBoundings())) {
				sbatch.draw(entity.getTexture(), entity.getPositionX(), entity.getPositionY(), entity.getWidth(), entity.getHeight());
//			}
		}
		sbatch.end();
		
		if (owner.settings.debugVisualEntities.isActive()) {
			srenderer.begin(ShapeType.Line);
			
			for( Iterator<Entity> it = entities.descendingIterator(); it.hasNext();) {
			    Entity entity = it.next();
			    
			    if (owner.settings.debugEntitiesBoundingBoxes.isActive()) {
					srenderer.setColor(owner.settings.debugEntitiesBoundingBoxesColor.get());
			    	srenderer.rect(entity.getPositionX(), entity.getPositionY(), entity.getWidth(), entity.getHeight());			    	
			    }
			    
			    if (owner.settings.debugEntitiesCollisionGeometries.isActive()) {
			    	for( Iterator<CollisionGeometry> itc = entity.listCollisionGeometries(); itc.hasNext();) {
			    		CollisionGeometry collGeo = itc.next();

						srenderer.setColor(owner.settings.debugEntitiesCollisionGeometriesColor.get());
						if (collGeo instanceof CollisionCircle) {
							srenderer.circle(collGeo.getCenterX(), collGeo.getCenterY(), collGeo.getRadius(), 16);
						} else if (collGeo instanceof CollisionBox) {
							CollisionBox collBox = (CollisionBox) collGeo;
							srenderer.rect(collBox.getX(), collBox.getY(), collBox.width, collBox.height);
						}
			    	}
			    }
			}

			srenderer.end();
		}
	}

	@Override
	public void update(float delta) {
		map.update(delta);

		for (Entity entity : entities) {
			entity.update(delta);
		}
		entities.removeDeadEntities();
		
		onUpdate(delta);
	}

	@Override
	public void onResize() {
		setBoundedOffset(map_offset);
	}

	/**
	 * @return the currently visible tiles (in tile-coordinates : 1 tile = 1 unit)
	 */
	public Rectangle getVisibleMapBox() {
		float tilesize = mapScaleResolver.getTileSize(owner.getScreenWidth(), owner.getScreenHeight(), map.height, map.width);
		
		Rectangle view = new Rectangle(map_offset.x, map_offset.y, owner.getScreenWidth() / tilesize, owner.getScreenHeight() / tilesize);
		
		return view;
	}
	
	/**
	 * Fixes the offset - it gets re-adjusted in case the current viewport has left the map
	 */
	private void limitOffset() {
		Rectangle viewport = getVisibleMapBox();
		
		if (viewport.x + viewport.width > map.width) {
			map_offset.x = map.width - viewport.width;
		}
		
		if (viewport.y + viewport.height > map.height) {
			map_offset.y = map.height - viewport.height;
		}
		
		viewport = getVisibleMapBox();
		
		if (viewport.x < 0) {
			map_offset.x = 0;
		}
		
		if (viewport.y < 0) {
			map_offset.y = 0;
		}
	}

	/**
	 * Sets the offset but limits it so that it can't leave the map boundaries
	 * 
	 * @param offset
	 */
	public void setBoundedOffset(Vector2 offset) {
		setRawOffset(offset);

		limitOffset();
	}

	/**
	 * Sets the offset, but unlike setBoundedOffset() it won't check the boundaries
	 * 
	 * @param offset
	 */
	public void setRawOffset(Vector2 offset) {
		this.map_offset = offset;
	}
	
	/**
	 * Sets the mapScaleResolver - the component to determine the size of a tile
	 * 
	 * @param resolver
	 */
	public void setMapScaleResolver(AbstractMapScaleResolver resolver) {
		this.mapScaleResolver = resolver;
	}
	
	/**
	 * return the map offset (in tile coordinates) (offset=1 :=> 1 tile offset)
	 * 
	 * @return
	 */
	public Vector2 getMapOffset() {
		return map_offset;
	}
	
	/**
	 * return the tile scale (= tile size)
	 * 
	 * @return
	 */
	public float getTileScale() {
		return mapScaleResolver.getTileSize(owner.getScreenWidth(), owner.getScreenHeight(), map.height, map.width);
	}
	
	/**
	 * Gets after each update() called
	 * 
	 * @param delta the time since the last update (in ms) - can be averaged over he last few cycles
	 */
	public abstract void onUpdate(float delta);

	/**
	 * return the (tiled) map
	 * 
	 * @return
	 */
	public TileMap getMap() {
		return map;
	}
	
	/**
	 * Adds an Entity to the game
	 * 
	 * @param e the to add entity
	 */
	public void addEntity(Entity e) {
		entities.add(e);
		
		e.collisionOwner = collisionMap;
		
		e.onLayerAdd(this);
	}
	
	/**
	 * Gets an List of entities to iterate through (DONT CHANGE THE LIST !)
	 * 
	 * @return
	 */
	public List<Entity> iterateEntities() {
		return entities;
	}

	/**
	 * Gets the amount of currently registered Entities
	 * 
	 * @return
	 */
	public int getEntityCount() {
		return entities.size();
	}
}

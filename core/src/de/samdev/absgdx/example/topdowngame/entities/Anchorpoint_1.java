package de.samdev.absgdx.example.topdowngame.entities;

import de.samdev.absgdx.example.Textures;
import de.samdev.absgdx.framework.entities.Entity;
import de.samdev.absgdx.framework.entities.colliosiondetection.CollisionGeometryOwner;
import de.samdev.absgdx.framework.entities.colliosiondetection.geometries.CollisionGeometry;
import de.samdev.absgdx.framework.layer.GameLayer;

public class Anchorpoint_1 extends Entity {
	
	public Anchorpoint_1() {
		super(Textures.tex_Anchorpoint_empty, 2, 4);
	}
	
	@Override
	public void onLayerAdd(GameLayer layer) {
		setPosition(30f, 60f);
		
//		addCollisionGeo(0.65f, 1.15f, new CollisionCircle(this, 0.35f));
//		addCollisionGeo(1.35f, 1.15f, new CollisionCircle(this, 0.35f));
		
//		addCollisionGeo(1.0f, 0.85f, new CollisionBox(this, 0.8f, 1.2f));
		
//		addFullCollisionTriangle(AlignCorner4.BOTTOMRIGHT);
		addFullCollisionBox();
	}

	@Override
	public void beforeUpdate(float delta) {
		speed.set(0,0);
		
//		if (Gdx.input.isKeyPressed(Keys.W)) speed.y += 0.01;
//		if (Gdx.input.isKeyPressed(Keys.A)) speed.x -= 0.01;
//		if (Gdx.input.isKeyPressed(Keys.S)) speed.y -= 0.01;
//		if (Gdx.input.isKeyPressed(Keys.D)) speed.x += 0.01;
//
//		if (Gdx.input.isKeyJustPressed(Keys.H)) setPositionY(getPositionY()+0.25f);
	}

	@Override
	public void onActiveCollide(CollisionGeometryOwner passiveCollider, CollisionGeometry myGeo, CollisionGeometry otherGeo) {
//		System.out.println("[COLLISION ACTIVE] " + this.getClass().getSimpleName() + " -> " + passiveCollider.getClass().getSimpleName() + "(" + Integer.toHexString(myGeo.hashCode()) + " | " + Integer.toHexString(otherGeo.hashCode()) + ")");
	}

	@Override
	public void onPassiveCollide(CollisionGeometryOwner activeCollider, CollisionGeometry myGeo, CollisionGeometry otherGeo) {
//		System.out.println("[COLLISION PASSIV] " + this.getClass().getSimpleName() + " -> " + activeCollider.getClass().getSimpleName() + "(" + Integer.toHexString(myGeo.hashCode()) + " | " + Integer.toHexString(otherGeo.hashCode()) + ")");
	}

	@Override
	public void onActiveMovementCollide(CollisionGeometryOwner passiveCollider, CollisionGeometry myGeo, CollisionGeometry otherGeo) {
//		System.out.println("[MOVE COLL ACTIVE] " + this.getClass().getSimpleName() + " -> " + passiveCollider.getClass().getSimpleName() + "(" + Integer.toHexString(myGeo.hashCode()) + " | " + Integer.toHexString(otherGeo.hashCode()) + ")");
	}

	@Override
	public void onPassiveMovementCollide(CollisionGeometryOwner activeCollider, CollisionGeometry myGeo, CollisionGeometry otherGeo) {
//		System.out.println("[MOVE COLL PASSIV] " + this.getClass().getSimpleName() + " -> " + activeCollider.getClass().getSimpleName() + "(" + Integer.toHexString(myGeo.hashCode()) + " | " + Integer.toHexString(otherGeo.hashCode()) + ")");
	}

	@Override
	public boolean canCollideWith(CollisionGeometryOwner other) {
		return true;
	}

	@Override
	public boolean canMoveCollideWith(CollisionGeometryOwner other) {
		return other.getClass() != Bucket_1.class && other.getClass() != Bucket_2.class && other.getClass() != Bucket_3.class;
	}

	float sx = 0;
	@Override
	public float getTextureScaleX() {
		return (float) Math.sin(sx+=0.01)*10;
	}

	float sy = 1;
	@Override
	public float getTextureScaleY() {
		return (float) Math.sin(sy+=0.02)*10;
	}

	float r = 0;
	@Override
	public float getTextureRotation() {
		return r += 1.75;
	}
	
}

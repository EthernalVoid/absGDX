package de.samdev.absgdx.topdowngame.entities;

import com.badlogic.gdx.math.Vector2;

import de.samdev.absgdx.Textures;
import de.samdev.absgdx.framework.entities.Entity;
import de.samdev.absgdx.framework.entities.colliosiondetection.CollisionGeometryOwner;
import de.samdev.absgdx.framework.entities.colliosiondetection.geometries.CollisionCircle;
import de.samdev.absgdx.framework.entities.colliosiondetection.geometries.CollisionGeometry;
import de.samdev.absgdx.framework.layer.GameLayer;
import de.samdev.absgdx.topdowngame.TopDownGameLayer;

public class Bucket_2 extends Entity {

	public TopDownGameLayer owner;
	
	private Vector2 acceleration = addNewAcceleration();

	public Bucket_2() {
		super(Textures.tex_Bucket_empty, 2, 2);
	}

	@Override
	public void onLayerAdd(GameLayer layer) {
		setPosition(25f, 15f);
		
		this.speed.y = - 5/1000f;
		
		addCollisionGeo(1, 1, new CollisionCircle(this, 1f));
	}
	
	@Override
	public void beforeUpdate(float delta) {
		this.acceleration.set(20, 20).sub(getPosition()).nor().scl(1/1000f * 1/100f);
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
		return other.getClass() != Bucket_1.class && other.getClass() != Bucket_2.class && other.getClass() != Bucket_3.class;
	}

	@Override
	public boolean canMoveCollideWith(CollisionGeometryOwner other) {
		return other.getClass() != Anchorpoint_1.class;
	}
}

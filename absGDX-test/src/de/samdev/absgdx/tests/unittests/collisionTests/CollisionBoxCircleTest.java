package de.samdev.absgdx.tests.unittests.collisionTests;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.badlogic.gdx.math.Vector2;

import de.samdev.absgdx.framework.entities.colliosiondetection.geometries.CollisionBox;
import de.samdev.absgdx.framework.entities.colliosiondetection.geometries.CollisionCircle;
import de.samdev.absgdx.tests.dummy.DummyGameLayer;
import de.samdev.absgdx.tests.dummy.DummyNoCollisionTileMap;
import de.samdev.absgdx.tests.dummy.DummyNoTileCollisionEntity;

@RunWith(Parameterized.class)
public class CollisionBoxCircleTest extends CollisionTest {

	@Parameters(name= "({1}|{2}) / {0}")
	public static Collection<Object[]> data() { return getParameters(); }
	
	public CollisionBoxCircleTest(int mapScale, int mapW, int mapH) {
		super(mapScale, mapW, mapH);
	}

    //#########################################################################

    @Test
    public void testBoxCircleCollisionMovement_1D_X() {
    	DummyGameLayer l = new DummyGameLayer(this.mapWidth, this.mapHeight, new DummyNoCollisionTileMap(this.mapWidth, this.mapHeight), this.expMapScale);
    	
    	DummyNoTileCollisionEntity e1 = new DummyNoTileCollisionEntity();
    	l.addEntity(e1);
    	e1.addCollisionGeo(0.5f, 0.5f, new CollisionBox(e1, 2f, 2f));
    	
    	DummyNoTileCollisionEntity e2 = new DummyNoTileCollisionEntity();
    	l.addEntity(e2);
    	e2.addCollisionGeo(0.5f, 0.5f, new CollisionCircle(e2, 1f));
    	
    	e1.setPosition(10, 10);
    	e2.setPosition(40, 10);
    	
    	assertEquals("0-1-0-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-0-0-0", e2.dummyCtrSignSummary());
    	
    	for (int i = 0; i < 400; i++)
    		e1.movePosition(0.1f, 0);
    	
    	assertEqualsExt(new Vector2(38, 10), e1.getPosition());

    	assertEquals(null, l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	assertEquals(null, l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-0-0-1-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-0-0-1", e2.dummyCtrSignSummary());
    }
    
    @Test
    public void testBoxCircleCollisionMovement_1D_Y() {
    	DummyGameLayer l = new DummyGameLayer(this.mapWidth, this.mapHeight, new DummyNoCollisionTileMap(this.mapWidth, this.mapHeight), this.expMapScale);
    	
    	DummyNoTileCollisionEntity e1 = new DummyNoTileCollisionEntity();
    	l.addEntity(e1);
    	e1.addCollisionGeo(0.5f, 0.5f, new CollisionBox(e1, 2f, 2f));
    	
    	DummyNoTileCollisionEntity e2 = new DummyNoTileCollisionEntity();
    	l.addEntity(e2);
    	e2.addCollisionGeo(0.5f, 0.5f, new CollisionCircle(e2, 1f));
    	
    	e1.setPosition(10, 10);
    	e2.setPosition(10, 40);
    	
    	assertEquals("0-1-0-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-0-0-0", e2.dummyCtrSignSummary());
    	
    	for (int i = 0; i < 400; i++)
    		e1.movePosition(0, 0.1f);
    	
    	assertEqualsExt(new Vector2(10, 38), e1.getPosition());

    	assertEquals(null, l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	assertEquals(null, l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-0-0-1-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-0-0-1", e2.dummyCtrSignSummary());
    }
    
    @Test
    public void testBoxCircleCollisionMovement_2D() {
    	DummyGameLayer l = new DummyGameLayer(this.mapWidth, this.mapHeight, new DummyNoCollisionTileMap(this.mapWidth, this.mapHeight), this.expMapScale);
    	
    	DummyNoTileCollisionEntity e1 = new DummyNoTileCollisionEntity();
    	l.addEntity(e1);
    	e1.addCollisionGeo(0.5f, 0.5f, new CollisionBox(e1, 2f, 2f));
    	
    	DummyNoTileCollisionEntity e2 = new DummyNoTileCollisionEntity();
    	l.addEntity(e2);
    	e2.addCollisionGeo(0.5f, 0.5f, new CollisionCircle(e2, 1f));
    	
    	e1.setPosition(10, 8.5f);
    	e2.setPosition(40, 10);
    	
    	assertEquals("0-1-0-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-0-0-0", e2.dummyCtrSignSummary());
    	
    	for (int i = 0; i < 400; i++)
    		e1.movePosition(0.1f, 0);
    	
    	assertEqualsExt(new Vector2(38.1339746f, 8.5f), e1.getPosition(), 0.0001f);

    	assertEquals(null, l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	assertEquals(null, l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-0-0-1-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-0-0-1", e2.dummyCtrSignSummary());
    }
    
    @Test
    public void testBoxCircleCollision_T() {
    	DummyGameLayer l = new DummyGameLayer(this.mapWidth, this.mapHeight, new DummyNoCollisionTileMap(this.mapWidth, this.mapHeight), this.expMapScale);
    	
    	DummyNoTileCollisionEntity e1 = new DummyNoTileCollisionEntity();
    	l.addEntity(e1);
    	e1.addCollisionGeo(0.5f, 0.5f, new CollisionBox(e1, 2f, 2f));
    	
    	DummyNoTileCollisionEntity e2 = new DummyNoTileCollisionEntity();
    	l.addEntity(e2);
    	e2.addCollisionGeo(0.5f, 0.5f, new CollisionCircle(e2, 1f));
    	
    	e2.setPosition(10f, 10f);
    	e1.setPosition(10f, 12.5f);

    	assertEquals(null, l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	assertEquals(null, l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-0-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-0-0-0", e2.dummyCtrSignSummary());
    	
    	e2.setPosition(10f, 10f);
    	e1.setPosition(10f, 10.5f);

    	assertEquals(e1.listCollisionGeometries().next(), l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	assertEquals(e2.listCollisionGeometries().next(), l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-1-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-1-0-0", e2.dummyCtrSignSummary());
    }
    
    @Test
    public void testBoxCircleCollision_TR() {
    	DummyGameLayer l = new DummyGameLayer(this.mapWidth, this.mapHeight, new DummyNoCollisionTileMap(this.mapWidth, this.mapHeight), this.expMapScale);
    	
    	DummyNoTileCollisionEntity e1 = new DummyNoTileCollisionEntity();
    	l.addEntity(e1);
    	e1.addCollisionGeo(0.5f, 0.5f, new CollisionBox(e1, 2f, 2f));
    	
    	DummyNoTileCollisionEntity e2 = new DummyNoTileCollisionEntity();
    	l.addEntity(e2);
    	e2.addCollisionGeo(0.5f, 0.5f, new CollisionCircle(e2, 1f));
    	
    	e2.setPosition(10f, 10f);
    	e1.setPosition(12f, 12f);

    	assertEquals(null, l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	assertEquals(null, l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-0-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-0-0-0", e2.dummyCtrSignSummary());
    	
    	e2.setPosition(10f, 10f);
    	e1.setPosition(10.5f, 10.5f);

    	assertEquals(e1.listCollisionGeometries().next(), l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	assertEquals(e2.listCollisionGeometries().next(), l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-1-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-1-0-0", e2.dummyCtrSignSummary());
    }
    
    @Test
    public void testBoxCircleCollision_R() {
    	DummyGameLayer l = new DummyGameLayer(this.mapWidth, this.mapHeight, new DummyNoCollisionTileMap(this.mapWidth, this.mapHeight), this.expMapScale);
    	
    	DummyNoTileCollisionEntity e1 = new DummyNoTileCollisionEntity();
    	l.addEntity(e1);
    	e1.addCollisionGeo(0.5f, 0.5f, new CollisionBox(e1, 2f, 2f));
    	
    	DummyNoTileCollisionEntity e2 = new DummyNoTileCollisionEntity();
    	l.addEntity(e2);
    	e2.addCollisionGeo(0.5f, 0.5f, new CollisionCircle(e2, 1f));
    	
    	e2.setPosition(10f, 10f);
    	e1.setPosition(12.5f, 10f);

    	assertEquals(null, l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	assertEquals(null, l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-0-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-0-0-0", e2.dummyCtrSignSummary());
    	
    	e2.setPosition(10f, 10f);
    	e1.setPosition(10.5f, 10f);

    	assertEquals(e1.listCollisionGeometries().next(), l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	assertEquals(e2.listCollisionGeometries().next(), l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-1-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-1-0-0", e2.dummyCtrSignSummary());
    }
    
    @Test
    public void testBoxCircleCollision_BR() {
    	DummyGameLayer l = new DummyGameLayer(this.mapWidth, this.mapHeight, new DummyNoCollisionTileMap(this.mapWidth, this.mapHeight), this.expMapScale);
    	
    	DummyNoTileCollisionEntity e1 = new DummyNoTileCollisionEntity();
    	l.addEntity(e1);
    	e1.addCollisionGeo(0.5f, 0.5f, new CollisionBox(e1, 2f, 2f));
    	
    	DummyNoTileCollisionEntity e2 = new DummyNoTileCollisionEntity();
    	l.addEntity(e2);
    	e2.addCollisionGeo(0.5f, 0.5f, new CollisionCircle(e2, 1f));
    	
    	e2.setPosition(10f, 10f);
    	e1.setPosition(12f, 8f);

    	assertEquals(null, l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	assertEquals(null, l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-0-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-0-0-0", e2.dummyCtrSignSummary());
    	
    	e2.setPosition(10f, 10f);
    	e1.setPosition(10.5f, 9.5f);

    	assertEquals(e1.listCollisionGeometries().next(), l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	assertEquals(e2.listCollisionGeometries().next(), l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-1-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-1-0-0", e2.dummyCtrSignSummary());
    }
    
    @Test
    public void testBoxCircleCollision_B() {
    	DummyGameLayer l = new DummyGameLayer(this.mapWidth, this.mapHeight, new DummyNoCollisionTileMap(this.mapWidth, this.mapHeight), this.expMapScale);
    	
    	DummyNoTileCollisionEntity e1 = new DummyNoTileCollisionEntity();
    	l.addEntity(e1);
    	e1.addCollisionGeo(0.5f, 0.5f, new CollisionBox(e1, 2f, 2f));
    	
    	DummyNoTileCollisionEntity e2 = new DummyNoTileCollisionEntity();
    	l.addEntity(e2);
    	e2.addCollisionGeo(0.5f, 0.5f, new CollisionCircle(e2, 1f));
    	
    	e2.setPosition(10f, 10f);
    	e1.setPosition(10f, 7.5f);

    	assertEquals(null, l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	assertEquals(null, l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-0-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-0-0-0", e2.dummyCtrSignSummary());
    	
    	e2.setPosition(10f, 10f);
    	e1.setPosition(10f, 9.5f);

    	assertEquals(e1.listCollisionGeometries().next(), l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	assertEquals(e2.listCollisionGeometries().next(), l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-1-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-1-0-0", e2.dummyCtrSignSummary());
    }
    
    @Test
    public void testBoxCircleCollision_BL() {
    	DummyGameLayer l = new DummyGameLayer(this.mapWidth, this.mapHeight, new DummyNoCollisionTileMap(this.mapWidth, this.mapHeight), this.expMapScale);
    	
    	DummyNoTileCollisionEntity e1 = new DummyNoTileCollisionEntity();
    	l.addEntity(e1);
    	e1.addCollisionGeo(0.5f, 0.5f, new CollisionBox(e1, 2f, 2f));
    	
    	DummyNoTileCollisionEntity e2 = new DummyNoTileCollisionEntity();
    	l.addEntity(e2);
    	e2.addCollisionGeo(0.5f, 0.5f, new CollisionCircle(e2, 1f));
    	
    	e2.setPosition(10f, 10f);
    	e1.setPosition(8f, 8f);

    	assertEquals(null, l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	assertEquals(null, l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-0-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-0-0-0", e2.dummyCtrSignSummary());
    	
    	e2.setPosition(10f, 10f);
    	e1.setPosition(9.5f, 9.5f);

    	assertEquals(e1.listCollisionGeometries().next(), l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	assertEquals(e2.listCollisionGeometries().next(), l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-1-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-1-0-0", e2.dummyCtrSignSummary());
    }
    
    @Test
    public void testBoxCircleCollision_L() {
    	DummyGameLayer l = new DummyGameLayer(this.mapWidth, this.mapHeight, new DummyNoCollisionTileMap(this.mapWidth, this.mapHeight), this.expMapScale);
    	
    	DummyNoTileCollisionEntity e1 = new DummyNoTileCollisionEntity();
    	l.addEntity(e1);
    	e1.addCollisionGeo(0.5f, 0.5f, new CollisionBox(e1, 2f, 2f));
    	
    	DummyNoTileCollisionEntity e2 = new DummyNoTileCollisionEntity();
    	l.addEntity(e2);
    	e2.addCollisionGeo(0.5f, 0.5f, new CollisionCircle(e2, 1f));
    	
    	e2.setPosition(10f, 10f);
    	e1.setPosition(7.5f, 10f);

    	assertEquals(null, l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	assertEquals(null, l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-0-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-0-0-0", e2.dummyCtrSignSummary());
    	
    	e2.setPosition(10f, 10f);
    	e1.setPosition(9.5f, 10f);

    	assertEquals(e1.listCollisionGeometries().next(), l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	assertEquals(e2.listCollisionGeometries().next(), l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-1-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-1-0-0", e2.dummyCtrSignSummary());
    }
    
    @Test
    public void testBoxCircleCollision_TL() {
    	DummyGameLayer l = new DummyGameLayer(this.mapWidth, this.mapHeight, new DummyNoCollisionTileMap(this.mapWidth, this.mapHeight), this.expMapScale);
    	
    	DummyNoTileCollisionEntity e1 = new DummyNoTileCollisionEntity();
    	l.addEntity(e1);
    	e1.addCollisionGeo(0.5f, 0.5f, new CollisionBox(e1, 2f, 2f));
    	
    	DummyNoTileCollisionEntity e2 = new DummyNoTileCollisionEntity();
    	l.addEntity(e2);
    	e2.addCollisionGeo(0.5f, 0.5f, new CollisionCircle(e2, 1f));
    	
    	e2.setPosition(10f, 10f);
    	e1.setPosition(8f, 12f);

    	assertEquals(null, l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	assertEquals(null, l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-0-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-0-0-0", e2.dummyCtrSignSummary());
    	
    	e2.setPosition(10f, 10f);
    	e1.setPosition(9.5f, 10.5f);

    	assertEquals(e1.listCollisionGeometries().next(), l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	assertEquals(e2.listCollisionGeometries().next(), l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-1-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-1-0-0", e2.dummyCtrSignSummary());
    }
    
    @Test
    public void testBoxCircleCollision_C() {
    	DummyGameLayer l = new DummyGameLayer(this.mapWidth, this.mapHeight, new DummyNoCollisionTileMap(this.mapWidth, this.mapHeight), this.expMapScale);
    	
    	DummyNoTileCollisionEntity e1 = new DummyNoTileCollisionEntity();
    	l.addEntity(e1);
    	e1.addCollisionGeo(0.5f, 0.5f, new CollisionBox(e1, 2f, 2f));
    	
    	DummyNoTileCollisionEntity e2 = new DummyNoTileCollisionEntity();
    	l.addEntity(e2);
    	e2.addCollisionGeo(0.5f, 0.5f, new CollisionCircle(e2, 1f));
    	
    	e2.setPosition(10f, 10f);
    	e1.setPosition(100f, 100f);

    	assertEquals(null, l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	assertEquals(null, l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-0-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-0-0-0", e2.dummyCtrSignSummary());
    	
    	e2.setPosition(10f, 10f);
    	e1.setPosition(10f, 10f);

    	assertEquals(e1.listCollisionGeometries().next(), l.collisionMap().getFirstCollider(e2.listCollisionGeometries().next()));
    	assertEquals(e2.listCollisionGeometries().next(), l.collisionMap().getFirstCollider(e1.listCollisionGeometries().next()));
    	
    	assertEquals("0-1-1-0-0-0", e1.dummyCtrSignSummary());
    	assertEquals("0-1-0-1-0-0", e2.dummyCtrSignSummary());
    }
}

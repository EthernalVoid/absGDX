package de.samdev.absgdx.framework.entities.colliosiondetection.geometries;

import com.badlogic.gdx.math.Vector2;

import de.samdev.absgdx.framework.entities.colliosiondetection.CollisionGeometryOwner;
import de.samdev.absgdx.framework.math.FloatMath;
import de.samdev.absgdx.framework.math.ShapeMath;

/**
 * A Collision Geometry in the shape of a triangle 
 *
 */
public class CollisionTriangle extends CollisionGeometry {
	
	/** The triangle point1.x (points are CounterClockWise)*/
	public final float point1_x;
	/** The triangle point1.y (points are CounterClockWise)*/
	public final float point1_y;
	/** The triangle point2.x (points are CounterClockWise)*/
	public final float point2_x;
	/** The triangle point2.y (points are CounterClockWise)*/
	public final float point2_y;
	/** The triangle point3.x (points are CounterClockWise)*/
	public final float point3_x;	
	/** The triangle point3.y (points are CounterClockWise)*/
	public final float point3_y;

	/** The circumRadius of the triangle*/
	public final float circumRadius;
	
	/** This is the x-value the initial points (in the constructor) got corrected*/
	public final float centroidCorrection_x;
	/** This is the y-value the initial points (in the constructor) got corrected*/
	public final float centroidCorrection_y;
	
	/**
	 * Creates a new Triangle
	 * 
	 * The vertices will be made relative to their centroid (!) they will use every translation they have
	 * 
	 * @param owner the Entity that owns this geometry
	 * @param p1x the first point (X)
	 * @param p1y the first point (Y) 
	 * @param p2x the second point (X) 
	 * @param p2y the second point (Y) 
	 * @param p3x the third point (X) 
	 * @param p3y the third point (Y)
	 * 
	 * @throws IllegalArgumentException if the 3 vertices lie in a line
	 */
	public CollisionTriangle(CollisionGeometryOwner owner, float p1x, float p1y, float p2x, float p2y, float p3x, float p3y) {
		super(owner);
		
		if ((p1x - p3x) * (p2y - p1y) <  (p1x - p2y) * (p3x - p1y)) { // ensure CCW
			float tmpx = p2x;
			p2x = p1x;
			p1x = tmpx;
			
			float tmpy = p2y;
			p2y = p1y;
			p1y = tmpy;
		}
		
		if ((p1x - p3x) * (p2y - p1y) ==  (p1x - p2x) * (p3y - p1y)) { // ensure real triangles
			throw new IllegalArgumentException("polygons must have an area (!= 0).");
		}
		
		this.centroidCorrection_x = (p1x+p2x+p3x) / 3f;
		this.centroidCorrection_y = (p1y+p2y+p3y) / 3f;
        		
		// Normalize around circumCenter
		p1x -= centroidCorrection_x;
		p1y -= centroidCorrection_y;
		p2x -= centroidCorrection_x;
		p2y -= centroidCorrection_y;
		p3x -= centroidCorrection_x;
		p3y -= centroidCorrection_y;
		
		this.circumRadius = FloatMath.fsqrt(FloatMath.fmax(FloatMath.fpyth(p1x, p1y), FloatMath.fpyth(p2x, p2y), FloatMath.fpyth(p3x, p3y)));
		
		this.point1_x = p1x;
		this.point1_y = p1y;
		this.point2_x = p2x;		
		this.point2_y = p2y;			
		this.point3_x = p3x;		
		this.point3_y = p3y;
	}
	
	/**
	 * Creates a new Triangle
	 * 
	 * The vertices will be made relative to their circumCenter (!) they will use every translation they have
	 * 
	 * @param owner the Entity that owns this geometry
	 * @param p1 the first point
	 * @param p2 the second point
	 * @param p3 the third point
	 * 
	 * @throws IllegalArgumentException if the 3 vertices lie in a line
	 */
	public CollisionTriangle(CollisionGeometryOwner owner, Vector2 p1, Vector2 p2, Vector2 p3) {
		this(owner, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
	}
	
	@Override
	public float area() {
		return 0.5f * Math.abs((point1_x - point3_x) * (point2_y - point1_y) - (point1_x - point2_x) * (point3_y - point1_y));
	}

	@Override
	public float getRadius() {
		return circumRadius;
	}
	
	/**
	 * Get point1 X coordinate
	 * 
	 * @return position[0].x
	 */
	public float getPoint1_X() {
		return center.x + point1_x;
	}

	/**
	 * Get point1 Y coordinate
	 * 
	 * @return position[1].y
	 */
	public float getPoint1_Y() {
		return center.y + point1_y;
	}

	/**
	 * Get point2 X coordinate
	 * 
	 * @return position[2].x
	 */
	public float getPoint2_X() {
		return center.x + point2_x;
	}

	/**
	 * Get point2 Y coordinate
	 * 
	 * @return position[2].y
	 */
	public float getPoint2_Y() {
		return center.y + point2_y;
	}

	/**
	 * Get point3 X coordinate
	 * 
	 * @return position[3].x
	 */
	public float getPoint3_X() {
		return center.x + point3_x;
	}

	/**
	 * Get point3 Y coordinate
	 * 
	 * @return position[3].y
	 */
	public float getPoint3_Y() {
		return center.y + point3_y;
	}
	
	/**
	 * Get point1
	 * 
	 * @return position[1]
	 */
	public Vector2 getPoint1() {
		return new Vector2(getPoint1_X(), getPoint1_Y());
	}
	
	/**
	 * Get point2
	 * 
	 * @return position[2]
	 */
	public Vector2 getPoint2() {
		return new Vector2(getPoint2_X(), getPoint2_Y());
	}
	
	/**
	 * Get point3
	 * 
	 * @return position[3]
	 */
	public Vector2 getPoint3() {
		return new Vector2(getPoint3_X(), getPoint3_Y());
	}

	@Override
	public float getXTouchDistance(CollisionBox other) {
		return ShapeMath.getXTouchDistance(this, other);
	}

	@Override
	public float getXTouchDistance(CollisionCircle other) {
		return ShapeMath.getXTouchDistance(this, other);
	}

	@Override
	public float getXTouchDistance(CollisionTriangle other) {
		float d1 = ShapeMath.getPointLineXDistance(getPoint1_X(), getPoint1_Y(), other.getPoint1_X(), other.getPoint1_Y(), other.getPoint2_X(), other.getPoint2_Y());
		float d2 = ShapeMath.getPointLineXDistance(getPoint1_X(), getPoint1_Y(), other.getPoint2_X(), other.getPoint2_Y(), other.getPoint3_X(), other.getPoint3_Y());
		float d3 = ShapeMath.getPointLineXDistance(getPoint1_X(), getPoint1_Y(), other.getPoint3_X(), other.getPoint3_Y(), other.getPoint1_X(), other.getPoint1_Y());

		float d4 = ShapeMath.getPointLineXDistance(getPoint2_X(), getPoint2_Y(), other.getPoint1_X(), other.getPoint1_Y(), other.getPoint2_X(), other.getPoint2_Y());
		float d5 = ShapeMath.getPointLineXDistance(getPoint2_X(), getPoint2_Y(), other.getPoint2_X(), other.getPoint2_Y(), other.getPoint3_X(), other.getPoint3_Y());
		float d6 = ShapeMath.getPointLineXDistance(getPoint2_X(), getPoint2_Y(), other.getPoint3_X(), other.getPoint3_Y(), other.getPoint1_X(), other.getPoint1_Y());

		float d7 = ShapeMath.getPointLineXDistance(getPoint3_X(), getPoint3_Y(), other.getPoint1_X(), other.getPoint1_Y(), other.getPoint2_X(), other.getPoint2_Y());
		float d8 = ShapeMath.getPointLineXDistance(getPoint3_X(), getPoint3_Y(), other.getPoint2_X(), other.getPoint2_Y(), other.getPoint3_X(), other.getPoint3_Y());
		float d9 = ShapeMath.getPointLineXDistance(getPoint3_X(), getPoint3_Y(), other.getPoint3_X(), other.getPoint3_Y(), other.getPoint1_X(), other.getPoint1_Y());
		
		float s1 = -ShapeMath.getPointLineXDistance(other.getPoint1_X(), other.getPoint1_Y(), getPoint1_X(), getPoint1_Y(), getPoint2_X(), getPoint2_Y());
		float s2 = -ShapeMath.getPointLineXDistance(other.getPoint1_X(), other.getPoint1_Y(), getPoint2_X(), getPoint2_Y(), getPoint3_X(), getPoint3_Y());
		float s3 = -ShapeMath.getPointLineXDistance(other.getPoint1_X(), other.getPoint1_Y(), getPoint3_X(), getPoint3_Y(), getPoint1_X(), getPoint1_Y());

		float s4 = -ShapeMath.getPointLineXDistance(other.getPoint2_X(), other.getPoint2_Y(), getPoint1_X(), getPoint1_Y(), getPoint2_X(), getPoint2_Y());
		float s5 = -ShapeMath.getPointLineXDistance(other.getPoint2_X(), other.getPoint2_Y(), getPoint2_X(), getPoint2_Y(), getPoint3_X(), getPoint3_Y());
		float s6 = -ShapeMath.getPointLineXDistance(other.getPoint2_X(), other.getPoint2_Y(), getPoint3_X(), getPoint3_Y(), getPoint1_X(), getPoint1_Y());

		float s7 = -ShapeMath.getPointLineXDistance(other.getPoint3_X(), other.getPoint3_Y(), getPoint1_X(), getPoint1_Y(), getPoint2_X(), getPoint2_Y());
		float s8 = -ShapeMath.getPointLineXDistance(other.getPoint3_X(), other.getPoint3_Y(), getPoint2_X(), getPoint2_Y(), getPoint3_X(), getPoint3_Y());
		float s9 = -ShapeMath.getPointLineXDistance(other.getPoint3_X(), other.getPoint3_Y(), getPoint3_X(), getPoint3_Y(), getPoint1_X(), getPoint1_Y());
		
		if (getCenter().x < other.getCenterX()) { // [+]
			float dmin = FloatMath.fnaturalmin(d1, d2, d3, d4, d5, d6, d7, d8, d9);
			float smin = FloatMath.fnaturalmin(s1, s2, s3, s4, s5, s6, s7, s8, s9);
			
			return (other.getCenter().x - getCenterX()) - FloatMath.fnaturalmin(dmin, smin) + CollisionGeometry.FDELTA;
		} else { // [-]
			float dmax = FloatMath.fnaturalmax(d1, d2, d3, d4, d5, d6, d7, d8, d9);
			float smax = FloatMath.fnaturalmax(s1, s2, s3, s4, s5, s6, s7, s8, s9);
			
			return (other.getCenter().x - getCenterX()) - FloatMath.fnaturalmax(dmax, smax) - CollisionGeometry.FDELTA;
		}
	}

	@Override
	public float getYTouchDistance(CollisionTriangle other) {
		float d1 = ShapeMath.getPointLineYDistance(getPoint1_X(), getPoint1_Y(), other.getPoint1_X(), other.getPoint1_Y(), other.getPoint2_X(), other.getPoint2_Y());
		float d2 = ShapeMath.getPointLineYDistance(getPoint1_X(), getPoint1_Y(), other.getPoint2_X(), other.getPoint2_Y(), other.getPoint3_X(), other.getPoint3_Y());
		float d3 = ShapeMath.getPointLineYDistance(getPoint1_X(), getPoint1_Y(), other.getPoint3_X(), other.getPoint3_Y(), other.getPoint1_X(), other.getPoint1_Y());

		float d4 = ShapeMath.getPointLineYDistance(getPoint2_X(), getPoint2_Y(), other.getPoint1_X(), other.getPoint1_Y(), other.getPoint2_X(), other.getPoint2_Y());
		float d5 = ShapeMath.getPointLineYDistance(getPoint2_X(), getPoint2_Y(), other.getPoint2_X(), other.getPoint2_Y(), other.getPoint3_X(), other.getPoint3_Y());
		float d6 = ShapeMath.getPointLineYDistance(getPoint2_X(), getPoint2_Y(), other.getPoint3_X(), other.getPoint3_Y(), other.getPoint1_X(), other.getPoint1_Y());

		float d7 = ShapeMath.getPointLineYDistance(getPoint3_X(), getPoint3_Y(), other.getPoint1_X(), other.getPoint1_Y(), other.getPoint2_X(), other.getPoint2_Y());
		float d8 = ShapeMath.getPointLineYDistance(getPoint3_X(), getPoint3_Y(), other.getPoint2_X(), other.getPoint2_Y(), other.getPoint3_X(), other.getPoint3_Y());
		float d9 = ShapeMath.getPointLineYDistance(getPoint3_X(), getPoint3_Y(), other.getPoint3_X(), other.getPoint3_Y(), other.getPoint1_X(), other.getPoint1_Y());
		
		float s1 = -ShapeMath.getPointLineYDistance(other.getPoint1_X(), other.getPoint1_Y(), getPoint1_X(), getPoint1_Y(), getPoint2_X(), getPoint2_Y());
		float s2 = -ShapeMath.getPointLineYDistance(other.getPoint1_X(), other.getPoint1_Y(), getPoint2_X(), getPoint2_Y(), getPoint3_X(), getPoint3_Y());
		float s3 = -ShapeMath.getPointLineYDistance(other.getPoint1_X(), other.getPoint1_Y(), getPoint3_X(), getPoint3_Y(), getPoint1_X(), getPoint1_Y());

		float s4 = -ShapeMath.getPointLineYDistance(other.getPoint2_X(), other.getPoint2_Y(), getPoint1_X(), getPoint1_Y(), getPoint2_X(), getPoint2_Y());
		float s5 = -ShapeMath.getPointLineYDistance(other.getPoint2_X(), other.getPoint2_Y(), getPoint2_X(), getPoint2_Y(), getPoint3_X(), getPoint3_Y());
		float s6 = -ShapeMath.getPointLineYDistance(other.getPoint2_X(), other.getPoint2_Y(), getPoint3_X(), getPoint3_Y(), getPoint1_X(), getPoint1_Y());

		float s7 = -ShapeMath.getPointLineYDistance(other.getPoint3_X(), other.getPoint3_Y(), getPoint1_X(), getPoint1_Y(), getPoint2_X(), getPoint2_Y());
		float s8 = -ShapeMath.getPointLineYDistance(other.getPoint3_X(), other.getPoint3_Y(), getPoint2_X(), getPoint2_Y(), getPoint3_X(), getPoint3_Y());
		float s9 = -ShapeMath.getPointLineYDistance(other.getPoint3_X(), other.getPoint3_Y(), getPoint3_X(), getPoint3_Y(), getPoint1_X(), getPoint1_Y());
		
		if (getCenter().y < other.getCenterY()) { // [+]
			float dmin = FloatMath.fnaturalmin(d1, d2, d3, d4, d5, d6, d7, d8, d9);
			float smin = FloatMath.fnaturalmin(s1, s2, s3, s4, s5, s6, s7, s8, s9);
			
			return (other.getCenter().y - getCenterY()) - FloatMath.fnaturalmin(dmin, smin) + CollisionGeometry.FDELTA;
		} else { // [-]
			float dmax = FloatMath.fnaturalmax(d1, d2, d3, d4, d5, d6, d7, d8, d9);
			float smax = FloatMath.fnaturalmax(s1, s2, s3, s4, s5, s6, s7, s8, s9);
			
			return (other.getCenter().y - getCenterY()) - FloatMath.fnaturalmax(dmax, smax) - CollisionGeometry.FDELTA;
		}
	}

	@Override
	public float getYTouchDistance(CollisionBox other) {
		return ShapeMath.getYTouchDistance(this, other);
	}

	@Override
	public float getYTouchDistance(CollisionCircle other) {
		return ShapeMath.getYTouchDistance(this, other);
	}

	@Override
	public boolean isIntersectingWith(CollisionBox other) {
		return ShapeMath.doGeometriesIntersect(this, other);
	}

	@Override
	public boolean isIntersectingWith(CollisionCircle other) {
		return ShapeMath.doGeometriesIntersect(this, other);
	}

	@Override
	public boolean isIntersectingWith(CollisionTriangle other) {
		return
				ShapeMath.doLinesIntersect(getPoint1_X(), getPoint1_Y(), getPoint2_X(), getPoint2_Y(), other.getPoint1_X(), other.getPoint1_Y(), other.getPoint2_X(), other.getPoint2_Y()) ||
				ShapeMath.doLinesIntersect(getPoint1_X(), getPoint1_Y(), getPoint2_X(), getPoint2_Y(), other.getPoint2_X(), other.getPoint2_Y(), other.getPoint3_X(), other.getPoint3_Y()) ||
				ShapeMath.doLinesIntersect(getPoint1_X(), getPoint1_Y(), getPoint2_X(), getPoint2_Y(), other.getPoint3_X(), other.getPoint3_Y(), other.getPoint1_X(), other.getPoint1_Y()) ||

				ShapeMath.doLinesIntersect(getPoint2_X(), getPoint2_Y(), getPoint3_X(), getPoint3_Y(), other.getPoint1_X(), other.getPoint1_Y(), other.getPoint2_X(), other.getPoint2_Y()) ||
				ShapeMath.doLinesIntersect(getPoint2_X(), getPoint2_Y(), getPoint3_X(), getPoint3_Y(), other.getPoint2_X(), other.getPoint2_Y(), other.getPoint3_X(), other.getPoint3_Y()) ||
				ShapeMath.doLinesIntersect(getPoint2_X(), getPoint2_Y(), getPoint3_X(), getPoint3_Y(), other.getPoint3_X(), other.getPoint3_Y(), other.getPoint1_X(), other.getPoint1_Y()) ||

				ShapeMath.doLinesIntersect(getPoint3_X(), getPoint3_Y(), getPoint1_X(), getPoint1_Y(), other.getPoint1_X(), other.getPoint1_Y(), other.getPoint2_X(), other.getPoint2_Y()) ||
				ShapeMath.doLinesIntersect(getPoint3_X(), getPoint3_Y(), getPoint1_X(), getPoint1_Y(), other.getPoint2_X(), other.getPoint2_Y(), other.getPoint3_X(), other.getPoint3_Y()) ||
				ShapeMath.doLinesIntersect(getPoint3_X(), getPoint3_Y(), getPoint1_X(), getPoint1_Y(), other.getPoint3_X(), other.getPoint3_Y(), other.getPoint1_X(), other.getPoint1_Y()) ||
				
				containsPoint(other.getPoint1_X(), other.getPoint1_Y()) ||

				other.containsPoint(getPoint1_X(), getPoint1_Y());
	}

	@Override
	public boolean containsPoint(float x, float y) {
	    boolean b1, b2, b3;

	    b1 = (x - getPoint2_X()) * (getPoint1_Y() - getPoint2_Y()) - (getPoint1_X() - getPoint2_X()) * (y - getPoint2_Y()) < 0;
	    b2 = (x - getPoint3_X()) * (getPoint2_Y() - getPoint3_Y()) - (getPoint2_X() - getPoint3_X()) * (y - getPoint3_Y()) < 0;
	    b3 = (x - getPoint1_X()) * (getPoint3_Y() - getPoint1_Y()) - (getPoint3_X() - getPoint1_X()) * (y - getPoint1_Y()) < 0;

	    return ((b1 == b2) && (b2 == b3));
	}

	@Override
	public CollisionGeometry copy() {
		CollisionGeometry g = new CollisionTriangle(owner, point1_x, point1_y, point2_x, point2_y, point3_x, point3_y);
		g.setCenter(getCenterX(), getCenterY());
		return g;
	}
}

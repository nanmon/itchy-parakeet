package com.hoohaainc.framework.utility

public class Plane implements R3Object {
	Point3 position;
	Vector3 normal;

	public Plane(Point3 position, Vector3 normal){
		this.position = position;
		this.normal = normal;
	}

	public Plane(Point3 a, Point3 b, Point3 c){
		position = a;
		Vector3 n1 = a.substract(b);
		Vector3 n2 = c.substract(b);
		normal = n1.cross(n2);
	}

	@Override
	public void draw(Graphics graphics){
		if(Camera.inBounds(this)){

		}
	}

	@Override
	public boolean contains(R3Object r3obj){
		if(r3obj instanceof Point3){
			Point3 point = (Point3)r3obj;
			return normal.x*(point.x - position.x) +
				   normal.y*(point.y - position.y) +
				   normal.z*(point.z - position.z) == 0;
		}

		if(r3obj instanceof Line3){
			Line3 line = (Line3)r3obj;
			if(contains(line.anchor) && contains(line.anchor.add(line.direction)))
				return true;
			return false;
		}

		if(r3obj instanceof Plane){
			Plane plane = (Plane)r3obj;
			return contains(plane.position) 
				&& normal.isParalellTo(plane.normal);
		}
	}

	@Override
	public boolean intersects(R3Object r3obj){
		if(r3obj instanceof Point3) return contains(r3obj);
		if(r3obj instanceof Line3){
			Line3 line = (Line3)r3obj;
			return normal.isPerpendicularTo(line.direction);
		}
		if(r3obj instanceof Plane){
			Plane plane = (Plane)r3obj;
			if(normal.isParalellTo(plane.normal))
				return contains(plane.position);
			return true;
		}
	}

	@Override 
	public R3Object intersection(R3Object r3obj){
		//C:
	} 
}
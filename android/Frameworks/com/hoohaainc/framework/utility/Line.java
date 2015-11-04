package com.hoohaainc.framework.utility

public class Line3 implements R3Object {
	Point3 anchor;
	Vector3 direction;
	boolean infinite = false;

	public Line3(Point3 anchor, Vector3 direction){
		this.anchor = anchor;
		this.direction = direction;
	}

	public Line3(Point3 anchor, Vector3 direction, boolean infinite){
		this.anchor = anchor;
		this.direction = direction;
		this.infinite = infinite;
	}

	@Override
	public void draw(Graphics graphics){
		if(Camera.inBounds(this)){

		}
	}

	@Override
	public boolean contains(R3Object r3obj){
		//Point
		if(r3obj instanceof Point3){
			Point3 point = (Point3)r3obj;
			//is on the infinite line
			if(point.substract(anchor).isParalellTo(direction)) {
				if(infinite) return true
				float alpha;
				if(direction.x != 0)
					alpha = (point.x - anchor.x)/direction.x;
				else if(direction.y != 0)
					alpha = (point.y - anchor.y)/direction.y;
				else if(direction.z != 0)
					alpha = (point.z - anchor.z)/direction.z;
				else return anchor.equals(point);
				//is betwen anchor && anchor + direction
				return alpha >= 0 && alpha <= 1;
			}
		}
		//Line
		else if(r3obj instanceof Line3){
			Line3 line = (Line3)r3obj;
			//contains anchor && anchor + direction
			if(contains(line.anchor) && contains(line.anchor.add(line.direction)))
				return true;
		}
		return false;
	}

	@Override
	public boolean intersects(R3Object r3obj){
		if(r3obj instanceof Point3) return contains(r3obj);
		if(r3obj instanceof Line3){
			Line3 line = (Line3)r3obj;
			//are the same line
			if(direction.isParalellTo(line.direction))
				return contains(line.anchor);
			//doesn't intersect
			if(direction.x == 0 || direction.x*line.direction.y + line.direction.x == 0)return false;
			//point of intersection in b
			float beta = (direction.x*(line.anchor.y - anchor.y) - (line.anchor.x - anchor.x))
						/(direction.x*line.direction.y + line.direction.x);
			//point of intersection in a
			float alpha = line.direction.x*beta/direction.x + (line.anchor.x - anchor.x)/direction.x;
			return (infinite || (alpha >=0 && alpha<=1)) &&
				(line.infinite || (beta >=0 && beta <=1))
		}
		if(r3obj instanceof Plane){
			return r3obj.intersects(this);
		}
	}

	@Override
	public R3Object intersection(R3Object r3obj){
		if(r3obj instanceof Point3){
			Point3 point = (Point3)r3obj;
			return new Point3(point.x, point.y, point.z);
		}
		if(r3obj instanceof Line3){
			Line3 line = (Line3)r3obj;
			//are the same line
			if(direction.isParalellTo(line.direction))
				if(contains(line.anchor)){
					if(infinite && line.infinite) 
						return new Line3(anchor, direction, true);
					else if(infinite)
						return new Line3(line.anchor, line.direction);
					else if(line.infinite)
						return new Line3(anchor, direction);
					else{
						float alpha1, alpha2;
						if(direction.x != 0){
							alpha1 = (line.anchor.x - anchor.x)/direction.x;
							alpha2 = (line.direction.x - line.anchor.x - anchor.x)/direction.x;
						}
						else if(direction.y != 0){
							alpha1 = (line.anchor.y - anchor.y)/direction.y;
							alpha2 = (line.direction.y - line.anchor.y - anchor.y)/direction.y;
						}
						else if(direction.z != 0){
							alpha1 = (line.anchor.z - anchor.z)/direction.z;
							alpha2 = (line.direction.z - line.anchor.z - anchor.z)/direction.z;
						}
						else return anchor.equals(point) ? new Point3(anchor.x, anchor.y, anchor.z) : null;
						return new Line3(alpha1>0 && alpha1<1 ? line.anchor : anchor,
							alpha2>0 && alpha2<1 ? line.direction : direction);
					}
				}else return null; //same direction, but don't intersect
			//don't intersect
			if(direction.x == 0 || direction.x*line.direction.y + line.direction.x == 0)
				return null;
			//point of intersection in b
			float beta = (direction.x*(line.anchor.y - anchor.y) - (line.anchor.x - anchor.x))
						/(direction.x*line.direction.y + line.direction.x);
			//point of intersection in a
			float alpha = line.direction.x*beta/direction.x + (line.anchor.x - anchor.x)/direction.x;
			if((infinite || (alpha >=0 && alpha<=1)) &&
				(line.infinite || (beta >=0 && beta <=1))) {
				return anchor.add(direction.scale(alpha));
			}
		}
		if(r3obj instanceof Plane){
			return ((Plane)r3obj).intersection(this);
		}
	}
}
package com.hoohaainc.framework.utility

public class Point3 implements R3Object{

	float x=0, y=0, z=0;

	public Point3(){}

	public Point3(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point3 add(Vector3 vector){
		return new Point3(x+vector.x, y+vector.y, z+vector.z);
	}

	public Point3 substract(Vector3 vector){
		return new Point3(x-vector.x, y-vector.y, z-vector.z);
	}

	public Vector3 substract(Point3 point){
		return new Vector3(x-point.x, y-point.y, z-point.z);
	}

	@Override
	public boolean contains(R3Object r3obj){
		if(r3obj instanceof Point3) return this.equals(r3obj);
		return false;
	}

	@Override 
	public boolean intersects(R3Object r3obj){
		if(r3obj instanceof Point3) return contains(r3obj);
		return r3obj.intersects(this);
	}

	@Override
	public R3Object intersection(R3Object r3obj){
		if(r3obj.contains(this)) 
			return new Point3(x, y, z);
		return null;
	}

	@Override
	public void draw(Graphics graphics){
		if(Camera.inBounds(this)){
			//do draw
		}
	}
}
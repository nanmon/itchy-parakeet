package com.hoohaainc.framework.utility;

public class Rectangle3 extends Plane {
	Point3 upLeft, downRight;
	Vector3 _i,_j;

	public Rectangle3(Point3 upLeft, Point3 downRight, Point3 upRight){
		super(upLeft, downRight, upRight);
		this.upLeft = upLeft;
		this.downRight = downRight;
		_i = upRight.substract(upLeft).normalize();
		_j = downRight.substract(upRight).normalize();
	}

	public R2Object projection(R3Object r3obj){
		if(r3obj instanceof Point3){
			Point3 point = (Point3)r3obj;
			return new Point2(
				_i.x*point.x + _i.y*point.y + _i.z*point.z,
				_j.x*point.x + _j.y*point.y + _j.z*point.z);
		}
	}
}
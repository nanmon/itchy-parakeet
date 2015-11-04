package com.hoohaainc.framework.utility

public static class Camera{
	static Point3 eyeBall;
	static Rectangle3 viewPort;

	public static boolean inBounds(Point3 point){
		if(point.x > minX && point.x < maxX &&
			point.y > minY && point.y < maxY &&
			point.z > minZ && point.z < maxZ) return true;
		return false;
	}

	public static R2Object projection(R3Object r3obj){
		if(r3obj instanceof Point3){
			Point3 point = (Point3)r3obj;
			Line3 line = new Line(eyeBall, point.substract(eyeBall));
			Point3 intersection = (Point3)viewPort.intersection(line);
			
		}
	}
}
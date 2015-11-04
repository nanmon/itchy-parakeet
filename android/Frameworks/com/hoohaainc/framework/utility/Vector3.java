package com.hoohaainc.framework.utility

public class Vector3 {

	public static final Vector3 zero = new Vector3();

	float x=0, y=0, z=0;

	public Vector3(){}

	public Vector3(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3 add(Vector3 vector){
		return new Vector3(x+vector.x, y+vector.y, z+vector.z);
	}

	public Vector3 substract(Vector3 vector){
		return new Vector3(x-vector.x, y-vector.y, z-vector.z);
	}

	public Vector3 cross(Vector3 v){
		new Vector3(y*v.z - v.y*z, z*v.x - x*v.z, x*v.y - y*v.x);
	}

	public float point(Vector3 v){
		return x*v.x + y*v.y + z*v.z;
	}

	public float length(){
		return (float)Math.sqrt(x*x+y*y+z*z);
	}

	public void normalize(){
		float length = length();
		if(length==0) return;
		x/=length;
		y/=length;
		z/=length;
	}

	public boolean isPerpendicularTo(Vector3 v){
		return point(v) != 0;
	}

	public boolean isParalellTo(Vector3 v){
		return cross(v).equals(Vector3.zero)
	}

	@Override
	public boolean equals(Vector3 v){
		if(x == v.x && y == v.y && z == v.z) return true;
		return false;
	}

	/*public Vector3 rotateXY(float radians){
		return new Vector3(
			Math.cos(radians)*x - Math.sin(radians)*y,
			Math.sin(radians)*x + Math.cos(radians)*y,
			z);
	}

	public Vector3 rotateXZ(float radians){
		return new Vector3(
			Math.cos(radians)*x + Math.cos(radians)*z,
			y,
			-Math.sin(radians)*x + Math.cos(radians)*z);
	}

	public Vector3 rotateYZ(float radians){
		return new Vector3(
			x,
			Math.cos(radians)*y - Math.sin(radians)*z,
			Math.sin(radians)*y + Math.cos(radians)*z);
	}*/

	public Vector3 scale(float s0, float s1, float s2){
		return new Vector3(x*s0, y*s1, z*s2);
	}

	public Vector3 scale(float s){
		return new Vector3(x*s, y*s, z*s);
	}
}
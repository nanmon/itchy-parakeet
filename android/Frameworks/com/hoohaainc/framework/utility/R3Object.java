package com.hoohaainc.framework.utility

public interface R3Object {
	public void draw(Graphics graphics);
	public boolean intersects(R3Object r3obj);
	public boolean contains(R3Object r3obj);
	public R3Object intersection(R3Object);
}
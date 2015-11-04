package com.hoohaainc.framework.utility

public interface R2Object {
	public void draw(Graphics graphics);
	public boolean contains(R2Object r2obj);
	public boolean intersects(R2Object r2obj);
	public R2Object intersection(R2Object r2obj);
}
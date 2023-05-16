package mcc.utils;

public class Vector3d {
	
	private double x,y,z;

	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3d normalize() {
		double max = Math.max(this.x, Math.max(this.y, this.z));
		this.x /= max;
		this.y /= max;
		this.z /= max;
		
		return this;
	}

	public Vector3d clone() {
		return new Vector3d(x, y, z);
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
}

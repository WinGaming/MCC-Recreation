package mcc.utils;

public class Vector3i {
	
	private int x,y,z;

	public Vector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3i add(int x, int y, int z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}

	public Vector3i setX(int x) {
		this.x = x;
		return this;
	}

	public Vector3i setY(int y) {
		this.y = y;
		return this;
	}

	public Vector3i setZ(int z) {
		this.z = z;
		return this;
	}

	public Vector3i clone() {
		return new Vector3i(x, y, z);
	}
}

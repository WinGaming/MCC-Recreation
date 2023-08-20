package mcc.utils;

public class Vector2f {
    
	private float x,y;

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2f normalize() {
		float max = Math.max(this.x, this.y);
		this.x /= max;
		this.y /= max;
		
		return this;
	}

	public Vector2f clone() {
		return new Vector2f(x, y);
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}

package com.mygdx.engine;

import com.badlogic.gdx.math.Vector2;

public class Collision {
	
	private Vector2 position;
	private Vector2 normal;
	
	public Collision(){
		
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public Vector2 getNormal() {
		return normal;
	}

	public void setNormal(Vector2 normal) {
		this.normal = normal;
	}
	
	
}

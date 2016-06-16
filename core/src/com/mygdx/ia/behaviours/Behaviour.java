package com.mygdx.ia.behaviours;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.ia.Steering;

/**
 * 
 * Clase abstracta que representa un comportamiento.
 * Hay muchos tipos de comportamiento: Seel, Arrive, Flee, Wander...
 *
 */
public abstract class Behaviour {
	
	protected float weight;
	
	public Behaviour() {
		weight = 1; // 1 Por defecto
	}

	public abstract Steering getSteering();
	
	public static Vector2 getNewOrientation(Vector2 orientation){
		return null;
	}
	
	public float getWeight() {
		return weight;
	}
	
	public void setWeight(float weight) {
		this.weight = weight;
	}
}

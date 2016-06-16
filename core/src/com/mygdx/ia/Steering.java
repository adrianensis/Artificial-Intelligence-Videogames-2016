package com.mygdx.ia;

import com.badlogic.gdx.math.Vector2;

/**
 * 
 * El Steering es una estructura simple, que indica la dirección que debe seguir un Bot.
 * Un steering viene a ser una orden que se le da al Bot para que siga una dirección.
 * 
 * La "U" es de Uniforme
 *
 */
public class Steering {
	
	/*
	 * PARAMETROS MOV.UNIF. 
	 */
	public Vector2 velocity;
	public float rotation;
	
	/*
	 * PARAMETROS MOV.UNIF.ACEL.
	 */
	public Vector2 linear; // linear acceleration
	public float angular; // angular acceleration
	
	public Steering() {
		velocity = new Vector2(0,0);
		rotation = 0;
		
		linear = new Vector2(0,0);
		angular = 0;
	}
	
	public void mul(float value){
		velocity.scl(value);
		rotation *= value;
		
		linear.scl(value);
		angular *= value;
	}
}

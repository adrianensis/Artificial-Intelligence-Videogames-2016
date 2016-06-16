package com.mygdx.engine;

import com.badlogic.gdx.math.Vector2;

/**
 * Este componente controla la posicion del GameObject.
 * 
 * Tambien puede contener la rotación y la escala.
 *
 */

public class Transform extends Component {

	public Vector2 position;
	public float orientation;
	
	private float minX,maxX,minY,maxY;
	private boolean hasLimits;
	
	public Transform() {
		position = new Vector2(0,0);
		orientation = 0;
		minX = minY = maxX = maxY = 0;
		hasLimits = false;
	}
	
	/**
	 * Establece limites en las coordenadas.
	 * @param minX
	 * @param maxX
	 * @param minY
	 * @param maxY
	 */
	public void setLimits(float minX,float maxX,float minY,float maxY) {
		hasLimits = true;
		this.maxX = maxX;
		this.maxY = maxY;
		this.minX = minX;
		this.minY = minY;
	}
	
	/**
	 * Desactiva los limites
	 */
	public void disableLimits(){
		hasLimits = false;
	}
	
	/**
	 * Comprueba si se han transpasado los limites
	 */
	public void checkLimits(){
		if(hasLimits){
			
			if(position.x > maxX)
				position.x = maxX;
			else if(position.x < minX)
				position.x = minX;
			
			if(position.y > maxY)
				position.y = maxY;
			else if(position.y < minY)
				position.y = minY;
		}
	}

	@Override
	public void update() {
		
		this.checkLimits();
		
	}

}

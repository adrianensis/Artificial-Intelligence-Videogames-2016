package com.mygdx.ia;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Collider;
import com.mygdx.engine.GameObject;
import com.mygdx.engine.Renderer;
import com.mygdx.engine.Transform;

/**
 * El Bot es simplemente un GameObject.
 *
 */
public class Bot extends GameObject {

	public Bot(String key, String textureName, float speed, float accel, float ang_accel, int mass, Vector2 pos, float rot) {
		super(key);
				
		// Creamos su Transform
		Transform t = new Transform();
		t.position = pos;
		this.addComponent(t);
		
		// Creamos su Renderer
		if(textureName != null && !textureName.isEmpty())
			this.addComponent(new Renderer(textureName, 1, 1));
		
		// Creamos su Collider
		this.addComponent(new Collider(1,1,true));
		
		// Le damos una logica
		/**
		 * El Bot tiene una logica de bot. Se le pueden dar comportamientos.
		 */
		this.addComponent(new BotScript(speed, accel, ang_accel, mass, pos, rot));
		
	}
	
	
}

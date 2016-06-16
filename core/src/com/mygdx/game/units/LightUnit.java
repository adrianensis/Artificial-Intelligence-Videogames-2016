package com.mygdx.game.units;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Scene;
import com.mygdx.engine.Script;

public class LightUnit extends Unit {

	public LightUnit(String key, int team, String textureName, Vector2 pos) {
		super(key, team,textureName,2.5f*Scene.SCALE, 70*Scene.SCALE, 1f*Scene.SCALE, 1, pos, 20, 100, 1.5f*Scene.SCALE,20*Scene.SCALE, 0.5f);
//		super(key, speed, accel, ang_accel, mass, pos, rot);
				
		this.addComponent(new Script(){
			
			// LightUnit tiene su maquina de estados personalizada
			private BasicStateMachine sm;
			
			@Override
			public void start() {
				sm = new BasicStateMachine((LightUnit)gameObject);
			}
			
			@Override
			public void update() {				
				sm.update();
			}
		});
		
	}

}

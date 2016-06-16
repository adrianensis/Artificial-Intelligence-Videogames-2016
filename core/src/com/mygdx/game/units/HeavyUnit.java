package com.mygdx.game.units;

import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Scene;
import com.mygdx.engine.Script;

public class HeavyUnit extends Unit {

	public HeavyUnit(String key, int team, String textureName, Vector2 pos, Map<Integer, Integer> terrainMap) {
		super(key, team,textureName, 1f*Scene.SCALE, 40*Scene.SCALE, 5f*Scene.SCALE, 1, pos, 10, 
				100, 1*Scene.SCALE, 6*Scene.SCALE, 0.5f,terrainMap);
//		super(key, speed, accel, ang_accel, mass, pos, rot);
		
		this.addComponent(new Script(){
			
			// LightUnit tiene su maquina de estados personalizada
			private BasicStateMachine sm;
			
			@Override
			public void start() {
				sm = new BasicStateMachine((HeavyUnit)gameObject);
			}
			
			@Override
			public void update() {
				sm.update();
			}
		});
	}

}

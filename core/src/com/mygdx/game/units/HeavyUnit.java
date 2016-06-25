package com.mygdx.game.units;

import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Scene;
import com.mygdx.engine.Script;
import com.mygdx.game.Waypoints;

public class HeavyUnit extends Unit {

	public HeavyUnit(String key, int team, String textureName, Vector2 pos, Map<Integer, Integer> terrainMap) {
		super(key, team,textureName, 1f*Scene.SCALE, 150*Scene.SCALE, 5f*Scene.SCALE, 1, pos, 10, 
				100, 1.5f*Scene.SCALE,5*Scene.SCALE, 0.5f,terrainMap,15,20);
//		super(key, speed, accel, ang_accel, mass, pos, rot);
		
		this.addComponent(new Script(){
			
			// LightUnit tiene su maquina de estados personalizada
			private TacticStateMachine sm;
			
			@Override
			public void start() {
				sm = new TacticStateMachine((HeavyUnit)gameObject);
			}
			
			@Override
			public void update() {
				sm.update();
			}
		});
	}

	@Override
	public void onOffensive() {
		
		Vector2 pos = Waypoints.getInstance().getEnemyBaseDefensePoint(this);
				
		setDestiny(pos);
		
	}

	@Override
	public void onDefensive() {
		
		Vector2 pos = Waypoints.getInstance().getDefensePoint(this);
		
//		System.out.println(defPos);
		
		setDestiny(pos);
		
	}
	
	@Override
	public void onRunaway() {
		Vector2 pos = Waypoints.getInstance().getHealingPoint(this);
		
		setDestiny(pos);
		
	}

}

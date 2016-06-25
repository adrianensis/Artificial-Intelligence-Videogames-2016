package com.mygdx.game.units;

import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Scene;
import com.mygdx.engine.Script;
import com.mygdx.game.Waypoints;
import com.mygdx.ia.statemachine.State;

public class LightUnit extends Unit {

	public LightUnit(String key, int team, String textureName, Vector2 pos, Map<Integer, Integer> terrainMap) {
		super(key, team,textureName,3f*Scene.SCALE, 150*Scene.SCALE, 1f*Scene.SCALE, 1, pos, 20, 
				100, 1.5f*Scene.SCALE,5*Scene.SCALE, 0.5f,terrainMap, 10,10);
//		super(key, speed, accel, ang_accel, mass, pos, rot);
				
		this.addComponent(new Script(){
			
			// LightUnit tiene su maquina de estados personalizada
			private TacticStateMachine sm;
			
			@Override
			public void start() {
				sm = new TacticStateMachine((LightUnit)gameObject);
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
		
		Vector2 pos = null;
		
		/*
		 * Si la base tiene una vida inferior al 50% vamos a defender la base.
		 */
		if(this.getBase().getLife() < (this.getBase().getMaxLife() * 0.5) ){
			pos = Waypoints.getInstance().getBaseDefensePoint(this);
			
		// si no, vamos a los puntos de defensa del puente.
		}else{
			pos = Waypoints.getInstance().getDefensePoint(this);
		}
				
		setDestiny(pos);
		
	}

	@Override
	public void onRunaway() {
		Vector2 pos = Waypoints.getInstance().getHidingPoint(this);
		
		setDestiny(pos);
		
	}

}

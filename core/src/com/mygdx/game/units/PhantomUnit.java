package com.mygdx.game.units;

import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Scene;
import com.mygdx.engine.Script;
import com.mygdx.game.Waypoints;

public class PhantomUnit extends Unit {
	//DA problemas para andar, no sale de la zona gris
	public PhantomUnit(String key, int team, String textureName,Vector2 pos,Map<Integer, Integer> terrainMap) {
		super(key, team,textureName, 2f*Scene.SCALE, 100*Scene.SCALE, 5f*Scene.SCALE, 1, pos, 10, 
				30, 1.5f*Scene.SCALE,5*Scene.SCALE, 0.5f,terrainMap,30,5);

		this.addComponent(new Script() {

			private TacticStateMachine sm;

			@Override
			public void start() {
				sm = new TacticStateMachine((PhantomUnit) gameObject);
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
				
		setDestiny(pos);
		
	}
	
	@Override
	public void onRunaway() {
		Vector2 pos = Waypoints.getInstance().getHealingPoint(this);
		
		setDestiny(pos);
		
	}

}

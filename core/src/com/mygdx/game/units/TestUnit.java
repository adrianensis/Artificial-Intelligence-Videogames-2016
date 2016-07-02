package com.mygdx.game.units;

import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Scene;
import com.mygdx.ia.BotScript;

import sun.font.Script;

public class TestUnit extends Unit {

	public TestUnit(String key, int team, String textureName,Vector2 pos,Map<Integer, Integer> terrainMap) {
		super(key, team,textureName, 2f*Scene.SCALE, 100*Scene.SCALE, 5f*Scene.SCALE, 1, pos, 10, 
				30, 1.5f*Scene.SCALE,5*Scene.SCALE, 0.5f,terrainMap,30,5);
		this.addComponent(new BotScript(2f*Scene.SCALE, 100*Scene.SCALE, 5f*Scene.SCALE, 1, pos, 10));
	}

	@Override
	public void onOffensive() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDefensive() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRunaway() {
		// TODO Auto-generated method stub

	}

}

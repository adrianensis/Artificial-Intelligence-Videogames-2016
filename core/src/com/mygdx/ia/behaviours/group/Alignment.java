package com.mygdx.ia.behaviours.group;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.Steering;
import com.mygdx.ia.behaviours.Behaviour;

public class Alignment extends GroupBehaviour {
	
	private BotScript bot;
	private List<BotScript> targets;
	private float threshold;

	public Alignment(BotScript bot, List<BotScript> targets, float threshold) {
		super(targets);
		this.bot = bot;
		this.threshold = threshold;
	}

	@Override
	public Steering getSteering() {
		Steering steering = new Steering();
		
		for (BotScript target : targets) {
			
			Vector2 direction = bot.getPosition().cpy().sub(target.getPosition());
			float distance = direction.len();
			
			if(distance < threshold){
				
			}
		}
		
		// BORRAR
		return null;
	}

}

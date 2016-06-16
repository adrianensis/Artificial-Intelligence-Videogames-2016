package com.mygdx.ia.behaviours.group;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Scene;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.Steering;
import com.mygdx.ia.behaviours.Behaviour;

public class Separation extends GroupBehaviour {
	
	private BotScript bot;
	private float threshold;
	private float decayCoefficient;
	
	public Separation(BotScript bot, List<BotScript> targets, float threshold, float decayCoefficient) {
		super(targets);
		this.bot = bot;
		this.threshold = threshold;
		this.decayCoefficient = decayCoefficient;
	}

	@Override
	public Steering getSteering() {
		Steering steering = new Steering();
		
		for (BotScript target : targets) {
			
			Vector2 direction = bot.getPosition().cpy().sub(target.getPosition());
			float distance = direction.len();
									
			if(distance < threshold){
				
				// Strength of repulsion
				float strength = Math.min(decayCoefficient/(distance*distance), bot.getMax_acceleration());
				
//				System.out.println(strength);
								
				steering.linear.add(direction.nor().scl(Scene.SCALE).scl(strength));
			}
		}
				
		return steering;
	}
}

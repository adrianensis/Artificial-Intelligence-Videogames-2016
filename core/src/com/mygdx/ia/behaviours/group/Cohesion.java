package com.mygdx.ia.behaviours.group;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.ia.Bot;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.Steering;
import com.mygdx.ia.behaviours.basic.SeekUA;

public class Cohesion extends GroupBehaviour {
	
	private BotScript bot;
	private float threshold;
	
	public Cohesion(BotScript bot, List<BotScript> targets, float threshold) {
		super(targets);
		this.bot = bot;
		this.threshold = threshold;
	}

	@Override
	public Steering getSteering() {
		
		int count = 0;
		Vector2 centerOfMass = new Vector2();
		
		for (BotScript target : targets) {
			
			Vector2 direction = bot.getPosition().cpy().sub(target.getPosition());
			float distance = direction.len();
						
			if(distance < threshold){
				centerOfMass.add(target.getPosition());
				count++;
			}
		}
		
		if(count == 0)
			return new Steering();
		
		centerOfMass.scl(1.0f/count);
		
		Bot destiny = new Bot(null,null, 1, 1, 1, 1, centerOfMass, 50);
		return new SeekUA(bot, destiny.getComponent(BotScript.class)).getSteering();
	}

}

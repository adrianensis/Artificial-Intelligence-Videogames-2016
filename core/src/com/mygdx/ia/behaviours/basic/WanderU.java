package com.mygdx.ia.behaviours.basic;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Scene;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.Steering;
import com.mygdx.ia.behaviours.Behaviour;

/**
 * 
 * Este comportamiento permite que el Bot persiga a un objetivo (targer).
 *
 */
public class WanderU extends Behaviour{
	
	private BotScript bot;
	
	public WanderU(BotScript bot) {
		super();
		this.bot = bot;
	}

	@Override
	public Steering getSteering() {
		
		Steering steering = new Steering();
		
		float vx = MathUtils.cos(MathUtils.degRad*bot.getOrientation());
		float vy = MathUtils.sin(MathUtils.degRad*bot.getOrientation());
		Vector2 orientationVector = new Vector2(vx,vy);
				
		steering.velocity = orientationVector.nor().scl(bot.getMax_speed());
		
		float randomBinomial = (float)( Math.random() - Math.random())*Scene.SCALE;
		steering.rotation = randomBinomial * bot.getMax_rotation();
		
		return steering;
	}

}

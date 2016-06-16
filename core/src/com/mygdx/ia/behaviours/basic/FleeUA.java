package com.mygdx.ia.behaviours.basic;

import com.mygdx.engine.Transform;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.Steering;
import com.mygdx.ia.behaviours.Behaviour;

/**
 * 
 * Este comportamiento permite que el Bot persiga a un objetivo (targer).
 *
 */
public class FleeUA extends Behaviour{
	
	protected BotScript bot;
	protected Transform target;
	
	public FleeUA(BotScript bot, BotScript t) {
		super();
		this.bot = bot;
		this.target = t.getGameObject().getComponent(Transform.class);;
	}

	@Override
	public Steering getSteering() {
		
		Steering steering = new Steering();
		
		steering.linear = bot.getPosition().cpy().sub(target.position); // cpy() hace una copia para no modificar el original
		
		steering.linear.nor();
		steering.linear.scl(bot.getMax_acceleration());
		
		//bot.setOrientation(MathUtils.atan2(steering.velocity.y, -steering.velocity.x));
		
		steering.angular = 0;
		return steering;
	}

}

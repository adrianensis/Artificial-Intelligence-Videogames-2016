package com.mygdx.ia.behaviours.basic;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.engine.Transform;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.Steering;
import com.mygdx.ia.behaviours.Behaviour;

/**
 * 
 * Este comportamiento permite que el Bot persiga a un objetivo (targer).
 *
 */
public class FleeU extends Behaviour{
	
	private BotScript bot;
	private Transform target;
	
	public FleeU(BotScript bot, BotScript t) {
		super();
		this.bot = bot;
		this.target = t.getGameObject().getComponent(Transform.class);
	}

	@Override
	public Steering getSteering() {
		
		Steering steering = new Steering();
		
		steering.velocity = bot.getPosition().cpy().sub(target.position); // cpy() hace una copia para no modificar el original
		
		steering.velocity.nor().scl(bot.getMax_speed());
		
		bot.setOrientation(MathUtils.atan2(steering.velocity.y, -steering.velocity.x));
		
		steering.rotation = 0;
		return steering;
	}

}

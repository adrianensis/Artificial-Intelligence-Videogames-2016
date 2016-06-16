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
public class SeekUA extends Behaviour{
	
	protected BotScript bot;
	protected Transform target;
	
	public SeekUA(BotScript bot, BotScript t) {
		super();
		this.bot = bot;
		this.target = t.getGameObject().getComponent(Transform.class);
	}

	@Override
	public Steering getSteering() {
		
		Steering steering = new Steering();
		
		steering.linear = target.position.cpy().sub(bot.getPosition()); // cpy() hace una copia para no modificar el original
		
		steering.linear.nor().scl(bot.getMax_acceleration());
		
		bot.setOrientation(MathUtils.atan2(bot.getVelocity().y, bot.getVelocity().x)*MathUtils.radDeg);
		
		steering.angular = 0;
		
		return steering;
	}

}

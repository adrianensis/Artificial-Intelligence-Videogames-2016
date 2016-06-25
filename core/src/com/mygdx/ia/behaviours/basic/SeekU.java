package com.mygdx.ia.behaviours.basic;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.engine.Scene;
import com.mygdx.engine.Transform;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.Steering;
import com.mygdx.ia.behaviours.Behaviour;

/**
 * 
 * Este comportamiento permite que el Bot persiga a un objetivo (targer).
 *
 */
public class SeekU extends Behaviour{
	
	private BotScript bot;
	private Transform target;
	
	public SeekU(BotScript bot, BotScript t) {
		super();
		this.bot = bot;
		this.target = t.getGameObject().getComponent(Transform.class);
	}

	public SeekU(BotScript bot, Transform t) {
		super();
		this.bot = bot;
		this.target = t;
	}

	@Override
	public Steering getSteering() {
		
		
		Steering steering = new Steering();
		
		/*
		 * PARCHE: si el punto destino es casi igual al origen, no hacemos nada.
		 */
		
		if(target.position.epsilonEquals(bot.getPosition(), Scene.SCALE*0.3f))
				return steering;
		
		steering.velocity = target.position.cpy().sub(bot.getPosition()); // cpy() hace una copia para no modificar el original
		
		steering.velocity.nor().scl(bot.getMax_speed());
		
		bot.setOrientation(MathUtils.atan2(steering.velocity.y, steering.velocity.x)*Scene.SCALE);
		
		steering.rotation = 0;
		return steering;
	}

}

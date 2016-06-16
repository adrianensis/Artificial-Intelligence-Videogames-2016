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
public class ArriveU extends Behaviour{
	
	private BotScript bot;
	private Transform target;
	private float radius, timeToTarget;
	
	public ArriveU(BotScript bot, BotScript t, float radius, float timeToTarget) {
		super();
		this.bot = bot;
		this.target = t.getGameObject().getComponent(Transform.class);
		this.radius = radius;
		this.timeToTarget = timeToTarget;
	}

	@Override
	public Steering getSteering() {
		
		Steering steering = new Steering();
		
		steering.velocity = target.position.cpy().sub(bot.getPosition()); // cpy() hace una copia para no modificar el original
		
		if(steering.velocity.len() < radius)
			return new Steering(); // Devolvemos un steering vacio.
		
		steering.velocity.scl(1/timeToTarget); // steering.velocity /= timeToTarget
		
		if(steering.velocity.len() > bot.getMax_speed()){
			steering.velocity.nor();
			steering.velocity.scl(bot.getMax_speed());
		}
		
		bot.setOrientation(MathUtils.atan2(steering.velocity.y, -steering.velocity.x)*Scene.SCALE*-1);
		
		steering.velocity.nor().scl(bot.getMax_speed());
		
		// TODO hay que mostrar las los vectores por pantalla
//		System.out.println(MathUtils.atan2(steering.velocity.y, -steering.velocity.x));
		bot.setOrientation(MathUtils.atan2(bot.getVelocity().y, bot.getVelocity().x)*MathUtils.radDeg);
		
		steering.rotation = 0;
		return steering;
	}

}

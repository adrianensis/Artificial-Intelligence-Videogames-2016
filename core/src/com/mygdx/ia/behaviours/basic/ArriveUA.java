package com.mygdx.ia.behaviours.basic;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Transform;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.Steering;
import com.mygdx.ia.behaviours.Behaviour;

/**
 * 
 * Este comportamiento permite que el Bot persiga a un objetivo (targer).
 *
 */
public class ArriveUA extends Behaviour{
	
	private BotScript bot;
	private Transform target;
	private float radiusint, radiusext, timeToTarget;
	
	public ArriveUA(BotScript bot, BotScript t, float radiusint, float radiusext, float timeToTarget) {
		super();
		this.bot = bot;
		this.target = t.getGameObject().getComponent(Transform.class);
		this.radiusint = radiusint;
		this.radiusext = radiusext;
		this.timeToTarget = timeToTarget;
	}

	@Override
	public Steering getSteering() {
		Steering steering = new Steering();
		//distancia entre el target y el bot
		Vector2 direction = target.position.cpy().sub(bot.getPosition());
		float distance = direction.len();
		//dentro del circulo del target
		if(distance < radiusint){
			//devolver vac�o
			return new Steering();
		}
		float speed;
		//si est� fuera del circulo exterior
		//TODO no se por que si cambio < por > no funciona bien
		if(distance<radiusext){
			//velocidad m�xima 
			//targetspeed=maxSpeed
			speed=bot.getMax_speed();
		} else {
			//velocidad escalada
			speed=(bot.getMax_acceleration()*distance)/radiusext;
			
		}
		//TODO aqui no afecta el vector velocity por que en pseudo codigo hace targetVelocity=direction
		direction.nor();
		direction.scl(speed);
		
		steering.linear=direction.sub(bot.getVelocity());
		steering.linear.scl(1/timeToTarget);
		
		if(steering.linear.len()>bot.getMax_acceleration()){
			steering.linear.nor();
			steering.linear.scl(bot.getMax_acceleration());
		}
//		System.out.println("Distancia: "+distance+" radiusint: "+radiusint+" radiuext: "+radiusext);
		
		bot.setOrientation(MathUtils.atan2(bot.getVelocity().y, bot.getVelocity().x)*MathUtils.radDeg);
		
		steering.angular=0;
		return steering;
	}

}

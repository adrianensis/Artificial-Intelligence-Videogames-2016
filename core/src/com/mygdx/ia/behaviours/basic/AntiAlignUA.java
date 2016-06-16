package com.mygdx.ia.behaviours.basic;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Transform;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.Steering;
import com.mygdx.ia.behaviours.Behaviour;

/**
 * 
 * Este comportamiento permite que el Bot se alinee un objetivo (target).
 *
 */
public class AntiAlignUA extends Behaviour{
	
	private BotScript bot;
	private Transform target;
	private float targetAngle;
	private float slowAngle;
	private float timeToTarget;
	
	public AntiAlignUA(BotScript bot, BotScript t, float targetAngle, float slowAngle, float timeToTarget) {
		super();
		this.bot = bot;
		this.target = t.getGameObject().getComponent(Transform.class);
		this.targetAngle = targetAngle;
		this.slowAngle = slowAngle;
		this.timeToTarget = timeToTarget;
	}

	@Override
	public Steering getSteering() {
		
		Steering steering = new Steering();
		
		float rotation = target.orientation - bot.getOrientation();
		rotation += 180;

//		rotation = mapToRange(rotation);
		
		float rotationSize = Math.abs(rotation);
		
		if(rotationSize < targetAngle)
			return new Steering();
		
		float targetRotation;
		
		if(rotationSize > slowAngle)
			targetRotation = bot.getMax_rotation();
		else
			targetRotation = bot.getMax_rotation()*rotationSize/slowAngle;
		
		targetRotation *= (rotation * rotationSize);
		
		steering.angular = targetRotation - bot.getRotation();
				
		steering.angular = steering.angular / timeToTarget;
				
		float angularAcceleration = Math.abs(steering.angular);
		
		if(angularAcceleration > bot.getMax_angular_acceleration()){
			steering.angular = steering.angular / angularAcceleration;
			steering.angular = steering.angular * bot.getMax_angular_acceleration();
		}
	
		steering.linear = new Vector2(0,0);
		return steering;
	}

}

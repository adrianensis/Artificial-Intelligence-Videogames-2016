package com.mygdx.ia.behaviours.basic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Scene;
import com.mygdx.engine.Transform;
import com.mygdx.engine.UI;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.Steering;
import com.mygdx.ia.behaviours.Behaviour;

/**
 * 
 * Este comportamiento permite que el Bot se alinee un objetivo (target).
 *
 */
public class AlignUA extends Behaviour{
	
	protected BotScript bot;
	protected Transform target;
	private float targetAngle;
	private float slowAngle;
	private float timeToTarget;
	
	public AlignUA(BotScript bot, BotScript t, float targetAngle, float slowAngle, float timeToTarget) {
		super();
		this.bot = bot;
		this.target = t.getGameObject().getComponent(Transform.class);
		this.targetAngle = targetAngle;
		this.slowAngle = slowAngle;
		this.timeToTarget = timeToTarget;
	}

	@Override
	public Steering getSteering() {
	
		float x = MathUtils.cos(MathUtils.degRad*(bot.getOrientation()+slowAngle/2))*Scene.SCALE;
		float y = MathUtils.sin(MathUtils.degRad*(bot.getOrientation()+slowAngle/2))*Scene.SCALE;
		UI.getInstance().drawLine(bot.getPosition(),bot.getPosition().cpy().add(x,y), Color.GREEN);
		
		x = MathUtils.cos(MathUtils.degRad*(bot.getOrientation()-slowAngle/2))*Scene.SCALE;
		y = MathUtils.sin(MathUtils.degRad*(bot.getOrientation()-slowAngle/2))*Scene.SCALE;
		UI.getInstance().drawLine(bot.getPosition(),bot.getPosition().cpy().add(x,y), Color.GREEN);
		
		x = MathUtils.cos(MathUtils.degRad*(bot.getOrientation()+targetAngle/2))*Scene.SCALE;
		y = MathUtils.sin(MathUtils.degRad*(bot.getOrientation()+targetAngle/2))*Scene.SCALE;
		UI.getInstance().drawLine(bot.getPosition(),bot.getPosition().cpy().add(x,y), Color.GREEN);
		
		x = MathUtils.cos(MathUtils.degRad*(bot.getOrientation()-targetAngle/2))*Scene.SCALE;
		y = MathUtils.sin(MathUtils.degRad*(bot.getOrientation()-targetAngle/2))*Scene.SCALE;
		UI.getInstance().drawLine(bot.getPosition(),bot.getPosition().cpy().add(x,y), Color.GREEN);
		
		Steering steering = new Steering();
		
		float ori = target.orientation;
		
		float rotation;
		rotation = ori - bot.getOrientation();
		
//		x = MathUtils.cos(MathUtils.degRad*bot.getOrientation())*Scene.SCALE;
//		y = MathUtils.sin(MathUtils.degRad*bot.getOrientation())*Scene.SCALE;
//		
//		Vector2 vel = new Vector2(x,y);
//		Vector2 des = target.position.cpy().sub(bot.getPosition());
//		float cross = vel.crs(des);
//		float rotationSense = (new Vector2(0,1)).scl(cross).nor().y; // 1 or -1
		
//		rotation = mapToRange(rotation);
		
//		if(rotationSense > 0){
//			rotation = ori - bot.getOrientation();
//		}else{
//			rotation = bot.getOrientation()-ori;
//		}
		
		float rotationSize = Math.abs(rotation);
		
//		System.out.println(rotationSense);
		
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

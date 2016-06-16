package com.mygdx.ia.behaviours.delgate;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Scene;
import com.mygdx.ia.Bot;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.Steering;

public class Wander extends Face {
	
//	BotScript target;
	float wanderOffset;
	float wanderRadius;
	float wanderRate;
	float wanderOrientation;
	
	public Wander(BotScript bot, float targetAngle, float slowAngle, float timeToTarget, float wanderOffset, float wanderRadius, float wanderRate, float wanderOrientation) {
		super(bot, (new Bot(null, null, 0, 0, 0, 0, new Vector2(), 0)).getComponent(BotScript.class), targetAngle, slowAngle, timeToTarget);
//		this.target = target;
		this.wanderOffset = wanderOffset;
		this.wanderRadius = wanderRadius;
		this.wanderRate = wanderRate;
		this.wanderOrientation = wanderOrientation;
	}
	
	@Override
	public Steering getSteering() {
		
		float randomBinomial = (float)( Math.random() - Math.random())*Scene.SCALE;
		this.wanderOrientation += randomBinomial * wanderRadius;
		
		float targetOrientation = this.wanderOrientation + bot.getOrientation();// TODO corregir
		// target.orientation = ...
		
		// Calcular el centro del circulo del wander.
		float vx = MathUtils.cos(MathUtils.degRad*bot.getOrientation());
		float vy = MathUtils.sin(MathUtils.degRad*bot.getOrientation());
		Vector2 orientationVector = new Vector2(vx,vy);
		
		Vector2 target = bot.getPosition().cpy().add(orientationVector.scl(wanderOffset)); 
		
		target = target.cpy().add(orientationVector.scl(wanderRadius));
		
		Steering steering = super.getSteering();
		
		steering.linear = orientationVector.cpy().scl(bot.getMax_acceleration());
		
		
		
		
		
		
//		Vector2 direction = this.target.getPosition().cpy().sub(bot.getPosition()); // cpy() hace una copia para no modificar el original
//		
//		if(direction.len() == 0)
//			return super.getSteering();
//		
//		
//		super.target = new Transform();
//		super.target.orientation = MathUtils.atan2(direction.y, direction.x)*MathUtils.radDeg;
//		
		return steering;
	}

}

package com.mygdx.ia.behaviours.basic;

import com.mygdx.ia.BotScript;
import com.mygdx.ia.Steering;
import com.mygdx.ia.behaviours.Behaviour;

public class VelocityMatch extends Behaviour {
	private BotScript bot;
	private BotScript target;
	private float timeToTarget;
	
	public VelocityMatch(BotScript bot, BotScript target, float time){
		super();
		this.bot=bot;
		this.target=target;
		this.timeToTarget=time;
	}
	
	@Override
	public Steering getSteering() {
		Steering steering = new Steering();
		steering.linear=target.getVelocity().cpy().sub(bot.getVelocity());
		System.out.println("Steering: "+steering.linear.x+" "+steering.linear.y);
		steering.linear.scl(1/timeToTarget);
		
		if(steering.linear.len()>bot.getMax_acceleration()){
			steering.linear.nor();
			steering.linear.scl(bot.getMax_acceleration());
		}
		
		steering.angular=0;
		return steering;
	}

}

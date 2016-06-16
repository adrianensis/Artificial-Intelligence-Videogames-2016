package com.mygdx.ia.behaviours.delgate;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.Steering;
import com.mygdx.ia.behaviours.basic.AlignUA;

public class LookWhereYouGoing extends AlignUA {
	
	BotScript target;
	
	public LookWhereYouGoing(BotScript bot, BotScript target, float targetAngle, float slowAngle, float timeToTarget) {
		super(bot, target, targetAngle, slowAngle, timeToTarget);
		this.target = target;
	}
	
	@Override
	public Steering getSteering() {

		if(bot.getVelocity().len() == 0)
			return super.getSteering();
		
		super.target.orientation = MathUtils.atan2(bot.getVelocity().y, bot.getVelocity().x)*MathUtils.radDeg;
		
		return super.getSteering();
	}

}

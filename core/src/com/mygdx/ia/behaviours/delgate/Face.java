package com.mygdx.ia.behaviours.delgate;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Transform;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.Steering;
import com.mygdx.ia.behaviours.basic.AlignUA;

public class Face extends AlignUA {
	
	BotScript target;
	
	public Face(BotScript bot, BotScript target, float targetAngle, float slowAngle, float timeToTarget) {
		super(bot, target, targetAngle, slowAngle, timeToTarget);
		this.target = target;
	}
	
	@Override
	public Steering getSteering() {
		
		Vector2 direction = this.target.getPosition().cpy().sub(bot.getPosition()); // cpy() hace una copia para no modificar el original
		
		if(direction.len() == 0)
			return super.getSteering();
		
		
		super.target = new Transform();
		super.target.orientation = MathUtils.atan2(direction.y, direction.x)*MathUtils.radDeg;
		
		return super.getSteering();
	}

}

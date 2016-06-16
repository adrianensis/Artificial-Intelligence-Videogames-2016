package com.mygdx.ia.behaviours.delgate;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Transform;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.Steering;
import com.mygdx.ia.behaviours.basic.SeekUA;

public class Persue extends SeekUA {
	
	BotScript target;
	float max_prediction;
	
	public Persue(BotScript bot, BotScript target, float maxPred) {
		super(bot, target);
		this.target = target;
		this.max_prediction = maxPred;
	}
	
	@Override
	public Steering getSteering() {
		
		Vector2 direction = this.target.getPosition().cpy().sub(bot.getPosition()); // cpy() hace una copia para no modificar el original
		float distance = direction.len();
		
		float speed = this.target.getVelocity().len();
		// TODO: el persue funciona cuando persigue a bots con velocidad, por ejemplo, a SeekUA.
		
		float prediction = 0;
		if (speed <= distance/max_prediction) {
			prediction = max_prediction;
		} else {
			prediction = distance/speed;
		}
		
		super.target = new Transform();
		super.target.position = this.target.getPosition().cpy();
		super.target.position.add(this.target.getVelocity().cpy().scl(prediction));
		
		return super.getSteering();
	}

}

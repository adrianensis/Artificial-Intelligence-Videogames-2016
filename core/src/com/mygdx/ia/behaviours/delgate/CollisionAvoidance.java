package com.mygdx.ia.behaviours.delgate;

import java.util.LinkedList;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.Steering;
import com.mygdx.ia.behaviours.Behaviour;

public class CollisionAvoidance extends Behaviour {
	BotScript bot;
	LinkedList<BotScript> targets;
	float maxAcel;
	float radius;

	public CollisionAvoidance(BotScript b, float acel, float rad) {
		super();
		this.bot = b;
		this.maxAcel = acel;
		this.radius = rad;
	}

	@Override
	public Steering getSteering() {
		float shortestTime = 999;
		BotScript firstTarget = null;
		float firstMinSeparation = 0;
		float firstDistance = 0;
		Vector2 firstRelativePos = new Vector2();
		Vector2 firstRelativeVel = new Vector2();
		Vector2 relativePos = new Vector2();
		Vector2 relativeVel = new Vector2();
		float relativeSpeed = 0;
		float timeToCollision = 0;
		float distance = 0;
		float minSeparation = 0;

		for (BotScript t : targets) {
			relativePos = t.getPosition().sub(bot.getPosition()).cpy();
			relativeVel = t.getVelocity().sub(bot.getVelocity()).cpy();
			relativeSpeed = relativeVel.len();
			timeToCollision = (relativePos.dot(relativeVel)) / (relativeSpeed * relativeSpeed);
			distance = relativePos.len();
			minSeparation = distance - relativeSpeed * shortestTime;

			if (minSeparation < 2 * radius) {
				break;
			}

			if ((timeToCollision > 0) & (timeToCollision < shortestTime)) {
				shortestTime = timeToCollision;
				firstTarget = t;
				firstMinSeparation = minSeparation;
				firstDistance = distance;
				firstRelativePos = relativePos;
				firstRelativeVel = relativeVel;
			}
		}

		Steering steering = new Steering();

		if (firstTarget == null) {
			return steering;
		}

		if ((firstMinSeparation <= 0) | (distance < 2 * radius)) {
			relativePos = firstTarget.getPosition().sub(bot.getPosition()).cpy();
		} else {
			relativePos = firstRelativePos.add(firstRelativeVel.scl(shortestTime));
		}

		relativePos.nor();
		steering.linear = relativePos.scl(maxAcel);

		return steering;
	}

}

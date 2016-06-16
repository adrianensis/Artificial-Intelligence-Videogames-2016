package com.mygdx.ia.behaviours.delgate;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.mygdx.engine.Collision;
import com.mygdx.engine.Scene;
import com.mygdx.engine.Transform;
import com.mygdx.engine.UI;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.Steering;
import com.mygdx.ia.behaviours.Behaviour;
import com.mygdx.ia.behaviours.basic.SeekU;
import com.mygdx.ia.behaviours.basic.SeekUA;

public class ObstacleAvoidance extends Behaviour {

	private BotScript bot;
	private float avoidDistance;
	private float lookahead;
	private Collision collision;
	private Scene scene;

	public ObstacleAvoidance(BotScript b, float distance, float raydist, Scene s) {
		
		super();
		this.bot = b;
		this.avoidDistance = distance;
		this.lookahead = raydist;
		this.scene = s;
	}

	@Override
	public Steering getSteering() {
		Vector2 rayVector = bot.getVelocity();
		rayVector.nor().scl(lookahead);

		RayCastCallback callback = new RayCastCallback() {

			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				// punto de la colisi�n
				collision = new Collision();
				collision.setPosition(point);
				collision.setNormal(normal.add(point));
				return 1;
			}

		};

		float x = MathUtils.cos(bot.getOrientation() * MathUtils.degRad) * lookahead;
		float y = MathUtils.sin(bot.getOrientation() * MathUtils.degRad) * lookahead;
		Vector2 arrowhead = new Vector2(x + bot.getPosition().x, y + bot.getPosition().y);
		scene.getWorld().rayCast(callback, bot.getPosition(), arrowhead);

		if (collision == null) {
			System.out.println("not running");
			// verde si no tiene colisi�n
			UI.getInstance().drawLine(bot.getPosition(), arrowhead, Color.GREEN);
			return new Steering();
		}
		System.out.println("start running");
		Transform t = new Transform();
		t.position = collision.getPosition().add(collision.getNormal().nor().scl(avoidDistance));
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>ObstacleAvoidance.getSteering()");
		System.out.println(t.position);
		// TODO el error es que el vector tira hacia el infinity, �por que?
		// rojo si tiene colisi�n
		this.collision = null;
		UI.getInstance().drawLine(bot.getPosition(), arrowhead, Color.RED);
		return new SeekU(bot, t).getSteering();
	}
}

package com.mygdx.engine;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Este componente hace que el GameObject se vuelva rigido 
 * y se vea afectado por las propiedades fisicas del mundo. 
 *
 */
public class Collider extends Component {
	
	private BodyDef bodyDef;
	private Body body;
	private Fixture fixture;
	float w,h;
	
	public Collider(float w, float h,boolean isStatic) {
		bodyDef = new BodyDef();
		
		this.w = w*Scene.SCALE;
        this.h = h*Scene.SCALE;
		
		if(isStatic)
			bodyDef.type = BodyDef.BodyType.StaticBody;
		else
			bodyDef.type = BodyDef.BodyType.DynamicBody;
	}
	
	public void start(World world){
		
		float x = this.gameObject.getComponent(Transform.class).position.x;
		float y = this.gameObject.getComponent(Transform.class).position.y;
		bodyDef.position.set(x,y);
				
		body = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
        
        shape.setAsBox(w/2, h/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        
        fixtureDef.isSensor = true;

        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this.gameObject);
        shape.dispose();
	}

	@Override
	public void update() {
		Transform t = this.gameObject.getComponent(Transform.class);
		
		// ESTO SE COMENTA PORQUE SOLO USAMOS EL COLLIDER PARA EL RAYCASTING
//		t.position.x = body.getPosition().x;
//		t.position.y = body.getPosition().y;
//		
//		this.gameObject.getComponent(Transform.class).checkLimits();
		body.setTransform(new Vector2(t.position.x, t.position.y), t.orientation);
	}
	
	public Vector3 getPosition() {
		return new Vector3(body.getPosition().x, body.getPosition().y, 0);
	}

	public Body getBody(){
		return this.body;
	}

	@Override
	public void destroy() {
		this.body.getWorld().destroyBody(this.body);
		for (Fixture f : body.getFixtureList()) {
			this.body.destroyFixture(f);
			f.setUserData(null);
		}
		
		
		fixture.setUserData(null);
		body.setUserData(null);
        body = null;
        fixture = null;
	}

}

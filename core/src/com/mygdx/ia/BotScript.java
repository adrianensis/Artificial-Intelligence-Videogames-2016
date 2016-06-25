package com.mygdx.ia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Engine;
import com.mygdx.engine.Renderer;
import com.mygdx.engine.Scene;
import com.mygdx.engine.Script;
import com.mygdx.engine.Transform;
import com.mygdx.engine.UI;
import com.mygdx.ia.behaviours.Behaviour;
import com.mygdx.ia.behaviours.group.GroupBehaviour;

/**
 * Esta clase es la logica del bot.
 *
 */
public class BotScript extends Script {
	
	private Vector2 tmpPos;
	
	/*
	 * Con estos nuevos parametros los bots estan preparados para comportamientos uniformes.
	 */
	private float max_speed;
	private float max_rotation;
	private float max_acceleration;
	private float max_angular_acceleration;
	private int mass;
	
	/*
	 * Con estos nuevos parametros los bots estan preparados para comportamientos unif. acelerados.
	 */
	private Vector2 velocity;
	private float rotation;
	
	// lista de comportamientos asociados al bot.
	private LinkedList<Behaviour> behaviours;
	
	public BotScript(float speed, float accel, float ang_accel, int mass, Vector2 pos, float rot) {
		
		this.tmpPos = pos.cpy();
				
		this.max_speed = speed;
		this.max_acceleration = accel;
		this.mass = mass;
		this.max_rotation = rot;
		this.max_angular_acceleration = ang_accel;
		
		this.velocity = new Vector2(0,0);
		this.rotation = 0;
				
		this.behaviours = new LinkedList<Behaviour>();		
	}
	
	@Override
	public
	void start() {
		
		Transform t = this.gameObject.getComponent(Transform.class);
		
		/**
		 * SE INICIALIZA EL BOT Y SE LE DA COMPORTAMIENTO DE PERSEGUIR AL JUGADOR.
		 */
		
		// Se coloca al bot en una posicion aleatoria del mapa.
		Scene currentScene = Engine.getInstance().getCurrentScene();
		
//		float x = ((float) (Math.random()*1000.0)) % currentScene.getMapWidth()*currentScene.getTilePixelWidth();
//		float y = ((float) (Math.random()*1000.0)) % currentScene.getMapHeight()*currentScene.getTilePixelHeight();
//	
//		t.position.x = x;
//		t.position.y = y;
		
		setPosition(tmpPos);
	}
	
	private void updateU(Steering steering, float time){
		// UPDATE MOV.UNIF.
		Vector2 pos = getPosition();
		float ori = getOrientation();
		
		pos.add(steering.velocity.cpy().scl(time)); // pos := pos + steering.vel * time
		setOrientation(ori+(steering.rotation*time)); // ori := ori + steering.rot * time
		
	}
	
	private void updateUA(Steering steering, float time){
		// UPDATE MOV.UNIF.ACCEL.
		Vector2 pos = getPosition();
		Vector2 vel = getVelocity();
		float ori = getOrientation();
		float rot = getRotation();
		
		pos.add(vel.scl(time)); // pos := pos + steering.vel * time
		setOrientation(ori+rot*time); // ori := ori + steering.rot * time
		
		vel.add(steering.linear.scl(time)); // vel := vel + steering.linear * time
		
		setRotation(rot + (steering.angular * time)); // rot := rot + steering.angular * time
		
		/*
		 * ROZAMIENTO
		 */
		
		float value = 10*time;
		
		if(getRotation() > 0){
			setRotation(getRotation()-value);
		}
		
		if(getRotation() < 0){
			setRotation(getRotation()+value);
		}
		
//		if(getVelocity().len() > 0){
//			setVelocity(getVelocity().add(-value, -value));
//		}
//		
//		if(getVelocity().len() < 0){
//			setVelocity(getVelocity().add(value, value));
//		}
	
	}
	
	/**
	 * Cada actualizacion se llama a arbiter.
	 * Se actualiza la posicion y la orientacion.
	 */
	@Override
	public void update() {
		
		// Nos devuelve un steering después de hacer todo el arbitraje.
		Steering steering = arbiter();
		float time  = Gdx.graphics.getDeltaTime();
		
		this.updateU(steering, time);
		this.updateUA(steering, time);
		
		applyLimits();
		
		float w = gameObject.getComponent(Renderer.class).getSprite().getWidth();
		Vector2 pos = getPosition();
		
//		UI.getInstance().drawTextWorld(gameObject.getName(),pos.x-(w/2),pos.y+(1.2f*Scene.SCALE), Color.WHITE);
		
		/*
		 * DIBUJAMOS LINEA DESDE EL CENTRO DEL SPRITE, CON DIRECCION SU VECTOR Velocidad
		 */
		UI.getInstance().drawLineWorld(getPosition().cpy().add(0, -1),getVelocity().cpy().nor().scl(2*Scene.SCALE).add(getPosition()), Color.RED, false);
		
		/*
		 * Dibujar aceleracion
		 */
		UI.getInstance().drawLineWorld(getPosition().cpy().add(0,1),steering.linear.cpy().nor().scl(2*Scene.SCALE).add(getPosition()), Color.YELLOW, false);
		
		float x = MathUtils.cos(MathUtils.degRad*getOrientation())*Scene.SCALE;
		float y = MathUtils.sin(MathUtils.degRad*getOrientation())*Scene.SCALE;
		
		UI.getInstance().drawLineWorld(getPosition(),getPosition().cpy().add(x,y), Color.CYAN, false);

	}
	
	/**
	 * Aplica los limites de velocidad y rotacion
	 */
	void applyLimits(){
		if(getVelocity().x > getMax_speed())
			getVelocity().set(getMax_speed(), getVelocity().y);
		if(getVelocity().y > getMax_speed())
			getVelocity().set(getVelocity().x, getMax_speed());
		if(getRotation() > getMax_rotation())
			setRotation(getMax_rotation());
	}
	
	/**
	 * Añadir comportamiento
	 * @param b
	 */
	public void addBehaviour(Behaviour b){
		this.behaviours.add(b);
	}
	
	/**
	 * Eliminar comportamiento
	 * @param b
	 */
	public void removeBehaviour(Behaviour b){
		this.behaviours.remove(b);
	}
	
	public boolean containsBehaviour(Class<? extends Behaviour> ... listClasses){
		for (Behaviour b : this.behaviours) {
			for (Class<? extends Behaviour> c : listClasses) {
				if(c.isInstance(b))
					return true;
			}
		}
		
		return false;
	}
	
	public void clearAllBehaviours() {
		this.behaviours.clear();
	}
	
	public void clearSingleBehaviours() {
		ArrayList<Behaviour> removables = new ArrayList<Behaviour>();
		

		for (Behaviour b : this.behaviours) {
			if( ! (b instanceof GroupBehaviour))
				removables.add(b);
		}
		
		this.behaviours.removeAll(removables);
	}
	
	public void clearBehaviours(Class<? extends Behaviour> ... listClasses) {
		
		ArrayList<Behaviour> removables = new ArrayList<Behaviour>();
		
		for (Class<? extends Behaviour> c : listClasses) {
			for (Behaviour b : this.behaviours) {
				if(( ! (b instanceof GroupBehaviour)) && c.isInstance(b))
					removables.add(b);
			}
		}
		
		this.behaviours.removeAll(removables);
	}
	
	public void clearGroupBehaviours(Class<? extends GroupBehaviour> ... listClasses) {
		
		ArrayList<GroupBehaviour> removables = new ArrayList<GroupBehaviour>();
		
		for (Class<? extends GroupBehaviour> c : listClasses) {
			for (Behaviour b : this.behaviours) {
				if(b instanceof GroupBehaviour){
					((GroupBehaviour) b).getTargets().remove(this);
					removables.add((GroupBehaviour)b);
				}
			}
		}
		
		this.behaviours.removeAll(removables);
	}
	
	public List<Behaviour> getBehaviours(){
		return this.behaviours;
	}
	
	/**
	 * 
	 * Funcion que devuelve el Steering que debe ejecutar el Bot.
	 * Depende de todos los comportamientos asociados al Bot.
	 */
	public Steering arbiter(){
		Steering totalSteering = new Steering();
		
		for (Behaviour b : behaviours) {
			
			Steering s = b.getSteering();
			
			s.mul(b.getWeight());
			
			totalSteering.velocity.add(s.velocity);
			totalSteering.rotation += s.rotation;
			
			totalSteering.linear.add(s.linear);
			totalSteering.angular +=  s.angular;
		}
				
		return totalSteering; // Devuelve el steering resultante de todos los comportamientos.
	}
	
	public float getMax_speed() {
		return max_speed;
	}

	public void setMax_speed(float max_speed) {
		this.max_speed = max_speed;
	}

	public float getMax_rotation() {
		return max_rotation;
	}

	public void setMax_rotation(float max_rotation) {
		this.max_rotation = max_rotation;
	}

	public float getMax_acceleration() {
		return max_acceleration;
	}

	public void setMax_acceleration(float max_acceleration) {
		this.max_acceleration = max_acceleration;
	}

	public int getMass() {
		return mass;
	}

	public void setMass(int mass) {
		this.mass = mass;
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getMax_angular_acceleration() {
		return max_angular_acceleration;
	}

	public void setMax_angular_acceleration(float max_angular_acceleration) {
		this.max_angular_acceleration = max_angular_acceleration;
	}

	/**
	 * 
	 * Funciones para acceder rapido a la posicion y la orientacion del transform.
	 */
	
	public Vector2 getPosition() {
		return gameObject.getComponent(Transform.class).position;
	}
	
	public void setPosition(Vector2 pos) {
		gameObject.getComponent(Transform.class).position.set(pos);
	}
	
	public float getOrientation() {
		return gameObject.getComponent(Transform.class).orientation;
	}
	
	public void setOrientation(float ori) {
		gameObject.getComponent(Transform.class).orientation = ori;
	}
	
}

package com.mygdx.game.units;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Renderer;
import com.mygdx.engine.Scene;
import com.mygdx.engine.Script;
import com.mygdx.engine.Transform;
import com.mygdx.engine.UI;
import com.mygdx.ia.Bot;

public class Unit extends Bot {
	
	private float rangeAttack, rangeVision, timeAttack, life, maxLife;
	private long lastTimeAttack;
	private int team;
	private Vector2 destiny;
	private Unit targetAttack, lastTargetAttack;
	private boolean isTargeted;
	private UnitGroup group;

	public Unit(String key, int team, String textureName, float speed, float accel, float ang_accel, int mass, Vector2 pos, float rot, 
			float life, float rangeAttack, float rangeVision, float timeAttack) {
		
		super(key, textureName, speed, accel, ang_accel, mass, pos, rot);
		
		this.life = this.maxLife = life; // maxima vida que puede tener. Se define con la vida incial.
		this.rangeAttack = rangeAttack; // distancia a la que puede atacar.
		this.rangeVision = rangeVision; // distancia de vision
		this.team = team; // equipo al que pertenece la unidad
		this.timeAttack = timeAttack; // tiempo entre cada ataque, en segundos.
		this.targetAttack = null; // target al que atacar
		this.lastTargetAttack = null; // ultimo target elegido, para comparar con el target actual
		this.destiny = null; // destino al que llegar
		this.lastTimeAttack = 0; // ultima vez que la unidad atacó
		
		this.addComponent(new Script(){
			
			Unit unit;
			
			@Override
			public void start() {
				unit = (Unit) this.gameObject;
			}
			
			@Override
			public void update() {
				Vector2 pos = unit.getComponent(Transform.class).position;
				float w = unit.getComponent(Renderer.class).getSprite().getWidth();
				
				float life = unit.getLife();
				float maxLife = unit.getMaxLife();
				
				if(life > maxLife*0.75){
					UI.getInstance().drawTextWorld(Float.toString(life), pos.x-(w/2),pos.y-(1*Scene.SCALE)+6, Color.GREEN);
				}else if(life > maxLife*0.5){
					UI.getInstance().drawTextWorld(Float.toString(life), pos.x-(w/2),pos.y-(1*Scene.SCALE)+6, Color.YELLOW);
				}else{
					UI.getInstance().drawTextWorld(Float.toString(life), pos.x-(w/2),pos.y-(1*Scene.SCALE)+6, Color.RED);
				}
				
			}
		});
		
	}

	public float getLife() {
		return life;
	}
	
	public float getMaxLife() {
		return maxLife;
	}

	public void setLife(float life) {
		this.life = life;
	}

	public UnitGroup getGroup() {
		return group;
	}

	public void setGroup(UnitGroup group) {
		this.group = group;
	}

	public float getRangeAttack() {
		return rangeAttack;
	}
	
	public float getRangeVision() {
		return rangeVision;
	}
	
	public int getTeam() {
		return team;
	}
	
	public void setDestiny(Vector2 destiny) {
		this.destiny = destiny;
	}
	
	public Vector2 getDestiny() {
		return destiny;
	}
	
	public float getTimeAttack() {
		return timeAttack;
	}

	public Unit getTargetAttack() {
		return targetAttack;
	}
	
	public void clearTargetAttack() {
		this.lastTargetAttack = null;
		this.targetAttack = null;
	}

	public void setTargetAttack(Unit targetAttack) {
		if((this.team != targetAttack.getTeam()) && (this.getName() != targetAttack.getName())){
			this.lastTargetAttack = this.targetAttack;
			this.targetAttack = targetAttack;
		}
	}
	
	public boolean hasNewTargetAttack(){
		
		if(lastTargetAttack == null)
			return (targetAttack != null);
		
		if(targetAttack == null)
			return (lastTargetAttack != null);
		
		return  (targetAttack != null) &&
				(lastTargetAttack != null) && 
				(lastTargetAttack.getName() != targetAttack.getName());
	}
	
	public void attack(Unit otherUnit){
		
		long now = System.currentTimeMillis();
		
		float attackValue = 10;
		
		if((now - this.lastTimeAttack) > timeAttack*1000){
	
			if(otherUnit.getLife()-attackValue <= 0){
				otherUnit.setLife(0);
			}else
				otherUnit.setLife(otherUnit.getLife()-attackValue);
			
			this.lastTimeAttack = now;
		}
		
		
		
	}
	
	

}

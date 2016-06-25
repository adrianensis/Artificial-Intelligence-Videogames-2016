package com.mygdx.game.units;

import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Engine;
import com.mygdx.engine.Renderer;
import com.mygdx.engine.Scene;
import com.mygdx.engine.Script;
import com.mygdx.engine.Transform;
import com.mygdx.engine.UI;
import com.mygdx.ia.Bot;
import com.mygdx.game.Config;
import com.mygdx.game.Waypoints;

public abstract class Unit extends Bot {
	
	public enum Mode{
		NONE,OFFENSIVE, DEFENSIVE
	}
	
	private float rangeAttack, rangeVision, timeAttack, life, maxLife, atk, def;
	private long lastTimeAttack, lastTimeHealing;
	private int team;
	private Vector2 destiny;
	private Unit targetAttack, lastTargetAttack;
	private UnitGroup group;
	private SpawnBaseUnit base;
	private Map<Integer, Integer> terrainMap;
	private boolean patrolBase;
	private Unit.Mode mode;

	public Unit(String key, int team, String textureName, float speed, float accel, float ang_accel, int mass, Vector2 pos, float rot, 
			float life, float rangeAttack, float rangeVision, float timeAttack, Map<Integer, Integer> terrainMap, int atk, int def) {
		
		super(key, textureName, speed, accel, ang_accel, mass, pos, rot);
		
		this.life = this.maxLife = life; // maxima vida que puede tener. Se define con la vida incial.
		this.rangeAttack = rangeAttack; // distancia a la que puede atacar.
		this.rangeVision = rangeVision; // distancia de vision
		this.team = team; // equipo al que pertenece la unidad
		this.timeAttack = timeAttack; // tiempo entre cada ataque, en segundos.
		this.targetAttack = null; // target al que atacar
		this.lastTargetAttack = null; // ultimo target elegido, para comparar con el target actual
		this.destiny = null; // destino al que llegar
		
		// ultima vez que la unidad atacó, ultima vez que la unidad se curó, ultima vez que la unidad hizo algo.
		this.lastTimeAttack = lastTimeHealing = 0;
		
		this.terrainMap = terrainMap;
		this.atk=atk;
		this.def =def;
		this.base = null;
		this.patrolBase = false; // flag que indica que debe patrullar la base.
		this.mode = Mode.NONE;
		
		
		// Script que actualiza el color de la vida.
		
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
				
				Scene scene = Engine.getInstance().getCurrentScene();
				
				TiledMapTile tile = scene.getTile(scene.fromWorldtoGridCoordinates(pos));

				// si estamos en tile de flores o es un arbol azul, entonces nos curamos.
				if(tile.getId() == 403 || tile.getId() == 529){

					long now = System.currentTimeMillis();
					
					if((now - lastTimeHealing) > 500){ //500ms
						setLife(getLife()+2);
						
						lastTimeHealing = now;
					}
					
				}
				
				if(life > maxLife*0.75){
					// 100 - 76
					UI.getInstance().drawTextWorld(Float.toString(life), pos.x-(w/2),pos.y-(1*Scene.SCALE)+6, Color.GREEN.cpy().add(Color.OLIVE));
				}else if(life > maxLife*0.5){
					// 75 - 49
					UI.getInstance().drawTextWorld(Float.toString(life), pos.x-(w/2),pos.y-(1*Scene.SCALE)+6, Color.YELLOW);
				}else{
					// 50 - 0
					UI.getInstance().drawTextWorld(Float.toString(life), pos.x-(w/2),pos.y-(1*Scene.SCALE)+6, Color.RED);
				}
				
				
			}
		});
		
	}
	
	
	/**
	 * Devuelve el HashMap que contiene los pesos del terreno para esta unidad.
	 * @return
	 */
	public Map<Integer, Integer> getTerrainMap() {
		return terrainMap;
	}

	public void setTerrainMap(Map<Integer, Integer> terrainMap) {
		this.terrainMap = terrainMap;
	}

	/**
	 * Devuelve la vida actual del personaje
	 * @return
	 */
	public float getLife() {
		return life;
	}
	
	public float getMaxLife() {
		return maxLife;
	}

	public void setLife(float life) {
		if(life <= getMaxLife())
			this.life = life;
		else{
			this.life = getMaxLife();
		}
	}
	
	public void setBase(SpawnBaseUnit base) {
		this.base = base;
	}
	
	public SpawnBaseUnit getBase() {
		return base;
	}
	
	public boolean isPatrolBase() {
		return patrolBase;
	}
	
	public void setPatrolBase(boolean patrolBase) {
		this.patrolBase = patrolBase;
	}

	/**
	 * Devuelve el grupo de unidades al que está asociado la unidad.
	 * 
	 * Por ejemplo, cuando la unidad está haciendo flocking o está en formación.
	 * @return
	 */
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
	
	/**
	 * Devuelve el equipo o bando al que pertenece la unidad.
	 * @return
	 */
	public int getTeam() {
		return team;
	}

	public void setDestiny(Vector2 destiny) {
		this.destiny = destiny;
	}
	
	/**
	 * Devuelve el destino que tiene asignado la unidad.
	 * @param destiny
	 */
	public Vector2 getDestiny() {
		return destiny;
	}
	
	/**
	 * Devuelve la frecuencia de ataque.
	 * @return
	 */
	public float getTimeAttack() {
		return timeAttack;
	}

	/**
	 * Devuelve la unidad que se tiene asignada como target para atacar.
	 * @return
	 */
	public Unit getTargetAttack() {
		return targetAttack;
	}
	
	/**
	 * Limpia el target.
	 */
	public void clearTargetAttack() {
		this.lastTargetAttack = null;
		this.targetAttack = null;
	}

	/**
	 * Establece el target para atacar. Si es la misma unidad o del mismo equipo la ignora.
	 * @param targetAttack
	 */
	public void setTargetAttack(Unit targetAttack) {
		if((this.team != targetAttack.getTeam()) && (this.getName() != targetAttack.getName())){
			this.lastTargetAttack = this.targetAttack;
			this.targetAttack = targetAttack;
			
//			this.destiny = null;
		}
	}
	
	/**
	 * Comprueba si hay un nuevo target seleccionado.
	 * Lo hace comparando con el target anterior.
	 * @return
	 */
	public boolean hasNewTargetAttack(){
		
		if(lastTargetAttack == null)
			return (targetAttack != null);
		
		if(targetAttack == null)
			return (lastTargetAttack != null);
		
		return  (targetAttack != null) &&
				(lastTargetAttack != null) && 
				(lastTargetAttack.getName() != targetAttack.getName());
	}
	
	
	
	public Unit.Mode getMode() {
		return mode;
	}


	public void setMode(Unit.Mode mode) {
		this.mode = mode;
	}


	public abstract void onOffensive();
	public abstract void onDefensive();
	
	public abstract void onRunaway();
	
	/**
	 * Función que ejecuta el ataque de la unidad contra otra unidad.
	 * @param otherUnit
	 */
	public void attack(Unit otherUnit){
		
		long now = System.currentTimeMillis();
		Scene scene =Engine.getInstance().getCurrentScene();
		Vector2 pos = this.getComponent(Transform.class).position;
		//extraer modificadores por terreno
		float FAD = Config.getInstacia().getFAD(this.getClass(), otherUnit.getClass());
		float FTA = Config.getInstacia().getFTA((scene.getTile(scene.fromWorldtoGridCoordinates(pos))).getId(), this.getClass());
		float FTD = Config.getInstacia().getFTD((scene.getTile(scene.fromWorldtoGridCoordinates(pos))).getId(), this.getClass());
		
		int chance = (int) (Math.random()*50+1);
		float attackValue = 0;
		
		if(chance == 50){
			//critico
			attackValue = atk*2*FAD*FTA-otherUnit.def*FTD;
		} else {
			attackValue = FTA*atk - otherUnit.def*FTD;
		}
		
		//en caso de que el ataque sea negativo se invierte el signo
		if (attackValue<0) {
			attackValue*=-1;
		}
		
		if((now - this.lastTimeAttack) > timeAttack*1000){
	
			if(otherUnit.getLife()-attackValue <= 0){
				otherUnit.setLife(0);
			}else
				otherUnit.setLife(otherUnit.getLife()-attackValue);
			
			this.lastTimeAttack = now;
		}
		
		
		
	}
	
	

}

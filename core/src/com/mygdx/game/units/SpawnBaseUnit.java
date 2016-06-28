package com.mygdx.game.units;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Engine;
import com.mygdx.engine.Scene;
import com.mygdx.engine.Script;
import com.mygdx.engine.Transform;
import com.mygdx.game.Config;
import com.mygdx.ia.Path;

public class SpawnBaseUnit extends Unit {
	
	private List<Unit> units;
	private float spawnTime; // tiempo de cada respawn.
	private int maxLight,maxHeavy,maxPhantom; // numero total de unidades de cada tipo.
	private int lightCounter,heavyCounter,phantomCounter; // contador para cada tipo de unidad.
	private Random random;
	private int spawnCounter;
	private boolean defenseSignal; // indica si se ha lanzado la señal de defensa de la base
	
	public SpawnBaseUnit(String key, int team, String textureName, Vector2 pos, int def) {
		
		super(key, team, textureName, 0, 0, 0, 0, pos, 0, 700, 0, 5*Scene.SCALE, 0,null, 10, def);
				
		this.spawnTime = 6;
		this.defenseSignal = false;
		
		units = new LinkedList<Unit>();
		
		/*
		 * PORCENTAJES DE UNIDADES
		 */
		int maxUnits = 13;
		this.maxLight = (int)(maxUnits * 0.4); // 40% unidades ligeras
		this.maxHeavy = (int)(maxUnits * 0.4); // 40% unidades pesadas
		this.maxPhantom = (int)(maxUnits * 0.2); // 20% unidades fantasma
		
		this.lightCounter = 0;
		this.heavyCounter = 0;
		this.phantomCounter = 0;
		
		this.spawnCounter = 0;
		
		/*
		 * Esto se usa para generar las posiciones aleatorias entorno a la base.
		 */
		this.random = new Random();
		this.random.setSeed(1234);
		
		
		/*
		 * Este script se encarga de actualizar.
		 */
		this.addComponent(new Script() {
			
			private SpawnBaseUnit base;
			private long lastSpawnTime, lastRegenerationTime;
			private boolean firstTime;
			
			/*
			 * La primera vez que se hace update() se generan las unidades.
			 */
			private void firstSpawn(){
				if(firstTime){
					for (int i = 0; i < base.getMaxUnits(); i++) {
						base.spawn();
					}
					
					firstTime = false;
				}
			}
			
			@Override
			public void update() {
				
				firstSpawn();
				
				long now = System.currentTimeMillis();
				
				// Si la  base tiene una vida inferior al 50% se avisan a las unidades para que defiendan la base.
				if (base.getLife() < (base.getMaxLife()*0.5))
					base.enableDefenseSignal();
				else
					base.disableDefenseSignal(); // si no, hay que desactivarla.
				
				base.checkDeaths(); // comprueba/limpia las unidades muertas
				
				// si ha pasado el tiempo de spawn y hay unidades muertas, se crean nuevas.
				if((now - lastSpawnTime)/1000 > base.getSpawnTime() && base.getUnits().size() < base.getMaxUnits()){
					base.spawn();
					
					lastSpawnTime = now;
				}
				
				
				// Cada 500ms se regenera la vida de la base en 1 punto. 
				if((now - lastRegenerationTime) > 500){
					base.setLife(base.getLife() + 1);
					
					lastRegenerationTime = now;
				}
				
			}
			
			@Override
			public void start() {
				base = (SpawnBaseUnit)gameObject;
				lastSpawnTime = lastRegenerationTime = System.currentTimeMillis();
				firstTime = true;

			}
		});
		
	}
	
	
	@Override
	public void enableTotalWar() {
		super.enableTotalWar();
		
		for (Unit unit : units) {
			unit.enableTotalWar();
			
			
			/*
			 * Cuando se activa Total War, algunas unidades iran a atacar otras a defender.
			 */
			float rand = (float) Math.random();
			
			if(rand < 0.5)
				unit.setMode(Mode.OFFENSIVE);
			else 
				unit.setMode(Mode.DEFENSIVE);
		}
	}
	
	@Override
	public void disableTotalWar() {
		super.disableTotalWar();
		
		for (Unit unit : units) {
			unit.disableTotalWar();
		}
	}
	
	/**
	 * Ordena a las unidades que pasen a modo defensivo
	 */
	public void enableDefenseSignal(){
		if(!defenseSignal){
			for (Unit unit : units) {
				unit.setMode(Mode.DEFENSIVE);
			}
			
			defenseSignal = true;
		}
	}
	
	public void disableDefenseSignal(){
		
		defenseSignal = false;
	}
	
	
	/**
	 * Comprueba y limpia las unidades muertas.
	 */
	public void checkDeaths(){
		
		Scene scene = Engine.getInstance().getCurrentScene();
		
		List<Unit> removables = new LinkedList<Unit>(); // lista de unidades que se van a borrar.
		
		// recorremos las unidades
		for (Unit unit : units) {
			
			// si ha muerto
			if(unit.getLife() == 0){
				// borramos el objeto de la escena
				scene.removeObject(unit);
				removables.add(unit);
				
				// comprobamos de que tipo era
				if(unit instanceof LightUnit){
					lightCounter--;
					
				}else if(unit instanceof HeavyUnit){
					heavyCounter--;
					
				}else if(unit instanceof PhantomUnit){
					phantomCounter--;
					
				}
			}
		}
		
		for (Unit unit : removables) {
			units.remove(unit);
		}
	}
	
	/**
	 * Genera una unidad
	 */
	public void spawn(){
		
//		System.out.println("SPAWN");
		
		Vector2 base_pos = this.getComponent(Transform.class).position;
		
		float spawnRadius = 5*Scene.SCALE;
	
		float randomX = random.nextFloat()*1000+random.nextFloat()*-1000; 
		float randomY = random.nextFloat()*1000+random.nextFloat()*-1000; 
		
		Vector2 randomVector = new Vector2((int)(randomX%spawnRadius),(int)(randomY%spawnRadius));
		
		Unit newUnit = null;
		
		if(lightCounter < maxLight){
			newUnit = spawnLightUnit(base_pos.cpy().add(randomVector));
		}else if(heavyCounter < maxHeavy){
			newUnit = spawnHeavyUnit(base_pos.cpy().add(randomVector));
		}else if(phantomCounter < maxPhantom){
			newUnit = spawnPhantomUnit(base_pos.cpy().add(randomVector));
		}
		
		if(newUnit != null){

			// Pero si estamos en modo total war, la base decide en que modo aparecen las unidades
			if(isTotalWar()){
				
				newUnit.enableTotalWar();
				
				// si no, si la vida es menor que 50% lo mandamos a defender
				if(getLife() < (getMaxLife()*0.5)){
				newUnit.setMode(Mode.DEFENSIVE);
					
				// si no, aleatorio
				}else{
					
					float rand = (float) Math.random();
					
					if(rand < 0.5)
						newUnit.setMode(Mode.OFFENSIVE);
					else if(rand < 0.9)
						newUnit.setMode(Mode.DEFENSIVE);
					else
						newUnit.setPatrolBase(true);
				}
				
			}else{
				float rand = (float) Math.random();
				
				if(rand > 0 && rand < 0.1)
					newUnit.setPatrolBase(true);
				else 
					newUnit.setMode(Mode.NONE);
					
			}
			
			
			
			units.add(newUnit);
			newUnit.setBase(this);
			Engine.getInstance().getCurrentScene().addObject(newUnit);
			
			spawnCounter++;
		}
	}
	
	private Unit spawnLightUnit(Vector2 pos){
		
		String textureName = "blue1.png";
		if(this.getTeam() == 1)
			textureName = "red1.png";
		
		LightUnit unit = new LightUnit("team"+this.getTeam()+"-unit"+spawnCounter, this.getTeam(), textureName, pos , Config.getInstacia().getUnitMap(LightUnit.class));
		
		lightCounter++;
		
		return unit;
		
	}
	
	private Unit spawnHeavyUnit(Vector2 pos){
		
		String textureName = "blue2.png";
		if(this.getTeam() == 1)
			textureName = "red2.png";
		
		HeavyUnit unit = new HeavyUnit("team"+this.getTeam()+"-unit"+spawnCounter, this.getTeam(), textureName, pos , Config.getInstacia().getUnitMap(HeavyUnit.class));
		
		heavyCounter++;
		
		return unit;
		
	}
	
	private Unit spawnPhantomUnit(Vector2 pos){
		
		String textureName = "blue3.png";
		if(this.getTeam() == 1)
			textureName = "red3.png";
		
		PhantomUnit unit = new PhantomUnit("team"+this.getTeam()+"-unit"+spawnCounter, this.getTeam(), textureName, pos , Config.getInstacia().getUnitMap(PhantomUnit.class));
		
		phantomCounter++;
		
		return unit;
		
	}
	
	
	public int getMaxUnits() {
		return maxLight+maxHeavy+maxPhantom;
	}
	
	public List<Unit> getUnits() {
		return units;
	}
	
	public void setUnits(List<Unit> units) {
		this.units = units;
	}
	
	public float getSpawnTime() {
		return spawnTime;
	}
	
	/**
	 * Devuelve un path para patrullar la base.
	 * @return
	 */
	public Path getPatrolPath() {
		Path patrolPath = new Path(0.5f*Scene.SCALE);
		
		Vector2 basePos = this.getComponent(Transform.class).position.cpy();
		
		int d = 3*Scene.SCALE;
		
		patrolPath.addParam(basePos.cpy().add(-d,-d));
		patrolPath.addParam(basePos.cpy().add(d,-d));
		patrolPath.addParam(basePos.cpy().add(d,d));
		patrolPath.addParam(basePos.cpy().add(-d,d));
		
		return patrolPath;
	}

	@Override
	public void onOffensive() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDefensive() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onRunaway() {
		// TODO Auto-generated method stub
		
	}

	public int getPhantomCounter() {
		return phantomCounter;
	}
	
	public void setPhantomCounter(int phantomCounter) {
		this.phantomCounter = phantomCounter;
	}

	public int getHeavyCounter() {
		return heavyCounter;
	}

	public void setHeavyCounter(int heavyCounter) {
		this.heavyCounter = heavyCounter;
	}

	public int getLightCounter() {
		return lightCounter;
	}

	public void setLightCounter(int lightCounter) {
		this.lightCounter = lightCounter;
	}


}

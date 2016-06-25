package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Scene;
import com.mygdx.engine.Transform;
import com.mygdx.game.units.SpawnBaseUnit;
import com.mygdx.game.units.Unit;

import sun.security.jca.GetInstance;

/**
 * Esta clase contiene puntos especiales/útiles del mapa.
 * 
 * @author 
 *
 */
public class Waypoints {
	
	private static Waypoints instance = null;

	private static Scene scene; // para acceder a los tiles.
	
	private static ArrayList<Vector2> healing; // sanacion, para ambos equipos
	private static ArrayList<Vector2> hiding; // escondite, para ambos equipos
	private static Vector2[] base; // array con las 2 bases (una de cada equipo).
	private static ArrayList<Vector2>[] baseDefense; // lista de posiciones defensivas para la base (para el equipo 0 y 1)
	private static ArrayList<Vector2>[] defense; // lista de posiciones defensivas (para el equipo 0 y 1)
	
	private Waypoints(){
		healing = new ArrayList<Vector2>();
		hiding = new ArrayList<Vector2>();
		
		base = new Vector2[2];
				
		baseDefense = (ArrayList<Vector2>[])(new ArrayList[2]); // ARRAY DE 2 porque son 2 equipos !
		defense = (ArrayList<Vector2>[])(new ArrayList[2]);
		
		defense[0] = new ArrayList<Vector2>();
		defense[1] = new ArrayList<Vector2>();
		
		baseDefense[0] = new ArrayList<Vector2>();
		baseDefense[1] = new ArrayList<Vector2>();
	}
	
	public static Waypoints getInstance(){
		if(instance == null)
			instance = new Waypoints();
		
		return instance;
	}
	
	/**
	 * Añade la base
	 */
	public void setBase(int team, Vector2 pos){
		base[team] = pos.cpy();
	}
	
	/**
	 * Carga los waypoints del mapa
	 * @param scene
	 */
	public  void loadWaypoints(Scene scene){
		Waypoints.scene = scene;
		
		int w = scene.getMapWidth();
		int h = scene.getMapHeight();
		
		// recorremos mapa
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				
				Vector2 pos = new Vector2(i,j);
				
				TiledMapTile tile = scene.getTile(pos);
				
				switch (tile.getId()) {
				case 403:
					healing.add(pos);
					break;
					
				case 529:
					hiding.add(pos);
					break;

				case 1087:
										
					/*
					 * esto es para diferenciar las defensas cercanas (las de la base) de las lejanas (las del puente).
					 */
					if(scene.fromWorldtoGridCoordinates(base[0]).dst(pos) < 10)
						baseDefense[0].add(pos);
					else
						defense[0].add(pos);
					
					break;
					
				case 1258:
					if(scene.fromWorldtoGridCoordinates(base[1]).dst(pos) < 10)
						baseDefense[1].add(pos);						
					else
						defense[1].add(pos);
					
					break;
					
				default:
					break;
				}
				
			}
			
		}
	}
	
	private Vector2 getNearestPoint(ArrayList<Vector2> array, Unit unit){
		Vector2 pos = unit.getComponent(Transform.class).position;
		
		Vector2 min = array.get(1);
		
		for (int i = 1; i < array.size(); i++) {
			if(array.get(i).dst(pos) < min.dst(pos)){
				min = array.get(i);
			}
		}
		
		return scene.fromGridToWorldCoordinates(min);
	}
	
	private Vector2 getRandomPoint(ArrayList<Vector2> array, Unit unit){

		
		int i = (int)(Math.random()*1000)%array.size();
		
//		System.out.println(i);
		
		return scene.fromGridToWorldCoordinates(array.get(i));
	}
	
	public Vector2 getHidingPoint(Unit unit){
		return getNearestPoint(hiding, unit);
	}
	
	public Vector2 getHealingPoint(Unit unit){
		return getNearestPoint(healing, unit);
	}
	
	public Vector2 getDefensePoint(Unit unit){
		return getRandomPoint(defense[unit.getTeam()], unit);
	}
	
	public Vector2 getBaseDefensePoint(Unit unit){
		return getRandomPoint(baseDefense[unit.getTeam()], unit);
	}
	
	public Vector2 getEnemyDefensePoint(Unit unit){
		return getRandomPoint(defense[(unit.getTeam()+1)%2], unit);
	}
	
	public Vector2 getEnemyBaseDefensePoint(Unit unit){
		return getRandomPoint(baseDefense[(unit.getTeam()+1)%2], unit);
	}
	
}

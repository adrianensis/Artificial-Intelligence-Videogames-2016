package com.mygdx.ia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;

public class PathfindingConfig {

	private Map<Class, Heuristic> map;
	
	public PathfindingConfig() {
		this.map = new HashMap<Class, Heuristic>();
	}
	
	/** 
	 * Asigna una heurística a la clase c.
	 * @param c
	 * @param h
	 */
	public void bindHeuristic(Class c, Heuristic h){
		this.map.put(c, h);
	}
	
	public Heuristic getHeuristic(Class c){
		return this.map.get(c);
	}
	
	/**
	 * Calcula el valor heurístico de ir de pos a goal, en función de la heurística asignada
	 * a la clase c.
	 * @param c
	 * @param pos
	 * @param goal
	 * @return
	 */
	public float calculateHeuristic(Class c, Vector2 pos, Vector2 goal){
		if(this.map.get(c) == Heuristic.MANHATTAN)
			return Math.abs(pos.x-goal.x)+Math.abs(pos.y-goal.y);
		
		if(this.map.get(c) == Heuristic.CHEBYCHEV)
			return (float) Math.max (Math.abs(pos.x-goal.x), Math.abs(pos.y-goal.y));
		
		if(this.map.get(c) == Heuristic.EUCLIDEAN)
			return (float) Math.sqrt(Math.pow(pos.x-goal.x,2) + Math.pow(pos.y-goal.y,2));
		
		return 0;
	}
	
	/**
	 * Esta funcion devuelve un conjunto de acciones posibles para cada heurística.
	 * @param c
	 * @return
	 */
	public ArrayList<Vector2> getActions(Class c){
		
		ArrayList<Vector2> actions = new ArrayList<Vector2>();

		// movimientos horizontales y verticales
		actions.add(new Vector2(0,1)); // ir hacia arriba
		actions.add(new Vector2(0,-1)); // ir hacia abajo
		actions.add(new Vector2(-1,0)); // ir a la izquierda
		actions.add(new Vector2(1,0)); // ir a la derecha
		
		if(this.map.get(c) == Heuristic.CHEBYCHEV || this.map.get(c) == Heuristic.EUCLIDEAN){
			
			// movimientos en diagonal
			actions.add(new Vector2(-1,1)); // ir hacia arriba izquierda
			actions.add(new Vector2(1,1)); // ir hacia arriba derecha
			actions.add(new Vector2(-1,-1)); // ir hacia abajo izquierda
			actions.add(new Vector2(1,-1)); // ir hacia abajo derecha
		}
		
		return actions;

	}
}

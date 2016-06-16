package com.mygdx.ia;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Scene;

public class Pathfinding {

	private Scene scene;
	private static PathfindingConfig config;
	private ArrayList<Float> h;
	private ArrayList<Vector2> actions;
	private Vector2 start, goal;
	private Map<Integer, Integer> terrainMap;
	
	public static void setConfig(PathfindingConfig configuration){
		Pathfinding.config = configuration;
	}
	
	/**
	 * Constructor. Necesita un punto origen, un punto destino y
	 * acceso a la información de la escena para poder obtener información
	 * táctica, como el tipo de tile (agua, tierra, bosque, etc), el bot para el que se calcula el pathfinding,
	 * y la configuración para el pathfinding.
	 * @param scene
	 * @param origin
	 * @param destiny
	 */
	public Pathfinding(Scene scene, Bot bot, Vector2 origin, Vector2 destiny, Map<Integer, Integer> terrainMap) {
		
		this.scene = scene;
		this.terrainMap = terrainMap;
		
		// punto de partida y punto de llegada en el grid.
		
		start = scene.fromWorldtoGridCoordinates(origin);
		goal = scene.fromWorldtoGridCoordinates(destiny);
		
		// Si el destino es alcanzable continuamos.
		if( ! isUnreachable(goal)){
			
			// Rellenamos la tabla h, con el valor heurístico de cada posición del grid.
			h = new ArrayList<Float>(scene.getMapHeight()*scene.getMapWidth());
			
			// filas
			for (int i = 0; i < scene.getMapHeight(); i++) {
							
				// columnas
				for (int j = 0; j < scene.getMapWidth(); j++) {
					
					Vector2 pos = new Vector2(j,i);
//					float heuristicValue = Math.abs(pos.x-goal.x)+Math.abs(pos.y-goal.y);
					float heuristicValue = config.calculateHeuristic(bot.getClass(), pos, goal);
					h.add(heuristicValue);
					
				}
				
			}
			
			actions = config.getActions(bot.getClass());
		}
	}
	
	/**
	 * Comprueba si dos posiciones son iguales.
	 * @param a
	 * @param b
	 * @return
	 */
	private  boolean isEqual(Vector2 a, Vector2 b){
		
		if(a == null && b == null)
			return true;
		else if(a == null || b == null)
			return false;
		
		return a.x == b.x && a.y == b.y;
	}
	
	/**
	 * Comprueba si la posición es la meta.
	 * @param pos
	 * @param goal
	 * @return
	 */
	private  boolean isGoal(Vector2 pos){
		return isEqual(pos,goal);
	}
	
	/**
	 * Para saber si el mapa contiene a una posición. Se usa para no salirse del mapa.
	 * @param pos
	 * @return
	 */
	private  boolean mapContains(Vector2 pos){
		return pos.x < scene.getMapWidth() && pos.y < scene.getMapHeight() &&
				pos.x >= 0 && pos.y >= 0;
	}
	
	/**
	 * Comprueba si el destino es inalcanzable. Es decir, si está fuera del mapa o es
	 * intransitable.
	 * @param pos
	 * @return
	 */
	private  boolean isUnreachable(Vector2 pos){
		
		if(!mapContains(pos))
			return true;
		
//		int value = Integer.parseInt((String)scene.getTile(pos).getProperties().get("w"));
		
		int id = scene.getTile(pos).getId();
		int weight = terrainMap.get(id);
				
		return weight == 1000;
	}
	
	/**
	 * Devuelve el coste de ir de la posición current a su sucesor con la acción action.
	 * Es decir, devuelve el coste de la acción.
	 * @param scene
	 * @param current
	 * @param action
	 * @return
	 */
	private  float cost(Scene scene, Vector2 current, Vector2 action){
		
		Vector2 succesor = successor(current,action);
		
		if( ! mapContains(succesor))
			return Float.POSITIVE_INFINITY;
		
//		int weight = Integer.parseInt((String)scene.getTile(succesor).getProperties().get("w"));
		
		int id = scene.getTile(succesor).getId();
		int weight = terrainMap.get(id);
		
		if(weight == 1000)
			return Float.POSITIVE_INFINITY;
		
		return weight;
	}
	
	/**
	 * Devuelve el sucesor de current, aplicando la acción action.
	 * @param current
	 * @param action
	 * @return
	 */
	private  Vector2 successor(Vector2 current, Vector2 action){
		return current.cpy().add(action);
	}
	
	/**
	 * Establece el valor heurístico de pos. Lo guarda en la tabla h.
	 * @param pos
	 * @param value
	 */
	private void setH(Vector2 pos, float value){
		h.set((int)(pos.x + pos.y*scene.getMapWidth()), value);
	}
	
	/**
	 * Devuelve el valor heurístico de pos.
	 * @param pos
	 * @return
	 */
	private  float getH(Vector2 pos){
		return h.get((int)(pos.x + pos.y*scene.getMapWidth()));
//		return Math.abs(pos.x-goal.x)+Math.abs(pos.y-goal.y);
	}
	
	/**
	 * Genera el camino y devuelve un array de puntos.
	 * @return
	 */
	public  List<Vector2> generate(){
		
		ArrayList<Vector2> path = new ArrayList<Vector2>();
		
		// Si el objetivo es inalcanzable ni nos molestamos en seguir.
		if( ! isUnreachable(goal)){
			
			// empezamos desde el punto de partida.
			
			Vector2 current = start;
//			path.add(scene.fromGridToWorldCoordinates(current));
			
			int iterations = 0;
								
			// mientras no lleguemos al punto de llegada.
			while ( ! isGoal(current) && iterations < 500 ) {
				
				iterations++;
												
				// El Local Search Space es solo el estado current. Porque es minimal.
							
				// obtener la accion de minimo coste
				Vector2 a = null;
				float v = Float.POSITIVE_INFINITY;
				for (Vector2 action : actions) {
					
					// si no nos salimos del mapa yendo hacia el sucesor.
					if(mapContains(successor(current, action))){
					
						float aux = cost(scene, current, action) + getH(successor(current, action));
																					
						if( aux < v ){
							v = aux;
							a = action.cpy();
						}
					}
				}
						
				// actualizamos h de current.
				setH(current,Math.max(getH(current),cost(scene, current, a) + getH(successor(current, a))));
				
				// pasamos al sucesor.
				// current = sucesor de current con la acción a.
				current = successor(current, a);
				path.add(scene.fromGridToWorldCoordinates(current));

			}
		}
		
		return path;
	}
}

package com.mygdx.ia;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Scene;
import com.mygdx.engine.UI;

/*
 * Esta clase representa un camino de puntos.
 */
public class Path {

	private ArrayList<Vector2> params;
	private ArrayList<Boolean> visited;
	private float radius; // Radio de llegada a cada punto
	private boolean finished;
	Color team = null;

	public Path(float radius) {
		this.params = new ArrayList<Vector2>();
		this.visited = new ArrayList<Boolean>();
		this.radius = radius;
		this.finished = false;
	}

	public boolean isFinished(){
		return finished;
	}
	
	public List<Vector2> getParams() {
		return params;
	}

	public void setParams(List<Vector2> params) {
		this.params = (ArrayList<Vector2>) params;
		for (int i = 0; i < params.size(); i++) {
			this.visited.add(false);
		}
	}
	
	public void addParam(Vector2 param){
		this.params.add(param);
		this.visited.add(false);
	}
	
	/*
	 * Funcion para calcular el punto mas proximo a la posicion actual del personaje.
	 */
	public int getParam(Vector2 position ,int lastParam){
		
		float distance = Float.MAX_VALUE; // variable para almacenar la distancia. Empieza siendo infinito
		
		for (int i = 0; i < this.params.size(); i++) {
			
			/*
			 * Comprobamos si hemos llegado al punto.
			 * Si hemos llegado lo marcamos como VISITADO.
			 */
			if(this.params.get(i).dst(position) < radius)
				this.visited.set(i, true);
			
			/* 
			 * si encontramos un punto mas cercano, y no visitado, se pone como el nuevo seleccionado
			 */
			if((! this.visited.get(i)) && (this.params.get(i).dst(position) < distance)){
				distance = this.params.get(i).dst(position);
				return i;
			}
		}
		
		this.finished = true;
		
		return -1;
	}
	
	public Vector2 getPosition(int param){
		return this.params.get(param);
	}
	
	/**
	 * Para comprobar si el path no tiene puntos.
	 * @return
	 */
	public boolean isEmpty(){
		return this.params.isEmpty();
	}
	
	
	public void setTeam(int i){
		if (i==0){
			team=Color.BLUE;
		} else{
			team=Color.RED;
		}
	}
	
	/*
	 * Funcion para renderizar el camino.
	 * Usa la clase UI.
	 */
	public void render(){
		
		if (team == null){
			team=Color.RED;
		}
		
		if( ! this.params.isEmpty()){
			UI.getInstance().drawCircleWorld(this.params.get(0), 2,Color.GREEN,false);
			
			Vector2 last = this.params.get(0);
			
			for (int i = 1; i < this.params.size(); i++) {
				UI.getInstance().drawLineWorld(last, this.params.get(i), team,false);
				last = this.params.get(i);
			}
			
			UI.getInstance().drawCircleWorld(this.params.get(this.params.size()-1), 2,Color.BLUE,false);
		}
	}
}

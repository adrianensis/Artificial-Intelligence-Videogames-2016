package com.mygdx.engine;

/**
 * Los GameObjects estam compuestos por Components.
 * Cada Component se encarga de un ambito diferente: graficos, fisica, logica, camara, etc...
 *
 */
public abstract class Component {
	
	protected GameObject gameObject;
	
	public Component() {
		this.gameObject = null;
	}

	/**
	 * Actualiza el componente. Se ejecuta cada iteración del motor.
	 */
	public abstract void update();
	
	/**
	 * Devuelve una referencia al gameObject en el que está contenido el componente.
	 * @return
	 */
	public GameObject getGameObject() {
		return gameObject;
	}

	/**
	 * Establece el gameObject al que pertenece el componente.
	 * @param gameObject
	 */
	public void setGameObject(GameObject gameObject) {
		this.gameObject = gameObject;
	}
}

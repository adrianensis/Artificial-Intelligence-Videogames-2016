package com.mygdx.engine;

/**
 * 
 * Este va ser el corazon de nuestra aplicacion.
 *
 */
public class Engine {
	private static Engine instance = null;
	private Scene currentScene;
	
	private Engine() {
		this.currentScene = null;
	}
	
	/**
	 * Inicializa la escena actual.
	 */
	public void start(){
		this.currentScene.start();
	}
	
	/**
	 * Actualiza la escena actual.
	 */
	public void update(){
		this.currentScene.update();
	}
	
	/**
	 * Dibuja la escena actual.
	 */
	public void render(){
		this.currentScene.render();
	}
	
	public Scene getCurrentScene() {
		return currentScene;
	}

	public void setCurrentScene(Scene currentScene) {
		this.currentScene = currentScene;
	}
	
	// SINGLETON
	public static Engine getInstance(){
		if(instance == null)
			instance = new Engine();
		return instance;
	}
	
	
}

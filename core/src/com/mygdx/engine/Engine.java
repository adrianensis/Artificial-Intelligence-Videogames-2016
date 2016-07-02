package com.mygdx.engine;

import java.util.LinkedList;

/**
 * 
 * Este va ser el corazon de nuestra aplicacion.
 *
 */
public class Engine {
	private static Engine instance = null;
	private Scene currentScene;
	private LinkedList<Scene> scenes;
	
	private Engine() {
		this.currentScene = null;
		this.scenes= new LinkedList<Scene>();
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
		if(!scenes.contains(currentScene)){
			scenes.add(currentScene);
		}
	}
	
	public void addScene(Scene scene){
		scenes.add(scene);
	}
	
	public Scene getScene(String sceneName){
		for (Scene scene : scenes) {
			if(scene.getSceneName().equals(sceneName))
				return scene;
		}
		return null;
	}
	
	public void changeScene(String sceneName){
		for (Scene scene : scenes) {
			if(scene.getSceneName().equals(sceneName))
				this.setCurrentScene(scene);
		}
	}
	
	// SINGLETON
	public static Engine getInstance(){
		if(instance == null)
			instance = new Engine();
		return instance;
	}
	
	
}

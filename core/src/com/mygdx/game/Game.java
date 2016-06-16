package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Camera;
import com.mygdx.engine.Engine;
import com.mygdx.engine.GameObject;
import com.mygdx.engine.Scene;
import com.mygdx.engine.Script;
import com.mygdx.engine.Transform;
import com.mygdx.game.units.HeavyUnit;
import com.mygdx.game.units.LightUnit;
import com.mygdx.game.units.Unit;

public class Game extends ApplicationAdapter {
		
	@Override
	public void create () {
		
		// Creamos una escena con un mapa.
		Engine.getInstance().setCurrentScene(new Scene("map.tmx"));
		
//		createPlayer("bot6", new Vector2(10f*Scene.SCALE,70f*Scene.SCALE));
		
		// Creamos Bots 
		createBot("bot0", new Vector2(9*Scene.SCALE,73*Scene.SCALE), 1);
		createBot("bot1", new Vector2(5*Scene.SCALE,70*Scene.SCALE), 0);
		createBot("bot2", new Vector2(6*Scene.SCALE,66*Scene.SCALE), 1);
		createBot("bot3", new Vector2(8*Scene.SCALE,60*Scene.SCALE), 1);
		createBot("bot4", new Vector2(5*Scene.SCALE,62*Scene.SCALE), 1);
		createBot("bot5", new Vector2(12*Scene.SCALE,65*Scene.SCALE), 0);

		
		// Creamos una camara para la escena, llamada cam.
		createCam("cam");
		
		createController("controller");
		
		// Inicializamos
		Engine.getInstance().start();
	}

	@Override
	public void render () {
		Engine.getInstance().update();
		Engine.getInstance().render();
	}
	
	/**
	 * 
	 * 
	 * FUNCIONES UTILES PARA CREAR OBJETOS DEL JUEGO DE FORMA RAPIDA.
	 * 
	 * 
	 */
	
	/**
	 * Funcion para crear un jugador.
	 * @param key
	 * @param textureName
	 */
	public void createPlayer(String key, Vector2 pos){
		
		Unit bot = new HeavyUnit(key, 0, "blue2.png", pos);

		// Lo aÃ±adimos a la escena.
		Engine.getInstance().getCurrentScene().addObject(key,bot);
	}
	
	/**
	 * Funcion para crear una camara.
	 * @param key
	 */
	public void createCam(String key){
			
		GameObject obj = new GameObject(key);
		obj.addComponent(new Transform());
		Camera cam = new Camera();
		obj.addComponent(cam);
		
		// Logica de la camara
		obj.addComponent(new Script() {
			
			@Override
			public void update() {
				
			}

			@Override
			public void start() {
				
				Scene currentScene = Engine.getInstance().getCurrentScene();
				//TODO LIMITES DE LA CAMARA
				this.gameObject.getComponent(Transform.class).setLimits(
						0, 
						currentScene.getMapWidth()*currentScene.getTilePixelWidth(), 
						0,
						currentScene.getMapHeight()*currentScene.getTilePixelHeight());
				
				// Buscamos al jugador.
//				GameObject target = currentScene.find("player");
				
				// Convertimos al jugador en nuestro target.
//				this.gameObject.getComponent(Camera.class).setTarget(target);
				
//				this.gameObject.getComponent(Transform.class).position.x = (currentScene.getMapWidth()/2)*currentScene.getTilePixelWidth();
//				this.gameObject.getComponent(Transform.class).position.y = (currentScene.getMapHeight()/2)*currentScene.getTilePixelHeight(); // Lo situamos en el centro.
			}
		});
		
		Engine.getInstance().getCurrentScene().setCamera(obj); // Seleccionamos esta camara como la camara a usar en nuestra escena.
		Engine.getInstance().getCurrentScene().addObject(key,obj); // AÃ±adimos la camara la primera, para que se actualize antes que los sprites.
	}
	
	/**
	 * Crea un controlador del juego.
	 * @param key
	 */
	public void createController(String key){
		
		GameObject obj = new GameObject(key);
		
		// Logica del controlador
		obj.addComponent(new GameController());
		
		Engine.getInstance().getCurrentScene().addObject(key,obj);
	}
		
	/**
	 * Funcion para crear un bot.
	 * @param key
	 */
	public void createBot(String key, Vector2 pos, int team){
		
		String textureName = "blue1.png";
		if(team == 1)
			textureName = "red1.png";
		
		LightUnit bot = new LightUnit(key, team, textureName,pos);
//		bot.getComponent(BotScript.class).addBehaviour(new WanderU(bot.getComponent(BotScript.class)));
		
//		bot.getComponent(BotScript.class).addBehaviour(new Persue(bot.getComponent(BotScript.class), Engine.getInstance().getCurrentScene().find("player").getComponent(BotScript.class), 5f));
		
	
		Engine.getInstance().getCurrentScene().addObject(key,bot);
	}
}

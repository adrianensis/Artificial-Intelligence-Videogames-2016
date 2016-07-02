package com.mygdx.game;

import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.engine.Camera;
import com.mygdx.engine.Engine;
import com.mygdx.engine.GameObject;
import com.mygdx.engine.Scene;
import com.mygdx.engine.Script;
import com.mygdx.engine.Transform;
import com.mygdx.engine.UI;
import com.mygdx.game.units.HeavyUnit;
import com.mygdx.game.units.LightUnit;
import com.mygdx.game.units.PhantomUnit;
import com.mygdx.game.units.SpawnBaseUnit;
import com.mygdx.game.units.TestUnit;
import com.mygdx.game.units.Unit;
import com.mygdx.ia.Bot;
import com.mygdx.ia.BotScript;
import com.mygdx.ia.behaviours.basic.WanderU;

public class Game extends ApplicationAdapter {
		
	@Override
	public void create () {
		
		// Creamos una escena con un mapa.
		Engine.getInstance().setCurrentScene(new Scene("map.tmx","game"));
		Engine.getInstance().addScene(new Scene("map.tmx","test"));
//		createPlayer("bot00", new Vector2(13*Scene.SCALE,74*Scene.SCALE));
		
		// Creamos Bots 
//		createBot("bot0", new Vector2(9*Scene.SCALE,73*Scene.SCALE), 1);
//		createBot("bot1", new Vector2(5*Scene.SCALE,70*Scene.SCALE), 0);
//		createBot("bot2", new Vector2(6*Scene.SCALE,66*Scene.SCALE), 1);
//		createBot("bot3", new Vector2(8*Scene.SCALE,60*Scene.SCALE), 1);
//		createBot("bot4", new Vector2(5*Scene.SCALE,62*Scene.SCALE), 1);
//		createBot("bot5", new Vector2(12*Scene.SCALE,65*Scene.SCALE), 0);
//		createPhantom("bot6", new Vector2(11*Scene.SCALE,62*Scene.SCALE), 1);
//		createPhantom("bot7", new Vector2(15*Scene.SCALE,70*Scene.SCALE), 0);
		prepareGame();
		prepareTest();
		
		// Inicializamos
		Engine.getInstance().start();
	}

	@Override
	public void render () {
		Engine.getInstance().update();
		Engine.getInstance().render();
	}
	
	
	public void prepareGame() {
		createBase("base0", new Vector2(10*Scene.SCALE,70*Scene.SCALE), 0);
		createBase("base1", new Vector2(70*Scene.SCALE,22*Scene.SCALE), 1);
		// Creamos una camara para la escena, llamada cam.
		createCam("cam","game");
		createController("controller","game");
		createMiniMap("map");
	}

	public void prepareTest(){
		createTestBot();
		createCam("cam","test");
		createController("controller","test");
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
		

		Unit bot = new HeavyUnit(key, 0, "blue2.png", pos, Config.getInstacia().getUnitMap(HeavyUnit.class));

		// Lo aÃ±adimos a la escena.
		Engine.getInstance().getCurrentScene().addObject(bot);
	}
	
	/**
	 * Funcion para crear una camara.
	 * @param key
	 */
	public void createCam(String key,final String sceneName){
			
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
				
				Scene currentScene = Engine.getInstance().getScene(sceneName);
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
				
				this.gameObject.getComponent(Transform.class).position.x = (currentScene.getMapWidth()/2)*currentScene.getTilePixelWidth();
				this.gameObject.getComponent(Transform.class).position.y = (currentScene.getMapHeight()/2)*currentScene.getTilePixelHeight(); // Lo situamos en el centro.
			}
		});
		
		Engine.getInstance().getScene(sceneName).setCamera(obj); // Seleccionamos esta camara como la camara a usar en nuestra escena.
		Engine.getInstance().getScene(sceneName).addObject(obj); // AÃ±adimos la camara la primera, para que se actualize antes que los sprites.
	}
	
	/**
	 * Crea un controlador del juego.
	 * @param key
	 */
	public void createController(String key,String sceneName){
		
		GameObject obj = new GameObject(key);
		
		// Logica del controlador
		obj.addComponent(new GameController());
		
		Engine.getInstance().getScene(sceneName).addObject(obj);
	}
	
	public void createTestBot(){
		
		String textureName = "yoshi.png";
		Vector2 pos =new Vector2(10*Scene.SCALE,70*Scene.SCALE);
		Unit bot = new TestUnit("testUnit", 0, textureName, pos, Config.getInstacia().getUnitMap(LightUnit.class));
//		bot.getComponent(BotScript.class).addBehaviour(new Persue(bot.getComponent(BotScript.class), Engine.getInstance().getCurrentScene().find("player").getComponent(BotScript.class), 5f));
		
	
		Engine.getInstance().getScene("test").addObject(bot);
	}
		
	
	/**
	 * Funcion para crear una unidad phantom.
	 * @param key
	 */
	public void createPhantom(String key, Vector2 pos, int team){
		
		String textureName = "blue3.png";
		if(team == 1)
			textureName = "red3.png";
		
		PhantomUnit bot = new PhantomUnit(key, team, textureName,pos,Config.getInstacia().getUnitMap(PhantomUnit.class));
//		bot.getComponent(BotScript.class).addBehaviour(new WanderU(bot.getComponent(BotScript.class)));
		
//		bot.getComponent(BotScript.class).addBehaviour(new Persue(bot.getComponent(BotScript.class), Engine.getInstance().getCurrentScene().find("player").getComponent(BotScript.class), 5f));
		
	
		Engine.getInstance().getCurrentScene().addObject(bot);
	}
	/**
	 * Funcion para crear un bot.
	 * @param key
	 */
	public void createBot(String key, Vector2 pos, int team){
		
		String textureName = "blue1.png";
		if(team == 1)
			textureName = "red1.png";
		
		LightUnit bot = new LightUnit(key, team, textureName,pos,Config.getInstacia().getUnitMap(LightUnit.class));
//		bot.getComponent(BotScript.class).addBehaviour(new WanderU(bot.getComponent(BotScript.class)));
		
//		bot.getComponent(BotScript.class).addBehaviour(new Persue(bot.getComponent(BotScript.class), Engine.getInstance().getCurrentScene().find("player").getComponent(BotScript.class), 5f));
		
	
		Engine.getInstance().getCurrentScene().addObject(bot);
	}
	
	public void createBase(String key, Vector2 pos, int team){
		
		SpawnBaseUnit base = new SpawnBaseUnit(key, team, "base0.png", pos, 20);
		Engine.getInstance().getCurrentScene().addObject(base);
		
		Waypoints.getInstance().setBase(team, pos);
	}
	
	public void createMiniMap(String key){
		
		GameObject obj = new GameObject(key);
		
		obj.addComponent(new Transform());
		obj.addComponent(new Script() {
			
			private Vector2 center;
			private float map_w, map_h, screen_w, screen_h;
			
			@Override
			public void update() {
				
				List<Unit> units = Engine.getInstance().getCurrentScene().getGameObjectsWithClass(Unit.class);
				
				UI.getInstance().drawRectangle(center, map_w, map_h, new Color(0,0,0,0.5f), true);
				UI.getInstance().drawRectangle(center, map_w, map_h, Color.RED, false);
				Vector2 cam_pos = Engine.getInstance().getCurrentScene().find("cam").getComponent(Transform.class).position;
				Vector2 map_cam_pos = new Vector2((cam_pos.x*map_w/2)/screen_w, (cam_pos.y*map_h/2)/screen_h);
				UI.getInstance().drawRectangle(map_cam_pos.cpy().add(center).sub(map_w/2, map_h/2), 40,40, Color.GREEN, false);
	
				for (Unit unit : units) {
					Transform t = unit.getComponent(Transform.class);
					Vector2 pos = t.position;
					
//					System.out.println("pos: "+pos);
					
					Vector2 map_pos = new Vector2((pos.x*map_w/2)/screen_w, (pos.y*map_h/2)/screen_h);
					
//					System.out.println("map_pos: "+map_pos);
					
					Color c = Color.RED;
					if(unit.getTeam() == 0)
						c = Color.BLUE;
					
					UI.getInstance().drawCircle(map_pos.cpy().add(center).sub(map_w/2, map_h/2), 2, c, true);
					
					if(unit instanceof SpawnBaseUnit)
						UI.getInstance().drawCircle(map_pos.cpy().add(center).sub(map_w/2, map_h/2), 4, Color.YELLOW, false);
				}
				
//				System.out.println("\n\n####################");
				
			}
			
			@Override
			public void start() {
				
//				OrthographicCamera orthoCamera = Engine.getInstance().getCurrentScene().getCamera();
			
				screen_h = Gdx.graphics.getHeight();
				screen_w = Gdx.graphics.getWidth();
				
				float ratio = 80;
				
				map_h = (screen_w/ratio)*Scene.SCALE;
				map_w = (screen_h/ratio)*Scene.SCALE;
				
				center = new Vector2(screen_w-map_w/2,0+map_h/2);
		
			}
		});
		
		
		Engine.getInstance().getCurrentScene().addObject(obj);
	}
	

}

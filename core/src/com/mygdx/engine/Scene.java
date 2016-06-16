package com.mygdx.engine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Game;

/**
 * Esta clase es la escena que contendra todos nuestros objetos.
 */
public class Scene {
	
	public static int SCALE;
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	private World world;
	
	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer tiledMapRenderer;
	
	private int mapWidth;
	private int mapHeight;
	private int tilePixelWidth;
	private int tilePixelHeight;
	
	Box2DDebugRenderer debugRenderer;
	Matrix4 debugMatrix;
	
	private HashMap<String,GameObject> objects; // Los objetos van indexados por una clave para que sea facil recuperarlos.
	
	public Scene(String mapPath) {
		this.camera = null;
		this.batch = new SpriteBatch();
		
		TmxMapLoader loader = new TmxMapLoader();
		this.tiledMap = loader.load(mapPath);
		this.tiledMapRenderer =  new OrthogonalTiledMapRenderer(tiledMap, 1);
		
//		TiledMapTileLayer x = ((TiledMapTileLayer) tiledMap.getLayers().get(0));
//		System.out.println(x.getCell(0, 0).getTile().getId());
		
		MapProperties prop = tiledMap.getProperties();
		this.mapWidth = prop.get("width", Integer.class);
		this.mapHeight = prop.get("height", Integer.class);
		this.tilePixelWidth = prop.get("tilewidth", Integer.class);
		this.tilePixelHeight = prop.get("tileheight", Integer.class);
		
		SCALE = tilePixelHeight;
		
		this.objects = new HashMap<String, GameObject>();
		
		//el engine contendra el World de Box2d
		this.world = new World(new Vector2(0,0), false);
		
	}
	
	public World getWorld() {
		return world;
	}
	
	/**
	 * Busca un objeto de la escena, por clave (nombre)
	 * @param key
	 * @return
	 */
	public GameObject find(String key){
		return this.objects.get(key);
	}
	

	public LinkedList<GameObject> getGameObjects() {
		return new LinkedList<GameObject>( objects.values());
	}
	
	public <T extends GameObject> List<T> getGameObjectsWithClass(Class<T> c) {
		
		List<GameObject> gameObjects = new LinkedList<GameObject>(objects.values()) ;
		
		List<T> gameObjectsWithClass = new LinkedList<T>();
		
		for (GameObject go : gameObjects) {
			if(c.isInstance(go))
				gameObjectsWithClass.add((T) go);
		}
	
		return gameObjectsWithClass;
	}

	/**
	 * Añade un objeto a la escena.
	 * @param key
	 * @param object
	 * @return
	 */
	public Scene addObject(String key, GameObject object) {
		this.objects.put(key, object);
		return this;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public Scene setCamera(GameObject cameraObj) {
		this.camera = cameraObj.getComponent(Camera.class).camera;
		
		//Create a copy of camera projection matrix
	    debugMatrix=new Matrix4(this.camera.combined);
	 
		//Scale it by 100 as our box physics bodies are scaled down by 100
		debugMatrix.scale(100f, 100f, 1f);
		 
		debugRenderer=new Box2DDebugRenderer();
		
		return this;
	}

	/**
	 * Devuelve la anchura del mapa en tiles.
	 * @return
	 */
	public int getMapWidth() {
		return mapWidth;
	}

	/**
	 * Devuelve la altura del mapa en tiles.
	 * @return
	 */
	public int getMapHeight() {
		return mapHeight;
	}

	/**
	 * Devuelve la anchura de un tile en pixeles.
	 * @return
	 */
	public int getTilePixelWidth() {
		return tilePixelWidth;
	}

	/**
	 * Devuelve la altura de un tile en pixeles.
	 * @return
	 */
	public int getTilePixelHeight() {
		return tilePixelHeight;
	}
	
	/**
	 * Convierte un punto, de coordenadas del grid, en coordenadas reales.
	 * @param gridCoordinates
	 * @return
	 */
	public Vector2 fromGridToWorldCoordinates(Vector2 gridCoordinates){
			
		float x = gridCoordinates.x;
		float y = gridCoordinates.y;
		
		float worldX = getTilePixelWidth()*x + (getTilePixelWidth()/2);
		float worldY = getTilePixelHeight()*y + (getTilePixelHeight()/2);
		
		return new Vector2(worldX,worldY);
	}

	/**
	 * Convierte un punto de coordenadas reales, a coordenadas del grid.
	 * @param worldCoordinates
	 * @return
	 */
	public Vector2 fromWorldtoGridCoordinates(Vector2 worldCoordinates){
		
		float x = worldCoordinates.x;
		float y = worldCoordinates.y;
		
		float width = getMapWidth()*getTilePixelWidth(); // width in pixels
		float height = getMapHeight()*getTilePixelHeight(); // height in pixels
		
		// the tile coordinates
		int mapX = (int)((x/width)*getMapWidth());
		int mapY = (int)((y/height)*getMapHeight());
		
		return new Vector2(mapX,mapY);
	}
	
	/**
	 * Devuleve un Tile, a partir de sus coordenadas en el grid.
	 * @param gridCoordinates
	 * @return
	 */
	public TiledMapTile getTile(Vector2 gridCoordinates){
		
		TiledMapTileLayer layer = ((TiledMapTileLayer) tiledMap.getLayers().get(0));
		
		TiledMapTile tile = layer.getCell((int)gridCoordinates.x, (int)gridCoordinates.y).getTile();
		
		return tile;
	}

	/**
	 * INICIALIZA LA ESCENA
	 */
	public void start(){
		
       for (GameObject gameObject : objects.values()){
    	   // Script
    	   List<Script> scripts = gameObject.getComponents(Script.class);
    	   if(!scripts.isEmpty()){
    		   for (Script script : scripts) {
    			   script.start();
			}
    	   }
    	   
    	   // Collider
    	   Collider col = gameObject.getComponent(Collider.class);
    	   if(col != null)
    		   col.start(this.world);
       }

	}
	
	/**
	 * ACTUALIZA LA ESCENA
	 */
	public void update(){
		UI.getInstance().setCamera(this.camera); // Pasamos la matriz de proyección a la clase UI

		// Actualizamos cada objeto de la escena
		for (GameObject gameObject : objects.values()){
			gameObject.update();
      	}
       
		// Actualizamos motor físico
		world.step(Gdx.graphics.getDeltaTime(), 6,2);
		world.clearForces();
	}
	
	/**
	 * DIBUJA LA ESCENA
	 */
	public void render(){
		
		// Limpiamos la pantalla.
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		 // Se dibuja el mapa.
        this.tiledMapRenderer.setView(this.camera);
        this.tiledMapRenderer.render();
        
        this.batch.setProjectionMatrix(this.camera.combined);
               
        // Se dibujan los sprites.
        this.batch.begin();
        for (GameObject gameObject : objects.values()){
        	Renderer renderer = gameObject.getComponent(Renderer.class);
			if(renderer != null){
				renderer.render(this.batch);
			}	
        }
        this.batch.end();
        
        /*
         * DIBUJAMOS LOS COMANDOS QUE QUEDAN
         */
        
        this.batch.begin();
        UI.getInstance().renderCommands();
        this.batch.end();
		
		/*
		 * POR ULTIMO DIBUJAMOS TEXTO
		 */
		UI.getInstance().renderTextCommands();
        
//		debugRenderer.render(world, this.camera.combined);
	}
}

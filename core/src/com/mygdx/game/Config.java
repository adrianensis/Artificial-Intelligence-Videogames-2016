package com.mygdx.game;

import java.util.HashMap;
import java.util.Map;

import com.mygdx.game.units.HeavyUnit;
import com.mygdx.game.units.LightUnit;
import com.mygdx.game.units.PhantomUnit;

public class Config {
	private static final Config instancia = new Config();
	
	/*
	 * TABLAS
	 */
	
	// mapa de unidad a tabla de terreno
	private HashMap<Class, Map<Integer, Integer>> map;
	// mapa de unidad a entero
	private HashMap<Class, Integer> unitToInt;
	// mapa de tileID a tabla
	private HashMap<Integer, Integer> tileToInt;
	// 			LightUnit HeavyUnit PhantomUnit
	// LightUnit 1 			0.5 		2
	// HeavyUnit 1.5 		1   		1.5
	// PhantomUnit 1.25	    1.5			1
	private float[][] FAD = { { 1, 0.5f,2 }, { 1.5f, 1,1.5f },{1.25f,1.5f,1} };
	// 			LightUnit HeavyUnit PhantomUnit
	// TileGris 	1 		2			2
	// TileVerde 	1.5 	0.5			2
	private float[][] FTA = { { 1, 2,2 }, { 1.5f, 0.5f,2 } };
	// 			LightUnit HeavyUnit PhantomUnit
	// TileGris 0.5 			1		1
	// TileVerde 1			 	1.5		1
	private float[][] FTD = { { 0.5f, 1,1 }, { 1, 1.5f,1 } };

	private Config() {
		createUnitToInt();
		createTileToInt();
		createUnitMoveMap();
	}

	private void createUnitToInt() {
		this.unitToInt = new HashMap<Class, Integer>();
		unitToInt.put(LightUnit.class, 0);
		unitToInt.put(HeavyUnit.class, 1);
		unitToInt.put(PhantomUnit.class, 2);
	}

	private void createTileToInt() {
		this.tileToInt = new HashMap<Integer, Integer>();
		// tile gris
		tileToInt.put(178, 0);
		// tile verde
		tileToInt.put(6, 1);
	}

	private void createUnitMoveMap() {
		
		this.map = new HashMap<Class, Map<Integer, Integer>>();
		
		HashMap<Integer, Integer> mapLightUnit = new HashMap<Integer, Integer>();
		
		mapLightUnit.put(7, 1000); // montaña
		mapLightUnit.put(6, 1); // cesped
		mapLightUnit.put(178, 1); // losa gris
		mapLightUnit.put(1261, 2); // desierto
		mapLightUnit.put(1, 1000); // agua
		mapLightUnit.put(403, 1); // flores
		mapLightUnit.put(1087, 1); // naranja
		mapLightUnit.put(1258, 1); // lila
		mapLightUnit.put(530, 1000); // pino
		mapLightUnit.put(529, 1); // arbol redondo azul
		mapLightUnit.put(527, 2); // arbol redondo verde
		
		
		this.map.put(LightUnit.class, mapLightUnit);
		
		HashMap<Integer, Integer> mapHeavyUnit = new HashMap<Integer, Integer>();
		
		mapHeavyUnit.put(7, 1000); // montaña
		mapHeavyUnit.put(6, 1); // cesped
		mapHeavyUnit.put(178, 1); // losa gris
		mapHeavyUnit.put(1261, 2); // desierto
		mapHeavyUnit.put(1, 1000); // agua
		mapHeavyUnit.put(403, 1); // flores
		mapHeavyUnit.put(1087, 1); // naranja
		mapHeavyUnit.put(1258, 1); // lila
		mapHeavyUnit.put(530, 1000); // pino
		mapHeavyUnit.put(529, 1000); // arbol redondo azul
		mapHeavyUnit.put(527, 1000); // arbol redondo verde
		
		
		this.map.put(HeavyUnit.class, mapHeavyUnit);
		
		HashMap<Integer, Integer> mapPhantomUnit = new HashMap<Integer, Integer>();
		
		mapPhantomUnit.put(7, 1000); // montaña
		mapPhantomUnit.put(6, 1); // cesped
		mapPhantomUnit.put(178, 1); // losa gris
		mapPhantomUnit.put(1261, 2); // desierto
		mapPhantomUnit.put(1, 1000); // agua
		mapPhantomUnit.put(403, 1); // flores
		mapPhantomUnit.put(1087, 1); // naranja
		mapPhantomUnit.put(1258, 1); // lila
		mapPhantomUnit.put(530, 1000); // pino
		mapPhantomUnit.put(529, 1000); // arbol redondo azul
		mapPhantomUnit.put(527, 1000); // arbol redondo verde
		
		this.map.put(PhantomUnit.class, mapPhantomUnit);
	}

	public static Config getInstacia() {
		return instancia;
	}

	public Map<Integer, Integer> getUnitMap(Class unitclass) {
		return map.get(unitclass);
	}

	public float getFAD(Class unit, Class unit2) {
		if (unitToInt.containsKey(unit) && unitToInt.containsKey(unit2)) {
			return FAD[unitToInt.get(unit)][unitToInt.get(unit2)];
		}
		return 1;
	}

	public float getFTA(int tileid, Class unit) {
		if (tileToInt.containsKey(tileid) && unitToInt.containsKey(unit)) {
			return FTA[tileToInt.get(tileid)][unitToInt.get(unit)];
		}
		return 1;
	}

	public float getFTD(int tileid, Class unit) {
		if (tileToInt.containsKey(tileid) && unitToInt.containsKey(unit)) {
			return FTD[tileToInt.get(tileid)][unitToInt.get(unit)];
		}
		return 1;
	}
}

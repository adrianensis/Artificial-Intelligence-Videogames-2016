package com.mygdx.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

public class JsonLoader {
	private Gson gson;
	private String path;

	public JsonLoader(String p) {
		this.gson = new Gson();
		this.path = p;
	}

	private String loadJson(String json) {
		String s = "";
		FileReader f = null;
		try {
			f = new FileReader(path+json);
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.getStackTrace();
		}
		BufferedReader b = new BufferedReader(f);
		try {
			while ((s = b.readLine()) != null) {}
			b.close();
		} catch (IOException e) {
			System.out.println("Error IO");
			e.printStackTrace();
		}
		return s;
	}
	
	private void saveJson(InfoTable t){
		String ruta = path+"infotable.json";
		File archivo = new File(ruta);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(archivo));
		} catch (IOException e) {
			System.out.println("Error IO");
			e.printStackTrace();
		}
	    try {
			bw.write(gson.toJson(t));
			bw.close();
		} catch (IOException e) {
			System.out.println("Error IO");
			e.printStackTrace();
		}
	    
	}
	
	public void saveTable(InfoTable t){
		saveJson(t);
	}
	
	public InfoTable getTable(String json){
		String content = loadJson(json);
		InfoTable table = gson.fromJson(content, InfoTable.class);
		return table;
	}
}

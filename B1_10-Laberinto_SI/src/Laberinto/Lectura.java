package Laberinto;

import java.io.FileReader;
import com.google.gson.*;

/*public class Lectura {

	
	public static Grid leerJSON(String file) throws java.io.IOException {
		Grid grid = new Grid();
        @SuppressWarnings("deprecation")
		JsonParser parser = new JsonParser();
        FileReader fr = new FileReader(file);
        @SuppressWarnings("deprecation")
		JsonElement datos = parser.parse(fr);
        dumpJSONElement(datos, grid);
        return grid;
    }	
	
	public static void dumpJSONElement(JsonElement elemento, Grid grid) {
	        JsonObject obj = elemento.getAsJsonObject();
	        java.util.Set<java.util.Map.Entry<String,JsonElement>> entradas = obj.entrySet();
	        java.util.Iterator<java.util.Map.Entry<String,JsonElement>> iter = entradas.iterator();
	        while (iter.hasNext()) {
	            java.util.Map.Entry<String,JsonElement> entrada = iter.next();
	            dumpJSONKey(entrada.getKey(), entrada.getValue(), grid);
	        }
	}
	
	public static void dumpJSONKey(String key, JsonElement entrada, Grid grid){
		switch(key) {
			case "rows":
				grid.setRows(entrada.getAsInt());
				break;
			case "cols":
				grid.setCols(entrada.getAsInt());
				break;
			case "max_n":
				grid.setMax_n(entrada.getAsInt());
			case "mov":
				grid.setMov(dumpJSONArray(entrada));
				break;
			case "id_mov":
				grid.setId_mov(dumpJSONStringArray(entrada));
				break;
			case "cells":
				grid.setCellsGrid(dumpJSONCellsArray(entrada));
				break;
		}
				
	}

	public static int[][] dumpJSONArray(JsonElement elemento){
		JsonArray array = elemento.getAsJsonArray();
		int[][] matriz = new int[array.size()][array.size()];
        java.util.Iterator<JsonElement> iter = array.iterator();
//		System.out.println("ARRAY: \t"+ array.toString());
        for(int i = 0; i < array.size() & iter.hasNext(); i++) {
            JsonElement entrada = iter.next();
            java.util.Iterator<JsonElement> iter2 = entrada.getAsJsonArray().iterator();
            for(int j = 0; j < array.size() & iter2.hasNext();j++) 
            	matriz[i][j] = Integer.parseInt(iter2.next().getAsJsonPrimitive().getAsString());
        }
        return matriz;
	}
	
	public static String[] dumpJSONStringArray (JsonElement elemento){
		JsonArray array = elemento.getAsJsonArray();
		String[] vector = new String[array.size()];
        java.util.Iterator<JsonElement> iter = array.iterator();
//		System.out.println("ARRAY: \t"+ array.toString());
        for(int i = 0; i < array.size() & iter.hasNext(); i++) {
        	vector[i] = iter.next().getAsJsonPrimitive().getAsString();
        }
        return vector;
	}
	
	public static Cell[][] dumpJSONCellsArray (JsonElement elemento){
		JsonArray array = elemento.getAsJsonArray();
		Cell[][] vector = new Cell[array.size()];
        java.util.Iterator<JsonElement> iter = array.iterator();
//		System.out.println("ARRAY: \t"+ array.toString());
        for(int i = 0; i < array.size() & iter.hasNext(); i++) {
        	vector[i] = iter.next().getAsJsonPrimitive().getAsString();
        }
        return vector;
	}
}*/
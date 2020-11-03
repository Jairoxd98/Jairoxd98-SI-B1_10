package Laberinto;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
    	
        menu(); /* Llamada al menu del programa */
        
    }
    
    public static void menu() {
    	Scanner sc = new Scanner(System.in);

        boolean salir = false;

        Grid grid = null;
        while (!salir) {

            try {
                System.out.println("Elige una opcion: ");
                System.out.println("1. Generar laberinto");
                System.out.println("2. Exportar imagen");
                System.out.println("3. Importar laberinto");
                System.out.println("4. Hito2");
                System.out.println("5. Salir");

                int option = sc.nextInt();
                switch (option) {
                    case 1: /*Opcion para generar el laberinto mediante los paremetros pedidos por teclado*/
                        System.out.println("Elige el numero de filas");
                        int filas = sc.nextInt();
                        while(filas<=0){
                        	System.out.println("NÃºmero de filas incorrecto, introduce un nÃºmero correcto\n");
                        	filas = sc.nextInt();
                        }
                        System.out.println("Elige el numero de columnas");
                        int columnas = sc.nextInt();
                    	while(columnas<=0) {
                    		System.out.println("NÃºmero de columnas incorrecto, introduce un nÃºmero correcto\n");
                    		filas = sc.nextInt();
                    		
                    	}

                        grid = new Grid(filas, columnas);
                        grid.generateMaze(); //Genera el laberinto
                        grid.generateJSON(); //Genera el JSON del laberinto
                        System.out.println("Laberinto creado correctamente, JSON creado correctamente");

                        break;
                    case 2: /*Opcion para generar la imagen del laberinto creado o exportado por el usuario*/
                        if (grid != null) {
                            grid.exportToIMG();
                            System.out.println("Imagen exportada correctamente\n");
                        } else {
                            System.out.println("Debes generar el laberinto\n");
                        }

                        break;
                    case 3: /*Opcion para importar el laberinto mediante el JSON especificado por el usuario*/

                        try {
                            System.out.println("Indica la ruta del json");
                            String path = sc.next();

                            String jsonContent = getJSON(path);
                            Gson gson = new Gson(); //Extrae el contenido del JSON

                            grid = gson.fromJson(jsonContent, Grid.class);
                            grid.generateCellsGrids(); //Genera el laberinto mediante los datos del JSON importado 
                            System.out.println("JSON importado correctamente\n");
                        } catch (Exception ex) {
                        	grid=null;
                            System.out.println("JSON no compatible\n");
                        }

                        break;
                        
                    case 4:
                        Hito2(grid);
                        break;
                        
                    case 5:/* Opcion para salir del programa*/
                        salir = true;
                        System.out.println("Has salido del programa");
                        break;
                }

            } catch (InputMismatchException ex) { /* Si el usuario no introduce un entero valido salta la excepcion*/
                System.out.println("Introduce una opcion valida\n");
                sc.next();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    /*
     * Metodo getJSON
     * Este metodo se utiliza para extraer el contenido del archivo .json a la hora de importarlo
     */
    private static String getJSON(String ruta) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(ruta));

        String linea, json = "";
        while ((linea = br.readLine()) != null) {
            json += linea;
        }
        br.close();

        return json;
    }
    
    private static ArrayList<Estado> funcionSucesores (Estado e, Grid g){
    	
    	ArrayList<Estado> list = new ArrayList<Estado>();
    	
    	if (e.getId()[0] != 0 && g.getCellsGrid()[e.getId()[0]][e.getId()[1]].getNeighbors()[0]) { //N(comprobar el estado de ir hacia el Norte)
    		list.add(new Estado(e.getId()[0], e.getId()[1]+1, g.getId_mov()[0]));
        }
    	if (e.getId()[1] != g.getCols()-1 && g.getCellsGrid()[e.getId()[0]][e.getId()[1]].getNeighbors()[1]) { //E
    		list.add(new Estado(e.getId()[0]+1, e.getId()[1], g.getId_mov()[1]));
        }
    	if (e.getId()[0] != g.getRows()-1 && g.getCellsGrid()[e.getId()[0]][e.getId()[1]].getNeighbors()[2]) { //S
    		list.add(new Estado(e.getId()[0], e.getId()[1]-1, g.getId_mov()[2]));
        }
    	if (e.getId()[0] != 0 && g.getCellsGrid()[e.getId()[0]][e.getId()[1]].getNeighbors()[3]) { //O
    		list.add(new Estado(e.getId()[0]-1, e.getId()[1], g.getId_mov()[3]));
        }
    	return list;
    }
    
    private static ArrayList<Nodo> nodoSucesores (Nodo n, Grid g, String estrategia, int id){ //A partir de cada estado se genera un nuevo sucesor
    	
    	ArrayList<Nodo> list = new ArrayList<Nodo>();
    	
    	for (Estado a: funcionSucesores(n.getEstado(), g)) {
    		
    		switch (estrategia) { /* Desde la celda acual marcamos el vecino al que va*/
            	case "Gready":
            		list.add(new Nodo(n, a, id, n.getCosto()+1, a.getAccion(), n.getD()+1, 0/*heuristica por definir*/, n.getD()));
            		break;
            	case "Deep":
            		list.add(new Nodo(n, a, id, n.getCosto()+1, a.getAccion(), n.getD()+1, 0/*heuristica por definir*/, 1/(n.getD()+1)));
            		break;
            	case "Cost":
            		list.add(new Nodo(n, a, id, n.getCosto()+1, a.getAccion(), n.getD()+1, 0/*heuristica por definir*/, n.getCosto()+1));
            		break;
            	case "H":
            		list.add(new Nodo(n, a, id, n.getCosto()+1, a.getAccion(), n.getD()+1, 0/*heuristica por definir*/, n.getH()));
            		break;
            	case "A":
            		list.add(new Nodo(n, a, id, n.getCosto()+1, a.getAccion(), n.getD()+1, 0/*heuristica por definir*/, n.getH()+n.getCosto()));
            		break;
    		}
    		id++;
    	}
    	return list;
    }
    
    public static void Hito2 (Grid g) {
    	
    	PriorityQueue<Nodo> frontera = new PriorityQueue<Nodo>();
    	//primero fila, después columna el valor del estado
    	for (int i = 0; i<12; i++) {
    	
    		frontera.add(new Nodo(null, new Estado(((int) (Math.random()*4+1)), ((int) (Math.random()*4+1)), "e") , ((int) (Math.random()*10+1)), 0, "accion", 0, 0/*heuristica por definir*/, 0));
    	}
    	
    	for (int i = 0; i<12; i++) {
    		
    		System.out.println(frontera.poll().toString());
    	}
    }
    
    public static boolean esObjetivo (Nodo n, Grid g) {
    	if (n.getEstado().getId()[0] == g.getRows()-1 && n.getEstado().getId()[1] == g.getCols()-1) {
    		return true;
    	} else return false;
    }
}

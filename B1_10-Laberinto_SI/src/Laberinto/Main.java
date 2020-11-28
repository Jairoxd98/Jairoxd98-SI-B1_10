//B1_10-Laberinto_SI/src/Laberinto/problema_5x5.json
//C:\Users\Carlos\Moreno\Maroto\git\Jairoxd98-SI-B1_10\B1_10-Laberinto_SI\src\Laberinto\problema_10x10.json

package Laberinto;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

public class Main {

    public static void main(String[] args) throws IOException {
    	
        menu(); /* Llamada al menu del programa */
        
    }
    
    public static void menu() {
    	Scanner sc = new Scanner(System.in);

        boolean salir = false;
        
        int inicial[]= new int [2];
        int objetivo[]= new int [2];

        Grid grid = null;
        while (!salir) {

            try {
            	System.out.println("Elige una opcion: ");
                System.out.println("\t"+"1. Generar laberinto");
                System.out.println("\t"+"2. Exportar imagen");
                System.out.println("\t"+"3. Importar laberinto");
                System.out.println("\t"+"4. Importar JSON");
                System.out.println("\t"+"5. Encontrar una Solucion mediante arbol de Busqueda");
                System.out.println("\t"+"6. Salir"+"\n");

                int option = sc.nextInt();
                switch (option) {
                
                    case 1: /*Opcion para generar el laberinto mediante los paremetros pedidos por teclado*/
                        System.out.println("Elige el numero de filas");
                        int filas = sc.nextInt();
                        while(filas<=0){
                        	System.out.println("Numero de filas incorrecto, introduce un numero correcto\n");
                        	filas = sc.nextInt();
                        }
                        System.out.println("Elige el numero de columnas");
                        int columnas = sc.nextInt();
                    	while(columnas<=0) {
                    		System.out.println("Numero de columnas incorrecto, introduce un numero correcto\n");
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
                            String jsonContent = getJSON(askJSON()); 
                            Gson gson = new Gson(); 

                            grid = gson.fromJson(jsonContent, Grid.class); //Extrae el contenido del JSON que pedimos por teclado
                            grid.generateCellsGrids(); //Genera el laberinto mediante los datos del JSON importado 
                            System.out.println("JSON importado correctamente\n");
                        } catch (Exception ex) {
                        	grid=null;
                            System.out.println("JSON no compatible\n");
                        }
                        break;
                        
                    case 4: /*Opcion para crear la fontera y sus nodos mediante un .json*/
                    	
                    	String jsonCont = getJSON(askJSON()); //Obtenemos el contenido del JSON
                        
                        @SuppressWarnings("deprecation") JsonParser parser = new JsonParser();
                        @SuppressWarnings("deprecation") JsonObject gsonObj = parser.parse(jsonCont).getAsJsonObject();
                        
                        String initial = gsonObj.get("INITIAL").getAsString();  //Nodo Inicio
                        inicial=extraerString(initial);
                        String objective = gsonObj.get("OBJETIVE").getAsString();  //Nodo Objetivo
                        objetivo=extraerString(objective);
                        String maze = gsonObj.get("MAZE").getAsString(); //Nombre del .json a utilizar
                        
                        try {
                            String jsonContent = getJSON("B1_10-Laberinto_SI/src/Laberinto/"+maze);/* PONER DONDE SE ENCUENTRA */ 
                            Gson gson = new Gson();

                            grid = gson.fromJson(jsonContent, Grid.class); //Extrae el contenido del JSON que pedimos por teclado
                            grid.generateCellsGrids(); //Genera el laberinto mediante los datos del JSON importado 
                            System.out.println("JSON importado correctamente\n");
                        } catch (Exception ex) {
                        	grid=null;
                            System.out.println("JSON no compatible\n"+ex);
                        }
                        //Ejecucion muestra Hito 2
                    	//System.out.println(initial+" "+objective+" "+maze);
                        //generarFrontera(grid);
                        break;
                        
                    case 5:/* Opcion para encontrar una solucion con las diferente heuristicas*/
                    	
                    	for(int i=0; i < grid.getCellsGrid().length; i++) {
                    		
                    		for(int j=0; j < grid.getCellsGrid()[0].length; j++) {
                    			System.out.print(grid.getCellsGrid()[i][j].getValue()+" ");
                    		}
                    		System.out.println();
                    	}
                    	
                    	boolean seguir = true;
                    	do {
                			System.out.println("\nElija la estrategia para implementar el problema\n1. Anchura"
                					+ "\n2. Profundidad\n3. Costo Uniforme\n4. Busqueda Voraz\n5. Busqueda A*\n6. Salir");
                			option = sc.nextInt();
                			Stack <Nodo> a = new Stack <Nodo> ();
                			switch (option) {
                			case 1:
                				System.out.println("\t[id][cost,state,father_id,action,depth,h,value]");
                				a = busqueda(new Estado(inicial[0], inicial[1], null, grid.getCellsGrid()[0][0].getValue()), grid, "BREADTH", 1000000,objetivo);
                				break;
                			case 2:
                				System.out.println("\t[id][cost,state,father_id,action,depth,h,value]");
                				a = busqueda(new Estado(inicial[0], inicial[1], null, grid.getCellsGrid()[0][0].getValue()), grid, "DEPTH", 1000000,objetivo);
                				break;
                			case 3:
                				System.out.println("\t[id][cost,state,father_id,action,depth,h,value]");
                				a = busqueda(new Estado(inicial[0], inicial[1], null, grid.getCellsGrid()[0][0].getValue()), grid, "UNIFORM", 1000000,objetivo);
                				break;
                			case 4:
                				System.out.println("\t[id][cost,state,father_id,action,depth,h,value]");
                				a = busqueda(new Estado(inicial[0], inicial[1], null, grid.getCellsGrid()[0][0].getValue()), grid, "GREEDY", 1000000,objetivo);
                				break;
                			case 5:
                				System.out.println("\t[id][cost,state,father_id,action,depth,h,value]");
                				a = busqueda(new Estado(inicial[0], inicial[1], null, grid.getCellsGrid()[0][0].getValue()), grid, "A", 1000000,objetivo);
                				break;
                			case 6:
                				seguir = false;
                				break;
                			default:
                				System.out.println("Opcion erronea ");
                				break;
                			}
            				while(!a.isEmpty()) {
                				System.out.println("\t" + a.pop().toString());
                			}
                			
                		} while (seguir);
                    	break;
                        
                    case 6:/* Opcion para salir del programa*/
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
    
    /*
     * Metodo askJSON
     * Metodo auxiliar para preguntar la ruta del JSON
     */
    private static String askJSON() {
    	Scanner s = new Scanner(System.in);
    	System.out.println("Indica la ruta del archivo .json");
        String path = s.next();
    	return path;
    }
    
    /*
     * Metodo extraerString
     * Este es un emtodo auxiliar para cojer el valor (x,y) del JSON
     */
    private static int[] extraerString(String v) {
    	int vec[]= new int[2];
        
        v = v.replace("(", " ").trim();
        v = v.replace(")", " ").trim();
        v = v.replace(" ", "").trim();

        String[] parts = v.split(","); //Partimos en "x" e "y"
        
        /*Tomamos los valores como enteros*/
        vec[0] = Integer.parseInt(parts[0]);
        vec[1] = Integer.parseInt(parts[1]);
    	return vec;
    }
    
    /*
     * Metodo generarFrontera 
     * Este metodo se utiliza para crearla frontera, y añadir nuevos nodos a la lista PriorityQueue de la frontera
     */
    public static void generarFrontera (Grid g) { //
    	PriorityQueue<Nodo> frontera = new PriorityQueue<Nodo>();
    	
    	for (int i = 0; i<20; i++) {
    		frontera.add(new Nodo(null, new Estado(((int) (Math.random()*4+1)), ((int) (Math.random()*4+1)),"e",  1) , ((int) (Math.random()*10+1)), 0, "accion", 0, 0/*heuristica por definir*/, ((int) (Math.random()*4+1))));
    	}
    	
    	for (int i = 0; i<20; i++) {
    		
    		System.out.println(frontera.poll().toString());
    	}
    }
    
    /*
     * Metodo Busqueda 
     * Se utiliza para realizar un arbol de busqueda con diferentes estrategias 
     */
    public static Stack<Nodo> busqueda (Estado inicial, Grid g, String estrategia, int cota, int []objetivo){
    	
    	PriorityQueue<Nodo> frontera = new PriorityQueue<Nodo>();
    	ArrayList<Nodo> visitados = new ArrayList<Nodo>();
    	Nodo nodo;
    	ArrayList<Nodo> sucesores;
    	int id = 0;//identificadores ed los nodos
    	
    	if (estrategia == "DEPTH") {//porque en caso de la profundidad, la formula es n/n, y como haga 0/0 peta
    		nodo = new Nodo(null, inicial, id, 0, null, 0, calcularHeuristica(inicial, g), 1);
    	} else nodo = new Nodo(null, inicial, id, 0, null, 0, calcularHeuristica(inicial, g), 0);//resto de casos
    	
    	id++;//al ya hacer el id 0, el siguiente será el id 1
    	frontera.add(nodo); //añadimos el nodo inicial
    	
    	while (!frontera.isEmpty() && !esObjetivo(frontera.peek(), objetivo)) { //no voy a tratar de buscar si ya he mirado en todo, o si ya he encontrado la solución
    		
    		//System.out.println(frontera.peek().toString());
    		nodo = frontera.poll();//porque el peek, no lo sacamos, y con poll, lo sacamos y eliminamos de la frontera para no tener que volverlo a mirar
    		if (nodo.getD() < cota) { //comprobamos de que no nos hemos pasado del límite de nodo
    			sucesores = nodoSucesores(nodo, g, estrategia, id);//miramos los caminos adyacentes
    			for (Nodo n: sucesores) { //para cada nodo adyacentes, miramos si lo hemos visitado o no, si no lo he visitado lo meto a la frontera
    				if(!visitados.contains(n)) {
    					frontera.add(n);
    					//System.out.println("\t" + n.toString());
    				}
    				id++; //aumentamos el identificador por cada nodo identificado
        			//System.out.println("\t" + n.toString());
        		}
    		}
    		visitados.add(copiarNodo(nodo)); //metemos el nodo actual
    		/*System.out.println("Visitados");
    		for (Nodo n: visitados) { //forma de ver todos los visitadosfvcv
    			System.out.println("\t" + n.toString());
    		}
    		System.out.println("Fin de Visitados");*/
    	}
    	if (!frontera.isEmpty()){ //comprobamos si hay solucion o no
    		if (esObjetivo(frontera.peek(), objetivo)) { //si es solucion entonces procedemos a coger la informacion y mostrarla 
	    		nodo = frontera.peek();
	    		Stack<Nodo> solucion = new Stack<Nodo>();
	    		while(nodo.getPadre()!=null) { //vas cogiendo el nodo actual y lo metes como parte de la solucion
	    			//System.out.println("\t" + nodo.toString());
	    			solucion.push(copiarNodo(nodo));
	    			nodo = nodo.getPadre();
	    		}
	    		//System.out.println("\t" + nodo.toString());
	    		solucion.add(copiarNodo(nodo)); //añadimos el nodo inicial
	    		return solucion; //agrupación de todos los nodos que hemos ido haciendo
	    	}
    	} return null;
    }
    
    /*
     * Metodo funcionSucesores
     * Este metodo comprueba el estado de poder ir hacia los diferentes vecinios que tiene y devuelve los possibles vecinos sucesores del estado pasado por parametros
     */
    private static ArrayList<Estado> funcionSucesores (Estado e, Grid g){
    	
    	ArrayList<Estado> list = new ArrayList<Estado>();
    	
    	if (e.getId()[0] != 0 && g.getCellsGrid()[e.getId()[0]][e.getId()[1]].getNeighbors()[0]) { //N (comprobar el estado de ir hacia el Norte)
    		list.add(new Estado(e.getId()[0]-1, e.getId()[1], g.getId_mov()[0], g.getCellsGrid()[e.getId()[0]-1][e.getId()[1]].getValue()));
        }
    	if (e.getId()[1] != g.getCols()-1 && g.getCellsGrid()[e.getId()[0]][e.getId()[1]].getNeighbors()[1]) { //E (comprobar el estado de ir hacia el Este)
    		list.add(new Estado(e.getId()[0], e.getId()[1]+1, g.getId_mov()[1], g.getCellsGrid()[e.getId()[0]][e.getId()[1]+1].getValue()));
    	}
    	if (e.getId()[0] != g.getRows()-1 && g.getCellsGrid()[e.getId()[0]][e.getId()[1]].getNeighbors()[2]) { //S (comprobar el estado de ir hacia el Sur)
    		list.add(new Estado(e.getId()[0]+1, e.getId()[1], g.getId_mov()[2], g.getCellsGrid()[e.getId()[0]+1][e.getId()[1]].getValue())); //ellos lo hacen asi
        }
    	if (e.getId()[1] != 0 && g.getCellsGrid()[e.getId()[0]][e.getId()[1]].getNeighbors()[3]) { //O (comprobar el estado de ir hacia el Oeste)
    		list.add(new Estado(e.getId()[0], e.getId()[1]-1, g.getId_mov()[3], g.getCellsGrid()[e.getId()[0]][e.getId()[1]-1].getValue()));
        }
    	return list;
    }

/*
 * Metodo nodoSucesores 
 * A apartir de cada estado comprueba los posibles sucesores y los genera dependiendo de la heuristica seleccionado     
 */
    //public Nodo(Nodo padre, Estado estado, int id, int costo, String accion, double d, double h, double f)
private static ArrayList<Nodo> nodoSucesores (Nodo n, Grid g, String estrategia, int id){
    	
    	ArrayList<Nodo> list = new ArrayList<Nodo>();
    	
    	for (Estado a: funcionSucesores(n.getEstado(), g)) {
    		
    		//System.out.println(id+"\t"+n.getCosto()+" "+a.getValor());
    		
    		switch (estrategia) { 
            	case "BREADTH":
            		list.add(new Nodo(n, a, id, (n.getCosto()+a.getValor())+1, a.getAccion(), n.getD()+1, calcularHeuristica(a, g), n.getD()));
            		break;
            	case "DEPTH":
            		list.add(new Nodo(n, a, id, (n.getCosto()+a.getValor())+1, a.getAccion(), n.getD()+1, calcularHeuristica(a, g), 1/(n.getD()+2)));
            		break;
            	case "UNIFORM":
            		list.add(new Nodo(n, a, id, (n.getCosto()+a.getValor())+1, a.getAccion(), n.getD()+1, calcularHeuristica(a, g), n.getCosto()));//quito+1
            		break;
            	case "GREEDY":
            		list.add(new Nodo(n, a, id, (n.getCosto()+a.getValor())+1, a.getAccion(), n.getD()+1, calcularHeuristica(a, g), n.getH()));
            		break;
            	case "A":
            		list.add(new Nodo(n, a, id, (n.getCosto()+a.getValor())+1, a.getAccion(), n.getD()+1, calcularHeuristica(a, g), n.getH()+n.getCosto()));
            		break;
    		}
    		id++;
    	}
    	return list;
    }
    
    /*
     * Metodo esObjetivo
     * Metodo que se utiliza para comprobar que hemos llegado al nodo final correcto
     */
	/*public static boolean esObjetivo (Nodo n, Grid g) { 
		if (n.getEstado().getId()[0] == g.getRows()-1 && n.getEstado().getId()[1] == g.getCols()-1) {
			return true;
		} else return false;
	}*/
	public static boolean esObjetivo (Nodo n, int []objetivo) { 
		if (n.getEstado().getId()[0] == objetivo[0] && n.getEstado().getId()[1] == objetivo[1]) {
			return true;
		} else return false;
	}
	
	/*
	 * Metodo calcularHeuristica
	 * Metodo que se utiliza para calcular la Heuristica como la distancia manhattan:
	 * Heurística((fila,columna))= |fila-fila_objetivo| + |columna-columna_objetivo|
	 */
	public static int calcularHeuristica (Estado e, Grid g) {
		return (Math.abs(g.getRows()-e.getId()[0]-1))+(Math.abs(g.getCols()-e.getId()[1]-1));
	}
	/*
	 * Metodo copiarNodo
	 * Este metodo se utiliza para realizar copias de los nodos a la hora de interactuar con ellos 
	 * en el metodo "busqueda" y asi evitar problemas 
	 */
	public static Nodo copiarNodo (Nodo n) {
		Nodo nCopia = new Nodo(n.getPadre(), n.getEstado(), n.getId(), n.getCosto(), n.getAccion(), n.getD(), n.getH(), n.getF());
		return nCopia;
	}
}

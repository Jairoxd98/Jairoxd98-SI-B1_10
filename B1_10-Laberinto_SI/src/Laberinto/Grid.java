package Laberinto;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import javax.imageio.ImageIO;

public class Grid {
	
	/*
	 * Los atributos declarados con transient, se declaran de esta forma para que 
	 * el JSON no los recoja a la hora de generar el archivo .json
	 */
	
    private int rows;
    private int cols;
    private int max_n; //Numero maximo de movimientos y vecinos que puede tener la celda del laberinto
    private int[][] mov;
    private String[] id_mov;
    transient private Cell[][] cellsGrid; //Celdas del laberinto
    transient private int numberCells; //Numero de celdas del laberinto para saber las celdas visitadas
    private TreeMap<Object, Object> cells; //Treemap para ordenar las celdas a la hora de generar el JSON
    //Os dais cuenta de que una matriz ya est� indexada por una columna y una fila ?? por lo que habeis creado una copia del objeto CellsGrid

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.numberCells = this.rows * this.cols;
        this.max_n = 4;

        this.cellsGrid = new Cell[this.rows][this.cols];

        int[][] mov = {
            {-1, 0},
            {0, 1},
            {1, 0},
            {0, -1}
        };

        this.mov = mov;

        String[] id_mov = {"N", "E", "S", "O"};
        this.id_mov = id_mov;

        this.init();

    }
    /*
     * Metodo init
     * Su funcion es generar todas las celdas del laberinto 
     */
    private void init() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.cellsGrid[i][j] = new Cell(i, j);
            }
        }
    }
    
    /*
     * Metodo generateMaze
     * Con este metodo generamos el laberinto mediante el algoritmo de Wilson
     * Lo primero que hacemos es coger una celda aleatoria del laberinto y la tomamos 
     * como inicio, luego cogemos otra celda aleatoria que no este visitada y la tomamos 
     * como destino, y de manera aleatoria nos movemos en una direccion a una celda adyacente 
     * mientras que esta no este ya visitada o sea la celda destino, y controlando cada 
     * vez que nos movemos que no se salga de los bordes del laberinto. En caso de llegar a 
     * una celda visitada evita el bucle igualando la celda destino a la visitada y asi volviendo 
     * al origen.
     * Asi vamos recorriendo el laberinto y marcando las celdas visitadas y guardando donde se encuentran 
     * los vecinos tanto de la celda en la que estabamos como de la celda adyacente a la que pasamos.
     * Durante el recorrido y su generacion se van poniendo los vecinos a true de las celdas
     * por donde se va realizando el camino, y las celdas que acaban en false son las que forman los muros.
     * Cuando se han visitado todas las celdas del laberinto, finaliza la generacion de este 
     */
    public void generateMaze() {

        int cellsVisited = 0;
        while (this.numberCells != cellsVisited) { /* Cuando todas esten visitadas significa que he terminado*/

            Cell origin = this.getCellEmpty(); /*Asignamos una celda no visitada a la celda inicial*/

            int rowOriginStart = origin.getX();
            int colOriginStart = origin.getY();

            Cell destiny; //Para la primera iteracion establecemos una celda aleatoria 
            if (cellsVisited == 0) {
                destiny = this.getCell();
            } else { // Para las siguientes iteracioens establecemos una celda aleatoria que no este visitada
                destiny = this.getCellNoBlank();
            }

            while (!origin.equals(destiny) && origin.isBlank()) { /*Se repite mientras no llegemos al destino o a una celda visitada*/

                int index = this.generaNumeroAleatorio(0, this.mov.length - 1);
                int[] movSelected = this.mov[index];
                String direction = this.id_mov[index];

                if ((origin.getX() + movSelected[0]) >= 0
                        && (origin.getX() + movSelected[0]) < this.rows
                        && (origin.getY() + movSelected[1]) >= 0
                        && (origin.getY() + movSelected[1]) < this.cols) { //Comprueba que no se salga de los bordes del laberinto

                    origin.setDirection(direction);

                    origin = this.cellsGrid[origin.getX() + movSelected[0]][origin.getY() + movSelected[1]]; // Se desplaza a la celda adyacente 
                }

            }

            if (!origin.isBlank()) { /*Si llega a una celda visitada acortamos el camino igualanda la celda al destino*/
                destiny = origin;
            }

            origin = this.cellsGrid[rowOriginStart][colOriginStart];
            /* Cuando a llegado al destino vamos guardando los vecinos de las celdas, primero en 
             * la celda origen y luego en la del vecino visitado respectivamente, y mientras vamos contando las celdas que han sido 
             * visitadas durante la generacion del laberinto  
             * */
            while (!origin.equals(destiny)) {

                cellsVisited++;
                String direction = origin.getDirection();

                boolean neighbors[] = origin.getNeighbors();// Cogemos sus vecinos, por si ya tiene alguno a true
                Cell nextCell = null;
                int[] mov = null;
                switch (direction) { /* Desde la celda acual marcamos el vecino al que va*/
                    case "N":
                        neighbors[0] = true;
                        mov = this.mov[0];
                        break;
                    case "E":
                        neighbors[1] = true;
                        mov = this.mov[1];
                        break;
                    case "S":
                        neighbors[2] = true;
                        mov = this.mov[2];
                        break;
                    case "O":
                        neighbors[3] = true;
                        mov = this.mov[3];
                        break;
                }

                origin.setNeighbors(neighbors);
                origin.setBlank(false);

                nextCell = this.cellsGrid[origin.getX() + mov[0]][origin.getY() + mov[1]]; // Celda adyacente
                neighbors = nextCell.getNeighbors(); // Cogemos sus vecinos, por si ya tiene alguno a true
                switch (direction) { /* Desde la celda adyacente marcamos el vecino de donde viene*/
                    case "N": //El sur del vecino es el norte de la celda adyacente
                        neighbors[2] = true;
                        break;
                    case "E": //El este del vecino es el oeste de la celda adyacente
                        neighbors[3] = true;
                        break;
                    case "S": //El norte del vecino es el sur de la celda adyacente
                        neighbors[0] = true;
                        break;
                    case "O"://El oeste del vecino es el este de la celda adyacente
                        neighbors[1] = true;
                        break;
                }

                nextCell.setNeighbors(neighbors);
                origin = nextCell;
            }

            if (origin.isBlank()) { //Si la celda siguiente no esta ya visitada la marcamos como visitada
                cellsVisited++;
            }

            origin.setBlank(false);

        }

    }
    /*
     * Metodo getCellNoBlank
     * Este metodo devuelve una celda visitada
     */
    private Cell getCellNoBlank() {
    	Cell c =getCellGet(true);
    	return c;
    }
    /*
     * Metodo getCellEmpty
     * Este metodo devuelve una celda no visitada
     */
    private Cell getCellEmpty() {
    	Cell c =getCellGet(false);
    	return c;
    }
    
    /*
     * Metodo getCellGet
     * Es un emtodo auxiliar de getCellEmpty y getCellNoBlank, que devuelve segun el metodo que lo llame 
     * la respuesta de si la celda esta visitada o no.
     */
    private Cell getCellGet(boolean n) {
    	Cell c = null;
    	boolean blank;
    	do {
        	c= getCell();
        	if(n) {
        		blank= c.isBlank();
        	}else {
        		blank= !c.isBlank();
        	}
        } while (blank);
    	return c;
    }
    /*
     * Metodo getCell
     * Este metodo devuelve una celda aleatoria del laberinto 
     */
    private Cell getCell() {

        Cell c = null;
        int row = this.generaNumeroAleatorio(0, this.rows - 1);
        int col = this.generaNumeroAleatorio(0, this.cols - 1);

        c = this.cellsGrid[row][col];

        return c;

    }
    
    /*
     * Metodo generateCells
     * Con este metodo rellenamos el TreeMap con (x,y) como llave y los valores visibles de la celda como valor
     * Es utilizado para generar el JSON
     */
    private void generateCells() {

        this.cells = new TreeMap<>();

        for (int i = 0; i < this.cellsGrid.length; i++) {
            for (int j = 0; j < this.cellsGrid[0].length; j++) {
                this.cells.put(this.cellsGrid[i][j].toString(), this.cellsGrid[i][j]);
            }
        }

    }
    
    /*
     * Metodo generateJSON
     * Genera el arcivo JSON con la libreria "gson" con la ayuda del metodo generateCells
     */
    public void generateJSON() throws IOException {

        Gson gson = new Gson();

        this.generateCells();

        BufferedWriter bw = new BufferedWriter(new FileWriter("puzzle_" + this.rows + "x" + this.cols + ".json"));

        bw.write(gson.toJson(this));

        bw.close();

    }
    
    /*
     * Metodo generateCellsGrids
     * Con este metodo importamos archivos .json y creamos un laberinto con las indicaciones de este 
     * Y por ultimo comprobamos si la semantica es correcta
     */
    public void generateCellsGrids() throws Exception {

        this.numberCells = this.rows * this.cols;
        this.max_n = 4;

        this.cellsGrid = new Cell[this.rows][this.cols];

        for (Map.Entry<Object, Object> entry : this.cells.entrySet()) {/* Recorremos como un TreeMap el JSON y establecemos las llaves de este con las posiciones de la celda (x,y)*/
            String key = (String) entry.getKey();
            LinkedTreeMap value = (LinkedTreeMap) entry.getValue();
            /*Reemplazamos la sintaxis necesaria para extraer los datos de la llave*/
            key = key.replace("(", " ").trim();
            key = key.replace(")", " ").trim();
            key = key.replace(" ", "").trim();

            String[] parts = key.split(","); //Partimos en "x" e "y" 
            /*Tomamos los calores como enteros*/
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);

            Cell c = new Cell(x, y, false);

            ArrayList<Boolean> n = (ArrayList<Boolean>) value.get("neighbors");

            boolean[] neighbors = new boolean[4];

            for (int i = 0; i < neighbors.length; i++) { //Rellenamos los vecinos de cada celda
                neighbors[i] = n.get(i);
            }

            c.setNeighbors(neighbors);

            this.cellsGrid[x][y] = c;

        }

        checkCells();//LLamada al metodo checkCells para comprobar si la semantica del laberinto a importar es correcta

    }
    /*
     * Metodo exportToIMG
     * Con este metodo generamos la imagen en formato .png del laberinto
     * Para ello usamos el objeto de java Graphics2D
     * Dibujaremos el laberinto recorriendo las celdas de este y pintando los muros que tenga cada una de estas 
     * mediante drawLine, que pinta una linea entre dos puntos dados
     */
    public void exportToIMG() {

        BufferedImage imagen = new BufferedImage(this.cols * 30, this.rows * 30, BufferedImage.TYPE_INT_RGB);/*Tamaño de la imagen*/

        Graphics2D g = imagen.createGraphics();

        for (int i = 0; i < this.cellsGrid.length; i++) { /* Recorremos las celads del laberinto */
            for (int j = 0; j < this.cellsGrid[0].length; j++) {
                boolean[] n = this.cellsGrid[i][j].getNeighbors(); /*Cogemos los vecinos de la celda*/
                /*Si tiene un muro en el Norte seleccionamos el punto de la esquina superior izquierda de la celda y el de la esquina superior derecha y pintamos una linea entre ellos */
                if (!n[0]) { 
                    g.drawLine((20 * j) + 10, 20 * (i + 1), (20 * j) + 30, 20 * (i + 1));
                }
                /*Si tiene un muro en el Este seleccionamos el punto de la esquina superior derecha de la celda y el de la esquina inferior derecha y pintamos una linea entre ellos */
                if (!n[1]) {
                    g.drawLine((20 * j) + 30, 20 * (i + 1), (20 * j) + 30, 20 * (i + 2));
                }
                /*Si tiene un muro en el Sur seleccionamos el punto de la esquina inferior izquierda de la celda y el de la esquina inferior derecha y pintamos una linea entre ellos */
                if (!n[2]) {
                    g.drawLine((20 * j) + 10, 20 * (i + 1) + 20, (20 * j) + 30, 20 * (i + 1) + 20);
                }
                /*Si tiene un muro en el Norte seleccionamos el punto de la esquina superior izquierda de la celda y el de la esquina inferior izquierda y pintamos una linea entre ellos */
                if (!n[3]) {
                    g.drawLine((20 * j) + 10, 20 * (i + 1), (20 * j) + 10, 20 * (i + 2));
                }

            }
        }

        try {
            ImageIO.write(imagen, "png", new File("puzzle_" + this.rows + "x" + this.cols + ".png"));
        } catch (IOException e) {
            System.out.println("Error de escritura");
        }

    }
    
    /*
     * Metodo checkCells
     * Comprueba la semantica del laberinto importado y si tiene una estructura correcta
     */
    private void checkCells() throws Exception {
        for (int i = 0; i < this.cellsGrid.length; i++) { /*Recorremos las celdas del laberinto*/
            for (int j = 0; j < this.cellsGrid[0].length; j++) {
                Cell c = this.cellsGrid[i][j];
                if (i == 0 && c.getNeighbors()[0]) { //Si es de la fila 0 y no tiene un muro al Norte, el JSON es incorrecto 
                    throw new Exception();
                }
                if (i == (this.cellsGrid.length - 1) && c.getNeighbors()[2]) { //Si es de la ultima fila y no tiene muro al Sur, el JSON es incorrecto
                    throw new Exception();
                }
                if (j == 0 && c.getNeighbors()[3]) { //Si es de la columna 0 y no tiene un muro al oeste, el JSON es incorrecto
                    throw new Exception();
                }
                if (j == (this.cellsGrid[0].length - 1) && c.getNeighbors()[1]) { //Si es de la ultima columna y no tiene un muro al este, el JSON es incorrecto
                    throw new Exception();
                }
                if (!c.getNeighbors()[0] && !c.getNeighbors()[1] && !c.getNeighbors()[2] && !c.getNeighbors()[3]) { //Si una celda tiene un muro en todas direcciones, el JSON es incorrecto 
                    throw new Exception();
                }
                
                /*Para controlar la semantica recorremos los vecinos de la celda y comprobamos si se corresponden los muros*/
                for (int k = 0; k < c.getNeighbors().length; k++) {
                    if (c.getNeighbors()[k]) {
                        
                        switch (k) {
                            case 0: /*Comprobamos que si hay un muro al norte, la celda al norte de esta tiene un muro al sur*/
                            	
                                if (!this.cellsGrid[i + this.mov[k][0]][j + this.mov[k][1]].getNeighbors()[2]) {
                                    throw new Exception();
                                }
                                break;
                            case 1: /*Comprobamos que si hay un muro al este, la celda al este de esta tiene un muro al oeste*/

                                if (!this.cellsGrid[i + this.mov[k][0]][j + this.mov[k][1]].getNeighbors()[3]) {
                                    throw new Exception();
                                }
                                break;
                            case 2: /*Comprobamos que si hay un muro al sur, la celda al sur de esta tiene un muro al norte*/

                                if (!this.cellsGrid[i + this.mov[k][0]][j + this.mov[k][1]].getNeighbors()[0]) {
                                    throw new Exception();
                                }
                                break;
                            case 3:/*Comprobamos que si hay un muro al oeste, la celda al oeste de esta tiene un muro al este*/

                                if (!this.cellsGrid[i + this.mov[k][0]][j + this.mov[k][1]].getNeighbors()[1]) {
                                    throw new Exception();
                                }
                                break;
                        }

                    }
                }

            }
        }

    }
    
    /*
     * Metodo generaNumeroAleatorio
     * Este metodo devuelve un numero aleatorio entre los parametros indicados
     */
    private int generaNumeroAleatorio(int minimo, int maximo) {
        int num = (int) (Math.random() * (minimo - (maximo + 1)) + (maximo + 1));
        return num;
    }
    
    /*Getters and Setters*/
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getCols() {
		return cols;
	}
	public void setCols(int cols) {
		this.cols = cols;
	}
	public int getMax_n() {
		return max_n;
	}
	public void setMax_n(int max_n) {
		this.max_n = max_n;
	}
	public int[][] getMov() {
		return mov;
	}
	public void setMov(int[][] mov) {
		this.mov = mov;
	}
	public String[] getId_mov() {
		return id_mov;
	}
	public void setId_mov(String[] id_mov) {
		this.id_mov = id_mov;
	}
	public Cell[][] getCellsGrid() {
		return cellsGrid;
	}
	public void setCellsGrid(Cell[][] cellsGrid) {
		this.cellsGrid = cellsGrid;
	}
	public int getNumberCells() {
		return numberCells;
	}
	public void setNumberCells(int numberCells) {
		this.numberCells = numberCells;
	}
	public TreeMap<Object, Object> getCells() {
		return cells;
	}
	public void setCells(TreeMap<Object, Object> cells) {
		this.cells = cells;
	}
	
	/*M�todo copiar Laberinto*/
	public Grid copyGrid (Grid l) {
    	Grid lc = new Grid (l.getRows(), l.getCols());
    	System.out.println("Este metodo esta por realizar por dudas tecnicas sobre la clase");
        /*private int rows;
        private int cols;
        private int max_n; //Numero maximo de movimientos y vecinos que puede tener la celda del laberinto
        private int[][] mov;
        private String[] id_mov;
        transient private Cell[][] cellsGrid; //Celdas del laberinto
        transient private int numberCells; //Numero de celdas del laberinto para saber las celdas visitadas (variable Local mejor)
        private TreeMap<Object, Object> cells; //Treemap para ordenar las celdas a la hora de generar el JSON*/
    	
    	return lc;
    }
}

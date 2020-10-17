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

    private int rows;
    private int cols;
    private int max_n;
    private int[][] mov;
    private String[] id_mov;
    transient private Cell[][] cellsGrid;
    transient private int numberCells;
    private TreeMap<Object, Object> cells;

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

    private void init() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.cellsGrid[i][j] = new Cell(i, j);
            }
        }
    }

    public void generateMaze() {

        int cellsVisited = 0;
        while (this.numberCells != cellsVisited) {

            Cell origin = this.getCellEmpty();

            int rowOriginStart = origin.getX();
            int colOriginStart = origin.getY();

            Cell destiny;
            if (cellsVisited == 0) {
                destiny = this.getCell();
            } else {
                destiny = this.getCellNoBlank();
            }

            while (!origin.equals(destiny) && origin.isBlank()) {

                int index = this.generaNumeroAleatorio(0, this.mov.length - 1);
                int[] movSelected = this.mov[index];
                String direction = this.id_mov[index];

                if ((origin.getX() + movSelected[0]) >= 0
                        && (origin.getX() + movSelected[0]) < this.rows
                        && (origin.getY() + movSelected[1]) >= 0
                        && (origin.getY() + movSelected[1]) < this.cols) {

                    origin.setDirection(direction);

                    origin = this.cellsGrid[origin.getX() + movSelected[0]][origin.getY() + movSelected[1]];
                }

            }

            if (!origin.isBlank()) {
                destiny = origin;
            }

            origin = this.cellsGrid[rowOriginStart][colOriginStart];
            while (!origin.equals(destiny)) {

                cellsVisited++;
                String direction = origin.getDirection();

                boolean neighbors[] = origin.getNeighbors();
                Cell nextCell = null;
                int[] mov = null;
                switch (direction) {
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

                nextCell = this.cellsGrid[origin.getX() + mov[0]][origin.getY() + mov[1]];
                neighbors = nextCell.getNeighbors();
                switch (direction) {
                    case "N":
                        neighbors[2] = true;
                        break;
                    case "E":
                        neighbors[3] = true;
                        break;
                    case "S":
                        neighbors[0] = true;
                        break;
                    case "O":
                        neighbors[1] = true;
                        break;
                }

                nextCell.setNeighbors(neighbors);
                origin = nextCell;
            }

            if (origin.isBlank()) {
                cellsVisited++;
            }

            origin.setBlank(false);

        }

    }

    
    
    private Cell getCellNoBlank() {
    	Cell c =getCellGet(true);
    	return c;
    }
    
    private Cell getCellEmpty() {
    	Cell c =getCellGet(false);
    	return c;
    }
    
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

    private Cell getCell() {

        Cell c = null;
        int row = this.generaNumeroAleatorio(0, this.rows - 1);
        int col = this.generaNumeroAleatorio(0, this.cols - 1);

        c = this.cellsGrid[row][col];

        return c;

    }

    private void generateCells() {

        this.cells = new TreeMap<>();

        for (int i = 0; i < this.cellsGrid.length; i++) {
            for (int j = 0; j < this.cellsGrid[0].length; j++) {
                this.cells.put(this.cellsGrid[i][j].toString(), this.cellsGrid[i][j]);
            }
        }

    }

    public void generateJSON() throws IOException {

        Gson gson = new Gson();

        this.generateCells();

        System.out.println(gson.toJson(this));

        BufferedWriter bw = new BufferedWriter(new FileWriter("puzzle_" + this.rows + "x" + this.cols + ".json"));

        bw.write(gson.toJson(this));

        bw.close();

    }

    public void generateCellsGrids() throws Exception {

        this.numberCells = this.rows * this.cols;
        this.max_n = 4;

        this.cellsGrid = new Cell[this.rows][this.cols];

        for (Map.Entry<Object, Object> entry : this.cells.entrySet()) {
            String key = (String) entry.getKey();
            LinkedTreeMap value = (LinkedTreeMap) entry.getValue();

            key = key.replace("(", " ").trim();
            key = key.replace(")", " ").trim();
            key = key.replace(" ", "").trim();

            String[] parts = key.split(",");

            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);

            Cell c = new Cell(x, y, false);

            ArrayList<Boolean> n = (ArrayList<Boolean>) value.get("neighbors");

            boolean[] neighbors = new boolean[4];

            for (int i = 0; i < neighbors.length; i++) {
                neighbors[i] = n.get(i);
            }

            c.setNeighbors(neighbors);

            this.cellsGrid[x][y] = c;

        }

        checkCells();

    }

    public void exportToIMG() {

        BufferedImage imagen = new BufferedImage(this.cols * 30, this.rows * 30, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = imagen.createGraphics();

        for (int i = 0; i < this.cellsGrid.length; i++) {
            for (int j = 0; j < this.cellsGrid[0].length; j++) {
                boolean[] n = this.cellsGrid[i][j].getNeighbors();

                if (!n[0]) {
                    g.drawLine((20 * j) + 10, 20 * (i + 1), (20 * j) + 30, 20 * (i + 1));
                }
                if (!n[1]) {
                    g.drawLine((20 * j) + 30, 20 * (i + 1), (20 * j) + 30, 20 * (i + 2));
                }
                if (!n[2]) {
                    g.drawLine((20 * j) + 10, 20 * (i + 1) + 20, (20 * j) + 30, 20 * (i + 1) + 20);
                }
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

    private void checkCells() throws Exception {

        for (int i = 0; i < this.cellsGrid.length; i++) {
            for (int j = 0; j < this.cellsGrid[0].length; j++) {
                Cell c = this.cellsGrid[i][j];
                if (i == 0 && c.getNeighbors()[0]) {
                    throw new Exception();
                }
                if (i == (this.cellsGrid.length - 1) && c.getNeighbors()[2]) {
                    throw new Exception();
                }
                if (j == 0 && c.getNeighbors()[3]) {
                    throw new Exception();
                }
                if (j == (this.cellsGrid[0].length - 1) && c.getNeighbors()[1]) {
                    throw new Exception();
                }
                if (!c.getNeighbors()[0] && !c.getNeighbors()[1] && !c.getNeighbors()[2] && !c.getNeighbors()[3]) {
                    throw new Exception();
                }

                for (int k = 0; k < c.getNeighbors().length; k++) {
                    if (c.getNeighbors()[k]) {
                        
                        switch (k) {
                            case 0:
                                if (!this.cellsGrid[i + this.mov[k][0]][j + this.mov[k][1]].getNeighbors()[2]) {
                                    throw new Exception();
                                }
                                break;
                            case 1:

                                if (!this.cellsGrid[i + this.mov[k][0]][j + this.mov[k][1]].getNeighbors()[3]) {
                                    throw new Exception();
                                }
                                break;
                            case 2:

                                if (!this.cellsGrid[i + this.mov[k][0]][j + this.mov[k][1]].getNeighbors()[0]) {
                                    throw new Exception();
                                }
                                break;
                            case 3:

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

    private int generaNumeroAleatorio(int minimo, int maximo) {
        int num = (int) (Math.random() * (minimo - (maximo + 1)) + (maximo + 1));
        return num;
    }

}

package Laberinto;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);

        boolean salir = false;

        Grid grid = null;
        while (!salir) {

            try {
                System.out.println("Elige una opcion: ");
                System.out.println("1. Generar laberinto");
                System.out.println("2. Exportar imagen");
                System.out.println("3. Importart laberinto");
                System.out.println("4. Salir");

                int option = sc.nextInt();
                switch (option) {
                    case 1:

                        int filas=0;
                    	int columnas=0;
                    	
                        System.out.println("Elige el numero de filas");
                        filas = sc.nextInt();
                        while(filas<=0){
                        	System.out.println("Número de filas incorrecto, introduce un número correcto");
                        	filas = sc.nextInt();
                        }
                        System.out.println("Elige el numero de columnas");
                        columnas = sc.nextInt();
                    	while(columnas<=0) {
                    		System.out.println("Número de columnas incorrecto, introduce un número correcto");
                    		filas = sc.nextInt();
                    		
                    	}
                    	System.out.println("Elige el numero de columnas");
                        columnas = sc.nextInt();

                        grid = new Grid(filas, columnas);
                        grid.generateMaze();
                        grid.generateJSON();

                        break;
                    case 2:
                        if (grid != null) {
                            grid.exportToIMG();
                        } else {
                            System.out.println("Debes generar el laberinto");
                        }

                        break;
                    case 3:

                        try {
                            System.out.println("Indica la ruta del json");
                            String path = sc.next();

                            String jsonContent = getJSON(path);
                            Gson gson = new Gson();

                            grid = gson.fromJson(jsonContent, Grid.class);
                            grid.generateCellsGrids();
                        } catch (Exception ex) {
                        	grid=null;
                            System.out.println("JSON no compatible");
                        }

                        break;
                    case 4:
                        salir = true;
                        break;
                }

            } catch (InputMismatchException ex) {
                System.out.println(ex.getMessage());
                sc.next();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    private static String getJSON(String ruta) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(ruta));

        String linea, json = "";
        while ((linea = br.readLine()) != null) {
            json += linea;
        }
        br.close();

        return json;
    }

}

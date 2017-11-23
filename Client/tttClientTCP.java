//
// TicTacToe
// Selutario - Joseca 
// UGR - Granada (2017)
//

import java.awt.SystemTray;
import java.io.*;
import java.util.Scanner;
import java.net.Socket;
import java.net.UnknownHostException;

public class tttClientTCP {
	// Colores
	static String	ANSI_RESET = "\u001B[0m",
					ANSI_BLACK = "\u001B[30m",
					ANSI_RED = "\u001B[31m",
					ANSI_GREEN = "\u001B[32m",
					ANSI_YELLOW = "\u001B[33m",
					ANSI_BLUE = "\u001B[34m",
					ANSI_PURPLE = "\u001B[35m",
					ANSI_CYAN = "\u001B[36m",
					ANSI_WHITE = "\u001B[37m";

	private static void printMatrix(String game){
		String linea = "";

		switch(game.length()){
			case 9:	 /* 2D Game (Classical) */
				for (int i = 0; i < 3; i ++){
					for (int j = 0; j < 3; j++)
						System.out.print("[" + game.charAt(j + 3*i) + "]");
				
					System.out.print("\n");
				}
				break;
			case 27: /* 3D Game */
				System.out.println("+-----------+-------------+");

				for (int k = 0; k < 3; k++){
					for (int i = 0; i < 3; i ++){
						linea = "|  ";

						if (i == 1)
							linea += "Nivel " + k + "  |  ";
						else 
							linea += "         |  ";

						for (int j = 0; j < 3; j++)
							linea += "[" + game.charAt(j + 3*i +9*k) + "]";
			
						linea += "  |\n";
						System.out.print(linea);
					}

					System.out.println("+-----------+-------------+");
				}
				break;
			case 81: /* 4D Game */
				System.out.println("            +-------------+-------------+-------------+");
				System.out.println("            |  Tablero 0  |  Tablero 1  |  Tablero 2  |");
				System.out.println("+-----------+-------------+-------------+-------------+");

				for (int k = 0; k < 3; k++){
					for (int i = 0; i < 3; i ++){
						linea = "|  ";

						if (i == 1)
							linea += "Nivel " + k;
						else 
							linea += "       ";

						for (int u = 0; u < 3; u++){
							linea += "  |  ";

							for (int j = 0; j < 3; j++)
								linea += "[" + game.charAt(j + 3*i + 9*k + 27*u) + "]";
						}

						linea += "  |\n";
						System.out.print(linea);
					}

					System.out.println("+-----------+-------------+-------------+-------------+");
				}
				break;
			default:
				System.err.println("ERROR: Los datos pasados no forman un tablero válido");
				break;
		}
	}

	private static String getNewToken(String game, String ficha, Scanner capt){
		String respuesta;
		int fil = 0, 
			col = 0, 
			niv = 0, 
			tab = 0, 
			tope = game.length();
		boolean control = true;

		do {
			System.out.print("Introduce casilla a marcar (fil,col" + 
							 ((tope >= 27) ? ",niv" : "") +
							 ((tope == 81) ? ",tab" : "") + "): ");
			respuesta = capt.next();
			
			if (respuesta.length() >= 4){ 
				fil = Character.getNumericValue( respuesta.charAt(1) );
				col = Character.getNumericValue( respuesta.charAt(3) );
				niv = 0;
				tab = 0;
			}

			if (tope >= 27 && respuesta.length() >= 6){
				niv = Character.getNumericValue( respuesta.charAt(5) );

				if (tope == 81 && respuesta.length() >= 8)
					tab = Character.getNumericValue( respuesta.charAt(7) );
			}

			if (0 <= fil && fil <= 2 && 0 <= col && col <= 2 && 
				0 <= niv && niv <= 2 && 0 <= tab && tab <= 2){
				if (game.charAt(col + 3*fil + 9*niv + 27*tab) == '_'){
					respuesta = "";

					for (int i = 0; i < tope; i++){
						if ( (col + 3*fil + 9*niv + 27*tab) == i )
							respuesta += ficha;
						else
							respuesta += game.charAt(i);
					}

					control = false;
				}
			}
		}while(control);

		return respuesta;
	}

	public static void main(String[] args) {	
		// Nombre host, puerto y socket conexión TCP
		String host = "localhost";
		int port = 8989;
		Socket socketServicio = null;

		// Variables para comunicación con el usuario
		Scanner capt = new Scanner(System.in);
		String respuesta = "";
		String dimension = "";
		String turno = "";
		String matrix = "";

		try {
			do{
				// Preguntamos al usuario si quiere empezar una partida
				System.out.print("\n¿Buscar partida? [y/n] ");
				respuesta = capt.next();

				// Nos conectamos al servidor
				if (respuesta.toLowerCase().equals("y")){	
					do{
						System.out.print("¿Cuantas dimensiones? [2/3/4] ");
						dimension = capt.next();
					}while(!dimension.equals("2") && !dimension.equals("3") && !dimension.equals("4"));
	
					// CONEXIÓN ////////////////////////////////////////////////////////////////////////////////////
					System.out.print(ANSI_RED + "\nConectando al servidor... " + ANSI_RESET);

					socketServicio = new Socket(host, port);			
					PrintWriter outPrinter = new PrintWriter(socketServicio.getOutputStream(), true);
					BufferedReader inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
			
					respuesta = inReader.readLine();
					outPrinter.println(dimension);									
					System.out.println(ANSI_GREEN + "Éxito." + ANSI_RESET);			
					///////////////////////////////////////////////////////////////////////////////////////////////
					
					System.out.println("Buscando oponente...\n");
					String tipo_ficha = inReader.readLine();
					System.out.println(ANSI_CYAN + "##############################################");
					System.out.println("# ¡Oponente encontrado! Pondrás las fichas " + ANSI_GREEN + tipo_ficha + ANSI_CYAN + " #");
					System.out.print("##############################################" + ANSI_RESET);

					do{
						turno = inReader.readLine();
						
						if (turno.equals("your_turn")){
							System.out.println("\nTe toca poner ficha");
							matrix = inReader.readLine();
							
							printMatrix(matrix);						
							matrix = getNewToken(matrix, tipo_ficha, capt);
							printMatrix(matrix);
							
							outPrinter.println(matrix);
						}
						else if(turno.equals("end_winner")){
							System.out.println(ANSI_GREEN + "¡HAS GANADO LA PARTIDA!" + ANSI_RESET);
							socketServicio.close();
						}
						else if(turno.equals("end_loser")){
							System.out.println(ANSI_RED + "HAS PERDIDO LA PARTIDA" + ANSI_RESET);
							socketServicio.close();
						}
						else if (turno.equals("end_tie")){
							System.out.println(ANSI_YELLOW + "HABEIS EMPATADO..." + ANSI_RESET);
							socketServicio.close();
						}
					}while(!turno.contains("end"));
				}
				else if (respuesta.toLowerCase().equals("n")){
					System.out.println(ANSI_RED + "\nDesconectando... \n" + ANSI_RESET);
					
					if (socketServicio != null)
						socketServicio.close();

					System.exit(0);
				}
				else{
					System.out.println(" " + respuesta + " no es una opción válida. Por favor, elija [y/n]. ");
					respuesta = "y";
				}		
			}while(true);	
		// Excepciones:
		} catch (UnknownHostException e) {
			System.err.println(ANSI_RED + "Error: Nombre de host no encontrado." + ANSI_RESET);
		} catch (IOException e) {
			System.err.println(ANSI_RED + "Error de entrada/salida al abrir el socket." + ANSI_RESET);
		}
	}
}

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
	private static void printMatrix(String game){
		if (game.length() == 9){
			for (int i = 0; i < 9; i += 3)
				System.out.println(	"[" + game.charAt(i) + "][" + game.charAt(i+1) + "][" + game.charAt(i+2) + "]" );
		}
	}

	private static String getNewToken(String game, String ficha, Scanner capt){
		String respuesta;
		int fil, col;
		boolean control = true;

		System.out.println("Introduce casilla a marcar (fil,col): ");

		do {
			respuesta = capt.next();
			fil = Character.getNumericValue( respuesta.charAt(1) );
			col = Character.getNumericValue( respuesta.charAt(3) );

			if (0 <= fil && fil <= 2 && 0 <= col && col <= 2){
				if (game.charAt(col + 3*fil) == '_'){
					respuesta = "";

					for (int i = 0; i < 9; i++){
						if ( (col + 3*fil) == i )
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
		
        // ID único para el cliente
		String message = "";

		// Variables para comunicación con el usuario
		Scanner capt = new Scanner(System.in);
		String respuesta;
		String dimension;
		String turno;
		String matrix;

		// Colores
		String ANSI_RESET = "\u001B[0m";
		String ANSI_BLACK = "\u001B[30m";
		String ANSI_RED = "\u001B[31m";
		String ANSI_GREEN = "\u001B[32m";
		String ANSI_YELLOW = "\u001B[33m";
		String ANSI_BLUE = "\u001B[34m";
		String ANSI_PURPLE = "\u001B[35m";
		String ANSI_CYAN = "\u001B[36m";
		String ANSI_WHITE = "\u001B[37m";
		
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
			
					message = inReader.readLine();
					outPrinter.println(dimension);									
					System.out.println(ANSI_GREEN + "Éxito." + ANSI_RESET);			
					///////////////////////////////////////////////////////////////////////////////////////////////
					
					System.out.println("Buscando oponente...\n\n");
					String tipo_ficha = inReader.readLine();
					System.out.println(ANSI_CYAN + "###############################################");
					System.out.println("# ¡Oponente encontrado! Pondrás las fichas " + ANSI_GREEN + tipo_ficha + ANSI_CYAN + "##");
					System.out.println("###############################################" + ANSI_RESET);

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
						else if(turno.equals("end_winner"))
							System.out.println(ANSI_GREEN + "¡HAS GANADO LA PARTIDA!" + ANSI_RESET);
						else if(turno.equals("end_loser"))
							System.out.println(ANSI_RED + "HAS PERDIDO LA PARTIDA" + ANSI_RESET);
						else if (turno.equals("end_tie"))
							System.out.println(ANSI_YELLOW + "HABEIS EMPATADO..." + ANSI_RESET);
					}while(!turno.contains("end"));
				}
				else if (respuesta.toLowerCase().equals("n")){
					System.out.println(ANSI_RED + "Desconectando... \n" + ANSI_RESET);
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

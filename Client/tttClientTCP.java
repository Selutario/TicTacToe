//
// TicTacToe
// Selutario - Joseca 
// UGR - Granada (2017)
//

import java.io.*;
import java.util.Scanner;
import java.net.Socket;
import java.net.UnknownHostException;

public class tttClientTCP {

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
				if(respuesta.toLowerCase().equals("y")){
					
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
					

					System.out.println("Buscando oponente...\n\n\n");
					String tipo_ficha = inReader.readLine();
					System.out.println(ANSI_CYAN + "###########################################");
					System.out.println("¡Oponente encontrado!. Pondrás las fichas " + ANSI_GREEN + tipo_ficha + ANSI_CYAN);
					System.out.println("###########################################" + ANSI_RESET);

					/*######################################
					# EJEMPLO SIMPLE A FALTA DEL ALGORITMO #
					######################################*/
					do{
						//outPrinter.println("still_here");
						turno = inReader.readLine();
						
						if(turno.equals("your_turn")){
							System.out.println("\nTe toca poner ficha");
							respuesta = capt.next();
							outPrinter.println(respuesta);
						}
						else if(turno.equals("wait")){
							System.out.println("\nLe toca poner ficha al oponente");
							System.out.println("El oponente ha puesto: " + inReader.readLine());
						}
						else if(turno.equals("end_winner"))
							System.out.println(ANSI_GREEN + "¡HAS GANADO LA PARTIDA!" + ANSI_RESET);
						else if(turno.equals("end_loser"))
							System.out.println(ANSI_RED + "HAS PERDIDO LA PARTIDA" + ANSI_RESET);
	
					}while(!turno.contains("end"));
				
				}
				else if(respuesta.toLowerCase().equals("n")){
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

//
// TicTacToe
// Selutario - Joseca 
// UGR - Granada (2017)
//
import java.io.*;
import java.util.ArrayList;
import java.util.Queue;
import java.net.ServerSocket;
import java.net.Socket;

public class tttServerTCP {
	private static ArrayList<Processor> games = new ArrayList<Processor>();

	private static int joinGame(int dimension){
		for (int i = 0; i< games.size(); i++){
			if (games.get(i).getGameDimension() == dimension)
				return i;
		}

		return -1;
	}

	private static void cleanFinishedGames(){
		for (int i = 0; i < games.size(); i++){
			if (games.get(i).ended())
				games.remove(i);
		}
	}

	public static void main(String[] args) {
		// Declaración de servidor TCP
		ServerSocket socketServidor;
		Socket unSocket = null;

		// Variables de tipo entero
		int game, vueltas = 0;
		int port = 8989;
		int dimension;
	

		// Canales de comunicación
		PrintWriter outPrinter;
		BufferedReader inReader;
	
		// PONER VARIABLE PARTIDA ACABADA EN PROCESADORES
		try {
			// Abrimos el socket en modo pasivo, escuchando el en puerto indicado por "port"
			socketServidor = new ServerSocket(port);

			do {			
				// Aceptamos una nueva conexión con accept()
				vueltas++;
				unSocket = socketServidor.accept();
				outPrinter = new PrintWriter(unSocket.getOutputStream(), true);
				inReader = new BufferedReader(new InputStreamReader(unSocket.getInputStream()));
				
				// Recibimos la petición de iniciar partida
				outPrinter.println("OK");
				dimension = Integer.parseInt( inReader.readLine() );
				game = joinGame(dimension);

				if (game == -1){
					System.out.println("ANTES DEL ARRAY");
					games.add(new Processor(unSocket, dimension));
					System.out.println("Nueva partida. Total partidas: " + games.size());	
				}
				else {
					games.get(game).addPlayer(unSocket);
					games.get(game).start();
				}

				if (vueltas == 10){
					cleanFinishedGames();
					vueltas = 0;
				}
			} while (true);			
		} catch (IOException e) {
			System.err.println("Error al escuchar en el puerto " + port);
		}
	}
}

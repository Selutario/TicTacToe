//
// TicTacToe
// Selutario - Joseca 
// UGR - Granada (2017)
//
import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.ArrayList;

//
// Nota: si esta clase extendiera la clase Thread, y el procesamiento lo hiciera el método "run()",
// ¡Podríamos realizar un procesado concurrente! 
//
public class Processor extends Thread{
	// Referencia a un socket para enviar/recibir las peticiones/respuestas
	private Socket socketPlayer1 = null;
	private Socket socketPlayer2 = null;
	private ArrayList<PrintWriter> outPrinters = new ArrayList<PrintWriter>();
	private ArrayList<BufferedReader> inReaders = new ArrayList<BufferedReader>();
	private boolean started = false, 
					ended = false;
	int first, second;
	int dimension;
	String respuesta;
	Grid ttt;

	// Constructor que tiene como parámetro una referencia al socket abierto en por otra clase
	public Processor(Socket socketPlayer1, int dimension){
		this.socketPlayer1 = socketPlayer1;

		if (dimension == 2)
			ttt = new ttt2D();
		else if (dimension == 3)
			ttt = new ttt3D();
		else if (dimension == 4)
			ttt = new ttt4D();
	}

	public void addPlayer(Socket socketPlayer2){
		this.socketPlayer2 = socketPlayer2;
	}

	public boolean ended(){
		return ended;
	}

	public boolean started(){
		return started;
	}

	public int getGameDimension(){
		return ttt.getDimension();
	}

	public void run() {
        procesa();
	}
	
	// Aquí es donde se realiza el procesamiento realmente:
	void procesa(){	
		try {
			// Obtiene los flujos de escritura/lectura - PLAYER 1
			outPrinters.add(new PrintWriter(socketPlayer1.getOutputStream(), true));
			inReaders.add(new BufferedReader(new InputStreamReader(socketPlayer1.getInputStream())));
			// Obtiene los flujos de escritura/lectura - PLAYER 2
			outPrinters.add(new PrintWriter(socketPlayer2.getOutputStream(), true));
			inReaders.add(new BufferedReader(new InputStreamReader(socketPlayer2.getInputStream())));
			
			//########################################
			//# EJEMPLO SIMPLE A FALTA DEL ALGORITMO #
			//########################################
			outPrinters.get(first).println("X");						
			outPrinters.get(second).println("O");
			
			outPrinters.get(first).println("your_turn");	
			outPrinters.get(second).println("wait");
			String respuesta = inReaders.get(first).readLine();
			outPrinters.get(second).println(respuesta);

			outPrinters.get(second).println("your_turn");	
			outPrinters.get(first).println("wait");
			respuesta = inReaders.get(second).readLine();
			outPrinters.get(first).println(respuesta);

			outPrinters.get(first).println("end_winner");
			outPrinters.get(second).println("end_loser");


			// CON FUNCIONES DE GRID
			// do{

			// 	outPrinters.get(first).println("your_turn");	
			// 	outPrinters.get(second).println("wait");
			// 	respuesta = inReaders.get(first).readLine();
			// 	ttt.updateMatrix(respuesta);
			// 	outPrinters.get(second).println( ttt.getStringMatrix() );
	
			// 	outPrinters.get(second).println("your_turn");	
			// 	outPrinters.get(first).println("wait");
			// 	respuesta = inReaders.get(second).readLine();
			// 	ttt.updateMatrix(respuesta);
			// 	outPrinters.get(first).println( ttt.getStringMatrix() );

			// }while(ttt.IsTheWinner() == "not_ended");


			outPrinters.get(first).println("end_winner");
			outPrinters.get(second).println("end_loser");
			ended = true;

			
		} catch (IOException e) {
			System.err.println("Error al obtener los flujos de entrada/salida.");
		}
	}
}

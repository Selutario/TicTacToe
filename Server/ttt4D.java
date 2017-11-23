/*
 * Estructura para el juego en 4 dimensiones
 */

import java.util.ArrayList;

/**
 *
 * @author Joseca & Selu
 */
public class ttt4D extends Grid {
    private ArrayList<ttt3D> tableros = new ArrayList<ttt3D>();
    
    public ttt4D(){
        super(4);

        for (int i = 0; i < 3; i++)
            tableros.add( new ttt3D() );
    }
    
    public ttt4D(int jugador){
        super(jugador, 4);

        for (int i = 0; i < 3; i++)
            tableros.add( new ttt3D() );
    }

    public char get(int u, int k, int i, int j){
        return tableros.get(u).get(k, i, j);
    }

    public String getStringMatrix(){
        String matrix = "";

        for (int i = 0; i < 3; i++)
            matrix += tableros.get(i).getStringMatrix();

        return matrix;
    }

    public void setStringMatrix(String matrix){
        String nivel;

        for (int i = 0; i < 3; i++){
            nivel = "";

            for (int j = 0; j < 27; j++)
                nivel += matrix.charAt(j + 27*i);
            
            tableros.get(i).setStringMatrix(nivel);
        }
    }

    public void updateMatrix(String matrix){
        String nivel;
        
        for (int i = 0; i < 3; i++){
            nivel = "";
        
            for (int j = 0; j < 27; j++)
                nivel += matrix.charAt(j + 27*i);
                    
            tableros.get(i).updateMatrix(nivel);
        }
    }

    protected boolean emptySquares(){
        for (int i = 0; i < 3; i++){
            if (tableros.get(i).emptySquares())
                return true;
        }

        return false;
    }

    private int getPosSquare(int i, int j, int k, int u){
        return j + 3*i + 9*k + 27*u;
    }

    protected ArrayList<ArrayList<Integer>> getSquaresCoordinates(char token){
        ArrayList<ArrayList<Integer>> coordenadas = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> tablero;

        for (int i = 0; i < 3; i++){
            tablero = tableros.get(i).getSquaresCoordinates(token);

            for (int j = 0; j < tablero.size(); j++){
                tablero.get(j).add(i);
                coordenadas.add(tablero.get(j));
            }
        }

        return coordenadas;
    }

    protected ArrayList<Integer> getSquares(char token){
        ArrayList<Integer> fichas = new ArrayList<Integer>(),
                           un_tablero;

        for (int i = 0; i < 3; i++){
            un_tablero = tableros.get(i).getSquares(token);

            for (int j = 0; j < un_tablero.size(); j++)
                fichas.add( un_tablero.get(j) + 27*i );
        }

        return fichas;
    }
    
    public tttState isItTheWinner(char token){
        for (int i = 0; i < 3; i++){
            if (tableros.get(i).isItTheWinner(token) == tttState.VICTORY)
                return tttState.VICTORY;
        }

        for (int k = 0; k < 3; k++){
            for (int i = 0; i < 3; i++){
                for (int j = 0; j < 3; j++){
                    if ( token == get(0, k, i, j) ){
                        if (get(0, k, i, j) == get(1, k, i, j) && 
                            get(0, k, i, j) == get(2, k, i, j))
                            return tttState.VICTORY;
                    }
                }
            }
        }
        
        if (!emptySquares())
            return tttState.TIE;

        return tttState.NOT_VICTORY;
    }
}

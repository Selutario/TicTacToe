/**
 * Clase abstracta para la creación de las estructuras del juego
 */

import java.util.*;

/**
 *
 * @author Joseca & Selu
 */
public abstract class Grid {
    private Integer jugador1 = 0, 
                    jugador2 = 0;
    private Integer D;

    public Grid(int dimension){
        D = dimension;
    }

    public Grid(int jugador, int dimension){
        jugador1 = jugador;
        D = dimension;
    }

    public boolean addPlayer(int jugador){
        if (jugador1 == 0)
            jugador1 = jugador;   
        else{
            if (jugador2 == 0)
                jugador2 = jugador;
            else
                return false;    
        }

        return true;
    }

    public int getDimension(){
        return D;
    }

    public int getPlayer1(){
        return jugador1;
    }
    public int getPlayer2(){
        return jugador2;
    }

    public abstract String getStringMatrix();
    
    public abstract void setStringMatrix(String matrix);

    public abstract void updateMatrix(String matrix);
    
    public abstract tttState isItTheWinner(char token);

    protected abstract boolean emptySquares();

    protected abstract ArrayList<ArrayList<Integer>> getSquaresCoordinates(char token);

    protected abstract ArrayList<Integer> getSquares(char token);
}

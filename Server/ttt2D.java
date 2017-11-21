/*
 * Estructura para el juego clásico (2 Dimensiones)
 */

import java.util.ArrayList;

/**
 *
 * @author Joseca & Selu
 */
public class ttt2D extends Grid {
    private Character[][] casillas = {{'_', '_', '_'},
                                      {'_', '_', '_'},
                                      {'_', '_', '_'}};

    public ttt2D(){
        super(2);
    }

    public ttt2D(int jugador1){
        super(jugador1, 2);
    }

    public char get(int i, int j){
        return casillas[i][j];
    }

    public String getStringMatrix(){
        String matrix = "";

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                matrix += casillas[i][j];

        return matrix;
    }

    public void setStringMatrix(String matrix){
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                casillas[i][j] = matrix.charAt(j + 3*i);
    }

    public void updateMatrix(String matrix){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if ( casillas[i][j] != matrix.charAt(j + 3*i) )
                    casillas[i][j] = matrix.charAt(j + 3*i);
            }
        }
    }

    protected boolean emptySquares(){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (casillas[i][j] == '_')
                    return true;
            }
        }

        return false;
    }

    private int getPosSquare(int i, int j){
        return j + 3*i;
    }

    private ArrayList<Integer> getSquares(char token){
        ArrayList<Integer> fichas = new ArrayList<Integer>();

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (casillas[i][j] == token)
                    fichas.add(j + 3*i);
            }
        }

        return fichas;
    }

    protected ArrayList<ArrayList<Integer>> getSquaresCoordinates(char token){
        ArrayList<ArrayList<Integer>> coordenadas = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> par;

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (casillas[i][j] == token){
                    par = new ArrayList<Integer>();

                    par.add(i);
                    par.add(j);
                    coordenadas.add(par);
                }
            }
        }

        return coordenadas;
    }

    public tttState isItTheWinner(char token){
        ArrayList<Integer> fichas = getSquares(token);
        
        if (fichas.size() >= 3){
            for (int i = 0; i < fichas.size(); i++){
                for (int j = i + 1; j < fichas.size(); j++){
                    for (int k = j + 1; k < fichas.size(); k++){
                        switch(fichas.get(i)){
                            case 0:
                                switch(fichas.get(j)){
                                    case 1:
                                        if (fichas.get(k) == 2)
                                            return tttState.VICTORY;
                                        break;
                                    case 3:
                                        if (fichas.get(k) == 6)
                                            return tttState.VICTORY;
                                        break;
                                    case 4:
                                        if (fichas.get(k) == 8)
                                            return tttState.VICTORY;
                                        break;
                                }
                                break;
                            case 1:
                                if (fichas.get(j) == 4)
                                    if (fichas.get(k) == 7)
                                        return tttState.VICTORY;
                                break;
                            case 2:
                                if (fichas.get(j) == 4)
                                    if (fichas.get(k) == 6)
                                        return tttState.VICTORY;
                                else if (fichas.get(j) == 5)
                                    if (fichas.get(k) == 8)
                                        return tttState.VICTORY;
                                break;
                            case 3:
                                if (fichas.get(j) == 4)
                                    if (fichas.get(k) == 5)
                                        return tttState.VICTORY;
                                break;
                            case 6:
                                if (fichas.get(j) == 7)
                                    if (fichas.get(k) == 8)
                                        return tttState.VICTORY;
                                break;
                        }
                    }
                }
            }
        }

        if (!emptySquares())
            return tttState.TIE;

        return tttState.NOT_VICTORY;
    }
}

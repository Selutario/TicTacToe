/*
 * Estructura para el juego en 3 dimensiones
 */

import java.util.ArrayList;

/**
 *
 * @author Joseca & Selu
 */
public class ttt3D extends Grid {
    private ArrayList<ttt2D> niveles = new ArrayList<ttt2D>();
    
    public ttt3D(){
        super(3);

        for (int i = 0; i < 3; i++)
            niveles.add( new ttt2D() );
    }
    
    public ttt3D(int jugador){
        super(jugador, 3);

        for (int i = 0; i < 3; i++)
            niveles.add( new ttt2D() );
    }

    public char get(int k, int i, int j){
        return niveles.get(k).get(i, j);
    }

    public String getStringMatrix(){
        String matrix = "";

        for (int i = 0; i < 3; i++)
            matrix += niveles.get(i).getStringMatrix();

        return matrix;
    }

    public void setStringMatrix(String matrix){
        String nivel;

        for (int i = 0; i < 3; i++){
            nivel = "";

            for (int j = 0; j < 9; j++)
                nivel += matrix.charAt(j + 9*i);
            
            niveles.get(i).setStringMatrix(nivel);
        }
    }

    public void updateMatrix(String matrix){
        String nivel;
        
        for (int i = 0; i < 3; i++){
            nivel = "";
        
            for (int j = 0; j < 9; j++)
                nivel += matrix.charAt(j + 9*i);
                    
            niveles.get(i).updateMatrix(nivel);
        }
    }

    protected boolean emptySquares(){
        for (int i = 0; i < 3; i++){
            if (niveles.get(i).emptySquares())
                return true;
        }

        return false;
    }

    private int getPosSquare(int i, int j, int k){
        return j + 3*i + 9*k;
    }

    protected ArrayList<ArrayList<Integer>> getSquaresCoordinates(char token){
        ArrayList<ArrayList<Integer>> coordenadas = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> nivel;

        for (int i = 0; i < 3; i++){
            nivel = niveles.get(i).getSquaresCoordinates(token);

            for (int j = 0; j < nivel.size(); j++){
                nivel.get(j).add(i);
                coordenadas.add(nivel.get(j));
            }
        }

        return coordenadas;
    }
    
    public tttState isItTheWinner(char token){
        ArrayList<ArrayList<Integer>> fichas = getSquaresCoordinates(token);
        int pos;
        
        if (fichas.size() >= 3){
            for (int i = 0; i < fichas.size(); i++){
                pos = getPosSquare(fichas.get(i).get(0), fichas.get(i).get(1), fichas.get(i).get(2));
                
                if ( (0 <= pos && pos < 13) || pos == 15 || (18 <= pos && pos < 22) || pos == 24 ){ 
                    for (int j = i+1; j < fichas.size(); j++){
                        if (Math.abs(fichas.get(i).get(0) - fichas.get(j).get(0)) != 2 &&
                            Math.abs(fichas.get(i).get(1) - fichas.get(j).get(1)) != 2 &&
                            Math.abs(fichas.get(i).get(2) - fichas.get(j).get(2)) != 2){
                            
                            for (int k = j+1; k < fichas.size(); k++){
                                if (fichas.get(i).get(0) == fichas.get(i).get(1) &&
                                    fichas.get(j).get(0) == fichas.get(j).get(1) &&
                                    fichas.get(k).get(0) == fichas.get(k).get(1))
                                    return tttState.VICTORY;
                                else if (fichas.get(i).get(0) == fichas.get(i).get(2) &&
                                        fichas.get(j).get(0) == fichas.get(j).get(2) &&
                                        fichas.get(k).get(0) == fichas.get(k).get(2))
                                    return tttState.VICTORY;
                                else if (fichas.get(i).get(1) == fichas.get(i).get(2) &&
                                        fichas.get(j).get(1) == fichas.get(j).get(2) &&
                                        fichas.get(k).get(1) == fichas.get(k).get(2))
                                    return tttState.VICTORY;
                            }
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

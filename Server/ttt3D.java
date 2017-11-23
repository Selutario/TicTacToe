/*
 * Estructura para el juego en 3 dimensiones
 */

import java.lang.reflect.Array;
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

    protected ArrayList<Integer> getSquares(char token){
        ArrayList<Integer> fichas = new ArrayList<Integer>(),
                           un_nivel;

        for (int i = 0; i < 3; i++){
            un_nivel = niveles.get(i).getSquares(token);

            for (int j = 0; j < un_nivel.size(); j++)
                fichas.add( un_nivel.get(j) + 9*i );
        }

        return fichas;
    }
    
    public tttState isItTheWinner(char token){
        int[][] movimientos_posibles = {{1, 1, 1}, {1, 0, 0}, {0, 1, 0}, {0, 0, 1}, {1, 1, 0},
                                        {1, 0, 1}, {0, 1, 1}, {1, -1, 0}, {1, -1, 1}, {0, -1, 1},
                                        {-1, -1, 1}, {-1, 0, 1}, {-1, 1, 1}};
        int[] movimiento = {0, 0, 0};
        ArrayList<ArrayList<Integer>> fichas = getSquaresCoordinates(token);
        int pos;
        boolean seguir;
        
        if (fichas.size() >= 3){
            for (int i = 0; i < fichas.size(); i++){
                pos = getPosSquare(fichas.get(i).get(0), fichas.get(i).get(1), fichas.get(i).get(2));

                if ( (0 <= pos && pos < 13) || pos == 15 || (18 <= pos && pos < 22) || pos == 24 ){ 
                    for (int j = i+1; j < fichas.size(); j++){
                        seguir = false;

                        for (int mov = 0; mov < 13 && !seguir; mov++){
                            if ( fichas.get(j).get(0) == fichas.get(i).get(0) + movimientos_posibles[mov][0] &&
                                 fichas.get(j).get(1) == fichas.get(i).get(1) + movimientos_posibles[mov][1] &&
                                 fichas.get(j).get(2) == fichas.get(i).get(2) + movimientos_posibles[mov][2] ){
                                movimiento = movimientos_posibles[mov];
                                seguir = true;
                            }
                        }

                        if (seguir){
                            for (int k = j+1; k < fichas.size(); k++){
                                if ( fichas.get(k).get(0) == fichas.get(j).get(0) + movimiento[0] &&
                                     fichas.get(k).get(1) == fichas.get(j).get(1) + movimiento[1] &&
                                     fichas.get(k).get(2) == fichas.get(j).get(2) + movimiento[2] )
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

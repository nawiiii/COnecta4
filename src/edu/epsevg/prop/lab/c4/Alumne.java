package edu.epsevg.prop.lab.c4;

/**
 * Jugador aleatori
 * "Alea jacta est"
 * @author 
 */
public class Alumne
  implements Jugador, IAuto
{
  private String nom;

  int[][] matriuPesos = { {3, 4, 5, 7, 7, 5, 4, 3}, 
                          {4, 6, 8, 1, 1, 8, 6, 4}, 
                          {5, 8, 11, 1, 1, 11, 8, 5}, 
                          {5, 8, 11, 1, 1, 11, 8, 5}, 
                          {5, 8, 11, 1, 1, 11, 8, 5}, 
                          {5, 8, 11, 1,1, 1, 8, 5}, 
                          {4, 6, 8, 7, 7, 8, 6, 4},
                          {3, 4, 5,  7,  7, 5, 4, 3}};
  
  public Alumne()
  {
    nom = "Alumne";
  }
  
  public int heuristica(Tauler t, int color) 
  {
    Tauler ct = new Tauler(t);
    int max = matriuPesos[0][0];
    Boolean millorTiradaTrobada = false;
    int r = 0;
    for (int i = 0; i < 8; ++i) {
        for (int j = 0; j < 8; ++j) {
            millorTiradaTrobada = (ct.getColor(i, j) == 0 && matriuPesos[i][j] > max);
            System.out.println("mp: " + matriuPesos[i][j]);
            System.out.println("mtt: " + millorTiradaTrobada);
            if (millorTiradaTrobada) {
                max = matriuPesos[i][j];
                r = j;
                System.out.println("r: " + r);
            }
        }
        
    }
    
   return r;
  }
  
  @Override
  public int moviment(Tauler t, int color)
  {    
    int col=0;    
// t.pintaTaulerALaConsola(); // Fer servir puntualment per depurar. (Sino -> Mal rendiment)
      try {
    col = heuristica(t, color);
      }catch(Exception e) {e.printStackTrace();}
    return col;
  }
  
  @Override
  public String nom()
  {
    return nom;
  }
}

/*
3 4 5 5 4 3 4 6 8 8 6 4 5 8
1 1 1 1 8 5 7
1 0 1 3 1 3 1
0 7 5 8 1 1 1
1 8 5 4 6 8 8 
6 4 3 4 5 5 4 3

*/


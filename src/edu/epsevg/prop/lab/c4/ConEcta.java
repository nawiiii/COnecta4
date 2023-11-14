package edu.epsevg.prop.lab.c4;

import java.lang.*;

/*
    Preguntes:
    - Cal crear constructor per defecte? -> Juga2.java 
      es pot modificar?
    - Quina es la profunditat per defecte per a començar a explorar?

  --------

  MINIMAX:
    Jo busco maxim benefici, enemic minimitza.

    Si JO començo -> som maxim.
    Si ENEMIC começa -> som minim.
*/

/**
 * Jugador aleatori
 * "Alea jacta est"
 * @author 
 */
public class ConEcta implements Jugador, IAuto 
{
  // Variables globals.
  private int MAX = Integer.MAX_VALUE; // ∞
  private int MIN = Integer.MIN_VALUE; // -∞
  private enum Dir {HORITZONTAL, VERTICAL, DIAGONAL_DRT, DIAGONAL_ESQ}
  
  private boolean primeraJugada = true;
  private boolean hemComencat;
  int colorEnemic = 0;
  int colorNostre = 0;
  
  // Atributs de la classe.
  private String _nom;
  private int _taulersExplorats;
  private int _profunditatInicial = 8;
  private Boolean _somPrimers = false;
  private Boolean _ambPoda;
  private Dir _dir;
  // Matriu de pesos inicial.
  private int[][] _matriuPesos = {{3, 4,  5, 7, 7, 5, 4, 3}, 
                                 {4, 6,  8, 11, 11, 8, 6, 4}, 
                                 {5, 8, 11, 13, 13, 11, 8, 5}, 
                                 {5, 8, 11, 13, 13, 11, 8, 5}, 
                                 {5, 8, 11, 13, 13, 11, 8, 5}, 
                                 {5, 8, 11, 13, 13, 11, 8, 5}, 
                                 {4, 6, 8, 7, 7, 8, 6, 4},
                                 {3, 4, 5,  7,  7, 5, 4, 3}};

  /**
   * CONSTRUCTOR de Jugador ConEcta. Busca jugades fins la profunditat prof.
   * @param prof: Profunditat maxima fins la que es cercarà a l'arbre de jugades.
   *              Ha de ser major a 1 (simular com a minim 2 tirades) i un nombre parell.
   * @param ambPoda: Indiquem si volem fer minimax amb/sense poda alpha-beta. 
  */
  public ConEcta(int prof, boolean ambPoda) throws Exception 
  {
    _nom = "ConEcta";
    _taulersExplorats = 0;
    _ambPoda = false;
    _profunditatInicial = prof;
    if (prof < 1 || prof % 2 != 0) {
      throw new Exception("Ha de ser major a 1 (simular com a minim 2 tirades). I un nombre parell.");
    }
  } 
  
  /**
   * Funció min de minimax.
   * @param t:
   * @param color: Jugador que tira la jugada.
   * @param profunditat:
   * @param col:
  */
  private int min(Tauler t, int color, int profunditat, int col) // min: col 
  {
    int millorValor = MAX;
    Tauler tc = new Tauler(t);
    tc.afegeix(col, color);
    
    // Fi partida si : guanyo jo o guanya enemic o no es poden posar mes peçes al tauler (empat).
//    Boolean fiPartida = tc.solucio(col, color) || tc.solucio(col, -color);
//    
//    if (fiPartida) {
//      if (t.solucio(col, color)) { 
//        return MAX;
//      } 
//      else if (t.solucio(col, -color)) 
//      {
//        return MIN;
//      } 
//      else {
//        return 0;
//      }
//    }
    
  if (profunditat > 0){
     System.out.println("PROF min: "+ profunditat);
    for (int colJugada = 0; colJugada < t.getMida(); colJugada++) {
      if (tc.movpossible(colJugada)) {
        tc.afegeix(colJugada, color);
        
        int nouValor = max(tc, -color, profunditat - 1, colJugada);
        millorValor = Math.min(millorValor, nouValor);
      }
    }
  }
    if ((profunditat == 0)) {
      int h = evalua(t, color);
      return h;
    }

    return millorValor;
  }

  /**
   * Funció max de minimax.
   */
  public int max(Tauler t, int color, int profunditat, int col)
  {
     int millorValor = MIN;
    Tauler tc = new Tauler(t);
    tc.afegeix(col, color);
    
    // Fi partida si : guanyo jo o guanya enemic o no es poden posar mes peçes al tauler (empat).
//    Boolean fiPartida = tc.solucio(col, color) || tc.solucio(col, -color);
//    
//    if (fiPartida) {
//      if (t.solucio(col, color)) { 
//        return MAX;
//      } 
//      else if (t.solucio(col, -color)) 
//      {
//        return MIN;
//      } 
//      else {
//        return 0;
//      }
//    }
    if (profunditat > 0){
      System.out.println("PROF max: "+ profunditat);
    for (int colJugada = 0; colJugada < t.getMida(); colJugada++) {
      if (tc.movpossible(colJugada)) {
        tc.afegeix(colJugada, color);
        
        int nouValor = min(tc, -color, profunditat - 1, colJugada);
        millorValor = Math.max(millorValor, nouValor);
      }
    }
    }

    if ((profunditat == 0)) {
      int h = evalua(t, color);
      return h;
    }

    return millorValor;
  }
  
  // Cal recalcular la matriu una vegada s'ha fet una tirada. 
  // Te prioritat bloquejar fitxes enemigues consecutives.
  // No es molt bona ja que inicialment nomes és necessaria 
  // la última fila de la matriu.
  public int heuristica1(Tauler t, int color) 
  {
    _taulersExplorats += 1; // Comptem quantes vegades hem calulat l'heuristica.
    Tauler ct = new Tauler(t);
    int max = _matriuPesos[0][0];
    Boolean millorTiradaTrobada = false;
    int r = 0;
    for (int i = 0; i < 8; ++i) {
      for (int j = 0; j < 8; ++j) {
        millorTiradaTrobada = (ct.getColor(i, j) == 0 && _matriuPesos[i][j] > max);
        System.out.println("mp: " + _matriuPesos[i][j]);
        System.out.println("mtt: " + millorTiradaTrobada);
        if (millorTiradaTrobada) {
          max = _matriuPesos[i][j];
          r = j;
          System.out.println("r: " + r);
        }
      }
    }

    return r;
  }
  
  /**
   *  Calculem l'heuristica segons el nombre de fitxes consecutives al tauler en 
   *  Horitzontal, vertical o diagonal esq, diagonal dreta.  
   */
//  public int heuristica2(Tauler t)
//  {
//    // per vertical (modifiquem c): [f, c], [f, c+1], [f, c+2], [f, c+3]
//    // per horitzontal (modifiquem f): [f, c], [f+1, c], [f+2, c], [f+3, c]
//    // per diagonal dreta/esq: [f, c], [f+1, c+1], [f+2, c+2], [f+3, c+3]
//    //                         []
//    int centre = t.getMida() / 2;
//    for (int f = 0; f < t.getMida(); ++f) {
//      for (int c = centre; c > 0; --c) {
//        
//      }
//    }
//    
//    return 0;
//  }
  
  public int contar(int[] casellas, int color){
    int count = 0;
    for(int casella: casellas){
        if(casella==color) ++count;
    }
    return count;
  }
  
  public int modificaMatPesos(Tauler t, int color, Dir d, int i, int j) 
  {
    int h = 0, comptFitxSeg = 0;
    boolean hiHaSeg = true, hiHaSegEsq = true, hiHaSegDret = true;
    int colFitxAdjSup = 0;
    int colFitxAdjInf = 0;
    
    switch (d) {
      case VERTICAL:
        while (hiHaSeg) 
        { 
          int clrFitxAdj = t.getColor(i + (comptFitxSeg + 1), j);
          
          if (clrFitxAdj == color) {
            ++comptFitxSeg;
            if (comptFitxSeg + 1 == 3) {
              hiHaSeg = false;
              h = MAX;
              _matriuPesos[i+comptFitxSeg][j] = h; // Si trobem 3 seguides -> Seg Jugada es Victoria.
              break;
            }
          } 
          else if (clrFitxAdj == -color) { // Si es enemic
            hiHaSeg = false;
          }
          else if (clrFitxAdj == 0) {
            if (comptFitxSeg == 0) {
              h += 1;
            }
            else if (comptFitxSeg == 1) {
              h += 5;
            }
            else if (comptFitxSeg == 2) {
              h += 10;
            }
            _matriuPesos[i+comptFitxSeg][j] = h;
            break;
          }
        }
        break;
        case HORITZONTAL:
          hiHaSegEsq = (t.getColor(i, j - (comptFitxSeg + 1)) == color);
          hiHaSegDret = (t.getColor(i, j + (comptFitxSeg + 1)) == color);
          
          // Fitxes a l'ESQUERRA:
          while (hiHaSegEsq) {
            int colFitxAdj = t.getColor(i, j - (comptFitxSeg + 1));
            if (colFitxAdj == color) {
              ++comptFitxSeg;
              if (comptFitxSeg == 3) {
                hiHaSegDret = false;
                h = MAX;
                _matriuPesos[i][j - (comptFitxSeg + 1)] = h;
              }
            }
            else if (colFitxAdj == -color) {
              hiHaSegEsq = false;
              break;
            }
            else if (colFitxAdj == 0) {
              if (comptFitxSeg == 0) {
                h += 1;
              } 
              else if (comptFitxSeg == 1) {
                h += 5;
              }
              else if (comptFitxSeg == 2) {
                h += 10;
              }
              _matriuPesos[i][j - (comptFitxSeg + 1)] = h;
              break;
            }
          }
          
          
          // Fitxes a la DRETA:
          while (hiHaSegDret) {
            int colFitxAdj = t.getColor(i, j + (comptFitxSeg + 1));
            if (colFitxAdj == color) {
              ++comptFitxSeg;
              if (comptFitxSeg == 3) {
                hiHaSegDret = false;
                h = MAX;
                _matriuPesos[i][j + (comptFitxSeg + 1)] = h;
              }
            }
            else if (colFitxAdj == -color) {
              hiHaSegDret = false;
              break;
            }
            else if (colFitxAdj == 0) {
              if (comptFitxSeg == 0) {
                h += 1;
              } 
              else if (comptFitxSeg == 1) {
                h += 5;
              }
              else if (comptFitxSeg == 2) {
                h += 10;
              }
              _matriuPesos[i][j + (comptFitxSeg + 1)] = h;
              break;
            }
          }
        break;
        case DIAGONAL_ESQ:
          colFitxAdjSup = t.getColor(i + 1, j - 1);
          colFitxAdjInf = t.getColor(i - 1, j - 1);
          
          if (colFitxAdjInf == color || colFitxAdjSup == color)
            System.out.println("HI HA DIAGONAL ESQ AMIGA");
          else if (colFitxAdjInf == -color || colFitxAdjSup == -color) 
            System.out.println("HI HA DIAGONAL ESQ ENEMIGA");
          else if (colFitxAdjInf == 0) {
            System.out.println("HI HA DIAGONAL ESQ BUIDA");
          }
          break;
        case DIAGONAL_DRT:
          colFitxAdjSup = t.getColor(i + 1, j + 1);
          colFitxAdjInf = t.getColor(i - 1, j + 1);
          
          if (colFitxAdjInf == color || colFitxAdjSup == color)
            System.out.println("HI HA DIAGONAL DRETA AMIGA");
          else if (colFitxAdjInf == -color || colFitxAdjSup == -color) 
            System.out.println("HI HA DIAGONAL DRETA ENEMIGA");
          else if (colFitxAdjInf == 0) {
            System.out.println("HI HA DIAGONAL DRETA BUIDA");
          }
        break;
    }
    
    return h;
  }
  
  public void imprimirMatriuPesos(Tauler t) 
  {
    t.pintaTaulerALaConsola();
    
    for (int i = 0; i < t.getMida(); ++i) {
      for (int j = 0; j < t.getMida(); ++j) {
        System.out.print(_matriuPesos[i][j]);
        if (i < t.getMida() - 1) 
          System.out.print(", ");
      }
      System.out.println();
    }
  }
  
  public int evalua(Tauler t, int color)
  {
    int h_final = 0;
    
    // (Fer) Millor mirar primer per columnes i començar pel centre.
    for (int j = 0; j < t.getMida(); ++j) {
      for (int i = 0; i < t.getMida(); ++i) {
        int colorFItxAct = t.getColor(j, j);
        if (colorFItxAct == color) {
          h_final += modificaMatPesos(t, color, Dir.VERTICAL, i, j) + 
                     modificaMatPesos(t, color, Dir.HORITZONTAL, i, j) + 
                     modificaMatPesos(t, color, Dir.DIAGONAL_ESQ, i, j) +
                     modificaMatPesos(t, color, Dir.DIAGONAL_DRT, i, j); 
        }
        else if (colorFItxAct == -color) {
          h_final += modificaMatPesos(t, color, Dir.VERTICAL, i, j) + 
                    modificaMatPesos(t, color, Dir.HORITZONTAL, i, j) + 
                    modificaMatPesos(t, color, Dir.DIAGONAL_ESQ, i, j) +
                    modificaMatPesos(t, color, Dir.DIAGONAL_DRT, i, j); 
        }
        else if (colorFItxAct == 0) {
          System.out.println("CASELLA BUIDA");
          // Pasem a la seg columna:
          ++j;
        }
      }
    }
    
    System.out.println("HEUR FIN: " + h_final);
    return h_final;
  }
  
  @Override
  public int moviment(Tauler t, int color)
  {   
    int colJugada = 0;

    if (primeraJugada) { // Comprovem el primer esstat de la casella per saber si som primers o no. Nomes es fa una vegada (a l'inici de la partida).
      for (int i = 0; i < t.getMida(); ++i) {
          if (t.getColor(t.getMida() - 1, i) != 0) {
            colorEnemic = t.getColor(t.getMida(), i);
            colorNostre = -colorEnemic;
          hemComencat = true;
        } 
      }
      primeraJugada = false;
    }
    else { // Fem el moviment normalment.
      try {
        for (int col = 0; col < t.getMida(); ++col) {
          if (hemComencat) { // max amb el nostre color, min amb el color enemic.
            colJugada = max(t, colorNostre, _profunditatInicial, col);
          }
          else if (hemComencat == false) { // max amb el color enemic, min amb el nostre color.
            colJugada = min(t, colorEnemic, _profunditatInicial, col);
          }
        }
        
        //colJugada = evalua(t, color);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return colJugada;
  }
  
  @Override
  public String nom()
  {
    return _nom;
  }
}


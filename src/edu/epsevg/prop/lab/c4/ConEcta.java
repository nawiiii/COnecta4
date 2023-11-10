package edu.epsevg.prop.lab.c4;

/*
    Preguntes:
    - Cal crear constructor per defecte? -> Juga2.java 
      es pot modificar?
    - Quina es la profunditat per defecte per a començar a explorar?
*/

/**
 * Jugador aleatori
 * "Alea jacta est"
 * @author 
 */
public class ConEcta implements Jugador, IAuto 
{
  private String nom;

  private int PROFUNDITAT_INICIAL = 8;
  private int MAX = Integer.MAX_VALUE; // ∞
  private int MIN = Integer.MIN_VALUE; // -∞

  private int TAULERS_EXPLORATS;
  private Boolean SomPrimers = false;

  // Matriu de pesos inicial.
  private int[][] matriuPesos = {{3, 4,  5, 7, 7, 5, 4, 3}, 
                                 {4, 6,  8, 11, 11, 8, 6, 4}, 
                                 {5, 8, 11, 13, 13, 11, 8, 5}, 
                                 {5, 8, 11, 13, 13, 11, 8, 5}, 
                                 {5, 8, 11, 13, 13, 11, 8, 5}, 
                                 {5, 8, 11, 13, 13, 11, 8, 5}, 
                                 {4, 6, 8, 7, 7, 8, 6, 4},
                                 {3, 4, 5,  7,  7, 5, 4, 3}};

  /**
   * Constructor de Jugador ConEcta. Busca jugades fins la profunditat prof.
   * @param prof: Profunditat maxima fins la que es cercara a l'arbre de jugades.
   *              Ha de ser major a 1 (simular com a minim 2 tirades) i un nombre parell.
   * @param ambPoda: Indiquem si volem fer minimax amb/sense poda alpha-beta. 
  */
  public ConEcta(int prof, boolean ambPoda) throws Exception 
  {
    nom = "ConEcta";
    if (prof < 1 || prof % 2 != 0) {
        throw new Exception("Ha de ser major a 1 (simular com a minim 2 tirades). I un nombre parell.");
    }
    TAULERS_EXPLORATS = 0;
  } 
  
  /**
   * Funció min de minimax.
   * @param t:
   * @param color: Jugador que tira la jugada.
   * @param profunditat:
   * @param col:
  */
  private int min(Tauler t, int color, int profunditat, int col) 
  {
    int millorValor = MAX;
    Boolean esTerminal = t.solucio(col, 1) || t.solucio(col, -1) || !t.espotmoure(); 
    if (esTerminal) {
      if (t.solucio(col, color)) { 
        return MAX;
      } 
      else if (t.solucio(col, -color)) 
      {
        return MIN;
      } 
      else {
        return 0;
      }
    }

    if ((profunditat == 0)) {
      int h = heuristica1(t);
      return h;
    }

    for (int moviment = 0; moviment < t.getMida(); moviment++) {
      Tauler tc = new Tauler(t);
      if (tc.movpossible(moviment)) {
        tc.afegeix(moviment, color);

        int nouValor = max(tc, -color, profunditat - 1, moviment);
        millorValor = Math.min(millorValor, nouValor);
      }
    }

    return millorValor;
  }

  /**
   * Funció max de minimax.
   */
  public int max(Tauler t, int color, int profunditat, int mov)
  {
    return 0;
  }
  
  // Cal recalcular la matriu una vegada s'ha fet una tirada. 
  // Te prioritat bloquejar fitxes enemigues consecutives.
  // No es molt bona ja que inicialment nomes és necessaria 
  // la última fila de la matriu.
  public int heuristica1(Tauler t) 
  {
    TAULERS_EXPLORATS += 1; // Comptem quantes vegades hem calulat l'heuristica.
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
  
  public int heuristica2(Tauler t)
  {
    int centre = t.getMida() / 2;
    for (int f = 0; f < t.getMida(); ++f) {
      for (int c = centre; c > 0; --c) {
        
      }
    }
    return 0;
  }
  
 // public int evalua();
  
  @Override
  public int moviment(Tauler t, int color)
  {    
    int col=0;    
    // t.pintaTaulerALaConsola(); // Fer servir puntualment per depurar. (Sino -> Mal rendiment)
    try {
       // col = heuristica(t, color);
    } catch(Exception e) {
        e.printStackTrace();
    }
    return col;
  }
  
  @Override
  public String nom()
  {
    return nom;
  }
}


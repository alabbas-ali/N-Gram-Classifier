package pt.tumba.ngram.bayes;

import java.util.*;

/**************************************************************************/

class DSeparation {
  BayesNet bn;
  boolean above[], below[];

  private final static int CONNECTED_VARIABLES = 0;
  private final static int AFFECTING_VARIABLES = 1;

  /**
   * Constructor for DSeparation object.
   */
  DSeparation(BayesNet b_n) {
    bn = b_n;
  }

  /**
   * Return a list of all variables that are d-connected to
   * a given variable.
   */
  public Vector all_connected(int x) {
    return(separation(x, CONNECTED_VARIABLES));
  }

  /**
   * Returns a list of all variables whose distributions can
   * affect the marginal posterior of a given variable.
   */
  public Vector all_affecting(int x) {
    return(separation(x, AFFECTING_VARIABLES));
  }

  /* 
   * Find all d-separation relations. 
   */
  private void separation_relations(int x, int flag) {
     int nvertices = bn.number_probability_functions();
     if (flag == AFFECTING_VARIABLES)
        nvertices += nvertices;

     boolean ans = false;

     above = new boolean[nvertices];
     below = new boolean[nvertices];

     int current[] = new int[2];

     int i, j, v, subscript;

     for (i=0; i < nvertices; i++) {
       above[i] = false;
       below[i] = false;
     }

     Stack stack = new Stack( );

     int Xabove[] = { x, 1 };
     int Xbelow[] = { x, -1 };

     stack.push(Xabove);
     stack.push(Xbelow);

     below[x] = true;
     above[x] = true;

     while( !stack.empty( ) ) {
       current = (int[]) stack.pop( );
       v = current[0];
       subscript = current[1];

       if(subscript < 0) {
          for ( i = 0; i < nvertices; i++ ) //parents
           if ( adj(i, v, flag) )
             if( (!below[i]) && ( !is_separator(i, flag) )) {
                below[i] = true;
                int Vbelow[] = { i, -1 };
                stack.push(Vbelow);
                }
          for ( j = 0; j < nvertices; j++) //children
           if ( adj(v, j, flag) )
             if(!above[j]) {
               above[j] = true;
               int Tabove[] = { j, 1 };
               stack.push(Tabove);
               }
	  above[v] = true;
         }  // subscript < 0
         else {
           if ( is_separator(v, flag) ) {  // v known
             for ( i = 0; i < nvertices; i++ ) //parents
               if ( adj(i, v, flag) )
                 if ( ( !is_separator(i, flag) ) && !below[i] ) {
                   below[i] = true;
                   int Tbelow[] = { i, -1 };
                   stack.push(Tbelow);
		 }
           }
           else                      // v not known
             for ( j = 0; j < nvertices; j++ ) //children
               if ( adj(v, j, flag) )
                 if(!above[j]) {
                   above[j] = true;
                   int Sabove[] = { j, 1 };
                   stack.push(Sabove);
		 }
	 } // subscript >= 0
     }  // while

  }

  /*
   * Run the separation algorithm and process its results.  
   */ 
  private Vector separation(int x, int flag) {
     int i;
     int nvertices = bn.number_probability_functions();
     Vector d_separated_variables = new Vector();

     // Run algorithm
     separation_relations(x, flag);

     // Process results
     if (flag == CONNECTED_VARIABLES) {
         for (i=0; i<nvertices; i++) {
           if (below[i] || above[i])
    	     d_separated_variables.addElement(bn.get_probability_variable(i));
         }
     }
     else {
         for (i=nvertices; i<(nvertices+nvertices); i++) {
            if (below[i] || above[i])
                d_separated_variables.addElement(bn.get_probability_variable(i - nvertices));
         }
     }

     return(d_separated_variables);
  }

  /* 
   * Check whether the variable given by the index is in the  
   * list of separators (i.e., it is observed).               
   */
  private boolean is_separator(int i, int flag) {
    if ((flag == CONNECTED_VARIABLES) ||
        ((flag == AFFECTING_VARIABLES) &&
         (i < bn.number_probability_functions())))
        return(bn.get_probability_variable(i).is_observed());
    else
        return(false);
  }

  /*
   * Check whether there is a link from variable index_from to 
   * variable index_to.                                        
   */
  private boolean adj(int index_from, int index_to, int flag) {
    ProbabilityFunction pf = null;

    if ((flag == CONNECTED_VARIABLES) ||
        ((flag == AFFECTING_VARIABLES) &&
         (index_to < bn.number_probability_functions()) &&
         (index_from < bn.number_probability_functions()))) {

        for (int i=0; i<bn.number_probability_functions(); i++) {
            if (bn.get_probability_function(i).get_index(0) == index_to) {
                pf = bn.get_probability_function(i);
                break;
            }
        }
        if (pf == null) return(false);

        for (int i=1; i<pf.number_variables(); i++) {
          if (pf.get_index(i) == index_from)
    	return(true);
        }
        return(false);
    }
    else {
        if ( ( index_from - index_to ) == bn.number_probability_functions() )
            return(true);
        else
            return(false);
    }
  }
}


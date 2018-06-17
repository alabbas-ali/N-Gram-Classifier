package pt.tumba.ngram.bayes;

import java.util.Vector;


abstract class FinitelyGeneratedSet
        extends QBProbabilityFunction {

  /**
   * Default constructor for a FinitelyGeneratedSet.
   */
  FinitelyGeneratedSet() {
  }

  /**
   * Constructor for FinitelyGeneratedSet.
   */
  FinitelyGeneratedSet(BayesNet b_n, int n_vb, int n_vl, Vector prop) {
    super(b_n, n_vb, n_vl, prop);
  }

  /**
   * Constructor for FinitelyGeneratedSet.
   */
  FinitelyGeneratedSet(BayesNet b_n, DiscreteVariable pvs[],
			     double v[], double lp[], double up[], Vector prop) {
    super(b_n, pvs, v, lp, up, prop);
  }

  /**
   * Constructor for FinitelyGeneratedSet.
   */
  FinitelyGeneratedSet(BayesNet b_n, DiscreteVariable pvs[],
			     double v[], Vector prop) {
    this(b_n, pvs, v, (double[])null, (double[])null, prop);
  }

  /**
   * Constructor for FinitelyGeneratedSet.
   */
  FinitelyGeneratedSet(DiscreteFunction df, double new_values[], 
                               double new_lp[], double new_up[]) {
    super(df, new_values, new_lp, new_up);
  }
  
  /**
   * Constructor for FinitelyGeneratedSet.
   */
  FinitelyGeneratedSet(DiscreteFunction df, double new_values[]) {
    super(df, new_values, (double[])null, (double[])null);
  }
}


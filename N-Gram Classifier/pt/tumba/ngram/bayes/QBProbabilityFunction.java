package pt.tumba.ngram.bayes;

import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

/*******************************************************************/

public class QBProbabilityFunction extends ProbabilityFunction {
  double lower_envelope[];
  double upper_envelope[];

  /**
   * Default constructor for a QBProbabilityFunction.
   */
  public QBProbabilityFunction() {
  }

  /**
   * Constructor for QBProbabilityFunction.
   */
  public QBProbabilityFunction(BayesNet b_n, int n_vb, int n_vl, Vector prop) {
    super(b_n, n_vb, n_vl, prop);
    lower_envelope = new double[n_vl];
    upper_envelope = new double[n_vl];
  }

  /**
   * Constructor for ProbabilityFunction.
   */
  public QBProbabilityFunction(BayesNet b_n, DiscreteVariable dvs[],
			     double v[], double lp[], double up[], Vector prop) {
    super(b_n, dvs, v, prop);
    lower_envelope = lp;
    upper_envelope = up;
  }

  /**
   * Constructor for QBProbabilityFunction.
   */
  public QBProbabilityFunction(DiscreteFunction df, double new_values[], 
                               double new_lp[], double new_up[]) {
    super(df, new_values);
    lower_envelope = new_lp;
    upper_envelope = new_up;
  }

  /** 
   * Print QBProbabilityFunction.
   */
  public void print() {
    print(System.out);
  }
  
  /**
   * Print QBProbabilityFunction.
   */
  public void print(PrintStream out) {
    int j;
    String property;

    if (variables != null) {
      out.print(" envelope ( ");
      for (j=0; j<variables.length; j++) {
	    out.print( " \"" + variables[j].get_name() + "\" ");
      }
      out.print(") {");
      if (lower_envelope != null) {
          out.println(" //" +  variables.length +
			 " variable(s) and " + lower_envelope.length + " values");
          out.print("\ttable lower-envelope ");
          for (j=0; j<lower_envelope.length; j++)
    	    out.print(lower_envelope[j] + " ");
          out.print(";");
      }
      out.println();
      if (upper_envelope != null) {
          out.print("\ttable upper-envelope ");
          for (j=0; j<upper_envelope.length; j++)
    	    out.print(upper_envelope[j] + " ");
          out.print(";");
      }
    }
    out.println();
    if ((properties != null) && (properties.size() > 0)) {
      for (Enumeration e = properties.elements(); e.hasMoreElements(); ) {
    	property = (String)(e.nextElement());
	    out.println("\tproperty \"" + property + "\" ;");
      }
    }
    out.println("}");
  }

  /* ************************************************************* */
  /* Methods that allow basic manipulation of non-public variables */
  /* ************************************************************* */

  /**
   * Get the lower_envelope array.
   */
  public double[] get_lower_envelope() {
    return(lower_envelope);
  }
  
  /**
   * Get the upper_envelope array.
   */
  public double[] get_upper_envelope() {
    return(upper_envelope);
  }
}


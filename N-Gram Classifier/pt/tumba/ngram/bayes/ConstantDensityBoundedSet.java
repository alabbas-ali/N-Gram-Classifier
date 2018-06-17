package pt.tumba.ngram.bayes;

public class ConstantDensityBoundedSet
       extends TwoMonotoneCapacity {
  /**
   * The "size" of the density bounded class.
   */
  private double k;

  /**
   * Constructor for a ConstantDensityBoundedSet given a 
   * ProbabilityFunction object and given constant.
   */
  public ConstantDensityBoundedSet(ProbabilityFunction pf, double kk) {
     super(pf);
     k = kk;
     if (k <= 0.0) k = 1.0;
     else {
       	if (k < 1.0) k = 1.0/k;
     }
  }

  /**
   * Obtain the lower probability of an event given the base    
   * probability for the event.                                 
   */
  public double get_lower_probability_from_base(double p) {
    return( Math.max( p/k, 1 - k * ( 1 - p ) ) );
  }

  /**
   * Obtain the upper probability of an event given the base    
   * probability for the event.                                 
   */
  public double get_upper_probability_from_base(double p) {
    return( Math.min( k * p, 1 - (1-p)/k ) );
  }

  /**
   * Get a base probability value for an atom. 
   */
  public double get_atom_probability(int index) {
    return( values[index] );
  }
}

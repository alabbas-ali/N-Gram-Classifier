package pt.tumba.ngram.bayes;

public class TotalVariationSet
       extends TwoMonotoneCapacity {

   private double epsilon;

  /**
   * Constructor for an TotalVariationQBProbabilityFunction 
   * ProbabilityFunction object and given epsilon.
   */
  public TotalVariationSet(ProbabilityFunction pf, double eps) {
     super(pf);
     epsilon = eps;
     if ((epsilon < 0.0) || (epsilon > 1.0)) epsilon = 0.0;
  }

  /**
   * Obtain the lower probability of an event given the base
   * probability for the event.
   */
  public double get_lower_probability_from_base(double p) {
    return( Math.max( p - epsilon, 0.0 ) );
  }

  /**
   * Obtain the upper probability of an event given the base
   * probability for the event.                             
   */
  public double get_upper_probability_from_base(double p) {
    return( Math.min( p + epsilon, 1.0 ) );
  }

  /**
   * Get a base probability value for an atom. 
   */
  public double get_atom_probability(int index) {
    return( values[index] );
  }
}
package pt.tumba.ngram.svm;

/**
 * Class to model an SVM Problem, containing both the training vectors
 * and the class (value) associated with each vector.
 * 
 * @author Bruno Martins
 */
public class SVMProblem {

	/** The number the training vectors. */
	public int l;

	/** The value associated with each training each vector. */
	public double[] y;

	/** The training vectors. */
	public SVMNode[][] x;

}
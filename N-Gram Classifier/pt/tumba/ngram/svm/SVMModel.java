package pt.tumba.ngram.svm;

/**
 * SVMModel encondes a classification model, describing both the model parameters
 * and the Support Vectors.
 * 
 * @author Bruno Martins
 */
public class SVMModel {

	/** The parameters of this model. */
	SVMParameter params;
	
	/** The number of classes. (2 in regression/one class svm). */
	int nrClasses;		
	
	/** The total number of Support Vectors. */
	int totalNrSupportVectors;
	
	/** The Support Vectors. */
	SVMNode[][] supportVectors;
	
	/** The coefficients for Support Vectors in decision functions. */
	double[][] supportVectorsCoef; 
	
	/** Constants (the bias terms) in decision functions. */
	double[] rho;
	
	/** Pariwise probability information. */
	double[] probA; 
	
	/** Pariwise probability information. */
	double[] probB;

	/** Label of each class (for classification only). */
	int[] label;
	
	/** Number of Support Vectors for each class (for classification only). */
	int[] numSupportVectors; 
	
}

package pt.tumba.ngram.svm;

/**
 * Constants and Parameters used used in the SVM package.
 * 
 * @author Bruno Martins
 */
public class SVMParameter {
	
	/** SVM formulation type type C-Support Vector classification */
	public static final int C_SVC = 0;
	
	/** SVM formulation type type Nu-Support Vector classification */
	public static final int NU_SVC = 1;
	
	/** SVM formulation type type distribution estimation */
	public static final int ONE_CLASS = 2;
	
	/** SVM formulation type type Epsilon-Support Vector regresion */
	public static final int EPSILON_SVR = 3;
	
	/** SVM formulation type type Nu-Support Vector regression */
	public static final int NU_SVR = 4;

	/** Linear Kernel type */
	public static final int LINEAR = 0;
	
	/** Polynomial kernel type */
	public static final int POLY = 1;
	
	/**  Gaussian  Radial Basis Functio kernel type */
	public static final int RBF = 2;
	
	/** Sigmoid kernel type */
	public static final int SIGMOID = 3;

	/** Parameter specifying the SVM type */
	public int svmType;
	
	/** Parameter specifying the Kernel type */
	public int kernelType;
	
	/** The degree of the polynomial (for polynomial kernel type) */
	public double degree;
	
	/** The gamma parameter.  For the polynomial kernel, gamma serves as inner
	 *  product coefficient in the polynomial. In the case of the RBF kernel, gamma determines 
	 * the RBF width. In the sigmoid kernel, gamma serves as as inner
	 *  product coefficient in the hiperbolic tangent function (default: 1/(data dimension)) */ 
	public double gamma;
	
	/** Parameter needed for kernels of type polynomial and sigmoid (default: 0) */
	public double coef0;

    /** The Kernel cache size, in MB */
	public double cacheSize;
	
	/** The stopping criteria */
	public double eps;
	
	/** The cost parameter, for C_SVC, EPSILON_SVR and NU_SVR. */
	public double C;
	
	/** The label weights, for C_SVC */
	public int nrWeight;
	
	/** The label weights, for C_SVC */
	public int[] weightLabel; 
	
	/** The label weights, for C_SVC */
	public double[] weight;
	
	/** The nu parameter, for NU_SVC, ONE_CLASS, and NU_SVR. It controls the number of support vectors and errors. */ 
	public double nu;
	
	/** The p parameter, for EPSILON_SVR */ 
	public double p;

	/** Boolean flag indicating the use of the shrinking heuristics */
	public boolean shrinking;

	/** Boolean flag indicating if probability estimates should be done */ 
	public boolean probability;
	
	/**
	 * Clone this object (replicate the parameters)
	 * 
	 * @return A clone of these parameters.  
	 */
	public Object clone()	{
			try { return super.clone(); }
			catch (CloneNotSupportedException e) { return null; }
	}
                                                                                           


}

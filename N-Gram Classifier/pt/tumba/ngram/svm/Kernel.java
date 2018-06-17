package pt.tumba.ngram.svm;

/**
 * Abstract interface to model an SVM Kernel.
 * 
 * @author Bruno Martins
 */
public abstract class Kernel {

	/**  */
	private SVMNode[][] x;

	/** */
	private final double[] xSquare;

	/** SVM Kernel type */
	private final int kernelType;
	
	/** */
	private final double degree;
	
	/** */
	private final double gamma;
	
	/** */
	private final double coef0;

	/** Return one column of the Kernel matrix.
	 * 
	 * @param column The column of the kernel matrix.
	 * @param len The nu
	 * @return One column of the Kernel matrix.
	 */
	abstract float[] getQ(int column, int len);

	/**
	 * Swap two nodes of the Kernel matrix.
	 * 
	 * @param i
	 * @param j
	 */
	void swapIndex(int i, int j)
	{
		{ SVMNode[] _=x[i]; x[i]=x[j]; x[j]=_; }
		if(xSquare != null) { double _=xSquare[i]; xSquare[i]=xSquare[j]; xSquare[j]=_;}
	}

	/**
	 * Returns the hyperbolic tangent of a given value
	 * 
	 * @param x A double value.
	 * @return The hyperbolic tangent of the given value.
	 */
	private static double tanh(double x) {
		double e = Math.exp(x);
		return 1.0-2.0/(e*e+1);
	}

	/**
	 * Kernel constructor, prepares to calculate the l*l kernel matrix.
	 * 
	 * @param l The length of the kernel matrix (number of rows and columns).
	 * @param x_ The SVM Nodes.
	 * @param param Kernel parameters.
	 */
	Kernel(int l, SVMNode[][] x_, SVMParameter param) {
		this.kernelType = param.kernelType;
		this.degree = param.degree;
		this.gamma = param.gamma;
		this.coef0 = param.coef0;
		x = (SVMNode[][])x_.clone();
		if(kernelType == SVMParameter.RBF) {
			xSquare = new double[l];
			for(int i=0;i<l;i++)
				xSquare[i] = dot(x[i],x[i]);
		}
		else xSquare = null;
	}

	/**
	 * Return the dot product of two vectors. 
	 * 
	 * @param x A vector.
	 * @param y Another vector.
	 * @return The dot product of both vectors.
	 */
	static double dot(SVMNode[] x, SVMNode[] y)
	{
		double sum = 0;
		int xlen = x.length;
		int ylen = y.length;
		int i = 0;
		int j = 0;
		while(i < xlen && j < ylen)
		{
			if(x[i].index == y[j].index)
				sum += x[i++].value * y[j++].value;
			else
			{
				if(x[i].index > y[j].index)
					++j;
				else
					++i;
			}
		}
		return sum;
	}

	/**
	 * Returns the value of a single kernel evaluation.
	 * 
	 * @param i The row of the kernel matrix.
	 * @param j The column of the kernel matrix.
	 * @return The value for the single kernel evaluation.
	 */
	double kernelFunction(int i, int j) {
		switch(kernelType) {
			case SVMParameter.LINEAR: return dot(x[i],x[j]);
			case SVMParameter.POLY: return Math.pow(gamma*dot(x[i],x[j])+coef0,degree);
			case SVMParameter.RBF: return Math.exp(-gamma*(xSquare[i]+xSquare[j]-2*dot(x[i],x[j])));
			case SVMParameter.SIGMOID:	return tanh(gamma*dot(x[i],x[j])+coef0);
			default: return 0;
		}
	} 

	/**
	 * Returns the value of a single kernel evaluation.
	 * 
	 * @param x The row of the kernel matrix.
	 * @param y The column of the kernel matrix.
	 * @param param SVM kernel parameters
	 * @return The value for the single kernel evaluation.
	 */
	static double kernelFunction(SVMNode[] x, SVMNode[] y,	SVMParameter param) {
		switch(param.kernelType) {
			case SVMParameter.LINEAR: return dot(x,y);
			case SVMParameter.POLY: return Math.pow(param.gamma*dot(x,y)+param.coef0,param.degree);
			case SVMParameter.RBF:	{
				double sum = 0;
				int xlen = x.length;
				int ylen = y.length;
				int i = 0;
				int j = 0;
				while(i < xlen && j < ylen)
				{
					if(x[i].index == y[j].index)
					{
						double d = x[i++].value - y[j++].value;
						sum += d*d;
					}
					else if(x[i].index > y[j].index)
					{
						sum += y[j].value * y[j].value;
						++j;
					}
					else
					{
						sum += x[i].value * x[i].value;
						++i;
					}
				}
				while(i < xlen)
				{
					sum += x[i].value * x[i].value;
					++i;
				}
				while(j < ylen)
				{
					sum += y[j].value * y[j].value;
					++j;
				}
				return Math.exp(-param.gamma*sum);
			}
			case SVMParameter.SIGMOID:	return tanh(param.gamma*dot(x,y)+param.coef0);
			default: return 0;
		}
	}
	
}

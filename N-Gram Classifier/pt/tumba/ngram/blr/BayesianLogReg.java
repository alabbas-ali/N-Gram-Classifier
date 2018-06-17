package pt.tumba.ngram.blr;

import java.io.*;
import java.util.*;
import pt.tumba.ngram.*;

/**
 *  
 * Simple, easy-to-use, and efficient software for Bayesian Logistic Regression
 * classification, based on the "Bayesian Logistic Regression Software" package
 * by Alexander Genkin, David D. Lewis, and David Madigan. A "one-against-one" 
 * approach is used for multiclass classification.</p><p>
 *
 *  A general binary regression classifier takes the form:</p><p>
 *          P(y=1|x,beta) = exp(beta*x) / ( 1 + exp(beta*x) )
 * where y is the class label (1 or -1), x is the predictor vector and beta is the vector of parameters.</p><p> 
 * 
 * This software finds the maximum a posteriori parameter estimates with two choices for prior:
 * Gaussian or Laplace (The Laplace prior corresponds to Tibshirani's LASSO algorithm).
 * To find the parameter estimates the software implements a coordinate descent algorithm that
 * draws on the ideas of Zhang and Oles (2001).
 * There are two ways for the user to define the hyperparameter value (laplace prior or gaussian variance):
 * The first way is to specify the hyperparameter value explicitly. The second way is to 
 * omit any specification and allow the program to set the value by default. The program 
 * sets the default prior variance equal to the inverse average squared value of all data 
 * elements in training.</p><p> 
 * 
 * Logistic regression estimates the probability that a data vector belongs to the class with 
 * label 1. Classification requires a threshold: the model assigns a case to class 1 iff the 
 * probability estimate is greater or equal to the threshold value. The program offers the 
 * following choices for threshold tuning criteria:</p><p>
 *
 *  <li>no tuning, threshold is equal to 0.5
 *  <li>sum of errors = b+c
 *  <li>balanced error rate = (b/(a+b) + c/(c+d))/2
 *  <li>T11U = 2*a - c
 *  <li>F1 = (2*a)/(2*a + b + c)
 *  <li>T13U = 20*a - c
 *  
 * The three latter measures are popular in text classification. A detailed technical report 
 * describing theoretical background, the algorithm, and  experimental results can be 
 * found at <a href="http://stat.rutgers.edu/~madigan/PAPERS/shortFat-v13.ps">http://stat.rutgers.edu/~madigan/PAPERS/shortFat-v13.ps</a>
 * 
 * @author Bruno Martins
 *
 */
public class BayesianLogReg {

	/** The training points */
	private double[][] data;
	
	/** */
	private double classes[];
	
	/** */
	private double beta[];
	
	/** */
	private double delta[];
	
	/** */
	private double theta[];
	
	/** */
	private double r[];
	
	/** */
	protected String names[];
	
	/** */
	protected List sortedGrams;

	/**
	 *  A <code>FilenameFilter</code> for filtering directory listings, recognizing
	 *  filenames for class profiles. Essentially, all filenames not ending with a ".corpus"
	 *  extension are valid. 
	 */
	public static FilenameFilter NGramFilter = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return !name.endsWith(".corpus");
		}
	};

	/**
	 *  Construct an uninitialized Cathegorizer.
	 */
	public BayesianLogReg() {
	}

	/**
	 *  Construct an Cathegorizer from a whole Directory of resources.
	 *
	 *@param      dirName                Pathname for the directory with the profiles.
	 *@exception  TCatNGException        A problem occured while reading the profiles.
	 *@exception  FileNotFoundException  The pathname was not found.
	 */
	public BayesianLogReg(String dirName)
		throws TCatNGException, FileNotFoundException {
		this();
		File fi = new File(dirName);
		if (!fi.isDirectory()) {
			throw new TCatNGException("Base must be a directory.");
		}
		String[] names = fi.list(NGramFilter);
		init(fi, names);
	}

	/**
	 *  Construct an Cathegorizer from a List of resource file names.
	 *
	 *@param       fileNames                     An array with the pathnames for the profiles.
	 *@exception  TCatNGException           A problem occured while reading the profiles.
	 *@exception  FileNotFoundException  One of the pathnames was not found.
	 */
	public BayesianLogReg(String[] fileNames)
		throws TCatNGException, FileNotFoundException {
		this();
		init(null, fileNames);
	}

	/**
	 *  Fetch the set of profiles from the disk.
	 *
	 *@param  fi                         Base directory for the profiles.
	 *@param  names                      Filenames of the profiles to fetch.
	 *@exception  TCatNGException           A problem occured while reading the profiles.
	 *@exception  FileNotFoundException  One of the pathnames was not found.
	 */
	private final void init(File fi, String[] names)
		throws TCatNGException, FileNotFoundException {
		if (names == null || names.length == 0) {
			throw new TCatNGException("Need at least one input file.");
		}
		HashMap profiles = new HashMap();
		HashMap grams = new HashMap();
		for (int index = 0; index < names.length; index++)
			try {
				InputStream str =
					new BufferedInputStream(
						new FileInputStream(new File(fi, names[index])));
				EntryProfile profile =
					new EntryProfile(str, NGramConstants.USEDNGRAMSMAX);
				profiles.put(new Integer(index), profile);
				Iterator it = profile.ngrams();
				while (it.hasNext()) {
					NGram gram = (NGram) (it.next());
					double count = gram.getCount();
					Double count2 = (Double) (grams.get(gram));
					if (count2 == null)
						count2 = new Double(count);
					else
						count2 = new Double(count2.intValue() + count);
					grams.put(gram, count2);
				}
				sortedGrams = new Vector();
				it = grams.keySet().iterator();
				while (it.hasNext())
					sortedGrams.add(it.next());
				Stack stack = new Stack();
				Random rand = new Random(0);
				int low = 0;
				int high = sortedGrams.size() - 1;
				int p;
				int i;
				int pivotIndex;
				int value;
				double ind1;
				double ind2;
				stack.push(new Integer(high));
				stack.push(new Integer(low));
				if (high > 0)
					while (!stack.empty()) {
						low = ((Integer) stack.pop()).intValue();
						high = ((Integer) stack.pop()).intValue();
						value = rand.nextInt();
						pivotIndex =
							low
								+ (((value < 0) ? -1 * value : value)
									% (high + 1 - low));
						sortedGrams = exchangePos(sortedGrams, low, pivotIndex);
						for (p = low, i = low + 1; i <= high; i += 1) {
							ind1 =
								- ((Double) (grams.get(sortedGrams.get(i))))
									.doubleValue();
							ind2 =
								- ((Double) (grams.get(sortedGrams.get(low))))
									.doubleValue();
							if (ind1 < ind2) {
								sortedGrams = exchangePos(sortedGrams, ++p, i);
							}
						}
						sortedGrams = exchangePos(sortedGrams, low, p);
						if (low < (p - 1)) {
							stack.push(new Integer(p - 1));
							stack.push(new Integer(low));
						}
						if ((p + 1) < high) {
							stack.push(new Integer(high));
							stack.push(new Integer(p + 1));
						}
					}
				grams = null;
			} catch (Exception e) {
			}
		data = new double[names.length][1000];
		classes = new double[names.length];
		beta = new double[names.length];
		delta = new double[names.length];
		theta = new double[names.length];
		r = new double[names.length];
		for (int i = 0; i < names.length; i++) {
			for (int j = 0; j < 1000; j++) {
				data[i][j] = ((Profile) (profiles.get(new Integer(i)))).getRank((NGram) (sortedGrams.get(j)));
				classes[i] = i;	
			}
		}
		initialize();
		optimization();
	}

	/**
	 *  Exchange two values in a list
	 *
	 *@param  v    The original list
	 *@param  p1  The index of the first element
	 *@param  p2  The index of the second element
	 *@return  The list with the two elements exchanged
	 */
	private static List exchangePos(List v, int p1, int p2) {
		Object tmp = v.get(p1);
		v.set(p1, v.get(p2));
		v.set(p2, tmp);
		return v;
	}

	/**
	 * Initialize the Bayesian Logistic Regression classifyer.
	 */
	private void initialize() {
		double deltav = 0;
		for (int i=0; i<beta.length; i++ ) {
			beta[i] = 0;
			delta[i] = 1;
			theta[i] = 0;
			for (int j=0; j<r.length; j++ ) theta[i]+=data[i][j] * data[i][j]; 
			theta[i] = beta.length / ((theta[i])/r.length);
		}
		for (int i=0; i<r.length; i++ ) {
			r[i] = 0;
		}
	}
	
	/**
	 * 
	 * @param deltar
	 * @return
	 */
	private double convergenceTest (double deltar[]) {
		double value1 = 0, value2 = 0;
		for (int i=0; i<r.length; i++) {
			value1 += Math.abs(deltar[i]);
			value2 += Math.abs(r[i]);
		}
		return (double)(value1 / (1.0+value2));
	}
	
	/**
	 * 
	 * @param j
	 * @return
	 */
	private double gaussianOptimization(int j) {
		double aux1 = 0, aux2 = 0, deltav;
		for (int i=0; i<r.length; i++) {
			double f = delta[j] * Math.abs(data[j][i]);
			if(r[j]<=f) f=0.25; else
			f=1/( 2+ Math.exp(Math.abs(r[j]) - Math.abs(f)) + Math.exp(Math.abs(r[j]) - Math.abs(f)) );
			aux1+= data[j][i] * classes[j] * (1/(1+Math.exp(r[i])));
			aux2+= data[j][i]*data[j][i] * f;
		}
		return - ((aux1 + (2*beta[j])/(theta[j]*theta[j])) / ( aux2 + (2/theta[j])));				
	}
	
	/**
	 * 
	 * @param j
	 * @return
	 */
	private double laplaceOptimization(int j) {
		double aux1 = 0, aux2 = 0, deltav;
		for (int i=0; i<r.length; i++) {
			double f = delta[j] * data[j][i];
			if(r[j]<=f) f=0.25; else
			f=1/( 2+ Math.exp(Math.abs(r[j]) - Math.abs(f)) + Math.exp(Math.abs(r[j]) - Math.abs(f)) );
			aux1+= data[j][i]  * classes[j] * (1/(1+Math.exp(r[i])));
			aux2+= data[j][i]*data[j][i] * f;
		}
		deltav=0;
		//deltav = (aux1 - ((Math.sqrt(2)/theta[j])*Math.signum(beta[j]))) / (aux2);
		if (beta[j]==0) {
			if(deltav<=0) return deltav;
			else return 0;
		} else {
			if(beta[j]+deltav<0) return -beta[j];
			else return deltav;
		}
	}

	/**
	 * 
	 *
	 */
	private void optimization () {
		double deltar[] = new double[r.length];
		double aux1, aux2, deltaBeta, deltav;
		do {
			for (int j=0; j<beta.length; j++) {
				deltav = gaussianOptimization(j);
				deltaBeta = Math.min(Math.max(deltav,-delta[j]),delta[j]);
				for (int i=0; i<r.length; i++) {
	                deltar[i] =deltaBeta * data[j][i] * classes[j];
					r[i]+=deltar[i];
				}
				beta[j]+=deltaBeta;
				aux1 = delta[j]/2;
				aux2 = 0;
				if(aux2>aux1) delta[j]=aux2; else delta[j]=aux1;
			}
		} while (convergenceTest(deltar)>0.0005);
	}

	/**
	 *  Match a given <code>File</code> against all the classes in the cathegorizer.
	 *
	 *@param  f  A <code>File</code>.
	 *@return  The closest matching class (given by the model File name) in the cathegorizer.
	 */
	public String match(File f) {
		String result = null;
		try {
			InputStream str = new BufferedInputStream(new FileInputStream(f));
			EntryProfile profile = new EntryProfile(str, NGramConstants.USEDNGRAMSMAX);
			double data[] = new double[1000];
			for (int i = 0; i < 1000; i++) data[i] = profile.getRank((NGram) (sortedGrams.get(i)));
			double value = Double.MIN_VALUE;
			double aux = 0;
			for (int i = 0; i < 1000; i++) aux += beta[i] * data[i];
			aux = Math.exp(aux) / (1 + Math.exp(aux));
			value = aux;
			result = names[(int) value];
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * Sample application to use the Cathegorizer from the command line.
	 *
	 *@param  args  The command line arguments, tokenized
	 */
	public static void main(String[] args) {
		try {
			BayesianLogReg cath = new BayesianLogReg(args[0]);
			for (int i = 1; i < args.length; i++) {
				File f = new File(args[i]);
				String res = cath.match(f);
				System.out.println(
					"Best match for " + args[i] + " is : " + res);
			}
		} catch (Exception e) {
			System.err.println("Terminated by " + e);
			e.printStackTrace();
		}
	}

}
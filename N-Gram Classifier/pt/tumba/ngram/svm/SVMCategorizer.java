package pt.tumba.ngram.svm;

import java.io.*;
import java.util.*;
import pt.tumba.ngram.*;

/**
 * Simple, easy-to-use, and efficient software for SVM classification and regression,
 * based on the LIBSVM implementation of Chin-Chung Chang and Chin-Jen Lin.
 * It can solve C-SVM classification, nu-SVM classification, one-class-SVM, 
 * epsilon-SVM regression, and nu-SVM regression. It also provides an automatic
 * model selection tool for C-SVM classification, and multiclass classification through
 * a "one-against-one" approach . </p><p>
 * 
 * The original implementation was much less "object-oriented", and didn't follow the
 * usual conventions for Java programs. Besides major code refactoring, additional
 * functionalities were also added:
 *
 */
public class SVMCategorizer {

	/**
	 *  Description of the Field
	 */
	protected SVMModel model;
	protected SVMParameter param;
	protected SVMProblem prob;
	protected String names[];
	protected List sortedGrams;
	
	private String inputFileName;
	private String modelFileName;
	private String errorMsg;
	private boolean crossValidation = false;
	private int nrFold;

	/**
	 *  Construct an uninitialized Cathegorizer.
	 */
	public SVMCategorizer() {
		model = new SVMModel();
		SVMParameter param = new SVMParameter();
		param.svmType = SVMParameter.C_SVC;
		param.kernelType = SVMParameter.RBF;
		param.degree = 3;
		param.gamma = 0.5;
		param.coef0 = 0;
		param.nu = 0.5;
		param.cacheSize = 40;
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = true;
		param.nrWeight = 0;
		param.weightLabel = new int[0];
		param.weight = new double[0];
		prob = new SVMProblem();
		names = new String[0];
		sortedGrams = new Vector();
	}

	/**
	 *  Construct an Cathegorizer from a whole Directory of resources.
	 *
	 *@param  dirName                    Description of the Parameter
	 *@exception  NGramException         Description of the Exception
	 *@exception  FileNotFoundException  Description of the Exception
	 */
	public SVMCategorizer(String dirName)
		throws TCatNGException, FileNotFoundException {
		this();
		File fi = new File(dirName);
		if (!fi.isDirectory()) {
			throw new TCatNGException("Base must be a directory.");
		}
		String[] names = fi.list();
		init(fi, names);
	}

	/**
	 *  Construct an Cathegorizer from a List of resource file names.
	 *
	 *@param  fileNames                  Description of the Parameter
	 *@exception  NGramException         Description of the Exception
	 *@exception  FileNotFoundException  Description of the Exception
	 */
	public SVMCategorizer(String[] fileNames)
		throws TCatNGException, FileNotFoundException {
		this();
		init(null, fileNames);
	}

	/**
	 *  Fetch the set of file resources.
	 *
	 *@param  fi                         Description of the Parameter
	 *@param  names                      Description of the Parameter
	 *@exception  NGramException         Description of the Exception
	 *@exception  FileNotFoundException  Description of the Exception
	 */
	private final void init(File fi, String[] names)
		throws TCatNGException, FileNotFoundException {
		this.names = names;
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
		prob.l = names.length;
		prob.y = new double[names.length];
		prob.x = new SVMNode[prob.l][1000];
		for (int i = 0; i < prob.l; i++) {
			for (int j = 0; j < 1000; j++) {
				prob.x[i][j] = new SVMNode();
				prob.x[i][j].index = j + 1;
				prob.x[i][j].value =
					((Profile) (profiles.get(new Integer(i)))).getRank(
						(NGram) (sortedGrams.get(j)));
				prob.y[i] = i;
			}
		}
		profiles = null;
		model = SVM.svmTrain(prob, param);
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

	public String match(File f) {
		String result = null;
		try {
			InputStream str = new BufferedInputStream(new FileInputStream(f));
			EntryProfile profile =
				new EntryProfile(str, NGramConstants.USEDNGRAMSMAX);
			SVMNode[] x = new SVMNode[1000];
			for (int i = 0; i < 1000; i++) {
				x[i] = new SVMNode();
				x[i].index = i + 1;
				x[i].value = profile.getRank((NGram) (sortedGrams.get(i)));
			}
			double value = SVM.svmPredict(model, x);
			result = names[(int) value];
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 *  Sample application, like the text_cat main mode.
	 *
	 *@param  args  The command line arguments
	 */
	public static void main(String[] argv) {
		int i, predict_probability = 0;
		SVMCategorizer cath = new SVMCategorizer();
		cath.param = new SVMParameter();
		cath.param.svmType = SVMParameter.C_SVC;
		cath.param.kernelType = SVMParameter.RBF;
		cath.param.degree = 3;
		cath.param.gamma = 0; // 1/k
		cath.param.coef0 = 0;
		cath.param.nu = 0.5;
		cath.param.cacheSize = 40;
		cath.param.C = 1;
		cath.param.eps = 1e-3;
		cath.param.p = 0.1;
		cath.param.shrinking = true;
		cath.param.probability = false;
		cath.param.nrWeight = 0;
		cath.param.weightLabel = new int[0];
		cath.param.weight = new double[0];
		if (argv.length == 0)
			exitWithHelp();
		if (argv[0].equals("-predict")) {
			for (i = 1; i < argv.length; i++) {
				if (argv[i].charAt(0) != '-')
					break;
				++i;
				switch (argv[i - 1].charAt(1)) {
					case 'b' :
						predict_probability = Integer.parseInt(argv[i]);
						break;
					default :
						System.err.print("unknown option\n");
						exitWithHelp();
				}
			}
			if (i >= argv.length)
				exitWithHelp();
			try {
				BufferedReader input =
					new BufferedReader(new FileReader(argv[i]));
				DataOutputStream output =
					new DataOutputStream(new FileOutputStream(argv[i + 2]));
				SVMModel model = SVM.svmLoadModel(argv[i + 1]);
				predict(input, output, model, predict_probability);
			} catch (IOException e) {
				exitWithHelp();
			} catch (ArrayIndexOutOfBoundsException e) {
				exitWithHelp();
			}
		} else if (argv[0].equals("-train")) {
			for (i = 1; i < argv.length; i++) {
				if (argv[i].charAt(0) != '-')
					break;
				++i;
				switch (argv[i - 1].charAt(1)) {
					case 's' :
						cath.param.svmType = Integer.parseInt(argv[i]);
						break;
					case 't' :
						cath.param.kernelType = Integer.parseInt(argv[i]);
						break;
					case 'd' :
						cath.param.degree =
							Double.valueOf(argv[i]).doubleValue();
						break;
					case 'g' :
						cath.param.gamma =
							Double.valueOf(argv[i]).doubleValue();
						break;
					case 'r' :
						cath.param.coef0 =
							Double.valueOf(argv[i]).doubleValue();
						break;
					case 'n' :
						cath.param.nu = Double.valueOf(argv[i]).doubleValue();
						break;
					case 'm' :
						cath.param.cacheSize =
							Double.valueOf(argv[i]).doubleValue();
						break;
					case 'c' :
						cath.param.C = Double.valueOf(argv[i]).doubleValue();
						break;
					case 'e' :
						cath.param.eps = Double.valueOf(argv[i]).doubleValue();
						break;
					case 'p' :
						cath.param.p = Double.valueOf(argv[i]).doubleValue();
						break;
					case 'h' :
						cath.param.shrinking = Boolean.parseBoolean(argv[i]);
						break;
					case 'b' :
						cath.param.probability = Boolean.parseBoolean(argv[i]);
						break;
					case 'v' :
						cath.crossValidation = true;
						cath.nrFold = Integer.parseInt(argv[i]);
						if (cath.nrFold < 2) {
							System.err.print(
								"n-fold cross validation: n must >= 2\n");
							exitWithHelp();
						}
						break;
					case 'w' :
						++cath.param.nrWeight;
						int[] old = cath.param.weightLabel;
						cath.param.weightLabel = new int[cath.param.nrWeight];
						System.arraycopy(
							old,
							0,
							cath.param.weightLabel,
							0,
							cath.param.nrWeight - 1);

						double[] old2 = cath.param.weight;
						cath.param.weight = new double[cath.param.nrWeight];
						System.arraycopy(
							old2,
							0,
							cath.param.weight,
							0,
							cath.param.nrWeight - 1);
						cath.param.weightLabel[cath.param.nrWeight - 1] =
							Integer.parseInt(argv[i - 1].substring(2));
						cath.param.weight[cath.param.nrWeight - 1] =
							Integer.parseInt(argv[i]);
						break;
					default :
						System.err.print("unknown option\n");
						exitWithHelp();
				}
			}
			if (i >= argv.length)
				exitWithHelp();
			cath.inputFileName = argv[i];
			if (i < argv.length - 1)
				cath.modelFileName = argv[i + 1];
			else {
				int p = argv[i].lastIndexOf('/');
				++p; // whew...
				cath.modelFileName = argv[i].substring(p) + ".model";
			}
			try {
				cath.readProblem();
			} catch (IOException e) {
				exitWithHelp();
			}
			cath.errorMsg = SVM.svmCheckParameter(cath.prob, cath.param);
			if (cath.errorMsg != null) {
				System.err.print("Error: " + cath.errorMsg + "\n");
				System.exit(1);
			}
			if (cath.crossValidation) {
				cath.doCrossValidation();
			} else {
				cath.model = SVM.svmTrain(cath.prob, cath.param);
				try {
					SVM.svmSaveModel(cath.modelFileName, cath.model);
				} catch (Exception e) {
					System.err.println("Terminated by " + e);
					e.printStackTrace();
				}
			}
		} else if (argv[0].equals("-textcat") || argv.length > 2) {
			try {
				cath = new SVMCategorizer(argv[1]);
				for (i = 2; i < argv.length; i++) {
					File f = new File(argv[i]);
					String res = cath.match(f);
					System.out.println(
						"Best match for " + argv[i] + " is : " + res);
				}
			} catch (Exception e) {
				System.err.println("Terminated by " + e);
				e.printStackTrace();
			}
		} else
			exitWithHelp();
	}

	private static void predict(
		BufferedReader input,
		DataOutputStream output,
		SVMModel model,
		int predict_probability)
		throws IOException {
		int correct = 0;
		int total = 0;
		double error = 0;
		double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
		int svm_type = model.params.svmType;
		int nr_class = model.nrClasses;
		int[] labels = new int[nr_class];
		double[] prob_estimates = null;
		if (predict_probability == 1) {
			if (svm_type == SVMParameter.EPSILON_SVR
				|| svm_type == SVMParameter.NU_SVR) {
				System.out.print(
					"Prob. model for test data: target value = predicted value + z,\nz: Laplace distribution e^(-|z|/sigma)/(2sigma),sigma="
						+ SVM.svmGetSVRProbability(model)
						+ "\n");
			} else {
				labels = model.label;
				prob_estimates = new double[nr_class];
				output.writeBytes("labels");
				for (int j = 0; j < nr_class; j++)
					output.writeBytes(" " + labels[j]);
				output.writeBytes("\n");
			}
		}
		while (true) {
			String line = input.readLine();
			if (line == null)
				break;
			StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
			double target = Double.valueOf(st.nextToken()).doubleValue();
			int m = st.countTokens() / 2;
			SVMNode[] x = new SVMNode[m];
			for (int j = 0; j < m; j++) {
				x[j] = new SVMNode();
				x[j].index = Integer.parseInt(st.nextToken());
				x[j].value = Double.valueOf(st.nextToken()).doubleValue();
				;
			}
			double v;
			if (predict_probability == 1
				&& (svm_type == SVMParameter.C_SVC
					|| svm_type == SVMParameter.NU_SVC)) {
				v = SVM.svmPredictProbability(model, x, prob_estimates);
				output.writeBytes(v + " ");
				for (int j = 0; j < nr_class; j++)
					output.writeBytes(prob_estimates[j] + " ");
				output.writeBytes("\n");
			} else {
				v = SVM.svmPredict(model, x);
				output.writeBytes(v + "\n");
			}

			if (v == target)
				++correct;
			error += (v - target) * (v - target);
			sumv += v;
			sumy += target;
			sumvv += v * v;
			sumyy += target * target;
			sumvy += v * target;
			++total;
		}
		System.out.print(
			"Accuracy = "
				+ (double) correct / total * 100
				+ "% ("
				+ correct
				+ "/"
				+ total
				+ ") (classification)\n");
		System.out.print(
			"Mean squared error = " + error / total + " (regression)\n");
		System.out.print(
			"Squared correlation coefficient = "
				+ ((total * sumvy - sumv * sumy) * (total * sumvy - sumv * sumy))
					/ ((total * sumvv - sumv * sumv)
						* (total * sumyy - sumy * sumy))
				+ " (regression)\n");
	}

	private static void exitWithHelp() {
		System.out.print(
			"usage: svm_predict [options] test_file model_file output_file\n"
				+ "options:\n"
				+ "-b probability_estimates: whether to predict probability estimates, 0 or 1 (default 0); one-class SVM not supported yet\n");
		System.out.print(
			"Usage: svm_train [options] training_set_file [model_file]\n"
				+ "options:\n"
				+ "-s svm_type : set type of SVM (default 0)\n"
				+ "	0 -- C-SVC\n"
				+ "	1 -- nu-SVC\n"
				+ "	2 -- one-class SVM\n"
				+ "	3 -- epsilon-SVR\n"
				+ "	4 -- nu-SVR\n"
				+ "-t kernel_type : set type of kernel function (default 2)\n"
				+ "	0 -- linear: u'*v\n"
				+ "	1 -- polynomial: (gamma*u'*v + coef0)^degree\n"
				+ "	2 -- radial basis function: exp(-gamma*|u-v|^2)\n"
				+ "	3 -- sigmoid: tanh(gamma*u'*v + coef0)\n"
				+ "-d degree : set degree in kernel function (default 3)\n"
				+ "-g gamma : set gamma in kernel function (default 1/k)\n"
				+ "-r coef0 : set coef0 in kernel function (default 0)\n"
				+ "-c cost : set the parameter C of C-SVC, epsilon-SVR, and nu-SVR (default 1)\n"
				+ "-n nu : set the parameter nu of nu-SVC, one-class SVM, and nu-SVR (default 0.5)\n"
				+ "-p epsilon : set the epsilon in loss function of epsilon-SVR (default 0.1)\n"
				+ "-m cachesize : set cache memory size in MB (default 40)\n"
				+ "-e epsilon : set tolerance of termination criterion (default 0.001)\n"
				+ "-h shrinking: whether to use the shrinking heuristics, 0 or 1 (default 1)\n"
				+ "-b probability_estimates: whether to train a SVC or SVR model for probability estimates, 0 or 1 (default 0)\n"
				+ "-wi weight: set the parameter C of class i to weight*C, for C-SVC (default 1)\n"
				+ "-v n: n-fold cross validation mode\n");
		System.exit(1);
	}

	private void doCrossValidation() {
		int i;
		int total_correct = 0;
		double total_error = 0;
		double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
		double[] target = new double[prob.l];

		SVM.svmCrossValidation(prob, param, nrFold, target);
		if (param.svmType == SVMParameter.EPSILON_SVR
			|| param.svmType == SVMParameter.NU_SVR) {
			for (i = 0; i < prob.l; i++) {
				double y = prob.y[i];
				double v = target[i];
				total_error += (v - y) * (v - y);
				sumv += v;
				sumy += y;
				sumvv += v * v;
				sumyy += y * y;
				sumvy += v * y;
			}
			System.out.print(
				"Cross Validation Mean squared error = "
					+ total_error / prob.l
					+ "\n");
			System.out.print(
				"Cross Validation Squared correlation coefficient = "
					+ ((prob.l * sumvy - sumv * sumy)
						* (prob.l * sumvy - sumv * sumy))
						/ ((prob.l * sumvv - sumv * sumv)
							* (prob.l * sumyy - sumy * sumy))
					+ "\n");
		} else
			for (i = 0; i < prob.l; i++)
				if (target[i] == prob.y[i])
					++total_correct;
		System.out.print(
			"Cross Validation Accuracy = "
				+ 100.0 * total_correct / prob.l
				+ "%\n");
	}

	// read in a problem (in svmlight format)

	private void readProblem() throws IOException {
		BufferedReader fp = new BufferedReader(new FileReader(inputFileName));
		Vector vy = new Vector();
		Vector vx = new Vector();
		int max_index = 0;

		while (true) {
			String line = fp.readLine();
			if (line == null)
				break;

			StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

			vy.addElement(st.nextToken());
			int m = st.countTokens() / 2;
			SVMNode[] x = new SVMNode[m];
			for (int j = 0; j < m; j++) {
				x[j] = new SVMNode();
				x[j].index = Integer.parseInt(st.nextToken());
				x[j].value = Double.valueOf(st.nextToken()).doubleValue();
			}
			if (m > 0)
				max_index = Math.max(max_index, x[m - 1].index);
			vx.addElement(x);
		}

		prob = new SVMProblem();
		prob.l = vy.size();
		prob.x = new SVMNode[prob.l][];
		for (int i = 0; i < prob.l; i++)
			prob.x[i] = (SVMNode[]) vx.elementAt(i);
		prob.y = new double[prob.l];
		for (int i = 0; i < prob.l; i++)
			prob.y[i] = Double.valueOf((String) vy.elementAt(i)).doubleValue();

		if (param.gamma == 0)
			param.gamma = 1.0 / max_index;

		fp.close();
	}

}

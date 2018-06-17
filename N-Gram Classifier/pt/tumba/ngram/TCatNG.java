package pt.tumba.ngram;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import pt.tumba.ngram.blr.BayesianLogReg;
import pt.tumba.ngram.compression.CompressionCategorizer;
import pt.tumba.ngram.svm.SVMCategorizer;

/**
 * The TCatNG Toolkit is a Java package that you can use to apply N-Gram analysis techniques
 * to the process of categorizing text files. </p><p>
 * 
 * N-Grams are short sequences of bytes or letters, and their statistics provide valuable informations
 * about byte sequences and strings. N-Gram based approaches are very powerful in text
 * categorization because every string is decomposed into small parts, and errors tend to affect only a
 * limited number of those parts, leaving the remainder intact.</p><p>
 *
 * The use of character N-Grams also does not explicitly or implicitly require the specification of a
 * separator, as it is necessary for words. Consequently, analyzing a text in terms of N-Grams 
 * constitutes a valuable approach for text written in any language based on an alphabet and the 
 * concatenation text-construction operator, eliminating the need for complex text tokenization, 
 * stemming, and/or lemmatization.</p><p>
 *
 * There are many possible applications: categorizing documents by topic, detecting the author
 * of a text, or recognizing the language and encoding for a bunch of bytes (i.e. in a search engine,
 * to figure the language of a document). This is actually the first application this software package
 * was designed for, but many other uncharted areas are up to you to explore.
 *
 * @author Bruno Martins
 */
public class TCatNG {

	private static String[] classifyNN(String path, String trainingPath) {
		String[] names = new File(path).list();
		return classifyNN(names, trainingPath);
	}

	private static String[] classifyNN(String[] names, String trainingPath) {
		return classifyNN(names, trainingPath);
	}

	private static String[] classifyNN(
		File fi,
		String[] names,
		String trainingPath) {
		String results[] = new String[names.length];
		try {
			NGramCathegorizer languageModels =
				new NGramCathegorizer(trainingPath);
			for (int i = 0; i < names.length; i++) {
				InputStream str =
					new BufferedInputStream(
						new FileInputStream(new File(fi, names[i])));
				EntryProfile profile =
					new EntryProfile(str, NGramConstants.USEDNGRAMSMAX);
				String result = null;
				Profile res = languageModels.match(profile);
				results[i] = res.toString();
				System.out.println(
					names[i].toString() + "  -> " + res.toString());
			}
			return results;
		} catch (Exception e) {
			return null;
		}
	}

	private static String[] classifyCompression(
		String path,
		String trainingPath) {
		String[] names = new File(path).list();
		return classifyCompression(names, trainingPath);
	}

	private static String[] classifyCompression(
		String[] names,
		String trainingPath) {
		return classifyCompression(names, trainingPath);
	}

	private static String[] classifyCompression(
		File fi,
		String[] names,
		String trainingPath) {
		String results[] = new String[names.length];
		try {
			CompressionCategorizer models =
				new CompressionCategorizer(trainingPath);
			for (int i = 0; i < names.length; i++) {
				String result = models.match(new File(fi, names[i]));
				results[i] = result;
				System.out.println(names[i].toString() + "  -> " + result);
			}
			return results;
		} catch (Exception e) {
			return null;
		}
	}

	private static String[] classifySVM(String path, String trainingPath) {
		String[] names = new File(path).list();
		return classifySVM(names, trainingPath);
	}

	private static String[] classifySVM(String[] names, String trainingPath) {
		return classifySVM(names, trainingPath);
	}

	private static String[] classifySVM(
		File fi,
		String[] names,
		String trainingPath) {
		String results[] = new String[names.length];
		try {
			SVMCategorizer models = new SVMCategorizer(trainingPath);
			for (int i = 0; i < names.length; i++) {
				String result = models.match(new File(fi, names[i]));
				results[i] = result;
				System.out.println(names[i].toString() + "  -> " + result);
			}
			return results;
		} catch (Exception e) {
			return null;
		}
	}

	private static String[] classifyLogRegression(
		String path,
		String trainingPath) {
		String[] names = new File(path).list();
		return classifyLogRegression(names, trainingPath);
	}

	private static String[] classifyLogRegression(
		String[] names,
		String trainingPath) {
		return classifyLogRegression(names, trainingPath);
	}

	private static String[] classifyLogRegression(
		File fi,
		String[] names,
		String trainingPath) {
		String results[] = new String[names.length];
		try {
			BayesianLogReg models = new BayesianLogReg(trainingPath);
			for (int i = 0; i < names.length; i++) {
				String result = models.match(new File(fi, names[i]));
				results[i] = result;
				System.out.println(names[i].toString() + "  -> " + result);
			}
			return results;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Prints command usage information to the standard output.
	 */
	private static void printUsage() {
		System.out.println(
			"Usage: java pt.tumba.ngram.TCatNG [options] <command>");
		System.out.println();
		System.out.println();
		System.out.println("<command> is one of the following:");
		System.out.println();
		System.out.println(
			"  -?|help                      Print this help message");
		System.out.println(
			"  -c|classify <file_list>      Classify files in the list");
		System.out.println(
			"  -f|features <a_file>         Generate feature list from the file");
		System.out.println();
		System.out.println("The available [options] include:");
		System.out.println();
		System.out.println(
			"  --mode=<str>                 Use a given classification method");
		System.out.println(
			"                                   ** <str> can be one of the following:");
		System.out.println(
			"                                   nn         Nearest Neighbour classification");
		System.out.println(
			"                                   compress   Compression Based classification");
		System.out.println(
			"                                   svm        Support Vector Machines");
		System.out.println(
			"                                   blr        Bayesian Logistic Regression");
		System.out.println();
		System.out.println(
			" Options specific for Nearest Neighbour classification");
		System.out.println(
			"  --lin                        Use Lin's similarity measure");
		System.out.println(
			"  --jiang                      Use Jiang's similarity measure");
		System.out.println(
			"  --rank-order                 Use simple rank order statistic");
		System.out.println();
		System.out.println(
			" Options specific for Bayesian Logistic Regression classification");
		System.out.println(
			"  --bayesian                   Bayesian optimization");
		System.out.println(
			"  --laplace                    Laplace optimization");
		System.out.println();
		System.out.println(
			" Options specific for Compression Based classification");
		System.out.println(
			"  --ac                         Arritmethic Compression");
		System.out.println();
		System.out.println(
			" Options specific for Support Vector Machines classification");
		System.out.println("  --lk                         Linear Kernel");
		System.out.println("  --pk                         Polinomial Kernel");
		System.out.println("  --rbf                        RBF Kernel");
		System.out.println("  --sig                        Sigmoid Kernel");
		System.out.println();
		System.exit(0);
	}

	/**
	 * The main method of this package.
	 * 
	 * @param args The command line arguments, tokenized.
	 */
	public static void main(String args[]) {
		System.out.println("TCatNG :: Text Categorization with N-Grams");
		printUsage();
		String names[] = new String[args.length - 1];
		for (int i = 1; i < args.length; i++) names[i - 1] = new String(args[i]);
		classifyNN(names, args[0]);
	}

}
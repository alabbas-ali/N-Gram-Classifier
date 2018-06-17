package pt.tumba.ngram;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *  <code>NGramCathegorizer</code> implements the classification technique described in 
 *  Cavnar & Trenkle, "N-Gram-Based Text Categorization". It was primarily developed
 *  for language guessing, a task on which it is known to perform with near-perfect accuracy. </p><p> 
 * 
 * The central idea of the Cavnar & Trenkle technique is to calculate a "fingerprint" of a 
 * document with an unknown category, and compare this with the fingerprints of a number 
 * of documents of which the categories are known. The categories of the closest matches
 * are output as the classification. A fingerprint is a list of the most frequent n-grams 
 * occurring in a document, ordered by frequency.</p><p> 
 * 
 * In the oroginal proposal, fingerprints are compared with a simple out-of-place metric (see 
 * the article for more details). This package implements some extentions to the original 
 * proposal, namelly offering support for Good-Turing smoothing and new fingerprint 
 * comparison methods, based on the similarity metrics proposed by Lin in
 *  "An information-theoretic definition of similarity" and Jiand & Conranth in 
 * "Semantic Similarity Based on Corpus Statistics and Lexical Taxonomy".</p><p>
 * 
 * This library was made with efficiency in mind. There are couple of parameters you 
 * may wish to tweak if you intend to use it for other tasks than language guessing.</p><p>
 * 
 * Since the speed of the classifier is roughly linear with respect to the number of models,
 * you should consider how many models you really need. For instance in case of language
 * guessing: do you really want to recognize every language ever invented?</p><p>
 *
 *@author     Bruno Martins
 */
public class NGramCathegorizer {

	/** The metric used to measure the distance between the profiles. */
	private int similarityMetric;

	/**
	 *  A <code>FilenameFilter</code> for filtering directory listings, recognizing
	 *  filenames for N-gram profiles. Essentially, all filenames not ending with a ".corpus"
	 * extension are valid. 
	 */
	public static FilenameFilter NGramFilter = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return !name.endsWith(".corpus");
		}
	};

	/**
	 *  Calculate "the distance" between two profiles, according to the metric
	 *  selected while instantiating this class. 
	 *
	 *@param  prof1  A Profile
	 *@param  prof2  Another Profile
	 *@return   The distance between the two profiles. The higher the value, the smaller the
	 *               similarity.
	 */
	public static double profileDistance(Profile prof1, Profile prof2) {
		return similarityLin(prof1, prof2);
	}

	/**
	 *  Calculate "the distance" between two profiles, using the
	 *  metric proposed by Cavnar & Trenkle. See the paper for more details.
	 *
	 *@param  prof1  A Profile
	 *@param  prof2  Another Profile
	 *@return   The distance between the two profiles. The higher the value, the smaller the
	 *               similarity.
	 */
	public static double deltaRank(Profile prof1, Profile prof2) {
		double delta = 0.0;
		Iterator grams = prof1.ngrams();
		int j = 0;
		while (grams.hasNext()) {
			j++;
			NGram ngram = (NGram) grams.next();
			double rank = prof2.getRank(ngram);
			if (rank != 0.0) {
				delta += Math.abs(rank - j);
			} else {
				delta += NGramConstants.USEDNGRAMSMAX;
			}
		}
		return delta;
	}

	/**
	 *  Calculate "the distance" between two profiles, using Lin's similarity measure
	 *  as proposed in "An information-theoretic definition of similarity".
	 *
	 *@param  prof1  A Profile
	 *@param  prof2  Another Profile
	 *@return   The distance between the two profiles. The higher the value, the smaller the
	 *               similarity.
	 */
	public static double similarityLin(Profile prof1, Profile prof2) {
		double d1 = 0.0, d2 = 0.0, d3 = 0.0;
		Iterator grams = prof1.ngrams();
		while (grams.hasNext()) {
			NGram ngram = (NGram) grams.next();
			double rank2 = prof2.getWeight(ngram);
			double rank1 = prof1.getWeight(ngram);
			if (rank2 != 0.0)
				d1 += Math.log(rank2 + rank1);
			d2 += Math.log(rank1);
		}
		grams = prof2.ngrams();
		while (grams.hasNext()) {
			NGram ngram = (NGram) grams.next();
			double rank1 = prof2.getWeight(ngram);
			d3 += Math.log(rank1);
		}
		return - (2 * d1) / (d2 + d3);
	}

	/**
	 *  Calculate "the distance" between two profiles, using Jiang's & Conranth similarity measure,
	 *  as proposed in "Semantic Similarity Based on Corpus Statistics and Lexical Taxonomy".
	 *
	 *@param  prof1  A Profile
	 *@param  prof2  Another Profile
	 *@return   The distance between the two profiles. The higher the value, the smaller the
	 *               similarity.
	 */
	public static double similarityJiang(Profile prof1, Profile prof2) {
		double d1 = 0.0, d2 = 0.0, d3 = 0.0;
		Iterator grams = prof1.ngrams();
		while (grams.hasNext()) {
			NGram ngram = (NGram) grams.next();
			double rank2 = prof2.getWeight(ngram);
			double rank1 = prof1.getWeight(ngram);
			if (rank2 != 0.0)
				d1 += Math.log(rank2 + rank1);
			d2 += Math.log(rank1);
		}
		grams = prof2.ngrams();
		while (grams.hasNext()) {
			NGram ngram = (NGram) grams.next();
			double rank1 = prof2.getWeight(ngram);
			d3 += Math.log(rank1);
		}
		return - ((2 * d1) - (d2 + d3));
	}

	/**
	 *  Sample application to use the Cathegorizer from the command line.
	 *
	 *@param  args  The command line arguments, tokenized
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println(
				"Usage: NGramCathegorizer profiles_dir [file_to_cathegorize]");
		} else
			try {
				NGramCathegorizer cath = new NGramCathegorizer(args[0]);
				for (int i = 1; i < args.length; i++) {
					EntryProfile prof =
						new EntryProfile(
							args[i],
							NGramConstants.USEDNGRAMSMAX,
							NGramConstants.USEDNGRAMSMIN);
					Profile res = cath.match(prof);
					System.out.println(
						"Best match for " + args[i] + " is : " + res);
				}
			} catch (Exception e) {
				System.err.println("Terminated by " + e);
				e.printStackTrace();
			}
	}

	/**
	 *  The list of profiles with the models for classification.
	 */
	protected List profiles;

	/**
	 *  Construct an uninitialized cathegorizer that uses Lin's similarity measure.
	 */
	public NGramCathegorizer() {
		profiles = new ArrayList();
		this.similarityMetric = NGramConstants.SIMILARITYLIN;
	}

	/**
	 *  Construct an uninitialized cathegorizer that uses a specific similarity measure.
	 * 
	 * @param similariMetric  The similarity metric to be used in the cathegorizer.
	 *                                     1 for Lin's measure, 2 for Jiang's & Conranth and 3 for Cavnar & Trenkle.
	 */
	public NGramCathegorizer(int similarityMetric) {
		profiles = new ArrayList();
		this.similarityMetric = similarityMetric;
	}

	/**
	 *  Construct an cathegorizer that uses Lin's similarity measure from a directory with model profiles.  
	 *
	 *@param       dirName                        Pathname for the directory with the profiles.
	 *@exception  TCatNGException           A problem occured while reading the profiles.
	 *@exception  FileNotFoundException  The pathname was not found.
	 */
	public NGramCathegorizer(String dirName)
		throws TCatNGException, FileNotFoundException {
		this(dirName, NGramConstants.SIMILARITYLIN);
	}

	/**
	 *  Construct an cathegorizer that uses a specific similarity measure from a directory with model profiles.  
	 *
	 *@param       dirName                        Pathname for the directory with the profiles.
	 * @param similariMetric  The similarity metric to be used in the cathegorizer.
	 *                                     1 for Lin's measure, 2 for Jiang's & Conranth and 3 for Cavnar & Trenkle.
	 *@exception  TCatNGException           A problem occured while reading the profiles.
	 *@exception  FileNotFoundException  The pathname was not found.
	 */
	public NGramCathegorizer(String dirName, int similarityMetric)
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
	 *  Construct an Cathegorizer that uses Lin's similarity measure from an array of resource file names.
	 *
	 *@param       fileNames                     An array with the pathnames for the model profiles.
	 *@exception  TCatNGException           A problem occured while reading the profiles.
	 *@exception  FileNotFoundException  One of the pathnames was not found.
	 */
	public NGramCathegorizer(String[] fileNames)
		throws TCatNGException, FileNotFoundException {
		this(fileNames, NGramConstants.SIMILARITYLIN);
	}

	/**
	 *  Construct an Cathegorizer that uses a specific similarity measure from an array of resource file names.
	 *
	 *@param       fileNames                     An array with the pathnames for the model profiles.
	 *@param similariMetric  The similarity metric to be used in the cathegorizer.
	 *                                     1 for Lin's measure, 2 for Jiang's & Conranth and 3 for Cavnar & Trenkle.
	 *@exception  TCatNGException           A problem occured while reading the profiles.
	 *@exception  FileNotFoundException  One of the pathnames was not found.
	 */
	public NGramCathegorizer(String[] fileNames, int similarityMetric)
		throws TCatNGException, FileNotFoundException {
		this();
		init(null, fileNames);
	}

	/**
	 *  Add a new Profiles to the list of models.
	 *
	 *@param  prof  The new Profile to be added.
	 */
	public void addProfile(Profile prof) {
		profiles.add(prof);
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
			throw new TCatNGException("Need at least one NGram input file.");
		}
		for (int i = 0; i < names.length; i++) {
			File ifi = new File(fi, names[i]);
			if (ifi.isDirectory()) continue;
			InputStream in = new FileInputStream(ifi);
			Profile prof = new DataProfile(names[i], in);
			profiles.add(prof);
		}
	}

	/**
	 *  Match a given <code>Profile</code> against all the profiles 
	 *  constituting the models in the cathegorizer.
	 *
	 *@param  prof  A <code>Profile</code>.
	 *@return  The closest matching <code>Profile</code> in the cathegorizer.
	 */
	public Profile match(Profile prof) {
		double error = Double.MAX_VALUE;
		Profile opt = null;
		Iterator iter = profiles.iterator();
		while (iter.hasNext()) {
			Profile prof2 = (Profile) iter.next();
			double newError = profileDistance(prof2, prof);
			if (newError < error) {
				error = newError;
				opt = prof2;
			}
		}
		return opt;
	}

}

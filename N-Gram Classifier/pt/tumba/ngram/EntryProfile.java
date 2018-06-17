package pt.tumba.ngram;

import java.io.*;
import java.util.*;

/**
 * A <code>Profile</code> stores N-gram frequency information for a given textual string.
 * This is a profile implementation which builds itself from an input text.
 *
 * @author     Bruno Martins
 */
public class EntryProfile implements Profile {

	/**
	 * A <code>Map</code> storing N-grams and the associated ranking position.
	 */
	protected Map gramRanks;

	/**
	 *  A <code>Map</code> storing N-grams and the associated weights.
	 */
	protected Map gramWeights;

	/**
	 *  A <code>Map</code> storing the textual String composing the N-grams.
	 */
	protected Map gramsStrings;

	/**
	 *  The lowest ranking position for storage in the N-gram profile. For instance with 
	 *  <code>theLimit=400</code> only the top 400 highest occurring N-grams will be stored.
	 */
	protected int theLimit = -1;

	/**
	 *  The highest ranking position for storage in the N-gram profile. For instance with 
	 *  <code>theLowerLimit=200</code> the top 200 highest occurring N-grams will be skipped.
	 */
	protected int theLowerLimit = 0;

	/**
	 * Constructor for the <code>EntryProfile</code> object.
	 *
	 *@param  stream           An <code>InputStream</code> from where to read the text, 
	 *                         in order to build the profile.
	 *@exception  IOException  A problem occured while reading from the <code>InputStream</code>.
	 */
	public EntryProfile(InputStream stream) throws IOException {
		this(stream, -1);
	}

	/**
	 * Constructor for the <code>EntryProfile</code> object.
	 *
	 *@param  stream           An <code>InputStream</code> from where to read the text, 
	 *                         in order to build the profile.
	 *@param  theLimit         The lowest ranking position for storage in the N-gram profile.
	 *@exception  IOException  A problem occured while reading from the <code>InputStream</code>.
	 */
	public EntryProfile(InputStream stream, int theLimit) throws IOException {
		this.theLimit = theLimit;
		digestStream(stream);
	}

	/**
	 * Constructor for the <code>EntryProfile</code> object.
	 *
	 *@param  stream           An <code>InputStream</code> from where to read the text, 
	 *                         in order to build the profile.
	 *@param  theLimit         The lowest ranking position for storage in the N-gram profile.
	 *@param  theLowerLimit    The highest ranking position for storage in the N-gram profile.
	 *@exception  IOException  A problem occured while reading from the <code>InputStream</code>.
	 */
	public EntryProfile(InputStream stream, int theLimit, int theLowerLimit)
		throws IOException {
		this.theLowerLimit = theLowerLimit;
		this.theLimit = theLimit;
		digestStream(stream);
	}

	/**
	 * Constructor for the <code>EntryProfile</code> object.
	 *
	 *@param  fname           The pathname to the File with the text used to build the profile.
	 *@exception  IOException  A problem occured while reading from the file.
	 *@exception  FileNotFoundException  A problem occured while reading from the file.
	 */
	public EntryProfile(String fname)
		throws IOException, FileNotFoundException {
		this(fname, -1);
	}

	/**
	 * Constructor for the <code>EntryProfile</code> object.
	 *
	 *@param  fname           The pathname to the File with the text used to build the profile.
	 *@param  theLimit         The lowest ranking position for storage in the N-gram profile.
	 *@exception  IOException  A problem occured while reading from the file.
	 *@exception  FileNotFoundException  A problem occured while reading from the file.
	 */
	public EntryProfile(String fname, int theLimit)
		throws IOException, FileNotFoundException {
		this.theLimit = theLimit;
		FileInputStream fi = new FileInputStream(fname);
		digestStream(fi);
		fi.close();
	}

	/**
	 * Constructor for the <code>EntryProfile</code> object.
	 *
	 *@param  fname           The pathname to the File with the text used to build the profile.
	 *@param  theLimit         The lowest ranking position for storage in the N-gram profile.
	 *@param  theLowerLimit    The highest ranking position for storage in the N-gram profile.
	 *@exception  IOException  A problem occured while reading from the file.
	 *@exception  FileNotFoundException  A problem occured while reading from the file.
	 */
	public EntryProfile(String fname, int theLimit, int theLowerLimit)
		throws IOException, FileNotFoundException {
		this.theLimit = theLimit;
		this.theLowerLimit = theLowerLimit;
		FileInputStream fi = new FileInputStream(fname);
		digestStream(fi);
		fi.close();
	}

	/**
	 * Build tbe profile from an <code>InputStream</code>
	 *
	 *@param  stream           An <code>InputStream</code> from where to read the text, 
	 *                         in order to build the profile.
	 *@exception  IOException  A problem occured while reading from the <code>InputStream</code>.
	 */
	private final void digestStream(InputStream stream) throws IOException {
		int i;
		List order = ProfileReader.read(stream);
		int limit, lowerlimit;
		if (order.size() == 0)
			return;
		if (theLimit < 0) {
			limit = -1;
			gramRanks = new HashMap(order.size());
			gramWeights = new HashMap(order.size());
			gramsStrings = new HashMap(order.size());
		} else if (order.size() < theLimit) {
			limit = ((NGram) order.get(order.size() - 1)).getCount();
			gramRanks = new HashMap(order.size());
			gramWeights = new HashMap(order.size());
			gramsStrings = new HashMap(order.size());
		} else {
			limit = ((NGram) order.get(theLimit - 1)).getCount();
			gramRanks = new HashMap(theLimit);
			gramWeights = new HashMap(theLimit);
			gramsStrings = new HashMap(theLimit);
		}
		if (theLowerLimit <= 0) {
			lowerlimit = ((NGram) order.get(0)).getCount();
		} else if (order.size() < theLowerLimit) {
			return;
		} else {
			lowerlimit = ((NGram) order.get(theLowerLimit - 1)).getCount();
		}
		i = 0;
		while (i < order.size()) {
			int cnt = ((NGram) order.get(i)).getCount();
			if (cnt > lowerlimit)
				continue;
			if (cnt < limit)
				break;
			int j = i;
			while (++j < order.size()
				&& ((NGram) order.get(j)).getCount() == cnt) {
				;
			}
			double h = (i + j + 1) * 0.5; // Smoothing - Average the position of N-grams with
			for (int k = i; k < j; k++) {   // the same occurence frequency.
				NGram gram_aux = ((NGram) order.get(k));
				Double value = new Double(h);
				gramRanks.put(gram_aux, value);
				if(NGramConstants.SMOOTHING) gramWeights.put(gram_aux,new Double(((NGram)order.get(j)).getSmoothedCount())); 
				else gramWeights.put(gram_aux,new Double(((NGram)order.get(j)).getCount()));
				gramsStrings.put(gram_aux.getString(), value);
			}
			i = j;
		}
	}

	/**
	 * Gets the ranking position of a given N-gram.
	 *
	 *@param  ng  An N-Gram
	 *@return  The associated ranking position.
	 */
	public double getRank(NGram ng) {
		Double in = (Double) gramRanks.get(ng);
		if (in == null) {
			return 0;
		} else {
			return in.doubleValue();
		}
	}

	/**
	 *  Gets the weighting score of a given N-gram.
	 *
	 *@param  ng  An N-Gram
	 *@return  The associated occurence frequency.
	 */
	public double getWeight(NGram ng) {
		Double in = (Double) gramWeights.get(ng);
		if (in == null) {
			return 0;
		} else {
			return in.doubleValue();
		}
	}

	/**
	 *  Gets the ranking position of a given N-gram.
	 *
	 *@param  ng  A String with the characters of the N-Gram
	 *@return  The associated ranking position.
	 */
	public double getRank(String ng) {
		Double in = (Double) gramsStrings.get(ng);
		if (in == null) {
			return 0;
		} else {
			return in.doubleValue();
		}
	}

	/**
	 *  Returns an <code>Iterator</code> over the N-grams in this profile.
	 *
	 *@return An <code>Iterator</code> over the N-grams in this profile.
	 */
	public Iterator ngrams() {
		return gramRanks.keySet().iterator();
	}

}

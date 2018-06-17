package pt.tumba.ngram;

import java.util.Iterator;

/**
 * Abstract interface to model an N-Gram Profile.
 * Profiles are responsible for storing N-gram occurence frequency
 * information for a given textual string.
 *
 * @author     Bruno Martins
 */
public interface Profile {
    
	/**
	 *  Gets the ranking position of a given N-gram.
	 *
	 *@param  gram  An N-Gram
	 *@return  The associated ranking position.
	 */
    public double getRank(NGram gram);
    
	/**
	 *  Gets the weighting score of a given N-gram.
	 *
	 *@param  gram  An N-Gram
	 *@return  The associated occurence frequency.
	 */
	public double getWeight(NGram gram);

	/**
	 *  Return an <code>Iterator</code> over all contained N-grams.
	 *
	 *@return    An <code>Iterator</code> over all contained N-grams.
	 */
	public Iterator ngrams();

}

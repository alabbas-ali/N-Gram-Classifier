package pt.tumba.ngram;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * A <code>Profile</code> stores N-gram frequency information for a given textual string.
 * This is a profile implementation which reads itself from a text file.
 *
 *@author     Bruno Martins
 */
public class DataProfile implements Profile {

    /**
     *  The name for the Profile (usually equal to the filename from where it was read).
     */
    protected String name;

    /**
     *  The list of N-grams in this Profile.
     */
    protected List rankedNGrams;


    /**
     *  Constructor for <code>DataProfile</code>.
     *
     *@param  name    The name of the profile.
     *@param  stream  An <code>InputStream</code> from where to read the Profile.
     */
    public DataProfile(String name, InputStream stream) {
        this.name = name;
        readStream(stream);
    }


    /**
     *  Gets the name of the Profile.
     *
     *@return    The name of the Profile.
     */
    public String getName() {
        return name;
    }

    /**
     *  Gets the ranking position of a given N-gram.
     *
     *@param  gram  An N-Gram
     *@return  The associated ranking position.
     */
    public double getRank(NGram gram) {
        Iterator iter = ngrams();
        int i = 0, lastPos = 0, lastCount = 0;
        while (iter.hasNext()) {
            i++;
            NGram g = (NGram)(iter.next());
            double count = g.getCount();
            if(count!=lastCount) lastPos = i;
            if (((NGram) iter.next()).equals(gram)) {
				while (iter.hasNext()) {
					g = (NGram)(iter.next());
					count = g.getCount();
					if(count!=lastCount) return (lastPos + i) * 0.5;
					i++;
				}
				return (lastPos + i) * 0.5;
            } 
        }
        return 0;
    }

	/**
	 *  Gets the weighting score of a given N-gram.
	 *
	 *@param  gram  An N-Gram
	 *@return  The associated occurence frequency.
	 */
	public double getWeight(NGram gram) {
		Iterator iter = ngrams();
		int i = 0;
		while (iter.hasNext()) {
			i++;
			NGram gram2 = (NGram) iter.next();
			if (gram2.equals(gram)) {
				return gram2.getCount();
			} 
		}
		return 0;
	}

    /**
     *  Gets the number of N-grams in the Profile.
     *
     *@return The number of N-grams in the Profile.
     */
    public int getSize() {
        return rankedNGrams.size();
    }


    /**
     *  Returns an <code>Iterator</code> over the N-grams in this profile.
     *
     *@return An <code>Iterator</code> over the N-grams in this profile.
     */
    public Iterator ngrams() {
        return rankedNGrams.iterator();
    }


    /**
     *  Reads the <code>Profile</code> data from an <code>InputStream</code>.
     *
     *@param  stream  The <code>InputStream</code> from where to read the <code>Profile</code>.
     */
    private final void readStream(InputStream stream) {
        BufferedReader bi = new BufferedReader(new InputStreamReader(stream));
        rankedNGrams = new ArrayList();
        int b = 0;
        byte[] bs = new byte[10];
        try {
            String aux;
            while((aux=bi.readLine())!=null) {
            		double cnt;
            		byte aux2[];
            		try {
            			cnt = (new Double(aux.substring(aux.indexOf(" \t")+2).trim())).doubleValue();
            			aux2 = aux.substring(0,aux.indexOf(" \t")).getBytes();
            		} catch ( Exception e ) { 
            			cnt = 1;
            			aux2 = aux.getBytes();
            		}
					int j = 0, k=0;
				    middleloop :
					while(k<aux2.length) {
						for (int i = 0; i < NGramConstants.SKIPABLE.length; i++) {
							if (aux2[k] == NGramConstants.SKIPABLE[i]) {
								k++;
								break middleloop;
							} 
						}
						if (j < 10) bs[j++] = (aux2[k] != '_') ? (byte) aux2[k] : (byte) ' ';
						k++;
					}
				    if (j > 0) rankedNGrams.add(new NGram(bs, 0, j - 1,cnt));
            }
        } catch (IOException e) {
        }
    }


    /**
     * Converts this profile to a String Object.
     *
     *@return    A String with the name of this <code>Profile</code>.
     */
    public String toString() {
        return getName();
    }

}

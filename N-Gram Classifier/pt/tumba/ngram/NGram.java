package pt.tumba.ngram;

/**
 *  This class models a concrete and simple N-Gram. To make it slightly more 
 *  interesting (and efficient), the class follows a Flyweight pattern, so that for each
 *  different N-Gram there will only be one instance in the System.
 *
 *@author     Bruno Martins
 */
public class NGram implements Comparable {

	/**  An array with the known N-grams. The size must be a power of 2. */
	public static NGram[] known = new NGram[512];
	
	/**  Number of N-grams in the array of known N-grams. */
	protected static int knownCount = 0;

	/** Empty space to leave each time we have to increment the cache. */
	protected static int knownStep = 400;

    /** Boolean flag indicating the use of the N-gram cache. */
	protected static boolean useCache = true;

	/**
	 * Array of bytes storing the N-gram.
	 */
	protected byte[] bytes;

	/**
	 * Size of this N-gram.
	 */
	protected int size;

	/**
	 * Number of occurences of this NGram.
	 */
	protected double count = 1;

	/**
	 *  Constructor for the NGram object.
	 */
	protected NGram() {
	}

	/**
	 *  Constructor for the NGram object
	 *
	 *@param  bytes   An array of bytes with the N-gram.
	 *@param  start   Starting position in the array of bytes.
	 *@param  length  Ending position in the array of bytes.
     *@param count Occurence frequency for this NGram.
	 */
	public NGram(byte[] bytes, int start, int length, double count) {
		this.count = count;
		this.size = length;
		this.bytes = new byte[length];
		for (int i = 0; i < length; i++) {
			this.bytes[i] = bytes[i + start];
		}
	}

	/**
	 *  Constructor for the NGram object
	 *
	 *@param  bytes   An array of bytes with the N-gram.
	 *@param  start   Starting position in the array of bytes.
	 *@param  length  Ending position in the array of bytes.
	 */
	public NGram(byte[] bytes, int start, int length) {
		this(bytes,start,length,1);
	}

	/**
	 *  Constructor for the NGram object which copies another N-gram.
	 *
	 *@param  ng   An N-gram.
	 */
	public NGram ( NGram ng ) {
		this.count = ng.count;
		this.bytes = ng.bytes;
		this.size = ng.size;
	}

	/**
	 * Constructor for the NGram object
	 *
	 *@param  str  A string with the N-gram.
	 */
	public NGram(String str) {
		this(str.getBytes(), 0, str.getBytes().length);
	}

	/**
	 * Encode a byte sequence.
	 *
	 *@param  bytes   An array of bytes.
	 *@param  start   Starting position in the array of bytes.
	 *@param  length  Ending position in the array of bytes.
	 *@return  An hash code for the array of bytes.
	 */
	private static int code(byte[] bytes, int start, int length) {
		int h = 0;
		for (int i = 0; i < length; i++) {
			h = (0x50501005 * h + 0x0AAA0AAA) ^ bytes[i + start];
		}
		return h;
	}

	/**
	 *  Gets the number of different N-Grams.
	 *
	 *@return    The number of different N-Grams.
	 */
	public static int getNGramCount() {
		return knownCount;
	}

	/**
	 *  QuasiConstructor. FlyWeight means that we first have to look if we
	 *  allready know the current N-gram.
	 *
	 *@param  bytes  Sequence of bytes with the N-gram
	 *@return        The N-gram in the sequence of bytes.
	 */
	public static NGram newNGram(byte[] bytes) {
		return newNGram(bytes, 0, bytes.length);
	}

	/**
	 *  QuasiConstructor. FlyWeight means that we first have to look if we
	 *  allready know the current N-gram.
	 *
	 *@param  bytes  Sequence of bytes with the N-gram
	 *@param  start  Starting position in the sequence of bytes.
	 *@return        The N-gram in the sequence of bytes.
	 */
	public static NGram newNGram(byte[] bytes, int start) {
		return newNGram(bytes, start, bytes.length - start);
	}

	/**
	 *  QuasiConstructor. FlyWeight means that we first have to look if we
	 *  allready know the current N-gram.
	 *
	 *@param  str  A string with the N-gram.
	 *@return        The N-gram in the String.
	 */
	public static NGram newNGram(String str) {
		return newNGram(str.getBytes(), 0, str.getBytes().length);
	}

	/**
	 *  QuasiConstructor. FlyWeight means that we first have to look if we
	 *  allready know the current beasty.
	 *
	 *@param  bytes  Sequence of bytes with the N-gram
	 *@param  start  Starting position in the sequence of bytes.
	 *@param  length Ending position in the sequence of bytes.
	 *@return        The N-gram in the sequence of bytes.
	 */
	public static NGram newNGram(byte[] bytes, int start, int length) {
		return newNGram(bytes, start, length,1);
	}

	/**
	 *  QuasiConstructor. FlyWeight means that we first have to look if we
	 *  allready know the current beasty.
	 *
	 *@param  bytes  Sequence of bytes with the N-gram
	 *@param  start  Starting position in the sequence of bytes.
	 *@param  length Ending position in the sequence of bytes.
     *@param count Occurence frequency for this NGram.
	 *@return        The N-gram in the sequence of bytes.
	 */
	public static NGram newNGram(byte[] bytes, int start, int length, double count) {
		if (!useCache) {
			NGram nn = new NGram(bytes, start, length,count);
			knownCount++;
			return nn;
		}
		int c = code(bytes, start, length);
		int p = (c % known.length);
		if (p < 0) p += known.length;
		synchronized (known) {
			while (known[p] != null) {
				if (known[p].equals(bytes, start, length)) break;
				p = (p + 5) % known.length;
			}
		}
		if (known[p] != null) return known[p];


		knownCount++;
		NGram nn = new NGram(bytes, start, length,count);
		if (knownCount < knownStep) {
			known[p] = nn;
			return known[p];
		}
		NGram[] oldknown = known;
		int len = known.length;
		int l2n = len * 2;
		known = new NGram[l2n];
		for (int i = 0; i < len; i++) {
			if (oldknown[i] != null) {
				int h = oldknown[i].hashCode() % l2n;
				if (h < 0) {
					h += l2n;
				}
				while (known[h] != null) {
					h = (h + 5) % l2n;
				}
				known[h] = oldknown[i];
			}
		}
		knownStep += knownStep;
		p = nn.hashCode() % l2n;
		if (p < 0) {
			p += l2n;
		}
		while (known[p] != null) {
			p = (p + 5) % l2n;
		}
		return known[p];
	}

	/**
	 * Compares this N-gram with another one supplied as an array of bytes.
	 *
	 *@param  bytes   An array of bytes with an N-gram.
	 *@param  start   Starting position in the array of bytes.
	 *@param  length  Ending position in the array of bytes.
	 *@return true if both N-grams are equal and false otherwise.
	 */
	public boolean equals(byte[] bytes, int start, int length) {
		if (size != length) {
			return false;
		} else {
			for (int i = 0; i < size; i++) {
				if (this.bytes[i] != bytes[i + start]) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Compares this N-gram with another Object (checking if its an N-gram object being compared).
	 *
	 *@param  e1   An object.
	 *@return true if the Object being compared is an N-Gram and if
         *        both N-grams are equal. false otherwise.
	 */
	public boolean equals(Object e1) {
		NGram aux;
		if (e1 instanceof NGram) {
			aux = ((NGram)(e1));
		} else return false;
		return aux.equals(bytes, 0, bytes.length);
	}

	/**
	 *  Return a single byte out of the NGram.
	 *
	 *@param  pos                         Return the 1st, 2nd, 3rd, ... byte.
	 *@return                                 The byte value.
	 *@throws  ArrayIndexOutOfBoundException   The NGram does not contain the given position.
	 */
	public int getByte(int pos) {
		return bytes[pos];
	}

	/**
	 *  Return the size of this NGram.
	 *
	 *@return    The size of this NGram.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Returns a <code>String</code> representation of this NGram.
	 *
	 *@return    A <code>String</code> representation of this NGram.
	 */
	public String getString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size; i++) try {
			sb.append((char) (bytes[i]));
		} catch ( Exception e) {}
		return sb.toString();
	}

	/**
	 *  Override the hashCode, allowing to hash NGrams against tiny byte sequences.
	 *
	 *@return    An hashcode for this NGram.
	 */
	public int hashCode() {
		return code(bytes, 0, bytes.length);
	}

	/**
	 * Returns a <code>String</code> representation of this NGram, where occurence
	 * frequency information is also present.
	 *
	 *@return    A <code>String</code> representation of this NGram.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(super.toString() + "{");
		for (int i = 0; i < size; i++) {
			if (i != 0) {
				sb.append(";");
			}
			sb.append(bytes[i]);
		}
		sb.append("}[");
		sb.append(getCount());
		sb.append("]");
		return sb.toString();
	}
	

	/**
	 *  Compares the number of occurences of this N-gram with another.
	 *
	 *@param  e1  An object (must be an instance of NGram)
	 *@return the value 0 if the argument is an NGram with equal occurence frequency;
	 *             a value less than 0 if the argument is an NGram occuring more frequently;
	 *             and a value greater than 0 if the argument is an Ngram occuring less frequently.
	 * @throws NullPointerException if e1 is null.
     * @throws ClassCastException if e1 is not an NGram object.  
	 */
	public int compareTo(Object e1) {
		return ((NGram) e1).getCount() - getCount();
	}


	/**
	 *  Returns the number of occurences of this N-gram.
	 *
	 *@return   The number of occurences of this N-gram.
	 */
	public int getCount() {
		return (int)count;
	}

	/**
	 * Returns the number of occurences of this N-gram, using Good-Turing smoothing.
	 * TODO: Only works if the cache is being used.
	 *
	 *@return   The smoothed number of occurences of this N-gram.
	 */
	public double getSmoothedCount() {
		int count = getCount();
		int numberCount = 0;
		int numberCountPlusOne = 0;
		for (int i=0; i<known.length; i++) {
			if(known[i]==null) continue;
			int countAux = known[i].getCount();
			if(countAux==count) numberCount++;
			else if(countAux==(count+1)) numberCountPlusOne++;
		}
		return (count + 1) * (numberCountPlusOne / numberCount);
	}

	/**
	 *  Increments the number of occurences of this N-gram. 
	 */
	public void inc() {
		count++;
	}

}

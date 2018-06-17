package pt.tumba.ngram;

/**
 * Contant values used in the TCatNG package.
 *
 *@author     Bruno Martins
 */
public class NGramConstants {
    
	/**
	 *  Bytes skipable while building the proviles. These correspond to number characters.
	 */
	public final static byte[] SKIPABLE = new byte[] {32, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 10, 13};

       /**
        *  The lowest ranking position for storage in the N-gram profiles. For instance with 
        *  <code>USEDNGRAMSMAX=400</code> only the top 400 highest occurring N-grams will be stored.
        */
	public final static int USEDNGRAMSMAX = 400;

       /**
        *  The highest ranking position for storage in the N-gram profile. For instance with 
        *  <code>USEDNGRAMSMIN=200</code> the top 200 highest occurring N-grams will be skipped.
        */
	public final static int USEDNGRAMSMIN = 0;

	/** 
	 * Use the similarity metric proposed by Lin in "An information-theoretic definition of similarity".
	 */
	public final static int SIMILARITYLIN = 1;
	
	/** 
	 * Use the similarity metric proposed by Jiand & Conranth in "Semantic Similarity Based on Corpus Statistics and Lexical Taxonomy".
	 */
	public final static int SIMILARITYJIANG = 2;
	
	/** 
	 * Use the similarity metric proposed by Cavnar & Trenkle.
	 */
	public final static int SIMILARITYOUTOFPLACE = 3;

	/** 
	 * Use Good-Turing smoothing on the NGram occurence frequency.
	 */
	public final static boolean SMOOTHING = true;

}

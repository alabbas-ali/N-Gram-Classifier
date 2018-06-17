package pt.tumba.parser;

import java.util.*;

/**
 * @author Bruno Martinss
 */
public class Content {

	protected List terms;
	protected Map termsLinks;
	protected List textBlocks;
	protected Map wordGrams;
	protected Map annotationCount;
	protected Map annotations;
	protected StringBuffer content;
	protected int numTokens;
	protected int numTokensWithStopWords;

	/**
	 * The rabin hash function used to produce an hash code for the content of the
	 * document
	 */
	private RabinHashFunction hashFunction = new RabinHashFunction();

	/**
	 * Returns the terms extracted from the document. The difference for the
	 * getTokens method is that this one filters the stop-words.
	 *
	 * @return An iterator over the terms extracted from the document
	 */
	public Iterator getTerms() {
		Vector v = new Vector();
		Iterator words = getTokens();
		String token;
		while (words.hasNext()) {
			token = words.next().toString();
			if (StopWords.isStopWord(token)) {
				v.addElement(token);
			}
		}
		return v.iterator();
	}

	/**
	 * Returns the text extracted from the document
	 *
	 * @return A String with the text extracted from the document
	 */
	public String getText() {
		StringBuffer aux = new StringBuffer();
		for (int i = 0; i < textBlocks.size(); i++) {
			aux.append(textBlocks.get(i).toString());
			aux.append("\n");
		}
		return aux.toString();
	}

	/**
	 * Returns the text extracted from the document, with garbage sentences removed
	 *
	 * @return A String with the text extracted from the document
	 */
	public String getFilteredText() {
		StringBuffer aux = new StringBuffer();
		for (int i = 0; i < textBlocks.size(); i++) {
			String auxs = textBlocks.get(i).toString().trim();
			if (auxs.toLowerCase().startsWith("this page uses frames"))
				continue;
			if (auxs.indexOf(" ") == -1 && auxs.indexOf("\t") == -1 && auxs.indexOf("-") == -1 && auxs.length() > 30)
				continue;
			if (auxs.startsWith("-----BEGIN PGP SIGNATURE----- Version: ")
					&& auxs.endsWith("-----END PGP SIGNATURE-----"))
				continue;
			aux.append(auxs);
			aux.append("\n");
		}
		return aux.toString();
	}

	/**
	 * Returns the sentences extracted from the documents
	 *
	 * @return An array of Strings with the sentences extracted from the documents
	 */
	public String getTextBlocks()[] {
		String aux[] = new String[textBlocks.size()];
		for (int i = textBlocks.size() - 1; i >= 0; i--) {
			aux[i] = textBlocks.get(i).toString();
		}
		return aux;
	}

	/**
	 * Returns the token at a given position in the document
	 *
	 * @param pos
	 *            The position within the document
	 * @return The token at the given position in the document
	 */
	public String getToken(int pos) {
		if (pos < 0 || pos >= terms.size()) {
			return null;
		}
		return terms.get(pos).toString();
	}

	/**
	 * Returns the tokens extracted from the document
	 *
	 * @return An iterator over the tokens extracted from the document
	 */
	public Iterator getTokens() {
		return annotations.keySet().iterator();
	}

	/**
	 * Return the sequences of 1 to 5 consequtive words extracted from the document
	 *
	 * @return An iterator over the word-grams of length 1 to 5 extracted from the
	 *         document
	 */
	public Iterator getWordGrams() {
		return wordGrams.keySet().iterator();
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
	public int getNumTokens() {
		return numTokens;
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
	public long getHashCode() {
		return hashFunction.hash(content.toString());
	}

	/**
	 * Gets the frequency attribute of the HTMLParser object
	 *
	 * @param word
	 *            Description of the Parameter
	 * @return The frequency value
	 */
	public int getFrequency(String word) {
		int aux[][] = (int[][]) (annotations.get(word));
		return ((aux == null) ? -1 : aux[0][0]);
	}

	/**
	 * Gets the frequentTerms attribute of the HTMLParser object
	 *
	 * @param minfreq
	 *            Description of the Parameter
	 * @return The frequentTerms value
	 */
	public Iterator getFrequentTerms(int minfreq) {
		Vector v = new Vector();
		Iterator words = getTerms();
		String token;
		while (words.hasNext()) {
			token = words.next().toString();
			if (getFrequency(token) >= minfreq) {
				v.addElement(token);
			}
		}
		return v.iterator();
	}

	protected int getCountAux(String term, int i) {
		int aux[] = (int[]) (annotationCount.get(term));
		if (aux == null)
			return 0;
		return aux[i];
	}

	/**
	 * Gets the frequency attribute of the HTMLParser object
	 *
	 * @param word
	 *            Description of the Parameter
	 * @return The frequency value
	 */
	public double getMean(String word) {
		int aux[][] = (int[][]) (annotations.get(word));
		return ((aux == null || numTokens == 0) ? -1 : ((double) (aux[0][0]) / (double) (numTokens)));
	}

	/**
	 * Gets the positions attribute of the HTMLParser object
	 *
	 * @param word
	 *            Description of the Parameter
	 * @return The positions value
	 */
	public int getPositions(String word)[] {
		int aux[][] = (int[][]) (annotations.get(word));
		return ((aux == null) ? null : aux[2]);
	}

	/**
	 * Gets the positions attribute of the HTMLParser object
	 *
	 * @param word
	 *            Description of the Parameter
	 * @param url
	 *            Description of the Parameter
	 * @return The positions value
	 */
	public int getPositions(String word, String url)[] {
		HashMap aux2 = (HashMap) (termsLinks.get(word));
		if (aux2 == null) {
			return null;
		}
		Vector aux3 = (Vector) (aux2.get(url));
		if (aux3 == null) {
			return null;
		}
		int aux[] = new int[aux3.size()];
		for (int i = aux3.size() - 1; i >= 0; i--) {
			aux[i] = ((Integer) (aux3.elementAt(i))).intValue();
		}
		return aux;
	}

	/**
	 * Gets the sentence positions attribute of the HTMLParser object
	 *
	 * @param word
	 *            Description of the Parameter
	 * @return The positions value
	 */
	public int getSentences(String word)[] {
		int aux[][] = (int[][]) (annotations.get(word));
		return ((aux == null) ? null : aux[3]);
	}

	/**
	 * Gets the termInfo attribute of the HTMLParser object
	 *
	 * @param word
	 *            Description of the Parameter
	 * @return The termInfo value
	 */
	public int getTermInfo(String word) {
		int aux[][] = (int[][]) (annotations.get(word));
		return ((aux == null) ? -1 : aux[1][0]);
	}

	/**
	 * Gets the frequency attribute of the HTMLParser object
	 *
	 * @param word
	 *            Description of the Parameter
	 * @return The frequency value
	 */
	public double getTScore(String word) {
		int aux[][] = (int[][]) (annotations.get(word));
		double mean = ((aux == null || numTokens == 0) ? -1 : ((double) (aux[0][0]) / (double) (numTokens)));
		double variance = (mean == -1) ? -1
				: ((aux[0][0] * aux[0][0]) - (numTokens * mean * mean)) / (double) (numTokens);
		return (mean == -1) ? -1 : (numTokens - mean) / (Math.sqrt(variance) / Math.sqrt(numTokens));
	}

	/**
	 * Gets the frequency attribute of the HTMLParser object
	 *
	 * @param word
	 *            Description of the Parameter
	 * @return The frequency value
	 */
	public double getVariance(String word) {
		int aux[][] = (int[][]) (annotations.get(word));
		double mean = ((aux == null || numTokens == 0) ? -1 : ((double) (aux[0][0]) / (double) (numTokens)));
		return (mean == -1) ? -1 : ((aux[0][0] * aux[0][0]) - (numTokens * mean * mean)) / (double) (numTokens);
	}

	/**
	 * Gets the nGramRank attribute of the HTMLParser object
	 *
	 * @param wgram
	 *            Description of the Parameter
	 * @return The nGramRank value
	 */
	public int getWordGramFrequency(String wgram) {
		Integer aux = (Integer) (wordGrams.get(wgram));
		return ((aux == null) ? 0 : aux.intValue());
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
	public String getOriginalContent() {
		return content.toString();
	}

	protected Content(MetaData metadata) {
		this.metadata = metadata;
		annotationCount = new HashMap();
		textBlocks = new Vector();
		terms = new Vector();
		annotations = new HashMap();
		termsLinks = new HashMap();
		wordGrams = new HashMap();
		numTokens = numTokensWithStopWords = 0;
		content = new StringBuffer();
	}

	private MetaData metadata;

}

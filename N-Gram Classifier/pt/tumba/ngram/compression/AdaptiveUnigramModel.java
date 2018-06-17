package pt.tumba.ngram.compression;

/** <P>Provides an adaptive model based on bytes observed in the input
 * stream.  Each byte count is initialized at <code>1</code> and
 * incremented by <code>1</code> for each instance seen. If
 * incrementing an outcome causes the total count to exceed
 * <code>MAX_COUNT</code>, then all counts are divided by 2 and
 * rounded up.  Estimation is by frequency (also known as a maximum
 * likelihood estimate).
 *
 * @author Bruno Martins
 */
public final class AdaptiveUnigramModel implements ArithCodeModel {

    /** Construct an adaptive unigram model, initializing all byte counts
     * and end-of-file to <code>1</code>.
     */
    public AdaptiveUnigramModel() {                           // initial cumulative counts
	for (int i = 0; i < NUM_BYTES; ++i)  _count[i] = i;   // low[i]   = high[i+1]
	_count[EOF_INDEX] = EOF_INDEX;                        // low[EOF] = high[255]
	_count[TOTAL_INDEX] = TOTAL_INDEX;                    // total    = high[EOF]
    }

	/** Calculates <code>{low count, high count, total count}</code> for
	 * the given symbol in the current context.  The symbol is either
	 * an integer representation of a byte (0-255) or -1 to denote end-of-file.
	 * The cumulative counts
	 * in the return must be such that <code>0 <= low count < high
	 * count <= total count</code>.  
	 * <P>
	 * This method will be called exactly once for each symbol being
	 * encoded or decoded, and the calls will be made in the order in
	 * which they appear in the original file.  Adaptive models
	 * may only update their state to account for seeing a symbol
	 * <emph>after</emph> returning its current interval.
	 * @param symbol The next symbol to decode.
	 * @param result Array into which to write range.
	 * @return Array containing low count, high count and total.
	 */
    public void interval(int symbol, int[] result) {
	if (symbol == EOF) symbol = EOF_INDEX;
	result[0] = lowCount(symbol);
	result[1] = highCount(symbol);
	result[2] = totalCount();
	increment(symbol);
    }

	/** Returns the symbol whose interval of low and high counts
	 * contains the given count.  Ordinary outcomes are positive
	 * integers, and the two special constants <code>EOF</code> or
	 * <code>ESCAPE</code>, which are negative.
	 * @param count The given count.
	 * @return The symbol whose interval contains the given count.
	 */
    public int pointToSymbol(int midCount) {
	int low = 0;
	int high = TOTAL_INDEX;
	while (true) { // binary search returns when it finds result
     	    int mid = (high+low)/2;
	    if (_count[mid] > midCount) { 
		if (high == mid) --high;
		else high = mid; 
	    } else if (_count[mid+1] > midCount) {
		return (mid==EOF_INDEX) ? EOF : mid;
	    } else { 
		if (low==mid) ++low;
		else low = mid;
	    }
	}
    }

	/** Returns the total count for the current context.
	 * @return Total count for the current context.
	 */
    public int totalCount() { return _count[TOTAL_INDEX]; }

	/** Returns <code>true</code> if current context has no count
	 * interval for given symbol.  Successive calls to
	 * <code>escaped(symbol)</code> followed by
	 * <code>interval(ESCAPE)</code> must eventually lead to a a
	 * <code>false</code> return from <code>escaped(symbol)</code>
	 * after a number of calls equal to the maximum context size.
	 * The integer representation of symbol is as in <code>interval</code>.
	 * @param symbol Symbol to test whether it is encoded.
	 * @return <code>true</code> if given symbol is not represented in the current context.
	 */
    public boolean escaped(int symbol) { return false; }

	/** Excludes outcome from occurring in next estimate.  A symbol must
	 * not be excluded and then coded or decoded.  Exclusions in the model
	 * must be coordinated for encoding and decoding.
	 * @param symbol Symbol which can be excluded from the next outcome.
	 */
    public void exclude(int i) { }

	/** Increments the model as if it had just encoded or decoded the
	 * specified symbol in the stream.  May be used to prime models by
	 * "injecting" a symbol into the model's stream without
	 * coding/decoding it in the stream of coded bytes.  Calls must be
	 * coordinated for encoding and decoding.  Will be called
	 * automatically by the models for symbols they encode or decode.
	 * @param symbol Symbol to add to the model.  
	 */
    public void increment(int i) {
	while (++i <= TOTAL_INDEX) ++_count[i];
	if (totalCount() >= MAX_COUNT) rescale();
    }
     
    /** Counts for each outcome. Indices 0 to 255 for the
     * usual counts, 256 for end-of-file, and 257 for total.
     * Each outcome i between 0-256 is coded by interval 
     * (_count[i],_count[i+1],_count[257]).
     */
    private int[] _count = new int[258];

    /** The cumulative count of all outcomes below given outcome.
     * @param i Index of given outcome.
     * @return Low count of interval for given symbol.
     */
    private int lowCount(int i) { return _count[i]; }

    /** The cumulative count of all outcomes below given outcome plus
     * the count of the outcome.
     * @param i Index of given outcome.
     * @return High count of interval for given symbol.
     */
    private int highCount(int i) { return _count[i+1]; }

    /** Rescale the counts by adding 1 to all counts and dividing by
     * <code>2</code>.
     */
    private void rescale() {
	int[] freqs = new int[_count.length];
	for (int i = 1; i < freqs.length; ++i)
	    freqs[i] = (_count[i] - _count[i-1] + 1) / 2;  // compute from cumulative; round up
	for (int i = 1; i < _count.length; ++i)            // compute cumulative;
	    _count[i] = _count[i-1] + freqs[i];            // _count[0] = 0 is implicit
    }

    /** Maximum count before rescaling.
     */
    private static final int MAX_COUNT = 64*1024;
    
    /** Total number of bytes.
     */
    private static final int NUM_BYTES = 256;

    /** Index in the count array for the end-of-file outcome.
     */
    private static final int EOF_INDEX = 256;
    
    /** Index in the count array for the cumulative total of all
     * outcomes.
     */
    private static final int TOTAL_INDEX = 257;

}

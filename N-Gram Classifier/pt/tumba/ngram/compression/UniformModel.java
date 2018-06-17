package pt.tumba.ngram.compression;

/** <P>A singleton uniform distribution byte model.  Provides a single
 * static member that is a non-adaptive model assigning equal
 * likelihood to all 256 bytes and the end-of-file marker.  This will
 * require approximately -log<sub>2</sub> 1/257 ~ 8.006, bits per symbol,
 * including the end-of-file symbol.
 *
 * @author Bruno Martins
 */
public final class UniformModel implements ArithCodeModel {

	/** Returns the total count for the current context.
	 * @return Total count for the current context.
	 */
    public int totalCount() {
	return NUM_OUTCOMES;
    }
     
	/** Returns the symbol whose interval of low and high counts
	 * contains the given count.  Ordinary outcomes are positive
	 * integers, and the two special constants <code>EOF</code> or
	 * <code>ESCAPE</code>, which are negative.
	 * @param count The given count.
	 * @return The symbol whose interval contains the given count.
	 */
    public int pointToSymbol(int midCount) {
	return (midCount == EOF_INDEX ? EOF : midCount);
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
	result[0] = symbol == EOF ? EOF_INDEX : symbol;
	result[1] = result[0] + 1;
	result[2] = NUM_OUTCOMES;
    }

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
    public void exclude(int symbol) {  }

	/** Increments the model as if it had just encoded or decoded the
	 * specified symbol in the stream.  May be used to prime models by
	 * "injecting" a symbol into the model's stream without
	 * coding/decoding it in the stream of coded bytes.  Calls must be
	 * coordinated for encoding and decoding.  Will be called
	 * automatically by the models for symbols they encode or decode.
	 * @param symbol Symbol to add to the model.  
	 */
    public void increment(int symbol) { }

    /** A re-usable uniform model. */
    public final static UniformModel MODEL = new UniformModel();

    /** Construct a uniform model.
     */
    private UniformModel() { }

    /** Total number of bytes.
     */
    private static final int NUM_BYTES = 256;
    
    /** Index in the implicit count array for the end-of-file outcome.
     */
    private static final int EOF_INDEX = 256;
    
    /** Index in the count array for the cumulative total
     * of all outcomes.
     */
    private static final int NUM_OUTCOMES=NUM_BYTES+1;
    
}

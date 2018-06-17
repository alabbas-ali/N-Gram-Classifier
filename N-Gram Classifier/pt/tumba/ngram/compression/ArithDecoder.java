package pt.tumba.ngram.compression;

import java.io.IOException;
import java.io.InputStream;

/** <P>Performs arithmetic decoding, converting bit input into
 * cumulative probability interval output.  Returns probabilities as
 * integer counts <code>low</code>, <code>high</code> and
 * <code>total</code>, with the range being
 * <code>[low/total,high/total)</code>.
 * 
 *
 * @author Bruno Martins
 * @see ArithEncoder
 * @see BitInput 
 */
public final class ArithDecoder {

	/** The low bound on the current interval for coding.  Initialized
	 * to zero.
	 */
	protected long _low; // implied = 0;

	/** The high bound on the current interval for coding.
	 * Initialized to top value possible.
	 */
	protected long _high = TOP_VALUE;

	/** Precision of coding, expressed in number of bits used for
	 * arithmetic before shifting out partial results.
	 */
	static final protected int CODE_VALUE_BITS = 27;
    
	/** The largest possible interval value. All <code>1</code>s.
	 */
	static final protected long TOP_VALUE = ((long) 1 << CODE_VALUE_BITS) - 1;

	/** 1/4 of the largest possible value plus one.
	 */
	static final protected long FIRST_QUARTER = TOP_VALUE / 4 + 1; 

	/** 1/2 of the largest possible value; <code>2 * FIRST_QUARTER</code>
	 */
	static final protected long HALF = 2 * FIRST_QUARTER;
    
	/** 3/4 of the largest possible value; <code>3 * FIRST_QUARTER</code>
	 */
	static final protected long THIRD_QUARTER = 3 * FIRST_QUARTER;

    /** Construct an arithmetic decoder that reads from the given
     * bit input.
     * @param in Bit input from which to read bits.
     * @throws IOException If there is an exception buffering from the bit input stream.
     */
    public ArithDecoder(BitInput in) throws IOException {
	_in = in;
	for (int i = 1; i <= CODE_VALUE_BITS; ++i) {
	    bufferBit();
	    ++_bufferedBits;
	}
    }

    /** Construct an arithmetic decoder that reads from the given
     * input stream.
     * @param in Input stream from which to read.
     * @throws IOException If there is an exception buffering from input stream.
     */
    public ArithDecoder(InputStream in) throws IOException {
	this(new BitInput(in));
    }

    /** Returns <code>true</code> if the end of stream has been reached
     * and there are no more symbols to decode.
     * @return <code>true</code> if the end of stream has been reached.
     */
    public boolean endOfStream() { return _endOfStream; }

    /** Returns a count for the current symbol that will be between the
     * low and high counts for the symbol in the model given the total count.
     * Once symbol is retrieved, the model is used to compute the actual low,
     * high and total counts and {@link #removeSymbolFromStream} is called.
     * @param totalCount The current total count for the model.
     * @return A count that is in the range above or equal to the low count and less than the high count of the next symbol decoded.
     */
    public int getCurrentSymbolCount(int totalCount) {
	return (int) (((_value - _low + 1) * totalCount - 1) / (_high - _low + 1));
    }

    /** Removes a symbol from the input stream that was coded with counts
     * <code>{ low, high, total }</code>.  Called after {@link #getCurrentSymbolCount}.
     * @param counts Array of low, high and total count used to code the symbol.
     * @throws IOException If there is an exception in buffering input from the underlying input stream.
     * @see #removeSymbolFromStream(long,long,long)
     */
    public void removeSymbolFromStream(int[] counts) throws IOException {
	removeSymbolFromStream(counts[0],counts[1],counts[2]);
    }

    /** Removes a symbol from the input stream.  Called after {@link #getCurrentSymbolCount}.
     * @param lowCount Cumulative count for symbols indexed below symbol to be removed.
     * @param highCount <code>lowCount</code> plus count for this symbol.
     * @param totalCount Total count for all symbols seen.
     * @throws IOException If there is an exception in buffering input from the underlying input stream.
     */
    public void removeSymbolFromStream(long lowCount, long highCount, long totalCount) throws IOException {
	long range = _high - _low + 1;
	_high = _low + (range * highCount) / totalCount - 1;
	_low = _low + (range * lowCount) / totalCount;
	while (true) {
	    if (_high < HALF) {
		// no effect
	    } else if (_low >= HALF) {
		_value -= HALF;
		_low -= HALF;
		_high -= HALF;
	    } else if (_low >= FIRST_QUARTER && _high <= THIRD_QUARTER) {
		_value -= FIRST_QUARTER;
		_low -= FIRST_QUARTER;
		_high -= FIRST_QUARTER;
	    } else {
		return;
	    }
	    _low <<= 1; // = 2 * _low; 	    // _low <<= 1;
	    _high = (_high << 1) + 1; // 2 * _high + 1; //	    _high = (_high<<1) + 1;
	    bufferBit();
	}
    }

    /** Closes underlying bit output.
     * @throws IOException If there is an underlying I/O exception in the bit input.
     */
    public void close() throws IOException { _in.close(); }

    /** Input stream from which to read bits.
     */
    private final BitInput _in;
    
    /** Current bits for decoding.
     */
    private long _value; // implied = 0;

    /** Value will be <code>true</code> if the end of stream has
     * been reached.
     */
    private boolean _endOfStream = false;

    /** Number of bits that have been buffered.
     */
    private int _bufferedBits; // implied = 0;

    /** Reads a bit from the underlying bit input stream and buffers it.
     * @throws IOException If there is an <code>IOException</code> buffering from the underlying bit stream.
     */
    private void bufferBit() throws IOException {
	if (_in.endOfStream()) { 
	    if (_bufferedBits == 0) { 
		_endOfStream = true; 
		return; 
	    }
	    _value <<= 1;
	    --_bufferedBits;
	} else {
	    _value = (_value<<1);
	    if (_in.readBit()) ++_value;
	}
    }

}

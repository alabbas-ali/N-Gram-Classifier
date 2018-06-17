package pt.tumba.ngram.compression;

/** Provides a cumulative, adaptive byte model implementing
 * prediction by partial matching up to a specified maximum context size.
 * Uses Method C for estimation.  
 * 
 * Constants that control behavior include the maximum total count before
 * rescaling, and the minimum count to retain after rescaling (an escape
 * is always maintained with a count of at least <code>1</code>).
 *
 * @author Bruno Martins
 */
public final class PPMModel implements ArithCodeModel {

    /** Construct a new model with the specified maximum length of
     * context to use for prediction.
     * @param maxContextLength Maximum length of context to use for prediction.
     */
    public PPMModel(int maxContextLength) {
	_maxContextLength = maxContextLength;
	_buffer = new ByteBuffer(maxContextLength+1);
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
    public boolean escaped(int symbol) {
	return (_contextNode != null
		&& (symbol == ArithCodeModel.EOF   
		    || !_contextNode.hasDaughter(symbol)));
    }

	/** Excludes outcome from occurring in next estimate.  A symbol must
	 * not be excluded and then coded or decoded.  Exclusions in the model
	 * must be coordinated for encoding and decoding.
	 * @param symbol Symbol which can be excluded from the next outcome.
	 */
    public void exclude(int i) {
	_excludedBytes.add(i);
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
	if (symbol == ArithCodeModel.EOF) _backoffModel.interval(EOF,result,_excludedBytes);
	else if (symbol == ArithCodeModel.ESCAPE) intervalEscape(result);
	else intervalByte(symbol,result); 
    }

	/** Returns the symbol whose interval of low and high counts
	 * contains the given count.  Ordinary outcomes are positive
	 * integers, and the two special constants <code>EOF</code> or
	 * <code>ESCAPE</code>, which are negative.
	 * @param count The given count.
	 * @return The symbol whose interval contains the given count.
	 */
    public int pointToSymbol(int count) {
	if (_contextNode != null) return _contextNode.pointToSymbol(count,_excludedBytes);
	return _backoffModel.pointToSymbol(count,_excludedBytes); 
    }

	/** Returns the total count for the current context.
	 * @return Total count for the current context.
	 */
    public int totalCount() {
	if (_contextNode == null) return _backoffModel.totalCount(_excludedBytes);
	return _contextNode.totalCount(_excludedBytes);
    }

	/** Increments the model as if it had just encoded or decoded the
	 * specified symbol in the stream.  May be used to prime models by
	 * "injecting" a symbol into the model's stream without
	 * coding/decoding it in the stream of coded bytes.  Calls must be
	 * coordinated for encoding and decoding.  Will be called
	 * automatically by the models for symbols they encode or decode.
	 * @param symbol Symbol to add to the model.  
	 */
    public void increment(int i) {
	increment((byte)(i-128));
    }

    /** Exclude all of the bytes in the specified byte set.
     * @param bytesToExclude Set of bytes to exclude from outcome.
     * @since 1.1
     */
    public void exclude(ByteSet bytesToExclude) {
	_excludedBytes.add(bytesToExclude);
    }

    /** Count of bytes coded to use in pruning.
     */
    private int _byteCount; // implied = 0; uncomment for pruning

    /** Model to use for short contexts. */
    private final ExcludingAdaptiveUnigramModel _backoffModel = new ExcludingAdaptiveUnigramModel();

    /** Nodes at depth 1 in the model. All order 0 nodes are included in the unigram
     */
    private final PPMNode[] _contexts = new PPMNode[256];
    
    /** Maximum context length to search in trie.  Maximum count will
     * be for maximum context length plus one.
     */
    private final int _maxContextLength;

    /** Root of the trie structure of counts.  Dummy byte as symbol.
     */
    private final PPMNode _rootNode = new PPMNode((byte)'.'); 

    /** Current context length.
     */
    private int _contextLength; // implied = 0;

    /** Current context node.
     */
    private PPMNode _contextNode; // = null;

    /** Bytes buffered for use as context.
     */
    private final ByteBuffer _buffer;

    /** Storage for the excluded bytes
     */
    private final ByteSet _excludedBytes = new ByteSet();

    /** Returns interval for byte specified as an integer in 0 to 255 range.
     * @param i Integer specification of byte in 0 to 255 range.
     * @param result Array specifying cumulative probability for byte i.
     */
    private void intervalByte(int i, int[] result) {
	if (_contextNode != null) _contextNode.interval(i,_excludedBytes,result);
	else _backoffModel.interval(i,result,_excludedBytes);
	increment(i); 
    }
	
    /** Returns interval for escape in current context. 
     * @param result Array for specifying cumulative probability for escape symbol in current context.
     */
    private void intervalEscape(int[] result) {
	_contextNode.intervalEscape(_excludedBytes,result);
	if (_contextLength >= MIN_CONTEXT_LENGTH)
	    for (PPMNode child = _contextNode._firstChild; child != null; child = child._nextSibling)
		_excludedBytes.add(child._byte);
	--_contextLength; // could decrement longer contexts more for a speedup in some cases
	getContextNodeLongToShort();
    }

    /**
     * Method used for pruning (edited out).
     */
     private void prune() {
       for (int i = 0; i < 256; ++i) if (_contexts[i] != null) _contexts[i] = _contexts[i].prune();
     }

    /** Adds counts for given byte to model in current context and then updates the current context. 
     * Rescales counts if necessary.  Called by both encoding and deocding.
     * @param b Byte to add to model. 
     */
    private void increment(byte b) {
	_buffer.buffer(b);
	byte firstByte = _buffer.bytes()[_buffer.offset()];
	if (_contexts[128+ (int)(firstByte)] == null) 
	    _contexts[128+ (int)(firstByte)] = new PPMNode(firstByte); 
	if (_buffer.length() > 1) 
	    _contexts[128+ (int)(firstByte)].increment(_buffer.bytes(),
								    _buffer.offset()+1,
								    _buffer.length()-1);
	// _backoffModel.increment(Converter.byteToInteger(b));  //  updates backoff model; best to exclude it by .1 b/B!
	_contextLength = Math.min(_maxContextLength,_buffer.length());
	getContextNodeBinarySearch();
	_excludedBytes.clear();
	//PRUNE if (++_byteCount == PRUNE_INTERVAL) { _byteCount = 0; prune(); } // pruning
    }	

    /** Use binary search to set the context node up to the currently
     * specified context length.  May set it to <code>null</code> if
     * not found.
     */
    private void getContextNodeBinarySearch() {
	int low = MIN_CONTEXT_LENGTH;
	int high = _contextLength;
	_contextLength = MIN_CONTEXT_LENGTH-1; // not sure we need this
	_contextNode = null;
	boolean isDeterministic = false;
	while (high >= low) {
	    int contextLength = (high + low)/2;
	    PPMNode contextNode = lookupNode(contextLength);
	    if (contextNode == null || contextNode.isChildless(_excludedBytes)) {
		if (contextLength < high) high = contextLength;
		else --high;
	    } else if (contextNode.isDeterministic(_excludedBytes)) {
		_contextLength = contextLength;
		_contextNode = contextNode;
		isDeterministic = true;
		if (contextLength < high) high = contextLength;
		else --high;
	    } else if (!isDeterministic) { 
		_contextLength = contextLength;
		_contextNode = contextNode;
		if (contextLength > low) low = contextLength;
		else ++low;
	    }  else {
		if (contextLength > low) low = contextLength;
		else ++low;
	    }
	}
    }

    /* un-used variant lookung up context lengths by starting with shortest and
     * continuing to increase until found.
    private void getContextNodeShortToLong() {
	int maxContextLength = _contextLength;
	_contextNode = null;
	_contextLength = MIN_CONTEXT_LENGTH-1;
	for (int contextLength = MIN_CONTEXT_LENGTH; contextLength <= maxContextLength; ++contextLength) {
	    PPMNode node = lookupNode(contextLength);
	    if (node == null || node.isChildless(_excludedBytes)) {
		continue; // return;  lose around .01 b/B total (not even average) with return, but 25% slower
	    } 
	    _contextNode = node;
	    _contextLength = contextLength;
	    if (node.isDeterministic(_excludedBytes)) return;
	}
    }
    */

    /** Starting at the longest context, count down in length to set
     * a valid context or give up.  This version finds the shortest deterministic
     * context <= in length to the current context length, but if there is
     * no deterministic context, returns longest context length that exists
     * that is <= in length to the current context.
     * Could also implement this in short to long order
     */
    private void getContextNodeLongToShort() { 
	while (_contextLength >= MIN_CONTEXT_LENGTH) {
	    _contextNode = lookupNode(_contextLength);
	    if (_contextNode == null || _contextNode.isChildless(_excludedBytes)) { --_contextLength; continue; }
	    while (_contextLength > MIN_CONTEXT_LENGTH && _contextNode.isDeterministic(_excludedBytes)) {
		// backoff to shortest deterministic node if context node is deterministic
		PPMNode backoffNode = lookupNode(_contextLength-1);
		if (backoffNode == null || !backoffNode.isDeterministic(_excludedBytes)) return;
		_contextNode = backoffNode; 
		--_contextLength;
	    }
	    return;
	}
	_contextNode = null;
    }

    /** Returns node from the current byte buffer of
     * the specified context length, or null if there isn't one.
     * @param contextLength Number of bytes of context used.
     * @return Node found at that context.
     */
    private PPMNode lookupNode(int contextLength) {
	PPMNode node = _contexts[128+ (int)(_buffer.bytes()[_buffer.offset()+_buffer.length()-contextLength])];
	if (node == null) return (PPMNode) null;
	return lookup(node,_buffer.bytes(),_buffer.offset()+_buffer.length()-contextLength+1,contextLength-1);
    }
    
    /** Looks up a node from the given bytes, offset and length starting
     * from the specified node.
     * @param node Node from which to search.
     * @param bytes Sequence of bytes to search.
     * @param offset Offset into sequence of bytes of the first byte.
     * @param length Number of bytes to look up.
     * @return Node found for the given bytes.
     */
    private static PPMNode lookup(PPMNode node, byte[] bytes, int offset, int length) {
	if (length == 0) return node;
	for (PPMNode child = node._firstChild; length > 0 && child != null; ) {
	    if (bytes[offset]==child._byte) {
		if (length == 1) return child;
		node = child;
		child = child._firstChild;
		++offset;
		--length;
	    } else {
		child = child._nextSibling;
	    }
	}
	return (PPMNode) null;
    }

    /** Minimum context length to look down sequence of nodes.
     * Shorter contexts use backoff model.
     */
    private static final int MIN_CONTEXT_LENGTH = 1;

    /** Period between prunings in number of bytes. */
    private static final int PRUNE_INTERVAL = 250000; // loses about 10% compression rate, saves lots of space

}




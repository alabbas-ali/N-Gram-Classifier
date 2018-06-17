package pt.tumba.ngram.svm;

/**
 * SVM Kernel Cache, implementing a least recently used (LRU) policy.
 * Since the Kernel is fully dense and may not be stored in computer memory,  
 * elements of the Kernel matrix are calculated as needed. The cache simply
 * stores the more recently used elements, reducing the computational cost of
 * later iterations.</p><p>
 * 
 * Since final iterations usually need few columns of the kernel matrix, most
 * evaluations can be avoided at the end.
 * 
 * @author Bruno Martins
 */
public class Cache {
	
	/** The number of total data items. */
	private final int l;
	
	/** The cache size limit in bytes. */
	private int size;
	
	/**
	 * Inner class representing a <code>Cache</code> node.
	 *  */
	private final class CacheNode {
		CacheNode prev, next;	// a cicular list
		float[] data;
		int len;		// data[0,len) is cached in this entry
	}
	
	/** The Nodes in the cache. */
	private final CacheNode[] nodes;
	
	/** The first node in the LRU cache. */
	private CacheNode lru_head;

	/**
	 * Constructor for Cache
	 * 
	 * @param l The number of total data items.
	 * @param size The cache size limit in bytes.
	 */
	public Cache(int l, int size)	{
		this.l = l;
		this.size = size;
		nodes = new CacheNode[l];
		for(int i=0;i<l;i++) nodes[i] = new CacheNode();
		size /= 4;
		size -= l * (16/4);	// sizeof(head_t) == 16
		lru_head = new CacheNode();
		lru_head.next = lru_head.prev = lru_head;
	}

	/**
	 * Delete a node from the cache.
	 * 
	 * @param h The node to delete.
	 */
	private void deleteLRU(CacheNode h) {
		h.prev.next = h.next;
		h.next.prev = h.prev;
	}

	/**
	 * Insert a node in the cache (at the last position).
	 * 
	 * @param h The node to insert.
	 */
	private void insertLRU(CacheNode h) {
		// insert to last position
		h.next = lru_head;
		h.prev = lru_head.prev;
		h.prev.next = h;
		h.next.prev = h;
	}

	/**
	 * Returns free space in the cache. [0,len)
	 * 
	 * @param index The least recently used index at the cache.
	 * @param data The data to store in the Cache.
	 * @param len maximum length of the data to store in the Cache.
	 * @return some position p where [p,len) need to be filled (p >= len if nothing needs to be filled) 
	 */
	int getData(int index, float[][] data, int len) {
		CacheNode h = nodes[index];
		if(h.len > 0) deleteLRU(h);
		int more = len - h.len;
		if(more > 0)
		{
			// free old space
			while(size < more)
			{
				CacheNode old = lru_head.next;
				deleteLRU(old);
				size += old.len;
				old.data = null;
				old.len = 0;
			}
			// allocate new space
			float[] new_data = new float[len];
			if(h.data != null) System.arraycopy(h.data,0,new_data,0,h.len);
			h.data = new_data;
			size -= more;
			do {int _=h.len; h.len=len; len=_;} while(false);
		}
		insertLRU(h);
		data[0] = h.data;
		return len;
	}

	/**
	 * Swap two nodes in the cache.
	 * 
	 * @param i Position in the List for the first node.
	 * @param j Position in the List for the second node.
	 */
	public void swapIndex(int i, int j) {
		if(i==j) return;
		if(nodes[i].len > 0) deleteLRU(nodes[i]);
		if(nodes[j].len > 0) deleteLRU(nodes[j]);
		{ float[] _=nodes[i].data; nodes[i].data=nodes[j].data; nodes[j].data=_; }
		{ int _=nodes[i].len; nodes[i].len=nodes[j].len; nodes[j].len=_; }
		if(nodes[i].len > 0) insertLRU(nodes[i]);
		if(nodes[j].len > 0) insertLRU(nodes[j]);
		if(i>j) {int _=i; i=j; j=_;}
		for(CacheNode h = lru_head.next; h!=lru_head; h=h.next)	{
			if(h.len > i) {
				if(h.len > j) { float _=h.data[i]; h.data[i]=h.data[j]; h.data[j]=_; } else {
					// give up
					deleteLRU(h);
					size += h.len;
					h.data = null;
					h.len = 0;
				}
			}
		}
	}
 }

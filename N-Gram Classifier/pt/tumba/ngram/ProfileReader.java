package pt.tumba.ngram;

import java.io.*;
import java.util.*;

/**
 * Class to hold (static) methods to read in profile data.
 * A <code>Profile</code> stores N-gram frequency information for a given textual string.
 *
 *@author     Bruno Martins
 */
public class ProfileReader {
    
   /** The single instance of this Singleton class. */   
   private static final ProfileReader _theInstance = new ProfileReader();

   /**
    * Returns an instance of this class.
    *
    * @return An instance of <code>ProfileReader</code>.
    */
   public static ProfileReader getInstance() {
	return _theInstance;
   }

    /**
     *  Create a new N-gram from an array of bytes.
     *
     *@param  count  A <code>Map</code> with N-gram frequency information.
     *@param  ba     An array of bytes with the chars corresponding to this N-Gram.
     *@param  start  Starting position in the array of bytes.
     *@param  len    Length in the array of bytes.
     */
    protected static Map newNGram(Map count, byte[] ba, int start, int len) {
        NGram ng = NGram.newNGram(ba, start, len);
        NGram cng = (NGram) count.get(ng);
        if (cng != null) {
            cng.inc();
        } else {
            count.put(ng, new NGram(ng));
        }
		return count;
    }
    
    /**
     * Read an N-gram profile from an <code>InputStream</code>.
     *
     *@param  stream           The <code>InputStream</code> from where to read the Profile.
     *@return                  A <code>List</code> with the N-Grams in the profile.
     *@exception  IOException  A problem occurred while reading from the <code>InputStream</code>.
     */
    public static List read(InputStream stream) throws IOException {
        Map count = new HashMap(1000);
        BufferedInputStream bi = new BufferedInputStream(stream);
        int b;
        byte ba[] = new byte[5];
        ba[4] = 42;
        int i = 0;
        while ((b = bi.read()) != -1) {
            if (b == 13 || b == 10 || b == 9) b = 32;
            i++;
            if (b != 32 || ba[3] != 32) {
                ba[0] = ba[1];
                ba[1] = ba[2];
                ba[2] = ba[3];
                ba[3] = ba[4];
                ba[4] = (byte) b;
                count = newNGram(count, ba, 4, 1);
                if (i > 1) {
                    count = newNGram(count, ba, 3, 2);
                }
                if (i > 2) {
                    count = newNGram(count, ba, 2, 3);
                }
                if (i > 3) {
                    count = newNGram(count, ba, 1, 4);
                }
                if (i > 4) {
                    count = newNGram(count, ba, 0, 5);
                }
            }
        }
        ArrayList order = new ArrayList(count.values());
        Collections.sort(order);
        return order;
    }

    /**
     * Sole constructor of ProfileReader.
     */
    private ProfileReader() {
	}

}

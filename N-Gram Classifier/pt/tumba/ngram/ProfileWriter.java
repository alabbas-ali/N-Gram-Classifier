package pt.tumba.ngram;

import java.io.*;
import java.util.*;

/**
 * Write an N-Gram profile to disk.
 * A <code>Profile</code> stores N-gram frequency information for a given textual string.
 * 
 *@author     Bruno Martins
 */
public class ProfileWriter {

	/** The single instance of this Singleton class. */
	private static final ProfileWriter _theInstance = new ProfileWriter();

	/**
	 * Returns an instance of this class.
	 *
	 * @return An instance of <code>ProfileWriter</code>.
	 */
	public static ProfileWriter getInstance() {
		return _theInstance;
	}

	/**
	 * The main method for the ProfileWriter class.
	 *
	 * @param  args             The command line arguments, tokenized.
	 * @exception  IOException  A problem occurred while processing files.
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.out.println("Usage: LMWriter [input_file] [output_file]");
			System.out.println("LMWriter: Need exactly 2 arguments.");
			System.exit(1);
		}
		InputStream in = new FileInputStream(args[0]);
		OutputStream out = new FileOutputStream(args[1]);
		List order = ProfileReader.read(in);
		int limit, limitlower = 0;
		if (NGramConstants.USEDNGRAMSMAX < 0) {
			limit = -1;
		} else if (order.size() < NGramConstants.USEDNGRAMSMAX) {
			limit = ((NGram) order.get(order.size() - 1)).getCount();
		} else {
			limit =
				((NGram) order.get(NGramConstants.USEDNGRAMSMAX - 1))
					.getCount();
		}
		if (order.size() <= NGramConstants.USEDNGRAMSMIN) {
			limitlower = Integer.MAX_VALUE;
		} else if (NGramConstants.USEDNGRAMSMIN <= 0) {
			limitlower = ((NGram) order.get(0)).getCount();
		} else {
			limitlower =
				((NGram) order.get(NGramConstants.USEDNGRAMSMIN - 1))
					.getCount();
		}
		int k;
		int i = 0;
		while (i < order.size()) {
			NGram gram = (NGram) order.get(i);
			int cnt = gram.getCount();
			double cnts;
			if(NGramConstants.SMOOTHING) cnts = gram.getSmoothedCount();
			if (cnt > limitlower)	continue;
			if (cnt < limit) break;
			for (k = 0; k < gram.getSize(); k++) {
				byte b = (byte) gram.getByte(k);
				if (b == (byte) ' ') {
					out.write((byte) '_');
				} else {
					out.write(b);
				}
			}
			out.write(' ');
			out.write('\t');
			String h;
			if(NGramConstants.SMOOTHING) h = Double.toString(cnts);
			else h = Integer.toString(cnt);
			for (k = 0; k < h.length(); k++) {
				out.write((byte) h.charAt(k));
			}
			out.write((byte) 13);
			out.write((byte) 10);
			i++;
		}
		out.flush();
		out.close();
	}

	/**
	 * Sole constructor of ProfileWriter.
	 */
	private ProfileWriter() {
	}

}

package pt.tumba.ngram.compression;

import java.io.*;
import java.util.zip.*;
import java.util.*;
import pt.tumba.ngram.*;

/**
 *  Recent results in bioinformatics and observations about the Kolmogorov complexity seem to
 *  suggest that simple classification systems can be built using off-the-shelf compression algorithms.
 * 
 *  <code>CompressionCathegorizer</code> implements the classification technique described in 
 *  the papers <a href="http://www.cs.ucr.edu/~eamonn/SIGKDD_2004_long.pdf">"Towards 
 *  Parameter-Free Data Mining"</a> and 
 *  <a href="http://cui.unige.ch/spc/Presentation/2003/Kolmogorov/chen_metric.pdf">"The Similarity
 *  Metric"</a>, respectivelly by Ming Li and Keogh et al. </p><p> 
 * 
 *  Essentially, these works claim that the distance between two textual strings can be given
 *  by C(xy)/(C(x)+C(y)), where C(x) is the length of string x compressed. This way of measuring
 *  similarity can be used for classification through a simple procedure: we keep example 
 *  documents of which the categories are known in a directory, and for a document with an 
 *  unknown category we simply compress it together with all the example documents and select
 *  the category achieving the best similarity as the classification.
 * 
 *  This implementation can use both the Zip compression algorithm available with the Java SDK,
 *  or a more efficient arithmethic coding compressor.
 * 
 * @author Bruno Martins
 */
public class CompressionCategorizer {

	/**
	 *  A <code>FilenameFilter</code> for filtering directory listings, recognizing
	 *  filenames for class profiles. Essentially, all filenames ending with a ".corpus"
	 *  extension are valid. 
	 */
	public static FilenameFilter CompressionFilter = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return name.endsWith(".corpus");
		}
	};

	/**
	 *  A List of example documents, for which the classes are known.
	 */
	protected List profiles;


	/** A boolean flag indicating if we should use PPM compression or Zip compression. */
	protected boolean usePPM = true;

	/** A boolean flag indicating if we should use the normalized compression
         * distance or the compression dissimilarity.
         */
	protected boolean useDistance = true;

	/**
	 *  Construct an uninitialized Cathegorizer.
	 */
	public CompressionCategorizer() {
		profiles = new ArrayList();
	}

	/**
	 *  Construct an Cathegorizer from a whole Directory of resources.
	 *
	 *@param      dirName                Pathname for the directory with the profiles.
	 *@exception  TCatNGException        A problem occured while reading the profiles.
	 *@exception  FileNotFoundException  The pathname was not found.
	 */
	public CompressionCategorizer(String dirName)
		throws TCatNGException, FileNotFoundException {
		this();
		File fi = new File(dirName);
		if (!fi.isDirectory()) {
			throw new TCatNGException("Base must be a directory.");
		}
		String[] names = fi.list(CompressionFilter);
		init(fi, names);
	}

	/**
	 *  Construct an Cathegorizer from a List of resource file names.
	 *
	 *@param       fileNames                     An array with the pathnames for the profiles.
	 *@exception  TCatNGException           A problem occured while reading the profiles.
	 *@exception  FileNotFoundException  One of the pathnames was not found.
	 */
	public CompressionCategorizer(String[] fileNames)
		throws TCatNGException, FileNotFoundException {
		this();
		init(null, fileNames);
	}

	/**
	 *  Fetch the set of profiles from the disk.
	 *
	 *@param  fi                         Base directory for the profiles.
	 *@param  names                      Filenames of the profiles to fetch.
	 *@exception  TCatNGException           A problem occured while reading the profiles.
	 *@exception  FileNotFoundException  One of the pathnames was not found.
	 */
	private final void init(File fi, String[] names)
		throws TCatNGException, FileNotFoundException {
		if (names == null || names.length == 0) {
			throw new TCatNGException("Need at least one input file.");
		}
		for (int i = 0; i < names.length; i++) {
			File ifi = new File(fi, names[i]);
			if (ifi.isDirectory()) continue;
			profiles.add(ifi);
		}
	}

	/**
	 * Compress a given byte array.
         * 
         * @param b A byte array.
         * @return A byte array with the compressed data.
         */
	private byte[] compress(byte b[]) {
		if(usePPM) return compressPPM(b);
		return compressZip(b);
	}

	/**
	 * Compress a given String.
         * 
         * @param b A String.
         * @return A byte array with the compressed data.
         */
	private  byte[] compress(String s) {
		if(usePPM) return compressPPM(s);
		return compressZip(s);
	}

	/**
	 * Compress a given File.
         * 
         * @param b A File.
         * @return A byte array with the compressed data.
         */
	private byte[] compress(File f) {
		if(usePPM) return compressPPM(f);
		return compressZip(f);
	}

	/**
	 * Compress a given String with the Zip algorithm.
         * 
         * @param b A String.
         * @return A byte array with the compressed data.
         */
	private static byte[] compressZip(String s) {
		return compressZip(s.getBytes());
	}

	/**
	 * Compress a given byte array with the Zip algorithm.
         * 
         * @param b A byte array.
         * @return A byte array with the compressed data.
         */
	private static byte[] compressZip(byte b[]) {
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		ZipOutputStream out =
			new ZipOutputStream(new BufferedOutputStream(dest));
		out.setLevel(9);
		try {
			out.write(b);
		} catch (Exception e) {
		}
		return dest.toByteArray();
	}

	/**
	 * Compress a given File with the Zip algorithm.
         * 
         * @param b A File.
         * @return A byte array with the compressed data.
         */
	private static byte[] compressZip(File f) {
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		int aux;
		try {
			BufferedInputStream s = new BufferedInputStream(new FileInputStream(f));
			while ((aux=s.read())!=-1) dest.write(aux);
		} catch (Exception e) {
		}
		return compressZip(dest.toByteArray());
	}

	/**
	 * Compress a given String with the arithmetic coding algorithm.
         * 
         * @param b A String.
         * @return A byte array with the compressed data.
         */
	private static byte[] compressPPM(String s) {
		return compressPPM(s.getBytes());
	}
	
	/**
	 * Compress a given byte array with the arithmetic coding algorithm.
         * 
         * @param b A byte array.
         * @return A byte array with the compressed data.
         */
	private static byte[] compressPPM(byte b[]) {
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		ArithCodeOutputStream out =
			new ArithCodeOutputStream(dest, new PPMModel(8));
		try {
			out.write(b);
		} catch (Exception e) {
		}
		return dest.toByteArray();
	}

	/**
	 * Compress a given File with the arithmetic coding algorithm.
         * 
         * @param b A File.
         * @return A byte array with the compressed data.
         */
	private static byte[] compressPPM(File f) {
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		int aux;
		try {
			BufferedInputStream s = new BufferedInputStream(new FileInputStream(f));
			while ((aux=s.read())!=-1) dest.write(aux);
		} catch (Exception e) {
		}
		return compressPPM(dest.toByteArray());
	}

	/**
         * Calculates the normalized compression distance
         * 
         * @param 
         */
	private static double normalizedCompressionDistance(
		byte conc[],
		byte a[],
		byte b[]) {
		int min, max;
		if (a.length < b.length) {
			min = a.length;
			max = b.length;
		} else {
			min = a.length;
			max = b.length;
		}
		return (conc.length - min) / max;
	}

	/**
	 * Returns the compression dissimilarity.
         * 
         * @param
         */
	private static double compressionDissimilarity(
		byte conc[],
		byte a[],
		byte b[]) {
		return conc.length / (a.length + b.length);
	}


	/**
         * Calculates the normalized compression distance between two Strings.
         * 
         * @param a A String.
         * @param b Another String.
         * @return The normalized compression distance between the two Strings.
         */
	private double normalizedCompressionDistance(String a, String b) {
		return normalizedCompressionDistance(
			compress(a + b),
			compress(a),
			compress(b));
	}

	/**
         * Calculates the compression dissimilarity between two Strings.
         * 
         * @param a A String.
         * @param b Another String.
         * @return The compression dissimilarity between the two Strings.
         */
	private double compressionDissimilarity(String a, String b) {
		return compressionDissimilarity(
			compress(a + b),
			compress(a),
			compress(b));
	}

	/**
         * Calculates the normalized compression distance between two Files.
         * 
         * @param a A File.
         * @param b Another File.
         * @return The normalized compression distance between the two Files.
         */
	private double normalizedCompressionDistance(File a, File b) {
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		int aux;
		try {
			BufferedInputStream s = new BufferedInputStream(new FileInputStream(a));
			while ((aux=s.read())!=-1) dest.write(aux);
		        s = new BufferedInputStream(new FileInputStream(b));
			while ((aux=s.read())!=-1) dest.write(aux);
		} catch (Exception e) {
		}
		return normalizedCompressionDistance(
			compress(dest.toByteArray()),
			compress(a),
			compress(b));
	}

	/**
         * Calculates the compression dissimilarity between two Files.
         * 
         * @param a A File.
         * @param b Another File.
         * @return The compression dissimilarity between the two Files.
         */
	private double compressionDissimilarity(File a, File b) {
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		int aux;
		try {
			BufferedInputStream s = new BufferedInputStream(new FileInputStream(a));
			while ((aux=s.read())!=-1) dest.write(aux);
			s = new BufferedInputStream(new FileInputStream(b));
			while ((aux=s.read())!=-1) dest.write(aux);
		} catch (Exception e) {
		}
		return compressionDissimilarity(
			compress(dest.toByteArray()),
			compress(a),
			compress(b));
	}


	/**
         * Calculates the compression dissimilarity between two byte arrays.
         * 
         * @param a A byte array.
         * @param b Another byte array.
         * @return The compression dissimilarity between the two byte arrays.
         */
	private double normalizedCompressionDistance(byte a[], byte b[]) {
		byte[] c= new byte[a.length+1+b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		c[a.length]= 0;
		System.arraycopy(b, 0, c, a.length+1, b.length);
		return normalizedCompressionDistance(
			compress(c),
			compress(a),
			compress(b));
	}

	/**
         * Calculates the compression dissimilarity between two byte arrays.
         * 
         * @param a A byte array.
         * @param b Another byte array.
         * @return The compression dissimilarity between the two byte arrays.
         */
	private double compressionDissimilarity(byte a[], byte b[]) {
		byte[] c= new byte[a.length+1+b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		c[a.length]= 0;
		System.arraycopy(b, 0, c, a.length+1, b.length);
		return compressionDissimilarity(
			compress(c),
			compress(a),
			compress(b));
	}
	
	/**
	 *  Match a given <code>File</code> against all the Files
	 *  constituting the models in the cathegorizer.
	 *
	 *@param  f  A <code>File</code>.
	 *@return  The closest matching class (given by the File name) in the cathegorizer.
	 */
	public String match(File f) {
		double min = Double.MAX_VALUE;
		double max = -1;
		String result = null;
		for(int i=0; i<profiles.size(); i++) {
			if(useDistance) {
				double val = normalizedCompressionDistance(f,(File)(profiles.get(i)));
				if(val<min) {
					min = val;
					result = ((File)(profiles.get(i))).toString();
				}
			} else {
				double val = compressionDissimilarity(f,(File)(profiles.get(i)));
				if(val>max) {
					max = val;
					result = ((File)(profiles.get(i))).toString();
				}
			}			
		}
		return result;
	}

	/**
	 * Sample application to use the Cathegorizer from the command line.
	 *
	 *@param  args  The command line arguments, tokenized
	 */
	public static void main(String[] args) {
		try {
			CompressionCategorizer cath = new CompressionCategorizer(args[0]);
			for (int i = 1; i < args.length; i++) {
				File f = new File(args[i]);
				String res = cath.match(f);
				System.out.println(
					"Best match for " + args[i] + " is : " + res);
			}
		} catch (Exception e) {
			System.err.println("Terminated by " + e);
			e.printStackTrace();
		}
	}

}

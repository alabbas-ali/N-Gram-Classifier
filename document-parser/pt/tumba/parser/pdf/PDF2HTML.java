package pt.tumba.parser.pdf;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.StringTokenizer;
import pt.tumba.parser.*;

/**
 * Description of the Class
 *
 * @author bmartins
 * @created 22 de Agosto de 2002
 */
public class PDF2HTML implements DocFilter {

	private Random rnd = new Random();
	private int sizeCount = 0;

	/**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
	public int originalSize() {
		return sizeCount;
	}

	public PDF2HTML() throws Exception {
		try {
			NativeExec.execute("pdftotext");
		} catch (Exception e) {
			throw new Exception("Required software not instaled: pdftotext");
		}
	}

	/**
	 * Description of the Method
	 *
	 * @param b2
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	public String convertPDFToHTML(byte[] b2) throws Exception {
		String filename = "" + System.currentTimeMillis() + "." + rnd.nextInt();
		try {
			sizeCount = b2.length;
			String aux = "";
			String s2;
			java.io.File file1 = java.io.File.createTempFile(filename, "pdf");
			java.io.File file2 = java.io.File.createTempFile(filename, "html");
			FileOutputStream out = new FileOutputStream(file1);
			out.write(b2);
			out.close();
			NativeExec.execute("pdftotext -q " + file1.getAbsolutePath() + " " + file2.getAbsolutePath());
			BufferedReader input = new BufferedReader(new FileReader(file2));
			while ((s2 = input.readLine()) != null) {
				s2 = s2.trim();
				String s3 = new String();
				StringTokenizer st = new StringTokenizer(s2);
				int maxLength = 0;
				while (st.hasMoreTokens()) {
					String s = st.nextToken();
					if (s.length() > maxLength)
						maxLength = s.length();
					if (s.startsWith("http://")) {
						s = "<a href='" + s + "'>" + s + "</a>;";
					}
					s3 += s;
					s3 += " ";
				}
				if (maxLength < 80)
					aux += s3 + "\n";
			}
			if (aux.equals("")) {
				throw new Exception("Empty Content.");
			}
			input.close();
			file1.delete();
			file2.delete();
			return aux;
		} catch (Exception e) {
			boolean errord = false;
			try {
				(java.io.File.createTempFile(filename, "pdf")).delete();
			} catch (Exception e2) {
				errord = true;
			}
			try {
				(java.io.File.createTempFile(filename, "html")).delete();
			} catch (Exception e2) {
				errord = true;
			}
			throw (e);
		}
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	public String convertPDFToHTML(InputStream input) throws Exception {
		BufferedInputStream strm = new BufferedInputStream(input);
		ByteArrayOutputStream sb = new ByteArrayOutputStream();
		int s = -1;
		while ((s = strm.read()) != -1) {
			sb.write(s);
		}
		return convertPDFToHTML(sb.toByteArray());
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	public String convertPDFToHTML(java.io.File input) throws Exception {
		BufferedInputStream strm = new BufferedInputStream(new FileInputStream(input));
		ByteArrayOutputStream sb = new ByteArrayOutputStream();
		int s = -1;
		while ((s = strm.read()) != -1) {
			sb.write(s);
		}
		return convertPDFToHTML(sb.toByteArray());
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	public String convertPDFToHTML(URL input) throws Exception {
		HttpURLConnection conn = (HttpURLConnection) input.openConnection();
		conn.setAllowUserInteraction(false);
		conn.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows 98");
		conn.setInstanceFollowRedirects(true);
		conn.connect();
		BufferedInputStream strm = new BufferedInputStream(conn.getInputStream());
		ByteArrayOutputStream sb = new ByteArrayOutputStream();
		int s = -1;
		while ((s = strm.read()) != -1) {
			sb.write(s);
		}
		return convertPDFToHTML(sb.toByteArray());
	}

}

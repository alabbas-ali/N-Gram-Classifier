package pt.tumba.parser.ppt;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import pt.tumba.parser.*;

/**
 * uses the api to present the contents excel 97 spreadsheet as an html file
 *
 * @author unknown
 * @created 15 de Setembro de 2002
 */
public class PPT2HTML implements DocFilter {

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

	public PPT2HTML() throws Exception {
		try {
			NativeExec.execute("ppthtml");
		} catch (Exception e) {
			throw new Exception("Required software not instaled: ppthtml");
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
	public String convertPPTToHTML(byte b2[]) throws Exception {
		String filename = "" + System.currentTimeMillis() + "." + rnd.nextInt();
		try {
			sizeCount = b2.length;
			String aux = new String("");
			String s2;
			java.io.File file1 = java.io.File.createTempFile(filename, "doc");
			java.io.File file2 = java.io.File.createTempFile(filename, "html");
			FileOutputStream out = new FileOutputStream(file1);
			out.write(b2);
			out.close();
			PrintWriter outw = new PrintWriter(new FileWriter(file2));
			NativeExec.execute("ppthtml " + file1.getAbsolutePath(), outw);
			outw.close();
			BufferedReader input = new BufferedReader(new FileReader(file2));
			while ((s2 = input.readLine()) != null) {
				aux += s2 + "\n";
			}
			if (aux.equals("")) {
				throw new Exception("Empty content.");
			}
			input.close();
			file1.delete();
			file2.delete();
			int index = aux.lastIndexOf(
					"<hr><FONT SIZE=-1>Created with <a href=\"http://chicago.sf.net/xlhtml\">pptHtml</a></FONT><br>");
			if (index == -1)
				return aux;
			return aux.substring(0, index) + "</body></html>";
		} catch (Exception e) {
			boolean errord = false;
			try {
				(java.io.File.createTempFile(filename, "doc")).delete();
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
	public String convertPPTToHTML(InputStream input) throws Exception {
		BufferedInputStream strm = new BufferedInputStream(input);
		ByteArrayOutputStream sb = new ByteArrayOutputStream();
		int s;
		while ((s = strm.read()) != -1) {
			sb.write(s);
		}
		return convertPPTToHTML(sb.toByteArray());
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
	public String convertPPTToHTML(java.io.File input) throws Exception {
		BufferedInputStream strm = new BufferedInputStream(new FileInputStream(input));
		ByteArrayOutputStream sb = new ByteArrayOutputStream();
		int s;
		while ((s = strm.read()) != -1) {
			sb.write(s);
		}
		return convertPPTToHTML(sb.toByteArray());
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
	public String convertPPTToHTML(URL input) throws Exception {
		HttpURLConnection conn = (HttpURLConnection) input.openConnection();
		conn.setAllowUserInteraction(false);
		conn.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows 98");
		conn.setInstanceFollowRedirects(true);
		conn.connect();
		BufferedInputStream strm = new BufferedInputStream(conn.getInputStream());
		ByteArrayOutputStream sb = new ByteArrayOutputStream();
		int s;
		while ((s = strm.read()) != -1) {
			sb.write(s);
		}
		return convertPPTToHTML(sb.toByteArray());
	}

}

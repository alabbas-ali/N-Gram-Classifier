package pt.tumba.parser.xls;

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
import java.util.StringTokenizer;
import pt.tumba.parser.*;

/**
 * uses the api to present the contents excel 97 spreadsheet as an html file
 *
 * @author unknown
 * @created 15 de Setembro de 2002
 */
public class XLS2HTML implements DocFilter {

	private int sizeCount = 0;

	/**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
	public int originalSize() {
		return sizeCount;
	}

	private Random rnd = new Random();

	public XLS2HTML() throws Exception {
		try {
			NativeExec.execute("xlhtml --help");
		} catch (Exception e) {
			throw new Exception("Required software not instaled: xlhtml");
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
	public String convertXLSToHTML(byte b2[]) throws Exception {
		/*
		 * StringWriter out2 = new StringWriter(); sizeCount = b2.length; String s2;
		 * PrintWriter out = new PrintWriter(out2); Workbook w =
		 * Workbook.getWorkbook(new ByteArrayInputStream(b2)); out.println("<html>");
		 * for (int sheet = 0; sheet < w.getNumberOfSheets(); sheet++) { Sheet s =
		 * w.getSheet(sheet); out.println("<p>"); out.println("<b>" +
		 * SSTRecord.convertToHTML(s.getName()) + "</b><br />"); Cell[] row = null; for
		 * (int i = 0; i < s.getRows(); i++) { row = s.getRow(i); int nonblank = 0; for
		 * (int j = row.length - 1; j >= 0; j--) { if (row[j].getType() !=
		 * CellType.EMPTY) { nonblank = j; break; } } out.print(row[0].getContents());
		 * for (int j = 1; j <= nonblank; j++) {
		 * out.print("&nbsp;&nbsp;<tab />&nbsp;&nbsp;");
		 * out.print(row[j].getContents()); } out.println("<br />"); }
		 * out.println("</p>"); } out.println("</html>"); return out2.toString();
		 */

		sizeCount = b2.length;
		String filename = "" + System.currentTimeMillis() + "." + rnd.nextInt();
		boolean errord = false;
		try {
			String aux = new String("");
			String s2;
			java.io.File file1 = java.io.File.createTempFile(filename, "doc");
			java.io.File file2 = java.io.File.createTempFile(filename, "html");
			FileOutputStream out = new FileOutputStream(file1);
			out.write(b2);
			out.close();
			PrintWriter outw = new PrintWriter(new FileWriter(file2));
			NativeExec.execute("xlhtml -asc " + file1.getAbsolutePath(), outw);
			outw.close();
			BufferedReader input = new BufferedReader(new FileReader(file2));
			while ((s2 = input.readLine()) != null) {
				String s3 = new String();
				StringTokenizer st = new StringTokenizer(s2);
				while (st.hasMoreTokens()) {
					String s = st.nextToken();
					if (s.startsWith("http://")) {
						String s22 = "<a href='" + s + "'>";
						s = s.replaceAll("&", "&amp;");
						s = s.replaceAll("<", "&lt;");
						s = s.replaceAll(">", "&gt;");
						s22 += s + "</a>";
						s = s22;
					} else {
						s = s.replaceAll("&", "&amp;");
						s = s.replaceAll("<", "&lt;");
						s = s.replaceAll(">", "&gt;");
					}
					s3 += s;
					s3 += " ";
				}
				aux += s3 + "\n";
			}
			input.close();
			file1.delete();
			file2.delete();
			if (aux.equals("")) {
				throw new Exception("Empty Content.");
			}
			return aux;
		} catch (Exception e) {
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
	public String convertXLSToHTML(InputStream input) throws Exception {
		BufferedInputStream strm = new BufferedInputStream(input);
		ByteArrayOutputStream sb = new ByteArrayOutputStream();
		int s;
		while ((s = strm.read()) != -1) {
			sb.write(s);
		}
		return convertXLSToHTML(sb.toByteArray());
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
	public String convertXLSToHTML(java.io.File input) throws Exception {
		BufferedInputStream strm = new BufferedInputStream(new FileInputStream(input));
		ByteArrayOutputStream sb = new ByteArrayOutputStream();
		int s;
		while ((s = strm.read()) != -1) {
			sb.write(s);
		}
		return convertXLSToHTML(sb.toByteArray());
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
	public String convertXLSToHTML(URL input) throws Exception {
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
		return convertXLSToHTML(sb.toByteArray());
	}

}

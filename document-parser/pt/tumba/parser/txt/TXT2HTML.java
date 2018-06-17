/*
 *  TXT2HTML.java
 *
 *  Created on 17 de Fevereiro de 2003, 17:35
 */
package pt.tumba.parser.txt;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.StringTokenizer;

import pt.tumba.parser.DocFilter;

/**
 *  Converts a txt document into HTML
 *
 *@author     dcgomes
 *@created    21 de Fevereiro de 2003
 */
public class TXT2HTML implements DocFilter {

	private int sizeCount = 0;

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public int originalSize() {
		return sizeCount;
	}

	/**
	 *  Creates a new instance of TXT2HTML
	 */
	public TXT2HTML() {
	}

	/**
	 *  Description of the Method
	 *
	 *@param  array  Description of the Parameter
	 *@return        Description of the Return Value
	 */
	public String convertTXTToHTML(byte[] array) {
		String aux = new String("<html><body>");
		try {
			sizeCount = array.length;
			String in = new String(array);

			BufferedReader input = new BufferedReader(new StringReader(in));
			String s2 = null;
			while ((s2 = input.readLine()) != null) {
				s2 = s2.trim();
				String s3 = new String();
				StringTokenizer st = new StringTokenizer(s2);
				while (st.hasMoreTokens()) {
					String s = st.nextToken();
					if (s.startsWith("http://")) {
						s = "<a href='" + s + "'>" + s + "</a>;";
					}
					s3 += s;
					s3 += " ";
				}
				aux += s3 + "\n";
			}
			if (aux.equals("<html><body>")) {
				throw new Exception("Empty Content.");
			}
			aux += "</body></html>";
			input.close();
			return aux;
		} catch (Exception e) {
			return aux;
		}
	}

	/**
	 *  Description of the Method
	 *
	 *@param  in               Description of the Parameter
	 *@return                  Description of the Return Value
	 *@exception  IOException  Description of the Exception
	 */
	public String convertTXTToHTML(InputStream in) throws IOException {
		BufferedInputStream strm = new BufferedInputStream(in);
		ByteArrayOutputStream sb = new ByteArrayOutputStream();
		int s = -1;
		while ((s = strm.read()) != -1) {
			sb.write(s);
		}
		return convertTXTToHTML(sb.toByteArray());
	}

	/**
	 *  Description of the Method
	 *
	 *@param  input                      Description of the Parameter
	 *@return                            Description of the Return Value
	 *@exception  IOException            Description of the Exception
	 *@exception  FileNotFoundException  Description of the Exception
	 */
	public String convertTXTToHTML(java.io.File input)
		throws IOException, FileNotFoundException {
		BufferedInputStream strm =
			new BufferedInputStream(new FileInputStream(input));
		ByteArrayOutputStream sb = new ByteArrayOutputStream();
		int s = -1;
		while ((s = strm.read()) != -1) {
			sb.write(s);
		}
		return convertTXTToHTML(sb.toByteArray());
	}

	/**
	 *  Description of the Method
	 *
	 *@param  input                      Description of the Parameter
	 *@return                            Description of the Return Value
	 *@exception  MalformedURLException  Description of the Exception
	 *@exception  IOException            Description of the Exception
	 */
	public String convertTXTToHTML(java.net.URL input)
		throws MalformedURLException, IOException {
		HttpURLConnection conn = (HttpURLConnection) input.openConnection();
		conn.setAllowUserInteraction(false);
		conn.setRequestProperty(
			"User-agent",
			"Mozilla/4.0 (compatible; MSIE 5.5; Windows 98");
		conn.setInstanceFollowRedirects(true);
		conn.connect();
		BufferedInputStream strm =
			new BufferedInputStream(conn.getInputStream());
		ByteArrayOutputStream sb = new ByteArrayOutputStream();
		int s = -1;
		while ((s = strm.read()) != -1) {
			sb.write(s);
		}
		return convertTXTToHTML(sb.toByteArray());
	}

}

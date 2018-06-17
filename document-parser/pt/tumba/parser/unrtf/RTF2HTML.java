package pt.tumba.parser.unrtf;

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
import pt.tumba.parser.DocFilter;
import pt.tumba.parser.*;

/**
 * Presents rtfs as html 
 * 
 * @author Daniel Gomes 
 * @created 27th April, 2004
 */
public class RTF2HTML implements DocFilter {

	private int sizeCount = 0;

	/**
	 * * Description of the Method * Requires unrtf: http://unrtf.50megs.com/
	 * *@return Description of the Return Value
	 */
	public int originalSize() {
		return sizeCount;
	}

	private Random rnd = new Random();

	public RTF2HTML() throws Exception {
		try {
			NativeExec.execute("unrtf");
		} catch (Exception e) {
			throw new Exception("Required software not instaled: unrtf. Make sure it is in the PATH");
		}
	}

	/**
	 * * Description of the Method * *@param b2 Description of the Parameter
	 * *@return Description of the Return Value *@exception Exception Description of
	 * the Exception
	 */
	public String convertRTFToHTML(byte b2[]) throws Exception {
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
			NativeExec.execute("unrtf -t html -n " + file1.getAbsolutePath(), outw);
			outw.close();
			BufferedReader input = new BufferedReader(new FileReader(file2));
			while ((s2 = input.readLine()) != null) {
				/*
				 * String s3 = new String(); StringTokenizer st = new StringTokenizer(s2); while
				 * (st.hasMoreTokens()) { String s = st.nextToken(); if
				 * (s.startsWith("http://")) { String s22 = "<a href='" + s + "'>"; s =
				 * s.replaceAll("&", "&amp;"); s = s.replaceAll("<", "&lt;"); s =
				 * s.replaceAll(">", "&gt;"); s22 += s + "</a>"; s = s22; } else { s =
				 * s.replaceAll("&", "&amp;"); s = s.replaceAll("<", "&lt;"); s =
				 * s.replaceAll(">", "&gt;"); } s3 += s; s3 += " "; } aux += s3 + "\n";
				 */
				aux += s2 + "\n";
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
	 * * Description of the Method * *@param input Description of the Parameter
	 * *@return Description of the Return Value *@exception Exception Description of
	 * the Exception
	 */
	public String convertRTFToHTML(InputStream input) throws Exception {
		BufferedInputStream strm = new BufferedInputStream(input);
		ByteArrayOutputStream sb = new ByteArrayOutputStream();
		int s;
		while ((s = strm.read()) != -1) {
			sb.write(s);
		}
		return convertRTFToHTML(sb.toByteArray());
	}

	/**
	 * * Description of the Method * *@param input Description of the Parameter
	 * *@return Description of the Return Value *@exception Exception Description of
	 * the Exception
	 */
	public String convertRTFToHTML(java.io.File input) throws Exception {
		BufferedInputStream strm = new BufferedInputStream(new FileInputStream(input));
		ByteArrayOutputStream sb = new ByteArrayOutputStream();
		int s;
		while ((s = strm.read()) != -1) {
			sb.write(s);
		}
		return convertRTFToHTML(sb.toByteArray());
	}

	/**
	 * * Description of the Method * *@param input Description of the Parameter
	 * *@return Description of the Return Value *@exception Exception Description of
	 * the Exception
	 */
	public String convertRTFToHTML(URL input) throws Exception {
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
		return convertRTFToHTML(sb.toByteArray());
	}

	public static void main(String args[]) throws Exception {
		System.out.println("usage: java RTF2HTML URL");
		RTF2HTML conv = new RTF2HTML();
		System.out.println(conv.convertRTFToHTML(new URL(args[0])));
	}
}
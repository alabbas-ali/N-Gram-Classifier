package pt.tumba.parser.tex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
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
public class TEX2HTML implements DocFilter {

	File filescript;

	String perlData = "$revno='$Revision: 1.2 $';\n" + "$revno =~ s/^.*:\\s*//;\n" + "$revno =~ s/\\$\\s*$//;\n"
			+ "$/='\\x00';\n" + "$_ = <>;\n" + "s/^(\\%[^\\n]*\\n)+//g;\n" + "s/([^\\\\])\\%[^\\n]*\\n/\\1/g;\n"
			+ "s/\\n\\n+/\\<P\\>\\n\\n/g;		\n" + "s/\\\\begin\\{enumerate\\}/\\<OL\\>/g;\n"
			+ "s/\\\\end\\{enumerate\\}/\\<\\/OL\\>/g;\n" + "s/\\\\begin\\{itemize\\}/\\<UL\\>/g;\n"
			+ "s/\\\\end\\{itemize\\}/\\<\\/UL\\>/g;\n" + "s/\\\\begin\\{description\\}/\\<DL\\>/g;\n"
			+ "s/\\\\end\\{description\\}/\\<\\/DL\\>/g;\n" + "s/\\\\item\\s*\\[([^\\]]*)\\]/\\<dt\\>\\1\\<dd\\>/g;\n"
			+ "s/\\\\item/\\<LI\\>/g;\n" + "s/\\\\begin\\{quotation\\}/\\<blockquote\\>/g;\n"
			+ "s/\\\\end\\{quotation\\}/\\<\\/blockquote\\>/g;\n" + "s/\\\\begin\\{quote\\}/\\<blockquote\\>/g;\n"
			+ "s/\\\\end\\{quote\\}/\\<\\/blockquote\\>/g;\n" + "s/\\\\begin\\{verbatim\\}/\\<pre\\>/g;\n"
			+ "s/\\\\end\\{verbatim\\}/\\<\\/pre\\>/g;\n" + "s/\\\\begin\\{tabbing\\}/\\<pre\\>/g;\n"
			+ "s/\\\\end\\{tabbing\\}/\\<\\/pre\\>/g;\n" + "s/\\\\begin\\{verse\\}/\\<pre\\>/g;\n"
			+ "s/\\\\end\\{verse\\}/\\<\\/pre\\>/g;\n" + "s/\\\\\\&/&amp\\;/g;\n" + "s/\\\\\\</\\&lt\\;/g;\n"
			+ "s/\\\\\\>/\\&gt\\;/g;\n" + "s/\\\\\\%/\\%/g;\n" + "s/\\\\\\#/\\#/g;\n" + "s/\\\\ / /g;\n"
			+ "s/\\\\\\'([AEIOUYaeiouy])\\s+/\\&\\1acute\\;/g;\n" + "s/\\\\\\\"([AEIOUaeiouy])\\s+/\\&\\1uml\\;/g;\n"
			+ "s/\\\\\\`([AEIOUaeiou])\\s+/\\&\\1grave\\;/g;\n" + "s/\\\\\\~([ANOano])\\s+/\\&\\1tilde\\;/g;\n"
			+ "s/\\\\\\^([AEIOUaeiou])\\s+/\\&\\1circ\\;/g;\n" + "s/\\\\\\,([Cc])\\s+/\\&\\1cedil\\;/g;\n"
			+ "s/\\\\([aoAO][Ee])\\s+/\\&\\1lig\\;/g;\n" + "s/\\\\ss/\\&szlig\\;/g;\n" + "s/\\\\aa/\\&aring\\;/g;\n"
			+ "s/\\\\AA/\\&Aring\\;/g;\n" + "s/\\\\([Oo])\\s+/\\&\\1slash\\;/g;\n" + "s/\\\\ldots/.../g;\n"
			+ "s/\\\\leq/\\&lt;=/g;\n" + "s/\\\\geq/\\&gt;=/g;\n" + "s/([^\\\\])\\~/\\1 /g;\n" + "s/^\\~/ /g;\n"
			+ "s/\\\\\\\\\\s*$/<br>/g;\n" + "s/\\\"/\\&quot;/g;\n"
			+ "s/\\\\verb\\+([^+]*)\\+/\\<tt\\>\\1\\<\\/tt\\>/g;\n"
			+ "s/\\s*\\{\\\\em\\s*([^}]*)\\}/ <em>\\1<\\/em>/g;\n" + "s/\\s*\\{\\\\bf\\s*([^}]*)\\}/ <b>\\1<\\/b>/g;\n"
			+ "s/\\s*\\{\\\\sc\\s*([^}]*)\\}/ \\U\\1\\E/g;\n" + "s/\\s*\\{\\\\it\\s*([^}]*)\\}/ <i>\\1<\\/i>/g;\n"
			+ "s/\\s*\\{\\\\tt\\s*([^}]*)\\}/ <tt>\\1<\\/tt>/g;\n" + "s/\\\\begin\\{em\\}/\\<em\\>/g;\n"
			+ "s/\\\\end\\{em\\}/\\<\\/em\\>/g;\n" + "s/\\\\begin\\{bf\\}/\\<b\\>/g;\n"
			+ "s/\\\\end\\{bf\\}/\\<\\/b\\>/g;\n" + "s/\\\\begin\\{it\\}/\\<i\\>/g;\n"
			+ "s/\\\\end\\{it\\}/\\<\\/i\\>/g;\n" + "s/\\\\begin\\{tt\\}/\\<tt\\>/g;\n"
			+ "s/\\\\end\\{tt\\}/\\<\\tt\\>/g;\n" + "s/\\\\\\///g;\n" + "s/\\\\noindent//g;\n"
			+ "s/\\\\section\\s*\\{([^}]*)\\}/<h1>$1<\\/h1>/g;\n"
			+ "s/\\\\subsection\\s*\\{([^}]*)\\}/<h2>$1<\\/h2>/g;\n"
			+ "s/\\\\subsubsection\\s*\\{([^}]*)\\}/<h3>$1<\\/h3>/g;\n" + "s/\\>\\s*\\<[Pp]\\>/\\>/g;\n"
			+ "s/\\\\index\\{([^}]*)\\}/\\<a name\\=\\\"ind-$1\\\"\\>/g;\n" + "$count = 1;\n"
			+ "while (/\\\\label\\W\\s*\\{/) {\n" + "	s/\\\\label\\W\\s*\\{([^}]*)\\}/<a name\\=\\\"ref-$1\\\"\\>/;\n"
			+ "	$labels{$1} = $count;\n" + "	$count += 1;\n" + "}\n"
			+ "s/\\\\ref\\{([^}]*)\\}/<a href\\=\\\"#ref-$1\\\"\\>$labels{$1}\\<\\/a\\>/g;\n"
			+ "$address=$ENV{'USER'} . '@' . `hostname`;\n" + "chop $address;\n" + "$header = \"<html><body>\\n\";\n"
			+ "$trailer =  \"<P></body></html>\\n\";\n" + "$footnotenumber=1;\n" + "$footnotes='';\n"
			+ "#while (/\\\\footnote/) {\n"
			+ "#    s/\\\\footnote\\{([^}]*)\\}/ \\<a href\\=\\\"#footnote-$footnotenumber\\\"\\>\\($footnotenumber\\) \\<\\/a\\>/;\n"
			+ "#    $footnotes .= \"\\<a name\\=\\\"footnote-$footnotenumber\\\"\\>\\<li\\> $1\\<\\/a\\>\\n\";\n"
			+ "#$footnotenumber++;\n" + "#}\n"
			+ "if ($footnotes) { $footnotes = \"\\<hr\\>\\n\\<h1\\>Footnotes\\<\\/h1\\>\\<ol\\>\\n\" . $footnotes . \"\\n\\<\\/ol\\>\\n\"; }\n"
			+ "$_ = $header .     $_ . $footnotes . $trailer;\n" + "print $_;\n";
	{
		boolean errord = false;
		try {
			File f1 = File.createTempFile("tex2html", "pl");
			try {
				f1.delete();
			} catch (Exception e) {
				errord = true;
			}
			FileOutputStream perlw = new FileOutputStream(f1);
			perlw.write(perlData.getBytes());
			perlw.close();
			filescript = f1;
		} catch (Exception e) {
			errord = true;
		}
	}

	/**
	 * Description of the Method
	 *
	 * @exception Throwable
	 *                Description of the Exception
	 */
	protected void finalize() throws Throwable {
		File f1 = File.createTempFile("tex2html", "pl");
		boolean errord = false;
		try {
			f1.delete();
		} catch (Exception e) {
			errord = true;
		}
		super.finalize();
	}

	private Random rnd = new Random();
	private int sizeCount = 0;

	public TEX2HTML() throws Exception {
		try {
			NativeExec.execute("perl --h");
		} catch (Exception e) {
			throw new Exception("Required software not instaled: perl");
		}
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
	public int originalSize() {
		return sizeCount;
	}

	/**
	 * Description of the Method
	 *
	 * @param s2
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	public String convertTEXToHTML(String s4) throws Exception {
		String s2 = s4;
		String filename = "" + System.currentTimeMillis() + "." + rnd.nextInt();
		boolean errord = false;
		try {
			sizeCount = s2.length();
			String aux = new String("");
			java.io.File file1 = java.io.File.createTempFile(filename, "tex");
			java.io.File file2 = java.io.File.createTempFile(filename, "html");
			PrintWriter out = new PrintWriter(new FileWriter(file1));
			PrintWriter out2 = new PrintWriter(new FileWriter(file2));
			out.print(s2);
			out.close();
			NativeExec.execute("perl " + filescript.getAbsolutePath() + " " + file1.getAbsolutePath(), out2);
			out2.close();
			BufferedReader input = new BufferedReader(new FileReader(file2));
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
			if (aux.equals("")) {
				throw new Exception("Empty Content.");
			}
			input.close();
			file1.delete();
			file2.delete();
			return aux;
		} catch (Exception e) {
			try {
				(java.io.File.createTempFile(filename, "tex")).delete();
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
	public String convertTEXToHTML(Reader input) throws Exception {
		BufferedReader strm = new BufferedReader(input);
		StringBuffer sb = new StringBuffer();
		int s;
		while ((s = strm.read()) != -1) {
			sb.append((char) s);
		}
		return convertTEXToHTML(sb.toString());
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
	public String convertTEXToHTML(InputStream input) throws Exception {
		BufferedReader strm = new BufferedReader(new InputStreamReader(input));
		StringBuffer sb = new StringBuffer();
		int s;
		while ((s = strm.read()) != -1) {
			sb.append((char) s);
		}
		return convertTEXToHTML(sb.toString());
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
	public String convertTEXToHTML(java.io.File input) throws Exception {
		BufferedReader strm = new BufferedReader(new FileReader(input));
		StringBuffer sb = new StringBuffer();
		int s;
		while ((s = strm.read()) != -1) {
			sb.append((char) s);
		}
		return convertTEXToHTML(sb.toString());
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
	public String convertTEXToHTML(URL input) throws Exception {
		HttpURLConnection conn = (HttpURLConnection) input.openConnection();
		conn.setAllowUserInteraction(false);
		conn.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows 98");
		conn.setInstanceFollowRedirects(true);
		conn.connect();
		BufferedReader strm = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuffer sb = new StringBuffer();
		int s;
		while ((s = strm.read()) != -1) {
			sb.append((char) s);
		}
		return convertTEXToHTML(sb.toString());
	}

}

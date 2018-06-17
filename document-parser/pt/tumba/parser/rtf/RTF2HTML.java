package pt.tumba.parser.rtf;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.StringTokenizer;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.rtf.RTFEditorKit;

import pt.tumba.parser.DocFilter;

/**
 * Description of the Class
 *
 * @author bmartins
 * @created 22 de Agosto de 2002
 */
public class RTF2HTML implements DocFilter {

	/**
	 * Description of the Class
	 *
	 * @author bmartins
	 * @created 22 de Agosto de 2002
	 */
	private class HTMLStateMachine {

		private String alignNames[] = { "left", "center", "right" };
		/**
		 * Description of the Field
		 */
		public boolean acceptFonts;
		private String fontName;
		private Color color;
		private int size;
		private int alignment;
		private boolean bold;
		private boolean italic;
		private boolean underline;
		private double firstLineIndent;
		private double oldLeftIndent;
		private double oldRightIndent;
		private double leftIndent;
		private double rightIndent;
		private boolean firstLine;

		/**
		 * Constructor for the HTMLStateMachine object
		 */
		HTMLStateMachine() {
			acceptFonts = true;
			fontName = "";
			alignment = -1;
			bold = false;
			italic = false;
			underline = false;
			color = null;
			size = -1;
			firstLineIndent = 0.0D;
			oldLeftIndent = 0.0D;
			oldRightIndent = 0.0D;
			leftIndent = 0.0D;
			rightIndent = 0.0D;
			firstLine = false;
		}

		/**
		 * Description of the Method
		 *
		 * @param attributeset
		 *            Description of the Parameter
		 * @param stringbuffer
		 *            Description of the Parameter
		 * @param element
		 *            Description of the Parameter
		 */
		public void updateState(AttributeSet attributeset, StringBuffer stringbuffer, Element element) {
			String s = element.getName();
			if (s.equalsIgnoreCase("paragraph")) {
				firstLine = true;
			}
			leftIndent = updateDouble(attributeset, leftIndent, StyleConstants.LeftIndent);
			rightIndent = updateDouble(attributeset, rightIndent, StyleConstants.RightIndent);
			if (leftIndent != oldLeftIndent || rightIndent != oldRightIndent) {
				closeIndentTable(stringbuffer, oldLeftIndent, oldRightIndent);
			}
			bold = updateBoolean(attributeset, StyleConstants.Bold, "b", bold, stringbuffer);
			italic = updateBoolean(attributeset, StyleConstants.Italic, "i", italic, stringbuffer);
			underline = updateBoolean(attributeset, StyleConstants.Underline, "u", underline, stringbuffer);
			size = updateFontSize(attributeset, size, stringbuffer);
			color = updateFontColor(attributeset, color, stringbuffer);
			if (acceptFonts) {
				fontName = updateFontName(attributeset, fontName, stringbuffer);
			}
			alignment = updateAlignment(attributeset, alignment, stringbuffer);
			firstLineIndent = updateDouble(attributeset, firstLineIndent, StyleConstants.FirstLineIndent);
			if (leftIndent != oldLeftIndent || rightIndent != oldRightIndent) {
				openIndentTable(stringbuffer, leftIndent, rightIndent);
				oldLeftIndent = leftIndent;
				oldRightIndent = rightIndent;
			}
		}

		/**
		 * Description of the Method
		 *
		 * @param stringbuffer
		 *            Description of the Parameter
		 * @param d
		 *            Description of the Parameter
		 * @param d1
		 *            Description of the Parameter
		 */
		private void openIndentTable(StringBuffer stringbuffer, double d, double d1) {
			if (d != 0.0D || d1 != 0.0D) {
				closeSubsetTags(stringbuffer);
				stringbuffer.append("<table><tr>");
				String s = getSpaceTab((int) (d / 4D));
				if (s.length() > 0) {
					stringbuffer.append("<td>" + s + "</td>");
				}
				stringbuffer.append("<td>");
			}
		}

		/**
		 * Description of the Method
		 *
		 * @param stringbuffer
		 *            Description of the Parameter
		 * @param d
		 *            Description of the Parameter
		 * @param d1
		 *            Description of the Parameter
		 */
		private void closeIndentTable(StringBuffer stringbuffer, double d, double d1) {
			if (d != 0.0D || d1 != 0.0D) {
				closeSubsetTags(stringbuffer);
				stringbuffer.append("</td>");
				String s = getSpaceTab((int) (d1 / 4D));
				if (s.length() > 0) {
					stringbuffer.append("<td>" + s + "</td>");
				}
				stringbuffer.append("</tr></table>");
			}
		}

		/**
		 * Description of the Method
		 *
		 * @param stringbuffer
		 *            Description of the Parameter
		 */
		public void closeTags(StringBuffer stringbuffer) {
			closeSubsetTags(stringbuffer);
			closeTag(alignment, -1, "div", stringbuffer);
			alignment = -1;
			closeIndentTable(stringbuffer, oldLeftIndent, oldRightIndent);
		}

		/**
		 * Description of the Method
		 *
		 * @param stringbuffer
		 *            Description of the Parameter
		 */
		private void closeSubsetTags(StringBuffer stringbuffer) {
			closeTag(bold, "b", stringbuffer);
			closeTag(italic, "i", stringbuffer);
			closeTag(underline, "u", stringbuffer);
			closeTag(color, "font", stringbuffer);
			closeTag(fontName, "font", stringbuffer);
			closeTag(size, -1, "font", stringbuffer);
			bold = false;
			italic = false;
			underline = false;
			color = null;
			fontName = "";
			size = -1;
		}

		/**
		 * Description of the Method
		 *
		 * @param flag
		 *            Description of the Parameter
		 * @param s
		 *            Description of the Parameter
		 * @param stringbuffer
		 *            Description of the Parameter
		 */
		private void closeTag(boolean flag, String s, StringBuffer stringbuffer) {
			if (flag) {
				stringbuffer.append("</" + s + ">");
			}
		}

		/**
		 * Description of the Method
		 *
		 * @param color1
		 *            Description of the Parameter
		 * @param s
		 *            Description of the Parameter
		 * @param stringbuffer
		 *            Description of the Parameter
		 */
		private void closeTag(Color color1, String s, StringBuffer stringbuffer) {
			if (color1 != null) {
				stringbuffer.append("</" + s + ">");
			}
		}

		/**
		 * Description of the Method
		 *
		 * @param s
		 *            Description of the Parameter
		 * @param s1
		 *            Description of the Parameter
		 * @param stringbuffer
		 *            Description of the Parameter
		 */
		private void closeTag(String s, String s1, StringBuffer stringbuffer) {
			if (s.length() > 0) {
				stringbuffer.append("</" + s1 + ">");
			}
		}

		/**
		 * Description of the Method
		 *
		 * @param i
		 *            Description of the Parameter
		 * @param j
		 *            Description of the Parameter
		 * @param s
		 *            Description of the Parameter
		 * @param stringbuffer
		 *            Description of the Parameter
		 */
		private void closeTag(int i, int j, String s, StringBuffer stringbuffer) {
			if (i > j) {
				stringbuffer.append("</" + s + ">");
			}
		}

		/**
		 * Description of the Method
		 *
		 * @param attributeset
		 *            Description of the Parameter
		 * @param i
		 *            Description of the Parameter
		 * @param stringbuffer
		 *            Description of the Parameter
		 * @return Description of the Return Value
		 */
		private int updateAlignment(AttributeSet attributeset, int k, StringBuffer stringbuffer) {
			int i = k;
			Object obj = attributeset.getAttribute(StyleConstants.Alignment);
			if (obj == null)
				return i;
			int j = ((Integer) obj).intValue();
			if (j == 3) {
				j = 0;
			}
			if (j != i && j >= 0 && j <= 2) {
				if (i > -1) {
					stringbuffer.append("</div>");
				}
				stringbuffer.append("<div align=\"" + alignNames[j] + "\">");
				i = j;
			}
			return i;
		}

		/**
		 * Description of the Method
		 *
		 * @param attributeset
		 *            Description of the Parameter
		 * @param color1
		 *            Description of the Parameter
		 * @param stringbuffer
		 *            Description of the Parameter
		 * @return Description of the Return Value
		 */
		private Color updateFontColor(AttributeSet attributeset, Color color3, StringBuffer stringbuffer) {
			Color color1 = color3;
			Object obj = attributeset.getAttribute(StyleConstants.Foreground);
			if (obj == null)
				return color1;
			Color color2 = (Color) obj;
			if (color2 != color1) {
				if (color1 != null) {
					stringbuffer.append("</font>");
				}
				if (color2 != null) {
					stringbuffer.append("<font color=\"#" + makeColorString(color2) + "\">");
				}
			}
			color1 = color2;
			return color1;
		}

		/**
		 * Description of the Method
		 *
		 * @param attributeset
		 *            Description of the Parameter
		 * @param s
		 *            Description of the Parameter
		 * @param stringbuffer
		 *            Description of the Parameter
		 * @return Description of the Return Value
		 */
		private String updateFontName(AttributeSet attributeset, String s2, StringBuffer stringbuffer) {
			String s = s2;
			Object obj = attributeset.getAttribute(StyleConstants.FontFamily);
			if (obj == null)
				return s;
			String s1 = (String) obj;
			if (!s1.equals(s)) {
				if (!s.equals("")) {
					stringbuffer.append("</font>");
				}
				stringbuffer.append("<font face=\"" + s1 + "\">");
			}
			s = s1;
			return s;
		}

		/**
		 * Description of the Method
		 *
		 * @param attributeset
		 *            Description of the Parameter
		 * @param d
		 *            Description of the Parameter
		 * @param obj
		 *            Description of the Parameter
		 * @return Description of the Return Value
		 */
		private double updateDouble(AttributeSet attributeset, double d2, Object obj) {
			double d = d2;
			Object obj1 = attributeset.getAttribute(obj);
			if (obj1 != null) {
				d = ((Float) obj1).floatValue();
			}
			return d;
		}

		/**
		 * Description of the Method
		 *
		 * @param attributeset
		 *            Description of the Parameter
		 * @param i
		 *            Description of the Parameter
		 * @param stringbuffer
		 *            Description of the Parameter
		 * @return Description of the Return Value
		 */
		private int updateFontSize(AttributeSet attributeset, int k, StringBuffer stringbuffer) {
			int i = k;
			Object obj = attributeset.getAttribute(StyleConstants.FontSize);
			if (obj == null)
				return i;
			int j = ((Integer) obj).intValue();
			if (j != i) {
				if (i != -1) {
					stringbuffer.append("</font>");
				}
				stringbuffer.append("<font size=\"" + j / 4 + "\">");
			}
			i = j;
			return i;
		}

		/**
		 * Description of the Method
		 *
		 * @param attributeset
		 *            Description of the Parameter
		 * @param obj
		 *            Description of the Parameter
		 * @param s
		 *            Description of the Parameter
		 * @param flag
		 *            Description of the Parameter
		 * @param stringbuffer
		 *            Description of the Parameter
		 * @return Description of the Return Value
		 */
		private boolean updateBoolean(AttributeSet attributeset, Object obj, String s, boolean flag2,
				StringBuffer stringbuffer) {
			boolean flag = flag2;
			Object obj1 = attributeset.getAttribute(obj);
			if (obj1 != null) {
				boolean flag1 = ((Boolean) obj1).booleanValue();
				if (flag1 != flag) {
					if (flag1) {
						stringbuffer.append("<" + s + ">");
					} else {
						stringbuffer.append("</" + s + ">");
					}
				}
				flag = flag1;
			}
			return flag;
		}

		/**
		 * Description of the Method
		 *
		 * @param color1
		 *            Description of the Parameter
		 * @return Description of the Return Value
		 */
		private String makeColorString(Color color1) {
			String s = Long.toString(color1.getRGB() & 0xffffff, 16);
			if (s.length() < 6) {
				StringBuffer stringbuffer = new StringBuffer();
				for (int i = s.length(); i < 6; i++) {
					stringbuffer.append("0");
				}
				stringbuffer.append(s);
				s = stringbuffer.toString();
			}
			return s;
		}

		/**
		 * Description of the Method
		 *
		 * @param s
		 *            Description of the Parameter
		 * @return Description of the Return Value
		 */
		public String performFirstLineIndent(String s2) {
			String s = s2;
			if (firstLine) {
				if (firstLineIndent != 0.0D) {
					int i = (int) (firstLineIndent / 4D);
					s = getSpaceTab(i) + s;
				}
				firstLine = false;
			}
			return s;
		}

		/**
		 * Gets the spaceTab attribute of the HTMLStateMachine object
		 *
		 * @param i
		 *            Description of the Parameter
		 * @return The spaceTab value
		 */
		public String getSpaceTab(int i) {
			StringBuffer stringbuffer = new StringBuffer();
			for (int j = 0; j < i; j++) {
				stringbuffer.append("&nbsp;");
			}
			return stringbuffer.toString();
		}

	}

	/**
	 * Constructor for the RTF2HTML object
	 */
	public RTF2HTML() {
	}

	private int sizeCount = 0;

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
	 */
	public String convertRTFToHTML(String s4) {
		String s2 = s4;
		sizeCount = s2.length();
		HTMLStateMachine htmlstatemachine = new HTMLStateMachine();
		s2 = convertRTFStringToHTML(s2, htmlstatemachine);
		String s3 = new String("<html><body>");
		StringTokenizer st = new StringTokenizer(s2);
		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			if (s.startsWith("http://")) {
				s = "<a href='" + s + "'>" + s + "</a>";
			}
			s3 += s + " ";
		}
		return s3 + "</body></html>";
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception IOException
	 *                Description of the Exception
	 */
	public String convertRTFToHTML(Reader input) throws IOException {
		BufferedReader strm = new BufferedReader(input);
		StringBuffer sb = new StringBuffer();
		int s;
		while ((s = strm.read()) != -1) {
			sb.append((char) s);
		}
		return convertRTFToHTML(sb.toString());
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception IOException
	 *                Description of the Exception
	 */
	public String convertRTFToHTML(InputStream input) throws IOException {
		BufferedReader strm = new BufferedReader(new InputStreamReader(input));
		StringBuffer sb = new StringBuffer();
		int s;
		while ((s = strm.read()) != -1) {
			sb.append((char) s);
		}
		return convertRTFToHTML(sb.toString());
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception IOException
	 *                Description of the Exception
	 */
	public String convertRTFToHTML(File input) throws IOException {
		BufferedReader strm = new BufferedReader(new FileReader(input));
		StringBuffer sb = new StringBuffer();
		int s;
		while ((s = strm.read()) != -1) {
			sb.append((char) s);
		}
		return convertRTFToHTML(sb.toString());
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception IOException
	 *                Description of the Exception
	 */
	public String convertRTFToHTML(URL input) throws IOException {
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
		return convertRTFToHTML(sb.toString());
	}

	/**
	 * Description of the Method
	 *
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public String convertRTFStringToHTML(String s) {
		HTMLStateMachine htmlstatemachine = new HTMLStateMachine();
		RTFEditorKit rtfeditorkit = new RTFEditorKit();
		DefaultStyledDocument defaultstyleddocument = new DefaultStyledDocument();
		readString(s, defaultstyleddocument, rtfeditorkit);
		return scanDocument(defaultstyleddocument, htmlstatemachine);
	}

	/**
	 * Description of the Method
	 *
	 * @param s
	 *            Description of the Parameter
	 * @param htmlstatemachine
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	private String convertRTFStringToHTML(String s2, HTMLStateMachine htmlstatemachine) {
		String s = s2;
		RTFEditorKit rtfeditorkit = new RTFEditorKit();
		DefaultStyledDocument defaultstyleddocument = new DefaultStyledDocument();
		readString(s, defaultstyleddocument, rtfeditorkit);
		s = scanDocument(defaultstyleddocument, htmlstatemachine);
		return s;
	}

	/**
	 * Description of the Method
	 *
	 * @param s
	 *            Description of the Parameter
	 * @param document
	 *            Description of the Parameter
	 * @param rtfeditorkit
	 *            Description of the Parameter
	 */
	private void readString(String s, Document document, RTFEditorKit rtfeditorkit) {
		try {
			ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(s.getBytes());
			rtfeditorkit.read(bytearrayinputstream, document, 0);
		} catch (Exception exception) {
			return;
			// exception.printStackTrace();
		}
	}

	/**
	 * Description of the Method
	 *
	 * @param document
	 *            Description of the Parameter
	 * @param htmlstatemachine
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	private String scanDocument(Document document, HTMLStateMachine htmlstatemachine) {
		String s = "";
		try {
			StringBuffer stringbuffer = new StringBuffer();
			Element element = document.getDefaultRootElement();
			recurseElements(element, document, stringbuffer, htmlstatemachine);
			htmlstatemachine.closeTags(stringbuffer);
			s = stringbuffer.toString();
		} catch (Exception exception) {
			return s;
			// exception.printStackTrace();
		}
		return s;
	}

	/**
	 * Description of the Method
	 *
	 * @param element
	 *            Description of the Parameter
	 * @param document
	 *            Description of the Parameter
	 * @param stringbuffer
	 *            Description of the Parameter
	 * @param htmlstatemachine
	 *            Description of the Parameter
	 */
	private void recurseElements(Element element, Document document, StringBuffer stringbuffer,
			HTMLStateMachine htmlstatemachine) {
		for (int i = 0; i < element.getElementCount(); i++) {
			Element element1 = element.getElement(i);
			scanAttributes(element1, document, stringbuffer, htmlstatemachine);
			recurseElements(element1, document, stringbuffer, htmlstatemachine);
		}
	}

	/**
	 * Description of the Method
	 *
	 * @param element
	 *            Description of the Parameter
	 * @param document
	 *            Description of the Parameter
	 * @param stringbuffer
	 *            Description of the Parameter
	 * @param htmlstatemachine
	 *            Description of the Parameter
	 */
	private void scanAttributes(Element element, Document document, StringBuffer stringbuffer,
			HTMLStateMachine htmlstatemachine) {
		try {
			int i = element.getStartOffset();
			int j = element.getEndOffset();
			String s = document.getText(i, j - i);
			javax.swing.text.AttributeSet attributeset = element.getAttributes();
			htmlstatemachine.updateState(attributeset, stringbuffer, element);
			String s1 = element.getName();
			if (s1.equalsIgnoreCase("content")) {
				s = s.replaceAll("\\t", htmlstatemachine.getSpaceTab(8));
				s = s.replaceAll("\\n", "<br />\n");
				s = htmlstatemachine.performFirstLineIndent(s);
				stringbuffer.append(s);
			}
		} catch (BadLocationException badlocationexception) {
			return;
			// badlocationexception.printStackTrace();
		}
	}

	/**
	 * Description of the Method
	 *
	 * @param in
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	public InputStream parse(File in) throws Exception {
		return parse(new FileInputStream(in));
	}

	/**
	 * Description of the Method
	 *
	 * @param in
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	public InputStream parse(URL in) throws Exception {
		HttpURLConnection conn = (HttpURLConnection) in.openConnection();
		conn.setAllowUserInteraction(false);
		conn.setRequestProperty("User-agent", "www.tumba.pt");
		conn.setInstanceFollowRedirects(true);
		conn.connect();
		return parse(conn.getInputStream());
	}

	/**
	 * Arguments are: 0. Name of input SWF
	 *
	 * @param in
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	public InputStream parse(InputStream in) throws Exception {
		BufferedReader strm = new BufferedReader(new InputStreamReader(in));
		StringBuffer sb = new StringBuffer();
		int s;
		while ((s = strm.read()) != -1) {
			sb.append((char) s);
		}
		String s2 = convertRTFToHTML(sb.toString());
		return new ByteArrayInputStream(s2.getBytes());
	}

}

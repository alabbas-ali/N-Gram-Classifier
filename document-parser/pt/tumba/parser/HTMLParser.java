package pt.tumba.parser;

import java.io.*;
import java.nio.charset.*;
import java.nio.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
//import pt.tumba.ngram.LanguageClass;
//import pt.tumba.ngram.EntryProfile;
//import pt.tumba.ngram.NGramConstants;
//import pt.tumba.ngram.NGram;

/**
 * Parser to extract metadata from HTML files
 *
 * @author bmartins
 * @created 22 de Agosto de 2002
 */
public class HTMLParser {

	// private EntryProfile profile;
	// private LanguageClass languageModels;

	private StringBuffer anchorText;

	private int tagsAnchor;
	private int tagsBig;
	private int tagsBold;
	private int tagsEmphasize;
	private int tagsHeading1;
	private int tagsHeading2;
	private int tagsHeading3;
	private int tagsHeading4;
	private int tagsHeading5;
	private int tagsHeading6;
	private int tagsItalic;
	private int tagsSmall;
	private int tagsStrong;
	private int tagsTitle;

	private int anotation;

	/**
	 * The base address used to resolve links
	 */
	private String base;

	private String[][] escape = { { "&lt;", "<" }, { "&gt;", ">" }, { "&amp;", "&" }, { "&quot;", "\"" },
			{ "&agrave;", "à" }, { "&Agrave;", "À" }, { "&aacute;", "á" }, { "&Aacute;", "Á" }, { "&acirc;", "â" },
			{ "&auml;", "ä" }, { "&Auml;", "Ä" }, { "&Acirc;", "Â" }, { "&aring;", "å" }, { "&Aring;", "Å" },
			{ "&aelig;", "æ" }, { "&AElig;", "Æ" }, { "&Atilde;", "Ã" }, { "&atilde;", "ã" }, { "&ccedil;", "ç" },
			{ "&Ccedil;", "Ç" }, { "&eacute;", "é" }, { "&Eacute;", "É" }, { "&egrave;", "è" }, { "&Egrave;", "È" },
			{ "&ecirc;", "ê" }, { "&Ecirc;", "Ê" }, { "&euml;", "ë" }, { "&Euml;", "Ë" }, { "&iuml;", "ï" },
			{ "&Iuml;", "Ï" }, { "&igrave;", "ì" }, { "&Igrave;", "Ì" }, { "&iacute;", "í" }, { "&Iacute;", "Í" },
			{ "&Ntilde;", "Ñ" }, { "&ntilde;", "ñ" }, { "&ograve;", "ò" }, { "&Ograve;", "Ò" }, { "&oacute;", "ó" },
			{ "&Oacute;", "Ó" }, { "&ocirc;", "ô" }, { "&Ocirc;", "Ô" }, { "&ouml;", "ö" }, { "&Ouml;", "Ö" },
			{ "&oslash;", "ø" }, { "&Oslash;", "Ø" }, { "&Otilde;", "Õ" }, { "&otilde;", "õ" }, { "&szlig;", "ß" },
			{ "&ugrave;", "ù" }, { "&Ugrave;", "Ù" }, { "&uacute;", "ú" }, { "&Uacute;", "Ú" }, { "&ucirc;", "û" },
			{ "&Ucirc;", "û" }, { "&uuml;", "ü" }, { "&middot;", "." }, { "&copy;", "©" }, { "&dot;", "." },
			{ "&reg;", "®" }, { "&Uuml;", "Ü" }, { "&apos;", "'" }, { "&euro;", "€" }, { "&nbsp;", " " } };

	private Map escapeMap;

	private boolean followRedirects = false;
	private boolean useEncoding = true;

	/**
	 * An input stream for the document
	 */
	private InputStream input;
	private String language;
	private boolean languageComputation;

	private List lastFormHidden;
	private List lastFormSelect;

	private String lastFormURL;
	private String lastSelectName;
	private String lastSubmit;

	private URL link;

	private int maxTerms = -1;

	private String modelsPath = "datafiles/language_models/";

	/**
	 * The url or the filename of the document being parsed
	 */
	private String name;

	/**
	 * Internal storage for the current character read from the document
	 */
	private int nextChar;

	/**
	 * Internal storage for the tokens parsed from the document
	 */
	private Object nextToken;

	/**
	 * Temporary internal storage for the document text
	 */
	private StringBuffer output;
	private int position;

	/**
	 * Flag to indicate the last caracter on the output is a linefeed
	 */
	private boolean printLine;

	/**
	 * Flag to indicate the last caracter on the output is a space
	 */
	private boolean printSpace;

	/**
	 * Number of folowed redirects (only 2 allowed)
	 */
	private int redirects;

	/**
	 * Flag to indicate that after the last token there was a tag break
	 */
	private boolean tagBreak;

	private int wgramcount;

	/**
	 * Flag to indicate the current token is within HTML comments or javascript code
	 */
	private boolean withinIgnore;

	private MetaData metadata;
	private HyperLinks hyperlinks;
	private ImageLinks images;
	private HTMLMarkup markup;
	private Content content;

	// /**
	// * Description of the Method
	// *
	// * @return Description of the Return Value
	// */
	// public Iterator getNGrams() {
	// Vector v = new Vector();
	// String aux;
	// try {
	// if (profile == null)
	// computeLanguageProfile();
	// Iterator it = profile.ngrams();
	// while (it.hasNext()) {
	// aux = ((NGram) (it.next())).getString();
	// v.addElement(aux);
	// }
	// } catch (Exception e) {
	// return v.iterator();
	// }
	// return v.iterator();
	// }

	/**
	 * Gets the nGramRank attribute of the HTMLParser object
	 *
	 * @param ngram
	 *            Description of the Parameter
	 * @return The nGramRank value
	 */
	// public double getNGramRank(String ngram) {
	// if (profile == null)
	// computeLanguageProfile();
	// return profile.getRank(ngram);
	// }

	// /**
	// * Description of the Method
	// *
	// * @return Description of the Return Value
	// */
	// private String guessLanguage() {
	// try {
	// if (content.getFilteredText().length() < 40) {
	// throw (new Exception("Insufficient Data to Determine Language"));
	// }
	// if (profile == null)
	// computeLanguageProfile();
	// return languageModels.classify(profile);
	// } catch (Exception e) {
	// return "unknown";
	// }
	// }

	// private void computeLanguageProfile() {
	// try {
	// StringBuffer sb = new StringBuffer(metadata.getDescription());
	// sb.append(' ');
	// sb.append(metadata.getKeywords());
	// sb.append(' ');
	// sb.append(content.getText());
	// String dat = sb.toString();
	// if (dat.endsWith("his page uses frames, but your browser doesn't support
	// them.")) {
	// dat = dat.substring(0, dat.length() - 60);
	// }
	// InputStream str = new ByteArrayInputStream(dat.getBytes());
	// profile = new EntryProfile(str, NGramConstants.USEDNGRAMSMAX);
	// if (!guessLanguage().equals("portuguese") &&
	// !guessLanguage().equals("english")) {
	// dat = "";
	// for (int ii = 0, jj = 0, kk = 0; ii < content.textBlocks.size(); ii++) {
	// if ((kk = content.textBlocks.get(ii).toString().length()) > jj) {
	// dat = content.textBlocks.get(ii).toString();
	// jj = kk;
	// }
	// }
	// sb = new StringBuffer(metadata.getDescription());
	// sb.append(' ');
	// sb.append(metadata.getKeywords());
	// sb.append(' ');
	// sb.append(dat);
	// dat = sb.toString();
	// if (dat.endsWith("his page uses frames, but your browser doesn't support
	// them.")) {
	// dat = dat.substring(0, dat.length() - 60);
	// }
	// str = new ByteArrayInputStream(dat.getBytes());
	// profile = new EntryProfile(str, NGramConstants.USEDNGRAMSMAX);
	// }
	// } catch (Exception e) {
	// return;
	// }
	// }

	/**
	 * Constructor for the HTMLParser object
	 */
	// public HTMLParser() {
	// try {
	// languageModels = new LanguageClass(modelsPath);
	// } catch (Exception e) {
	// return;
	// }
	// }

	/**
	 * Constructor for the HTMLParser object
	 *
	 * @param modelsPath
	 *            Description of the Parameter
	 */
	public HTMLParser(String modelsPath) {
		try {
			this.modelsPath = modelsPath;
			//languageModels = new LanguageClass(modelsPath);
		} catch (Exception e) {
			return;
		}
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 * @exception IOException
	 *                Description of the Exception
	 */
	private boolean advanceScanner() throws IOException {
		if (nextChar != -1) {
			nextChar = input.read();
			if (nextChar != -1) {
				content.content.append((char) nextChar);
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Description of the Method
	 *
	 * @param tag
	 *            Description of the Parameter
	 * @param key
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception StringIndexOutOfBoundsException
	 *                Description of the Exception
	 */
	private String analyseTagAux(String tag, String key) throws StringIndexOutOfBoundsException {
		int i = StringUtils.toLowerCase(tag, false).indexOf(key);
		if (i == -1) {
			return null;
		}
		char aux[] = tag.toCharArray();
		int i2;
		int targ = aux.length;
		for (i += key.length(); i < targ && aux[i] == ' '; i++) {
		}
		if (i < targ) {
			if (aux[i] == '=') {
				for (++i; i < targ && aux[i] == ' '; i++) {
				}
				if (aux[i] == '"') {
					for (i2 = ++i; i2 < targ; i2++) {
						if (aux[i2] == '"') {
							return tag.substring(i, i2);
						}
					}
					return tag.substring(i, targ);
				} else if (aux[i] == '\'') {
					for (i2 = ++i; i2 < targ; i2++) {
						if (aux[i2] == '\'') {
							return tag.substring(i, i2);
						}
					}
					return tag.substring(i, targ);
				} else {
					for (i2 = i; i < targ; i++) {
						if (aux[i] == ' ') {
							break;
						}
					}
					return tag.substring(i2, i);
				}
			} else {
				return analyseTagAux(tag.substring(++i), key);
			}
		}
		return null;
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
	public String getBase() {
		return (base != null) ? ("http:\\" + base) : null;
	}

	/**
	 * Description of the Method
	 *
	 * @param type
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	private boolean checkPath(String type) {
		if (type.endsWith(".jar") || type.endsWith(".zip") || type.endsWith(".exe") || type.endsWith(".com")
				|| type.endsWith(".obj") || type.endsWith(".tar") || type.endsWith(".rar") || type.endsWith(".gz2")
				|| type.endsWith(".gz") || type.endsWith(".pdf") || type.endsWith(".doc") || type.endsWith(".ps")
				|| type.endsWith(".ppt") || type.endsWith(".tgz") || type.endsWith(".hqx") || type.endsWith(".bin")
				|| type.endsWith(".cab") || type.endsWith(".zoo") || type.endsWith(".mov") || type.endsWith(".mpg")
				|| type.endsWith(".mpeg") || type.endsWith(".avi") || type.endsWith(".mp3") || type.endsWith(".wav")
				|| type.endsWith(".mid") || type.endsWith(".xls") || type.endsWith(".rm") || type.endsWith(".dll")
				|| type.endsWith(".rpm") || type.endsWith(".class")) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the sentence positions attribute of the HTMLParser object
	 *
	 * @param word
	 *            Description of the Parameter
	 * @return The positions value
	 */
	public Iterator getLinks(String word) {
		HashMap aux = (HashMap) (content.termsLinks.get(word));
		if (aux == null) {
			return (new Vector()).iterator();
		}
		return aux.keySet().iterator();
	}

	/**
	 * Description of the Method
	 *
	 * @param word
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public Iterator getURLs(String word) {
		Iterator it = getLinks(word);
		Vector v = new Vector();
		boolean error = false;
		while (it.hasNext()) {
			try {
				v.addElement(new URL(it.next().toString()));
			} catch (Exception e) {
				error = true;
			}
		}
		return v.iterator();
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	public void initTokenizer(File input) throws Exception {
		initTokenizer(input, (URL) null);
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	public void initTokenizer(File input, String encoding) throws Exception {
		initTokenizer(input, encoding, null);
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @param base
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	public void initTokenizer(File input, URL base) throws Exception {
		initTokenizer(input, null, base);
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @param base
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	public void initTokenizer(File input, String encoding, URL base) throws Exception {
		String path = input.getAbsolutePath();
		if (!checkPath(path)) {
			throw new Exception("Not a text file.");
		}
		if (encoding == null)
			initTokenizer(new FileInputStream(input), base);
		else
			initTokenizer(new FileInputStream(input), encoding, base);
		this.name = path;
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	public void initTokenizer(InputStream input) throws Exception {
		initTokenizer(input, (URL) null);
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	public void initTokenizer(InputStream input, String encoding) throws Exception {
		initTokenizer(input, encoding, (URL) null);
		this.useEncoding = false;
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	private void initTokenizer(InputStream input, boolean encoding) throws Exception {
		initTokenizer(input, (URL) null);
		this.useEncoding = encoding;
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @param base
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	public void initTokenizer(InputStream input, URL base) throws Exception {
		initTokenizer(input, null, base);
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @param base
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	public void initTokenizer(InputStream input, String encoding, URL base) throws Exception {
		this.input = new BufferedInputStream(input);
		if (encoding == null)
			this.useEncoding = true;
		else {
			this.useEncoding = false;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int aux = -1;
			while ((aux = this.input.read()) != -1)
				out.write(aux);
			this.input = new ByteArrayInputStream(new String(out.toByteArray(), encoding).getBytes());
		}
		metadata = new MetaData();
		hyperlinks = new HyperLinks();
		images = new ImageLinks();
		content = new Content(metadata);
		markup = new HTMLMarkup(content);
		output = new StringBuffer();
		link = null;
		tagsBold = tagsAnchor = tagsTitle = tagsSmall = tagsBig = tagsEmphasize = tagsItalic = tagsStrong = 0;
		tagsHeading1 = tagsHeading2 = tagsHeading3 = tagsHeading4 = tagsHeading5 = tagsHeading6 = 0;
		anchorText = new StringBuffer();
		name = new String();
		language = new String();
		languageComputation = false;
		metadata.setMIMEType(HttpURLConnection.guessContentTypeFromStream(input));
		// if (!metadata.equals("") && !metadata.getMIMEType().startsWith("text/") &&
		// !metadata.getMIMEType().equals("application/xml")) {
		// throw new Exception("Not a text file");
		// }
		lastFormURL = new String();
		lastSelectName = new String();
		lastSubmit = new String();
		lastFormHidden = new Vector();
		lastFormSelect = new Vector();
		// profile = null;
		nextToken = null;
		tagBreak = withinIgnore = false;
		printSpace = printLine = true;
		wgramcount = 1;
		anotation = position = nextChar = 0;
		if (escapeMap == null) {
			escapeMap = new HashMap();
			for (int i = escape.length - 1; i >= 0; i--) {
				escapeMap.put(escape[i][0], escape[i][1]);
			}
		}
		if (base != null) {
			this.base = base.toString();
			int index1 = this.base.lastIndexOf('/');
			if (index1 <= 6) {
				this.base = this.base.substring(7) + '/';
			} else {
				this.base = this.base.substring(7, index1 + 1);
			}
		} else {
			this.base = null;
		}
		this.name = "";
		advanceScanner();
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	public void initTokenizer(URL input) throws Exception {
		initTokenizer(input, 0);
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	public void initTokenizer(URL input, String encoding) throws Exception {
		initTokenizer(input, encoding, 0);
	}

	public void initTokenizer(URL input, int redirects) throws Exception {
		initTokenizer(input, null, 0);
	}

	/**
	 * Description of the Method
	 *
	 * @param input
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	public void initTokenizer(URL input, String encoding, int redirects) throws Exception {
		if (redirects > 2) {
			throw new Exception("Too many redirects");
		}
		this.redirects = redirects;
		HttpURLConnection conn = (HttpURLConnection) input.openConnection();
		conn.setAllowUserInteraction(false);
		conn.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows 98");
		conn.setInstanceFollowRedirects(true);
		conn.connect();
		InputStream strm = conn.getInputStream();
		initTokenizer(strm, encoding, null);
		metadata.setMIMEType(StringUtils.toLowerCase(input.getFile(), false));
		if (!checkPath(metadata.getMIMEType())) {
			throw new Exception("Not a text file.");
		}
		metadata.setMIMEType(conn.getContentType());
		if (metadata.getMIMEType().equals("")) {
			metadata.setMIMEType(URLConnection.guessContentTypeFromName(input.getFile()));
		} else if (!metadata.getMIMEType().startsWith("text/")) {
			throw new Exception("Not a text file");
		}
		this.name = input.toString();
		this.base = conn.getHeaderField("Content-Base");
		if (this.base == null) {
			this.base = input.toString();
		}
		int index1 = this.base.lastIndexOf('/');
		if (index1 <= 6) {
			this.base = this.base.substring(7) + '/';
		} else {
			this.base = this.base.substring(7, index1 + 1);
		}
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
	public String getLanguage() {
		if (languageComputation)
			return language;
		language = language.toLowerCase();
		// String lang2 = guessLanguage();
		// if (language.equals("pt") || language.startsWith("pt-p") ||
		// language.equals("por")
		// || language.startsWith("port")) {
		// language = "portuguese";
		// } else if (language.equals("es") || language.startsWith("es-") ||
		// language.equals("esp")
		// || language.equals("spanish")) {
		// language = "spanish";
		// } else if (lang2.equals("unknown")) {
		// if (language.equals("en") || language.equals("eng") ||
		// language.startsWith("en-")
		// || language.startsWith("engl")) {
		// language = "english";
		// } else if (language.equals("fr") || language.equals("fre") ||
		// language.equals("french")
		// || language.startsWith("fr-")) {
		// language = "french";
		// } else if (language.equals("de") || language.equals("ger") ||
		// language.equals("deu")
		// || language.equals("german") || language.startsWith("de-")) {
		// language = "german";
		// } else
		// language = lang2;
		// } else
		// language = lang2;
		languageComputation = true;
		return language;
	}

	/**
	 * Description of the Method
	 *
	 * @param modelsPath
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	public void loadLanguageModels(String modelsPath) throws Exception {
		this.modelsPath = modelsPath;
		// languageModels = new LanguageClass(modelsPath);
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
	public String getName() {
		return name;
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 * @exception IOException
	 *                Description of the Exception
	 */
	private String getNextToken() throws IOException {
		if (nextChar == -1) {
			return null;
		}
		StringBuffer token = new StringBuffer();
		tagBreak = false;
		while (nextChar != -1) {
			switch (nextChar) {
			case '\f':
			case '\t':
			case ' ':
				processSpaces(false);
				advanceScanner();
				if (!withinIgnore && token.length() != 0) {
					return token.toString();
				}
				break;
			case '\r':
			case '\n':
				processSpaces(false);
				advanceScanner();
				if (!withinIgnore && token.length() != 0) {
					return token.toString();
				}
				break;
			case '.':
				processOthers();
				advanceScanner();
				if (!withinIgnore && token.length() != 0) {
					return token.toString();
				}
				break;
			case '<':
				if (!processTag()) {
					advanceScanner();
					if (token.length() != 0) {
						tagBreak = true;
						return token.toString();
					}
				}
				break;
			default:
				if (nextChar == '&') {
					processSpecialChar();
				} else {
					processOthers();
				}
				nextChar = Character.toLowerCase((char) nextChar);
				char auxChar = StringUtils.replaceAccent((char) nextChar);
				if (!withinIgnore && ((auxChar >= 'a' && auxChar <= 'z') || (auxChar >= '0' && auxChar <= '9'))) {
					token.append((char) (nextChar));
					advanceScanner();
					if (nextChar >= '0' && nextChar <= '9') {
						boolean isNumber = true;
						try {
							// Integer i = new Integer(token.toString());
							new Integer(token.toString());
						} catch (Exception e) {
							isNumber = false;
						}
					}
				} else {
					advanceScanner();
					if (!withinIgnore && token.length() != 0) {
						return token.toString();
					}
				}
			}
		}
		return token.toString();
	}

	/**
	 * Description of the Method
	 */
	public void processData() {
		Vector aux2;
		Integer cnt;
		String token = null;
		String auxs;
		StringBuffer sb;
		HashMap aux;
		int annot[][];
		int i = 0;
		int j;
		int auxa1[];
		int auxa2[];
		do {
			try {
				do {
					token = getNextToken();
				} while (withinIgnore && token != null);
				if (token != null && token.length() > 0) {
					content.numTokens++;
					annot = (int[][]) (content.annotations.get(token));
					if (annot == null) {
						annot = new int[4][1];
						annot[0][0] = 1;
						annot[1][0] = anotation;
						annot[2][0] = position++;
						annot[3][0] = content.textBlocks.size();
					} else {
						auxa1 = new int[annot[2].length + 1];
						for (i = annot[2].length - 1; i >= 0; i--) {
							auxa1[i] = annot[2][i];
						}
						auxa1[annot[2].length] = position++;
						annot[0][0]++;
						annot[1][0] |= anotation;
						annot[2] = auxa1;
						if (annot[3][annot[3].length - 1] != content.textBlocks.size()) {
							auxa2 = new int[annot[3].length + 1];
							auxa2[annot[3].length] = content.textBlocks.size();
							for (i = annot[3].length - 1; i >= 0; i--) {
								auxa2[i] = annot[3][i];
							}
							annot[3] = auxa2;
						}
					}
					content.annotations.put(token, annot);
					updateAnnotationCount(token, anotation);
					content.terms.add(token);
					for (i = content.terms.size() - 1; i >= 0 && i >= content.terms.size() - wgramcount; i--) {
						for (j = i + 1, sb = new StringBuffer(content.terms.get(i).toString()); j < content.terms
								.size(); j++) {
							sb.append(' ');
							sb.append(content.terms.get(j).toString());
						}
						if ((cnt = (Integer) (content.wordGrams.get(sb.toString()))) == null) {
							cnt = new Integer(1);
						} else {
							cnt = new Integer(1 + cnt.intValue());
						}
						content.wordGrams.put(sb.toString(), cnt);
					}
					if (wgramcount < 5) {
						wgramcount++;
					}
					if (link != null) {
						aux = (HashMap) (content.termsLinks.get(token));
						if (aux == null) {
							aux = new HashMap();
						}
						if ((aux2 = (Vector) (aux.get(link.toString()))) == null) {
							aux2 = new Vector();
						}
						aux2.addElement(new Integer(position - 1));
						aux.put(link.toString(), aux2);
						content.termsLinks.put(token, aux);
					}
				}
			} catch (Exception e) {
				token = null;
			}
		} while (token != null && (maxTerms == -1 || content.terms.size() < maxTerms));
		if (output != null) {
			auxs = output.toString().trim();
			if (auxs.length() != 0) {
				content.textBlocks.add(auxs);
			}
		}
	}

	/**
	 * Description of the Method
	 *
	 * @param name
	 *            Description of the Parameter
	 * @param value
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	private boolean processMetaTags(String name2, String value2) {
		String value = value2;
		if (value == null) {
			return false;
		}
		String name = StringUtils.toLowerCase(name2, false);
		if (name.equals("content-type") && useEncoding) {
			int index = value.indexOf("charset=");
			if (index != -1)
				value = value.substring(index + 8);
			String aux = value.toUpperCase();
			this.processData();
			try {
				Charset charset = Charset.forName(aux);
				CharsetDecoder decoder = charset.newDecoder();
				CharsetEncoder encoder = charset.newEncoder();
				ByteBuffer bbuf = ByteBuffer.wrap(this.getContent().getOriginalContent().getBytes(aux));
				CharBuffer cbuf = decoder.decode(bbuf);
				initTokenizer(new ByteArrayInputStream(cbuf.toString().getBytes()), false);
			} catch (Exception e) {
			}
			return true;
		} else if (name.equals("refresh")) {
			String aux = StringUtils.toLowerCase(value, false);
			int index = aux.indexOf("url");
			if (index == -1) {
				return false;
			}
			value = value.substring(index + 4);
			index = value.indexOf("'");
			if (index != -1) {
				value = value.substring(index + 1);
			}
			if (value.endsWith(">")) {
				value = value.substring(0, value.length() - 1);
			}
			if (value.endsWith("\"")) {
				value = value.substring(0, value.length() - 1);
			}
			if (value.endsWith("'")) {
				value = value.substring(0, value.length() - 1);
			}
			URL url = URLUtils.normalizeURL(base, value);
			if (url != null) {
				hyperlinks.addLink(url.toString(), null);
				boolean error = false;
				try {
					if ((new Integer(aux.substring(0, aux.indexOf(';')).trim()).intValue()) == 0) {
						return false;
					}
				} catch (Exception e) {
					error = true;
				}
				try {
					if (followRedirects) {
						initTokenizer(url, this.redirects + 1);
						return true;
					} else {
						if (url != null)
							hyperlinks.addLink(url.toString(), null);
					}
				} catch (Exception e) {
					return false;
				}
			}
		}
		if (name.startsWith("dc.")) {
			name.substring(3);
		}
		if (name.equals("icbm") || name.equals("geo.position")) {
			int index = value.indexOf(";");
			if (index == -1)
				index = value.indexOf(",");
			if (index != -1) {
				metadata.setLatitude(value.substring(0, index).trim());
				metadata.setLongitude(value.substring(index + 1).trim());
			}
		} else if (name.equals("keywords")) {
			metadata.setKeywords(value);
		} else if (name.equals("generator")) {
			metadata.setGenerator(value);
		} else if (name.equals("author")) {
			metadata.setAuthor(value);
		} else if (name.equals("abstract")) {
			metadata.setAbstract(value);
		} else if (name.equals("copyright")) {
			metadata.setCopyright(value);
		} else if (name.equals("distribution")) {
			metadata.setDistribution(value);
		} else if (name.equals("expires")) {
			metadata.setExpires(value);
		} else if (name.equals("language")) {
			language = value;
		} else if (name.equals("content-language")) {
			language = value;
		} else if (name.equals("revisit")) {
			metadata.setRevisit(value);
		} else if (name.equals("robots")) {
			metadata.setRobots(value);
		} else if (name.equals("source")) {
			metadata.setSource(value);
		} else if (name.equals("creator")) {
			metadata.setCreator(value);
		} else if (name.equals("type")) {
			metadata.setMIMEType(value);
		} else if (name.equals("date")) {
			metadata.setDate(value);
		} else if (name.equals("format")) {
			metadata.setFormat(value);
		} else if (name.equals("publisher")) {
			metadata.setPublisher(value);
		} else if (name.equals("contributor")) {
			metadata.setContributor(value);
		} else if (name.equals("identifier")) {
			metadata.setIdentifier(value);
		} else if (name.equals("coverage")) {
			metadata.setCoverage(value);
		} else if (name.equals("description")) {
			metadata.setDescription(value);
		} else if (name.toString().startsWith("date")) {
			name = name.substring(4);
			if (name.length() == 0) {
				metadata.setDate(value);
			} else if (name.equals("created")) {
				metadata.setDateCreated(value);
			} else if (name.equals("available")) {
				metadata.setDateAvailable(value);
			} else if (name.equals("valid")) {
				metadata.setDateValid(value);
			} else if (name.equals("acquired")) {
				metadata.setDateAcquired(value);
			} else if (name.equals("accepted")) {
				metadata.setDateAccepted(value);
			} else if (name.equals("gathered")) {
				metadata.setDateGathered(value);
			} else if (name.equals("issued")) {
				metadata.setDateIssued(value);
			}
		} else if (name.toString().startsWith("relation")) {
			name = name.substring(8);
			if (name.length() == 0) {
				metadata.setRelation(value);
			} else if (name.equals("ispartof")) {
				metadata.setRelationIsPartOf(value);
			} else if (name.equals("isformatof")) {
				metadata.setRelationIsFormatOf(value);
			} else if (name.equals("isversionof")) {
				metadata.setRelationIsVersionOf(value);
			} else if (name.equals("references")) {
				metadata.setRelationReferences(value);
			} else if (name.equals("isbasedon")) {
				metadata.setRelationIsBasedOn(value);
			} else if (name.equals("requires")) {
				metadata.setRelationRequires(value);
			}
		}
		return false;
	}

	public MetaData getMetaData() {
		return this.metadata;
	}

	public HTMLMarkup getHTMLMarkup() {
		return this.markup;
	}

	public HyperLinks getHyperLinks() {
		return this.hyperlinks;
	}

	public ImageLinks getImages() {
		return this.images;
	}

	public Content getContent() {
		return this.content;
	}

	/**
	 * Description of the Method
	 */
	private void processOthers() {
		if (!withinIgnore)
			try {
				printSpace = printLine = false;
				output.append((char) nextChar);
				if (tagsTitle > 0) {
					metadata.appendTitle((char) nextChar);
				}
				if (tagsAnchor > 0) {
					anchorText.append((char) nextChar);
				}
			} catch (Exception e) {
			}
	}

	/**
	 * Description of the Method
	 *
	 * @param line
	 *            Description of the Parameter
	 */
	private void processSpaces(boolean line) {
		if (line && !withinIgnore && !printLine) {
			content.textBlocks.add(output.toString().trim());
			output = new StringBuffer();
			printSpace = false;
			printLine = true;
			wgramcount = 1;
			if (tagsTitle > 0) {
				metadata.appendTitle(' ');
			}
			if (tagsAnchor > 0)
				try {
					anchorText.append(' ');
				} catch (Exception e) {
				}
		} else if (!line && !withinIgnore && !printSpace && !printLine)
			try {
				output.append(' ');
				printSpace = true;
				printLine = false;
				if (tagsTitle > 0) {
					metadata.appendTitle(' ');
				}
				if (tagsAnchor > 0) {
					anchorText.append(' ');
				}
			} catch (Exception e) {
			}
	}

	/**
	 * Description of the Method
	 *
	 * @exception IOException
	 *                Description of the Exception
	 */
	private void processSpecialChar() throws IOException {
		if (withinIgnore) {
			return;
		}
		StringBuffer specialChar = new StringBuffer();
		int schar;
		do {
			specialChar.append((char) nextChar);
			advanceScanner();
		} while (nextChar != ';' && nextChar != ' ' && nextChar != '\n' && nextChar != '.' && nextChar != '\t'
				&& nextChar != '\f'
				// && nextChar != '<'
				&& nextChar != -1);
		if (nextChar == ';') {
			specialChar.append((char) nextChar);
			schar = removeSpecialChars(specialChar.toString());
			if (schar != -1) {
				if ((schar == ' ' || schar == '\t' || schar == '\f')) {
					if (!printSpace && !printLine) {
						output.append(' ');
						if (tagsTitle > 0) {
							metadata.appendTitle(' ');
						}
						if (tagsAnchor > 0) {
							anchorText.append(' ');
						}
						printLine = false;
						printSpace = true;
					}
				} else {
					output.append((char) (schar));
					if (tagsTitle > 0) {
						metadata.appendTitle((char) schar);
					}
					if (tagsAnchor > 0) {
						anchorText.append((char) schar);
					}
				}
				nextChar = schar;
			} else {
				output.append(specialChar.toString());
				if (tagsTitle > 0) {
					metadata.appendTitle(specialChar.toString());
				}
				if (tagsAnchor > 0) {
					anchorText.append(specialChar.toString());
				}
				printSpace = printLine = false;
			}
		} else {
			output.append(specialChar.toString());
			if (tagsTitle > 0) {
				metadata.appendTitle(specialChar.toString());
			}
			if (tagsAnchor > 0) {
				anchorText.append(specialChar.toString());
			}
			if (nextChar == '<') {
				if (!processTag()) {
					advanceScanner();
					tagBreak = true;
				}
				return;
			} else if ((nextChar == ' ' || nextChar == '\t' || nextChar == '\f')) {
				if (!printSpace && !printLine) {
					output.append(' ');
					if (tagsTitle > 0) {
						metadata.appendTitle(' ');
					}
					if (tagsAnchor > 0) {
						anchorText.append(' ');
					}
					printLine = false;
					printSpace = true;
				}
			} else if ((nextChar == '\r' || nextChar == '\n')) {
				if (!printSpace && !printLine) {
					content.textBlocks.add(output.toString().trim());
					output = new StringBuffer();
					if (tagsTitle > 0) {
						metadata.appendTitle(' ');
					}
					if (tagsAnchor > 0) {
						anchorText.append(' ');
					}
					wgramcount = 1;
					printLine = true;
					printSpace = false;
				}
			} else if (nextChar == '.') {
				if (tagsTitle > 0) {
					metadata.appendTitle((char) nextChar);
				}
				if (tagsAnchor > 0) {
					anchorText.append((char) nextChar);
				}
				output.append((char) nextChar);
				content.textBlocks.add(output.toString().trim());
				output = new StringBuffer();
				if (tagsTitle > 0) {
					metadata.appendTitle(' ');
				}
				if (tagsAnchor > 0) {
					anchorText.append(' ');
				}
				wgramcount = 1;
				printLine = true;
				printSpace = false;
			} else if (nextChar != -1) {
				if (tagsTitle > 0) {
					metadata.appendTitle((char) nextChar);
				}
				if (tagsAnchor > 0) {
					anchorText.append((char) nextChar);
				}
				output.append((char) nextChar);
				printSpace = printLine = false;
			}
		}
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 * @exception IOException
	 *                Description of the Exception
	 */
	private boolean processTag() throws IOException {
		boolean b1 = false;
		boolean b2 = false;
		boolean b3 = false;
		boolean b4 = false;
		boolean auxb = false;
		StringBuffer tag = new StringBuffer();
		StringBuffer lower_case = null;
		int len = 0;
		tag.append((char) (nextChar));
		do {
			advanceScanner();
			if (nextChar == ' ' || nextChar == '\n' || nextChar == '\r' || nextChar == '\f' || nextChar == '\t') {
				if (lower_case != null) {
					if (!lower_case.toString().equals("/")) {
						b3 = true;
					}
				}
				nextChar = ' ';
				if (lower_case == null || !lower_case.toString().equals("/"))
					tag.append(' ');
			} else if (nextChar != -1) {
				tag.append((char) nextChar);
				if (nextChar != '>') {
					if (lower_case == null) {
						lower_case = new StringBuffer();
						lower_case.append(Character.toLowerCase((char) nextChar));
						len++;
					} else if (!b3) {
						lower_case.append(Character.toLowerCase((char) nextChar));
						len++;
					}
				}
			}
			if (b1 && nextChar == '>') {
				b2 = tag.toString().endsWith("-->");
			} else {
				if (!b1 && (nextChar == '>' || nextChar == '<')) {
					b2 = true;
				} else if (tag.length() == 4) {
					b1 = tag.toString().equals("<!--");
				}
			}
		} while (!b2 && nextChar != -1);
		if (b1)
			return false;

		tag = new StringBuffer(tag.toString().trim());
		if (nextChar != '>') {
			if (!withinIgnore && tag.length() != 0) {
				printSpace = printLine = false;
				output.append(tag);
				if (tagsTitle > 0) {
					metadata.appendTitle(tag.toString());
				}
				if (tagsAnchor > 0) {
					anchorText.append(tag.toString());
				}
			}
			if (nextChar == '<') {
				output = new StringBuffer();
				printSpace = false;
				printLine = true;
				wgramcount = 1;
				processTag();
			}
		} else if (len != 0) {
			String print = null;
			String aux;
			String name;
			String value;
			String anchor;
			URL auxurl;
			char chr = lower_case.charAt(0);
			if (chr == 'b' && !withinIgnore) {
				if (len == 1) {
					tagsBold++;
				} else {
					lower_case = new StringBuffer(lower_case.substring(1));
					if (!withinIgnore && len == 2 && lower_case.charAt(0) == 'r') {
						b4 = true;
					} else if (lower_case.toString().equals("ody")) {
						tagsTitle--;
						b4 = true;
					} else if (lower_case.toString().equals("ase")) {
						if ((link = URLUtils.normalizeURL(base, analyseTagAux(tag.toString(), "href"))) != null) {
							base = link.toString();
							int index1 = this.base.lastIndexOf('/');
							if (index1 <= 6) {
								this.base = this.base.substring(7) + '/';
							} else {
								this.base = this.base.substring(7, index1 + 1);
							}
						}
					} else if (lower_case.toString().equals("ig")) {
						tagsBig++;
					}
				}
			} else if (chr == 'p' && !withinIgnore && len == 1) {
				b4 = true;
			} else if (chr == '/') {
				chr = lower_case.charAt(1);
				if (len == 3 && chr == 'h' && !withinIgnore) {
					switch (lower_case.charAt(2)) {
					case '1':
						tagsHeading1--;
						break;
					case '2':
						tagsHeading2--;
						break;
					case '3':
						tagsHeading3--;
						break;
					case '4':
						tagsHeading4--;
						break;
					case '5':
						tagsHeading5--;
						break;
					case '6':
						tagsHeading6--;
						break;
					default:
					}
				} else if (len > 1 && chr == 's') {
					lower_case = new StringBuffer(lower_case.substring(2));
					if (lower_case.toString().equals("mall")) {
						tagsSmall--;
					} else if (lower_case.toString().equals("trong") && !withinIgnore) {
						tagsStrong--;
					} else if (lower_case.toString().equals("tyle")) {
						withinIgnore = false;
					} else if (lower_case.toString().equals("cript")) {
						withinIgnore = false;
					} else if (lower_case.toString().equals("elect")) {
						lastSelectName = "";
					}
				} else if (chr == 'b' && !withinIgnore) {
					if (len == 2) {
						tagsBold--;
					} else {
						lower_case = new StringBuffer(lower_case.substring(2));
						if (lower_case.toString().equals("ig")) {
							tagsBig--;
						} else if (lower_case.toString().equals("ody")) {
							tagsTitle--;
						}
					}
				} else if (!withinIgnore) {
					lower_case = new StringBuffer(lower_case.substring(1));
					if (chr == 'i' && len == 2) {
						tagsItalic--;
					} else if (chr == 'p' && len == 2) {
						b4 = true;
					} else if (chr == 'a' && len == 2) {
						if (tagsAnchor > 0)
							tagsAnchor--;

						if (link != null) {
							hyperlinks.addLink(link.toString(), anchorText.toString());
						}
						anchorText = new StringBuffer();
						link = null;
					} else if (lower_case.toString().equals("em")) {
						tagsEmphasize--;
					} else if (lower_case.toString().equals("ul") || lower_case.toString().equals("li")) {
						b4 = true;
					} else if (lower_case.toString().equals("form") && lastFormURL.length() != 0) {
						/*
						 * char addchr = '&'; if (lastFormURL.indexOf("?") == -1) { addchr = '?'; } for
						 * (int i = 0; i < lastFormHidden.size(); i++) { lastFormURL += addchr +
						 * lastFormHidden.elementAt(i).toString(); addchr = '&'; } if
						 * (lastSubmit.length() != 0) { lastFormURL += addchr + lastSubmit; addchr =
						 * '&'; } auxurl = URLUtils.normalizeURL(base, lastFormURL); anchor = (String)
						 * (links.get(auxurl.toString())); if (anchor == null) { anchor = ""; }
						 * links.put(auxurl, anchor); for (int i = 0; i < lastFormSelect.size(); i++) {
						 * auxurl = URLUtils.normalizeURL(base, lastFormURL + addchr +
						 * lastFormSelect.elementAt(i).toString()); anchor = (String)
						 * (links.get(auxurl.toString())); if (anchor == null) { anchor = ""; }
						 * links.put(auxurl, anchor); }
						 */
						lastFormURL = new String();
						lastSubmit = new String();
						lastSelectName = new String();
						lastFormHidden = new Vector();
						lastFormSelect = new Vector();
					} else if (lower_case.toString().equals("title") || lower_case.toString().equals("head")) {
						tagsTitle--;
						b4 = true;
					}
				}
			} else if (chr == 'h' && len == 2 && !withinIgnore) {
				switch (lower_case.charAt(1)) {
				case '1':
					tagsHeading1++;
					break;
				case '2':
					tagsHeading2++;
					break;
				case '3':
					tagsHeading3++;
					break;
				case '4':
					tagsHeading4++;
					break;
				case '5':
					tagsHeading5++;
					break;
				case '6':
					tagsHeading6++;
				default:
				}
			} else if (chr == 's') {
				lower_case = new StringBuffer(lower_case.substring(1));
				if (lower_case.toString().equals("mall") && !withinIgnore) {
					tagsSmall++;
				} else if (lower_case.toString().equals("tyle")) {
					withinIgnore = true;
				} else if (lower_case.toString().equals("trong") && !withinIgnore) {
					tagsStrong++;
				} else if (lower_case.toString().equals("cript")) {
					withinIgnore = true;
				} else if (lower_case.toString().equals("elect")) {
					if ((lastSelectName = analyseTagAux(tag.toString(), "name")) == null) {
						lastSelectName = "";
					}
				}
			} else if (chr == 'i' && !withinIgnore) {
				lower_case = new StringBuffer(lower_case.substring(1));
				if (len == 1) {
					tagsItalic++;
				} else if (lower_case.toString().equals("mg")) {
					print = analyseTagAux(tag.toString(), "alt");
					print = new String((print == null) ? "" : removeSpecialChars2(print));
					auxurl = URLUtils.normalizeURL(base, analyseTagAux(tag.toString(), "src"));
					if (auxurl != null) {
						images.addImage(auxurl.toString(), print);
					}
					if (tagsAnchor > 0 && link != null) {
						hyperlinks.addLink(link.toString(), print);
					}
				} else if (lower_case.toString().equals("nput") && lastFormURL.length() != 0) {
					if ((aux = analyseTagAux(tag.toString(), "type")) != null) {
						aux = StringUtils.toLowerCase(aux, false);
						if (aux.equals("hidden")) {
							name = analyseTagAux(tag.toString(), "name");
							value = analyseTagAux(tag.toString(), "value");
							if (name != null && value != null) {
								lastFormHidden.add(name + "=" + value);
							}
						} else if (aux.equals("radio") || aux.equals("checkbox")) {
							name = analyseTagAux(tag.toString(), "name");
							value = analyseTagAux(tag.toString(), "value");
							if (value == null) {
								value = "on";
							}
							if (name != null) {
								lastFormHidden.add(name + "=" + value);
							}
						} else if (aux.equals("submit")) {
							name = analyseTagAux(tag.toString(), "name");
							value = analyseTagAux(tag.toString(), "value");
							if (value == null) {
								value = "Submit";
							}
							if (name != null) {
								lastSubmit = name + "=" + value;
							}
						}
					}
				}
			} else if (chr == 'f' && !withinIgnore) {
				lower_case = new StringBuffer(lower_case.substring(1));
				if (lower_case.toString().equals("orm")) {
					auxurl = URLUtils.normalizeURL(base, analyseTagAux(tag.toString(), "action"));
					if (auxurl != null) {
						hyperlinks.addLink(auxurl.toString(), null);
						lastFormURL = auxurl.toString();
						lastFormHidden = new Vector();
						lastFormSelect = new Vector();
					}
				} else if (lower_case.toString().equals("rame")) {
					auxurl = URLUtils.normalizeURL(base, analyseTagAux(tag.toString(), "src"));
					if (auxurl != null) {
						hyperlinks.addLink(auxurl.toString(), null);
					}
				}
			} else if (chr == 'a' && !withinIgnore) {
				if (len == 1) {
					tagsAnchor++;
					link = URLUtils.normalizeURL(base, analyseTagAux(tag.toString(), "href"));
					if (link != null) {
						hyperlinks.addLink(link.toString(), null);
					}
					anchorText = new StringBuffer();
					print = analyseTagAux(tag.toString(), "alt");
				} else if (lower_case.toString().equals("area")) {
					auxurl = URLUtils.normalizeURL(base, analyseTagAux(tag.toString(), "href"));
					if (auxurl != null) {
						hyperlinks.addLink(auxurl.toString(), null);
					}
				}
			} else if (lower_case.toString().equals("ul") && !withinIgnore) {
				b4 = true;
			} else if (lower_case.toString().equals("li") && !withinIgnore) {
				b4 = true;
			} else if (lower_case.toString().equals("em") && !withinIgnore) {
				tagsEmphasize++;
			} else if (lower_case.toString().equals("embed")) {
				auxurl = URLUtils.normalizeURL(base, analyseTagAux(tag.toString(), "src"));
				if (auxurl != null) {
					hyperlinks.addLink(auxurl.toString(), null);
				}
			} else if (lower_case.toString().equals("title")) {
				tagsTitle++;
			} else if (lower_case.toString().equals("meta")) {
				if ((aux = analyseTagAux(tag.toString(), "name")) != null) {
					processMetaTags(aux, analyseTagAux(tag.toString(), "content"));
				} else if ((aux = analyseTagAux(tag.toString(), "http-equiv")) != null) {
					auxb = processMetaTags(aux, analyseTagAux(tag.toString(), "content"));
				}
			} else if (lower_case.toString().equals("option") && !withinIgnore) {
				value = null;
				if (lastSelectName.length() != 0 && (value = analyseTagAux(tag.toString(), "value")) != null) {
					lastFormSelect.add(lastSelectName + "=" + value);
				}
			} else if (lower_case.toString().equals("link") && !withinIgnore) {
				auxurl = URLUtils.normalizeURL(base, analyseTagAux(tag.toString(), "href"));
				if (auxurl != null) {
					String rel = analyseTagAux(tag.toString(), "rel");
					String type = analyseTagAux(tag.toString(), "type");
					String title = analyseTagAux(tag.toString(), "title");
					anchor = "";
					if (rel != null && rel.length() != 0) {
						anchor = rel;
						if (type != null && rel.toLowerCase().equals("alternate")
								&& (type.toLowerCase().indexOf("rss") != -1
										|| type.toLowerCase().indexOf("rdf") != -1)) {
							metadata.setLinkRSS(auxurl.toString());
						}
					}
					if (title != null && title.length() != 0) {
						if (anchor.length() != 0)
							anchor += ";";
						anchor += title;
					}
					hyperlinks.addLink(auxurl.toString(), anchor);
				}
			}
			if (withinIgnore) {
				return false;
			}
			anotation = 0x00000000;
			if (tagsTitle > 0) {
				anotation |= 1;
			}
			if (tagsBold > 0) {
				anotation |= 2;
			}
			if (tagsBig > 0) {
				anotation |= 4;
			}
			if (tagsHeading1 > 0) {
				anotation |= 8;
			}
			if (tagsHeading2 > 0) {
				anotation |= 16;
			}
			if (tagsHeading3 > 0) {
				anotation |= 32;
			}
			if (tagsHeading4 > 0) {
				anotation |= 64;
			}
			if (tagsHeading5 > 0) {
				anotation |= 128;
			}
			if (tagsHeading6 > 0) {
				anotation |= 256;
			}
			if (tagsEmphasize > 0) {
				anotation |= 512;
			}
			if (tagsStrong > 0) {
				anotation |= 1024;
			}
			if (tagsItalic > 0) {
				anotation |= 2048;
			}
			if (tagsAnchor > 0) {
				anotation |= 4096;
			}
			if (tagsSmall > 0) {
				anotation |= 8192;
			}
			if (b4) {
				if (!printLine) {
					content.textBlocks.add(output.toString().trim());
					output = new StringBuffer();
					printSpace = false;
					printLine = true;
					wgramcount = 1;
				}
			} else if (!auxb) {
				if (!printSpace && !printLine) {
					output.append(' ');
					if (tagsAnchor > 0) {
						anchorText.append(' ');
					}
					printSpace = true;
					printLine = false;
				}
				if (print == null) {
					return false;
				}
				print = print.trim();
				if (!print.toLowerCase().equals("")) {
					output.append(removeSpecialChars2(print));
					output.append(' ');
					if (tagsAnchor > 0) {
						anchorText.append(removeSpecialChars2(print));
						anchorText.append(' ');
					}
					printSpace = true;
					printLine = false;
				}
			}
		}
		return auxb;
	}

	/**
	 * Description of the Method
	 *
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	private int removeSpecialChars(String s) {
		int i;
		int j;
		int k;
		int c = -1;
		boolean error = false;
		String substr = null;
		if ((i = s.indexOf('&')) != -1) {
			if ((j = s.indexOf(';')) > i) {
				if ((k = s.indexOf('#')) == i + 1) {
					try {
						c = (new Integer(s.substring(k + 1, j))).intValue();
					} catch (Exception e) {
						error = true;
					}
				} else {
					substr = (String) (escapeMap.get(s.substring(i, j + 1)));
					if (substr != null) {
						c = substr.charAt(0);
					}
				}
			}
		}
		return c;
	}

	/**
	 * Description of the Method
	 *
	 * @param s
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	private String removeSpecialChars2(String s) {
		int i;
		int j;
		int k;
		int c = -1;
		if ((i = s.indexOf('&')) != -1) {
			if ((j = s.indexOf(';')) > i) {
				String aux = s.substring(i, j + 1);
				if ((k = s.indexOf('#')) == i + 1) {
					try {
						c = (new Integer(s.substring(k + 1, j))).intValue();
					} catch (Exception e) {
						c = -1;
					}
				} else {
					String substr = (String) (escapeMap.get(s.substring(i, j + 1)));
					if (substr != null) {
						c = substr.charAt(0);
					}
				}
				if (c != -1) {
					aux = "" + ((char) c);
				}
				return s.substring(0, i) + aux + removeSpecialChars2(s.substring(j + 1));
			}
		}
		return s;
	}

	public void setMaxTerms(int max) {
		this.maxTerms = max;
	}

	/**
	 * Updates the internal count regarding the HTML markup information for specific
	 * terms
	 * 
	 * @param term
	 *            The term for which the markup information is to be updated
	 * @param annotation
	 *            A bit flag with the markup information assigned to the term
	 */
	private void updateAnnotationCount(String term, int annotation) {
		int aux[] = (int[]) (content.annotationCount.get(term));
		if (aux == null) {
			aux = new int[15];
			for (int i = 0; i < 15; i++)
				aux[i] = 0;
		}
		if ((annotation & 1) != 0x00000000)
			aux[0]++;
		if ((annotation & 2) != 0x00000000)
			aux[1]++;
		if ((annotation & 4) != 0x00000000)
			aux[2]++;
		if ((annotation & 8) != 0x00000000)
			aux[3]++;
		if ((annotation & 16) != 0x00000000)
			aux[4]++;
		if ((annotation & 32) != 0x00000000)
			aux[5]++;
		if ((annotation & 64) != 0x00000000)
			aux[6]++;
		if ((annotation & 128) != 0x00000000)
			aux[7]++;
		if ((annotation & 256) != 0x00000000)
			aux[8]++;
		if ((annotation & 512) != 0x00000000)
			aux[9]++;
		if ((annotation & 1024) != 0x00000000)
			aux[10]++;
		if ((annotation & 2048) != 0x00000000)
			aux[11]++;
		if ((annotation & 4096) != 0x00000000)
			aux[12]++;
		if ((annotation & 8192) != 0x00000000)
			aux[13]++;
		if ((annotation & 16384) != 0x00000000)
			aux[14]++;
		content.annotationCount.put(term, aux);
	}

	public Iterator getStems() {
		HashMap m = new HashMap();
		/*
		 * Iterator words = content.getTerms(); String token; try { Object [] emptyArgs
		 * = new Object[0]; Class stemClass = Class.forName("net.sf.snowball.ext." +
		 * getLanguage() + "Stemmer"); SnowballProgram stemmer = (SnowballProgram)
		 * stemClass.newInstance(); Method stemMethod = stemClass.getMethod("stem", new
		 * Class[0]); while (words.hasNext()) { token = words.next().toString();
		 * stemmer.setCurrent(token); stemMethod.invoke(stemmer, emptyArgs);
		 * m.put(stemmer.getCurrent(),null); } } catch ( Exception e) {}
		 */
		return m.keySet().iterator();
	}

}
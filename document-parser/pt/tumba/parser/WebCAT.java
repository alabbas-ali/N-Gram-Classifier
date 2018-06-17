package pt.tumba.parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import pt.tumba.parser.bib.BIB2HTML;
import pt.tumba.parser.doc.DOC2HTML;
import pt.tumba.parser.dvi.DVI2HTML;
import pt.tumba.parser.pdf.PDF2HTML;
import pt.tumba.parser.ppt.PPT2HTML;
import pt.tumba.parser.ps.PS2HTML;
//import pt.tumba.parser.rtf.RTF2HTML;
import pt.tumba.parser.unrtf.RTF2HTML;
import pt.tumba.parser.swf.SWF2HTML;
import pt.tumba.parser.tex.TEX2HTML;
import pt.tumba.parser.txt.TXT2HTML;
import pt.tumba.parser.xls.XLS2HTML;

/**
 * The main class of the WebCAT package. Dumps the metadata information to the
 * standard output, in the RDF format.
 *
 * @author Bruno Martins
 */
public class WebCAT {

	/**
	 * The single instance of this class
	 */
	private static final WebCAT _theInstance = new WebCAT();

	/**
	 * The constructor of the class
	 */
	private WebCAT() {
	}

	/**
	 * This class implements the singleton pattern. Within a JVM, there exists a
	 * single instance of this class.
	 * 
	 * @return The single instance of this class
	 */
	public static WebCAT getInstance() {
		return _theInstance;
	}

	public static String encodeSGML(String input) {
		char chars_replace[] = { '"', '&', '<', '>', '\\' };
		String string_replace[] = { "&quot;", "&amp;", "&lt;", "&gt;", "&apos;" };
		String saux = "";
		int len = input.length();
		char array_input[] = new char[len];
		input.getChars(0, len, array_input, 0);
		int i, j;
		boolean replace;
		for (i = 0; i < len; i++) {
			replace = false;
			for (j = 0; j < chars_replace.length && !replace; j++)
				if (array_input[i] == chars_replace[j]) {
					saux += string_replace[j];
					replace = true;
				}
			if (!replace)
				saux += array_input[i];
		}
		return saux;
	}

	/**
	 * Returns a string with an RDF representation of the parser object
	 *
	 * @param parser
	 *            The parser object, with the metadata for a given resource
	 * @return A string with the RDF representation of the meta-data.
	 */
	public static String getRDFData(HTMLParser parser) {
		StringBuffer sbuf = new StringBuffer();
		int pos[];
		int i;
		Iterator it1;
		Iterator it2;
		URL u;
		String s;
		String s2;
		sbuf.append("<?xml version='1.0' ?>\n");
		sbuf.append("<r:RDF xmlns:r='http://www.w3.org/1999/02/22-rdf-syntax-ns#'\n");
		sbuf.append("	   xmlns:d='http://purl.org/dc/elements/1.1/'\n");
		sbuf.append("	   xmlns:s='http://www.w3.org/2000/01/rdf-schema#'\n");
		sbuf.append("	   xmlns:h='http://www.w3.org/1999/xx/http#'\n");
		sbuf.append("	   xmlns:t='http://purl.org/dc/terms/'>\n");
		sbuf.append("<r:Description r:about=\"" + encodeSGML(parser.getName()) + "\">\n");
		if (!parser.getMetaData().getTitle().equals("")) {
			sbuf.append("<d:Title>" + encodeSGML(parser.getMetaData().getTitle()) + "</d:Title>\n");
		}
		if (!parser.getMetaData().getDescription().equals("") || !parser.getMetaData().getKeywords().equals("")) {
			sbuf.append("<t:abstract>\n");
			sbuf.append("<r:Alt>\n");
			sbuf.append(
					"<r:li r:ID='DocumentAbstract'>" + encodeSGML(parser.getMetaData().getDescription()) + "</r:li>\n");
			sbuf.append(
					"<r:li r:ID='DocumentKeywords'>" + encodeSGML(parser.getMetaData().getKeywords()) + "</r:li>\n");
			sbuf.append("</r:Alt>\n");
			sbuf.append("</t:abstract>\n");
		}
		if (!parser.getMetaData().getAuthor().equals("") || !parser.getMetaData().getCreator().equals("")) {
			sbuf.append("<d:Creator>\n");
			sbuf.append("<r:Alt>\n");
			if (!parser.getMetaData().getAuthor().equals(""))
				sbuf.append("<r:li>" + encodeSGML(parser.getMetaData().getAuthor()) + "</r:li>\n");
			if (!parser.getMetaData().getCreator().equals(""))
				sbuf.append("<r:li>" + encodeSGML(parser.getMetaData().getCreator()) + "</r:li>\n");
			sbuf.append("</r:Alt>\n");
			sbuf.append("</d:Creator>\n");
		}
		if (!parser.getMetaData().getCopyright().equals("")) {
			sbuf.append("<d:Rights>" + encodeSGML(parser.getMetaData().getCopyright()) + "</d:Rights>\n");
		}
		if (!parser.getLanguage().equals("")) {
			sbuf.append("<d:Language>" + encodeSGML(parser.getLanguage()) + "</d:Language>\n");
		}
		if (!parser.getMetaData().getMIMEType().equals("")) {
			sbuf.append("<d:Type>" + encodeSGML(parser.getMetaData().getMIMEType()) + "</d:Type>\n");
		}
		if (!parser.getMetaData().getFormat().equals("")) {
			sbuf.append("<d:Format>" + encodeSGML(parser.getMetaData().getFormat()) + "</d:Format>\n");
		}
		if (!parser.getMetaData().getPublisher().equals("")) {
			sbuf.append("<d:Publisher>" + encodeSGML(parser.getMetaData().getPublisher()) + "</d:Publisher>\n");
		}
		if (!parser.getMetaData().getContributor().equals("")) {
			sbuf.append("<d:Contributor>" + encodeSGML(parser.getMetaData().getContributor()) + "</d:Contributor>\n");
		}
		if (!parser.getMetaData().getSource().equals("")) {
			sbuf.append("<d:Source>" + encodeSGML(parser.getMetaData().getSource()) + "</d:Source>\n");
		}
		if (!parser.getMetaData().getDate().equals("")) {
			sbuf.append("<d:Date>" + encodeSGML(parser.getMetaData().getDate()) + "</d:Date>\n");
		}

		if (!parser.getMetaData().getDateCreated().equals("")) {
			sbuf.append("<t:created>" + encodeSGML(parser.getMetaData().getDateCreated()) + "</t:created>\n");
		}
		if (!parser.getMetaData().getDateAvailable().equals("")) {
			sbuf.append("<t:available>" + encodeSGML(parser.getMetaData().getDateAvailable()) + "</t:available>\n");
		}
		if (!parser.getMetaData().getDateValid().equals("") || !parser.getMetaData().getExpires().equals("")
				|| !parser.getMetaData().getRevisit().equals("")) {
			sbuf.append("<t:valid>\n");
			sbuf.append("<r:Alt>\n");
			if (!parser.getMetaData().getDateValid().equals(""))
				sbuf.append("<r:li r:ID='DateValid'>" + encodeSGML(parser.getMetaData().getDateValid()) + "</r:li>\n");
			if (!parser.getMetaData().getExpires().equals(""))
				sbuf.append("<r:li r:ID='DateExpires'>" + encodeSGML(parser.getMetaData().getExpires()) + "</r:li>\n");
			if (!parser.getMetaData().getRevisit().equals(""))
				sbuf.append("<r:li r:ID='DateRevisit'>" + encodeSGML(parser.getMetaData().getRevisit()) + "</r:li>\n");
			sbuf.append("</r:Alt>\n");
			sbuf.append("</t:valid>\n");
		}
		if (!parser.getMetaData().getDateAcquired().equals("")) {
			sbuf.append("<t:acquired>" + encodeSGML(parser.getMetaData().getDateAcquired()) + "</t:acquired>\n");
		}
		if (!parser.getMetaData().getDateAccepted().equals("")) {
			sbuf.append("<t:accepted>" + encodeSGML(parser.getMetaData().getDateAccepted()) + "</t:accepted>\n");
		}
		if (!parser.getMetaData().getDateGathered().equals("")) {
			sbuf.append("<t:gathered>" + encodeSGML(parser.getMetaData().getDateGathered()) + "</t:gathered>\n");
		}
		if (!parser.getMetaData().getDateIssued().equals("")) {
			sbuf.append("<t:issued>" + encodeSGML(parser.getMetaData().getDateIssued()) + "</t:issued>\n");
		}
		sbuf.append("<d:Relation>\n");
		sbuf.append("<r:Alt>\n");
		if (!parser.getMetaData().getRelation().equals(""))
			sbuf.append("<r:li>" + encodeSGML(parser.getMetaData().getRelation()) + "</r:li>\n");
		if (!parser.getMetaData().getLinkRSS().equals(""))
			sbuf.append("<r:li r:ID='LinkRSS'>" + encodeSGML(parser.getMetaData().getLinkRSS()) + "</r:li>\n");
		sbuf.append("</r:Alt>\n");
		sbuf.append("</d:Relation>\n");
		if (!parser.getMetaData().getRelationIsPartOf().equals("")) {
			sbuf.append("<t:isPartOf>" + encodeSGML(parser.getMetaData().getRelationIsPartOf()) + "</t:isPartOf>\n");
		}
		if (!parser.getMetaData().getRelationIsFormatOf().equals("")) {
			sbuf.append(
					"<t:isFormatOf>" + encodeSGML(parser.getMetaData().getRelationIsFormatOf()) + "</t:isFormatOf>\n");
		}
		if (!parser.getMetaData().getRelationIsVersionOf().equals("")) {
			sbuf.append("<t:isVersionOf>" + encodeSGML(parser.getMetaData().getRelationIsVersionOf())
					+ "</t:isVersionOf>\n");
		}
		if (!parser.getMetaData().getRelationIsBasedOn().equals("")) {
			sbuf.append("<t:isBasedOn>" + encodeSGML(parser.getMetaData().getRelationIsBasedOn()) + "</t:isBasedOn>\n");
		}
		if (!parser.getMetaData().getRelationRequires().equals("")) {
			sbuf.append("<t:requires>" + encodeSGML(parser.getMetaData().getRelationRequires()) + "</t:requires>\n");
		}
		sbuf.append("<t:references>\n");
		sbuf.append("<r:Alt>\n");
		if (!parser.getMetaData().getRelationReferences().equals("")) {
			sbuf.append("<r:li>" + encodeSGML(parser.getMetaData().getRelationReferences()) + "</r:li>\n");
		}
		it1 = parser.getHyperLinks().getURLs();
		if (it1.hasNext()) {
			sbuf.append("<r:li r:ID=\"#DocumentLinks\">\n");
			sbuf.append("<r:Bag>\n");
			do {
				u = (URL) (it1.next());
				sbuf.append("<r:li r:resource=\"" + encodeSGML(u.toString()));
				s = parser.getHyperLinks().getAnchorText(u);
				sbuf.append((s.equals("")) ? ("\" />\n") : ("\" r:label=\"" + encodeSGML(s) + "\" />\n"));
			} while (it1.hasNext());
			sbuf.append("</r:Bag>\n");
			sbuf.append("</r:li>\n");
		}
		it1 = parser.getImages().getImages();
		if (it1.hasNext()) {
			sbuf.append("<r:li r:ID=\"#DocumentImages\">\n");
			sbuf.append("<r:Bag>\n");
			do {
				s = (String) (it1.next());
				sbuf.append("<r:li r:resource=\"" + encodeSGML(s));
				s = parser.getImages().getCaption(s);
				sbuf.append((s.equals("")) ? ("\" />\n") : ("\" r:label=\"" + encodeSGML(s) + "\" />\n"));
			} while (it1.hasNext());
			sbuf.append("</r:Bag>\n");
			sbuf.append("</r:li>\n");
		}
		sbuf.append("</r:Alt>\n");
		sbuf.append("</t:references>\n");

		sbuf.append("<d:description>\n");
		sbuf.append("<r:Alt>\n");
		String blocks[] = parser.getContent().getTextBlocks();
		if (blocks.length != 0) {
			sbuf.append("<r:li r:ID=\"DocumentSentences\">\n");
			sbuf.append("<r:Seq>\n");
			for (i = 0; i < blocks.length; i++) {
				sbuf.append("<r:li>" + encodeSGML(blocks[i]) + "</r:li>\n");
			}
			sbuf.append("</r:Seq>\n");
			sbuf.append("</r:li>\n");
		}
		sbuf.append("<r:li r:ID='OriginalContent'>\n");
		sbuf.append("<h:ContentType>text/html</h:ContentType>\n");
		sbuf.append("<h:ContentLength>" + parser.getContent().getOriginalContent().length() + "</h:ContentLength>\n");
		// sbuf.append("<h:Body r:parseType='Literal'>\n");
		// sbuf.append(parser.getContent().getOriginalContent());
		// sbuf.append("</h:Body>\n");
		sbuf.append("</r:li>\n");
		sbuf.append("<r:li r:ID='FilteredText'>\n");
		sbuf.append("<h:ContentType>text/plain</h:ContentType>\n");
		sbuf.append("<h:ContentLength>" + parser.getContent().getFilteredText().length() + "</h:ContentLength>\n");
		sbuf.append("<h:Body r:parseType='Literal'>\n");
		sbuf.append(parser.getContent().getFilteredText());
		sbuf.append("</h:Body>\n");
		sbuf.append("</r:li>\n");
		sbuf.append("</r:Alt>\n");
		sbuf.append("</d:description>\n");
		sbuf.append("<d:Coverage>\n");
		sbuf.append("<r:Alt>\n");
		if (!parser.getMetaData().getCoverage().equals("")) {
			sbuf.append("<r:li r:ID='Coverage'>" + encodeSGML(parser.getMetaData().getCoverage()) + "</r:li>\n");
		}
		if (!parser.getMetaData().getLatitude().equals("")) {
			sbuf.append("<r:li r:ID='Latitude'>" + encodeSGML(parser.getMetaData().getLatitude()) + "</r:li>\n");
		}
		if (!parser.getMetaData().getLongitude().equals("")) {
			sbuf.append("<r:li r:ID='Longitude'>" + encodeSGML(parser.getMetaData().getLongitude()) + "</r:li>\n");
		}
		sbuf.append("</r:Alt>\n");
		sbuf.append("</d:Coverage>\n");
		sbuf.append("<d:Identifier>\n");
		sbuf.append("<r:Alt>\n");
		if (!parser.getMetaData().getIdentifier().equals("")) {
			sbuf.append("<r:li>" + encodeSGML(parser.getMetaData().getIdentifier()) + "</r:li>\n");
		}
		sbuf.append("<r:li r:ID='DocumentHashCode'>" + parser.getContent().getHashCode() + "</r:li>\n");
		sbuf.append("</r:Alt>\n");
		sbuf.append("</d:Identifier>\n");

		// ======== this part was commeted ==========//
		if (!parser.getMetaData().getGenerator().equals("")) {
			sbuf.append("  <d:Generator>" + encodeSGML(parser.getMetaData().getGenerator()) + "</d:Generator>\n");
		}
		if (!parser.getMetaData().getDistribution().equals("")) {
			sbuf.append(
					"  <d:Distribution>" + encodeSGML(parser.getMetaData().getDistribution()) + "</d:Distribution>\n");
		}

		if (!parser.getMetaData().getRobots().equals("")) {
			sbuf.append("  <d:Robots>" + encodeSGML(parser.getMetaData().getRobots()) + "</d:Robots>\n");
		}
		it1 = parser.getContent().getTerms();
		if (it1.hasNext()) {
			sbuf.append("  <r:Description r:ID=\"#DocumentTerms\">\n");
			sbuf.append("   <r:Bag>\n");
			do {
				s = (String) (it1.next());
				sbuf.append("<r:li r:resource=\"" + encodeSGML(s) + "\">\n");
				sbuf.append("<r:Description r:ID=\"#TermFrequency\">" + parser.getContent().getFrequency(s)
						+ "</r:Description>\n");
				sbuf.append("<r:Description r:ID=\"#TermMean\">" + parser.getContent().getMean(s)
						+ "</r:Description>\n");
				sbuf.append("<r:Description r:ID=\"#TermVariance\">" + parser.getContent().getVariance(s)
						+ "</r:Description>\n");
				sbuf.append("<r:Description r:ID=\"#TermTScore\">" + parser.getContent().getTScore(s)
						+ "</r:Description>\n");
				sbuf.append("<r:Description r:ID=\"#TermFlags\">" + parser.getContent().getTermInfo(s)
						+ "</r:Description>\n");
				if (parser.getHTMLMarkup().isTitle(s)) {
					sbuf.append("<r:Property r:ID=\"#TermIsTitle\" >" + parser.getHTMLMarkup().getTitleCount(s)
							+ "</r:Property>\n");
				}
				if (parser.getHTMLMarkup().isBold(s)) {
					sbuf.append("<r:Property r:ID=\"#TermIsBold\" >" + parser.getHTMLMarkup().getBoldCount(s)
							+ "</r:Property>\n");
				}
				if (parser.getHTMLMarkup().isBig(s)) {
					sbuf.append("<r:Property r:ID=\"#TermIsBig\" >" + parser.getHTMLMarkup().getBigCount(s)
							+ "</r:Property>\n");
				}
				if (parser.getHTMLMarkup().isHeading1(s)) {
					sbuf.append("<r:Property r:ID=\"#TermIsHeading1\" >"
							+ parser.getHTMLMarkup().getHeading1Count(s) + "</r:Property>\n");
				}
				if (parser.getHTMLMarkup().isHeading2(s)) {
					sbuf.append("<r:Property r:ID=\"#TermIsHeading2\" >"
							+ parser.getHTMLMarkup().getHeading2Count(s) + "</r:Property>\n");
				}
				if (parser.getHTMLMarkup().isHeading3(s)) {
					sbuf.append("<r:Property r:ID=\"#TermIsHeading3\" >"
							+ parser.getHTMLMarkup().getHeading3Count(s) + "</r:Property>\n");
				}
				if (parser.getHTMLMarkup().isHeading4(s)) {
					sbuf.append("<r:Property r:ID=\"#TermIsHeading4\" >"
							+ parser.getHTMLMarkup().getHeading4Count(s) + "</r:Property>\n");
				}
				if (parser.getHTMLMarkup().isHeading5(s)) {
					sbuf.append("<r:Property r:ID=\"#TermIsHeading5\" >"
							+ parser.getHTMLMarkup().getHeading5Count(s) + "</r:Property>\n");
				}
				if (parser.getHTMLMarkup().isHeading6(s)) {
					sbuf.append("<r:Property r:ID=\"#TermIsHeading6\" >"
							+ parser.getHTMLMarkup().getHeading6Count(s) + "</r:Property>\n");
				}
				if (parser.getHTMLMarkup().isEmphasize(s)) {
					sbuf.append("<r:Property r:ID=\"#TermIsEmphasize\" >"
							+ parser.getHTMLMarkup().getEmphasizeCount(s) + "</r:Property>\n");
				}
				if (parser.getHTMLMarkup().isStrong(s)) {
					sbuf.append("<r:Property r:ID=\"#TermIsStrong\" >" + parser.getHTMLMarkup().getStrongCount(s)
							+ "</r:Property>\n");
				}
				if (parser.getHTMLMarkup().isItalic(s)) {
					sbuf.append("<r:Property r:ID=\"#TermIsItalic\" >" + parser.getHTMLMarkup().getItalicCount(s)
							+ "</r:Property>\n");
				}
				if (parser.getHTMLMarkup().isAnchor(s)) {
					sbuf.append("<r:Property r:ID=\"#TermIsAnchor\" >" + parser.getHTMLMarkup().getAnchorCount(s)
							+ "</r:Property>\n");
				}
				if (parser.getHTMLMarkup().isSmall(s)) {
					sbuf.append("<r:Property r:ID=\"#TermIsSmall\" >" + parser.getHTMLMarkup().getSmallCount(s)
							+ "</r:Property>\n");
				}
				if (parser.getHTMLMarkup().isMeta(s)) {
					sbuf.append("<r:Property r:ID=\"#TermIsMeta\" >" + parser.getHTMLMarkup().getMetaCount(s)
							+ "</r:Property>\n");
				}
				sbuf.append("<r:Description r:ID=\"#TermPositions\"><r:Seq>");
				pos = parser.getContent().getPositions(s);
				for (i = 0; i < pos.length; i++) {
					sbuf.append("<r:li>" + pos[i] + "</r:li>");
				}
				sbuf.append("</r:Seq></r:Description>\n");
				it2 = parser.getLinks(s);

				if (it2.hasNext()) {
					sbuf.append("<r:Description r:ID=\"#TermLinks\"><r:Bag>");
					do {
						s2 = it2.next().toString();
						pos = parser.getContent().getPositions(s, s2);
						if (pos.length > 0) {
							sbuf.append("<r:li r:resource=\"" + encodeSGML(s2) + "\"><r:Bag>");
							for (i = 0; i < pos.length; i++) {
								sbuf.append("<r:li>" + pos[i] + "</r:li>");
							}
							sbuf.append("</r:Bag></r:li>");
						}
					} while (it2.hasNext());
					sbuf.append("</r:Bag></r:Description>\n");
				}
				sbuf.append("</r:li>\n");
			} while (it1.hasNext());
			sbuf.append("</r:Bag>\n");
			sbuf.append("</r:Description>\n\n");
			sbuf.append("\n");
		}
		//it1 = parser.getNGrams();
		if (it1.hasNext()) {
			sbuf.append("  <r:Description r:ID=\"#DocumentNGrams\">\n");
			sbuf.append("   <r:Bag>\n");
			do {
				s = (String) (it1.next());
				//sbuf.append("    <r:li r:resource=\"" + encodeSGML(s) + "\">" + parser.getNGramRank(s) + "</r:li>\n");
			} while (it1.hasNext());
			sbuf.append("   </r:Bag>\n");
			sbuf.append("  </r:Description>\n");
			sbuf.append("\n");
		}
		it1 = parser.getContent().getWordGrams();
		if (it1.hasNext()) {
			sbuf.append("  <r:Description r:ID=\"#DocumentWordGrams\">\n");
			sbuf.append("   <r:Bag>\n");
			do {
				s = (String) (it1.next());
				sbuf.append("    <r:li r:resource=\"" + encodeSGML(s) + "\">"
						+ parser.getContent().getWordGramFrequency(s) + "</r:li>\n");
			} while (it1.hasNext());
			sbuf.append("   </r:Bag>\n");
			sbuf.append("  </r:Description>\n");
			sbuf.append("\n");
		}
		it1 = parser.getStems();
		if (it1.hasNext()) {
			sbuf.append("  <r:Description r:ID=\"#DocumentStems\">\n");

			sbuf.append("   <r:Bag>\n");
			do {
				s = (String) (it1.next());
				sbuf.append("    <r:li r:resource=\"" + encodeSGML(s) + "\" />\n");
			} while (it1.hasNext());
			sbuf.append("   </r:Bag>\n");
			sbuf.append("  </r:Description>\n");
			sbuf.append("\n");
		}
		// =================================================================== //
		sbuf.append("</r:Description>\n");
		sbuf.append("</r:RDF>");
		return sbuf.toString();
	}

	/**
	 * 
	 * Initializes a parser object, with basis on the supplied MIME type
	 * 
	 * @param src
	 *            An InputStream to the source document
	 * @param t
	 *            The MIME type of the source document
	 * @param aux
	 *            The parser object to be initialized
	 */
	public static HTMLParser initParser(InputStream src, String t, HTMLParser aux) throws Exception {
		int type = 0;
		if (t.endsWith("application/pdf")) {
			type = 1;
		} else if (t.endsWith("application/postscript")) {
			type = 2;
		} else if (t.endsWith("application/msword")) {
			type = 3;
		} else if (t.endsWith("application/vnd.ms-powerpoint") || t.endsWith("application/powerpoint")
				|| t.endsWith("application/mspowerpoint")) {
			type = 4;
		} else if (t.endsWith("application/x-latex") || t.endsWith("application/x-tex")) {
			type = 5;
		} else if (t.endsWith("application/x-bibtex")) {
			type = 6;
		} else if (t.endsWith("application/x-dvi")) {
			type = 7;
		} else if (t.endsWith("text/rtf") || t.endsWith("application/rtf") || t.endsWith("text/richtext")) {
			type = 8;
		} else if (t.endsWith("text/tab-separated-values") || t.endsWith("text/plain")) {
			type = 9;
		} else if (t.endsWith("application/excel") || t.endsWith("application/vnd.ms-excel")
				|| t.endsWith("application/x-excel")) {
			type = 10;
		} else if (t.endsWith("application/x-shockwave-flash")) {
			type = 11;
		}
		String s = null;
		switch (type) {
		case 1:
			PDF2HTML pdf2html = new PDF2HTML();
			s = pdf2html.convertPDFToHTML(src);
			break;
		case 2:
			PS2HTML ps2html = new PS2HTML();
			s = ps2html.convertPSToHTML(src);
			break;
		case 3:
			DOC2HTML doc2html = new DOC2HTML();
			s = doc2html.convertDOCToHTML(src);
			break;
		case 4:
			PPT2HTML ppt2html = new PPT2HTML();
			s = ppt2html.convertPPTToHTML(src);
			break;
		case 5:
			TEX2HTML tex2html = new TEX2HTML();
			s = tex2html.convertTEXToHTML(src);
			break;
		case 6:
			BIB2HTML bib2html = new BIB2HTML();
			s = bib2html.convertBIBToHTML(src);
			break;
		case 7:
			// DVI2HTML dvi2html = new DVI2HTML();
			DVI2HTML dvi2html = null;
			s = dvi2html.convertDVIToHTML(src);
			break;
		case 8:
			RTF2HTML rtf2html = new RTF2HTML();
			s = rtf2html.convertRTFToHTML(src);
			break;
		case 9:
			TXT2HTML txt2html = new TXT2HTML();
			s = txt2html.convertTXTToHTML(src);
			break;
		case 10:
			XLS2HTML xls2html = new XLS2HTML();
			s = xls2html.convertXLSToHTML(src);
			break;
		case 11:
			SWF2HTML swf2html = new SWF2HTML();
			s = swf2html.convertSWFToHTML(src);
			break;
		default:
		}
		if (s == null) {
			aux.initTokenizer(src);
		} else {
			aux.initTokenizer(new ByteArrayInputStream(s.getBytes()));
		}
		return aux;
	}

	/**
	 * Initializes a parser object, with basis on the file extension
	 *
	 * @param src
	 *            An InputStream to the source document
	 * @param aux
	 *            The parser object to be initialized
	 */
	public static HTMLParser initParser(String src, HTMLParser aux) throws Exception {
		int type = 0;
		if (src.endsWith(".pdf")) {
			type = 1;
		} else if (src.endsWith(".ps")) {
			type = 2;
		} else if (src.endsWith(".doc")) {
			type = 3;
		} else if (src.endsWith(".ppt") || src.endsWith(".pps")) {
			type = 4;
		} else if (src.endsWith(".tex")) {
			type = 5;
		} else if (src.endsWith(".bib")) {
			type = 6;
		} else if (src.endsWith(".dvi")) {
			type = 7;
		} else if (src.endsWith(".rtf")) {
			type = 8;
		} else if (src.endsWith(".txt")) {
			type = 9;
		} else if (src.endsWith(".xls")) {
			type = 10;
		} else if (src.endsWith(".swf")) {
			type = 11;
		}
		if (src.startsWith("file:")) {
			String s = null;
			switch (type) {
			case 1:
				PDF2HTML pdf2html = new PDF2HTML();
				s = pdf2html.convertPDFToHTML(new File(src.substring(5)));
				break;
			case 2:
				PS2HTML ps2html = new PS2HTML();
				s = ps2html.convertPSToHTML(new File(src.substring(5)));
				break;
			case 3:
				DOC2HTML doc2html = new DOC2HTML();
				s = doc2html.convertDOCToHTML(new File(src.substring(5)));
				break;
			case 4:
				PPT2HTML ppt2html = new PPT2HTML();
				s = ppt2html.convertPPTToHTML(new File(src.substring(5)));
				break;
			case 5:
				TEX2HTML tex2html = new TEX2HTML();
				s = tex2html.convertTEXToHTML(new File(src.substring(5)));
				break;
			case 6:
				BIB2HTML bib2html = new BIB2HTML();
				s = bib2html.convertBIBToHTML(new File(src.substring(5)));
				break;
			case 7:
				// DVI2HTML dvi2html = new DVI2HTML();
				DVI2HTML dvi2html = null;
				s = dvi2html.convertDVIToHTML(new File(src.substring(5)));
				break;
			case 8:
				RTF2HTML rtf2html = new RTF2HTML();
				s = rtf2html.convertRTFToHTML(new File(src.substring(5)));
				break;
			case 9:
				TXT2HTML txt2html = new TXT2HTML();
				s = txt2html.convertTXTToHTML(new File(src.substring(5)));
				break;
			case 10:
				XLS2HTML xls2html = new XLS2HTML();
				s = xls2html.convertXLSToHTML(new File(src.substring(5)));
				break;
			case 11:
				SWF2HTML swf2html = new SWF2HTML();
				s = swf2html.convertSWFToHTML(new File(src.substring(5)));
				break;
			default:
			}
			if (s == null) {
				aux.initTokenizer(new File(src.substring(5)));
			} else {
				aux.initTokenizer(new ByteArrayInputStream(s.getBytes()));
			}
		} else {
			String s = null;
			switch (type) {
			case 1:
				PDF2HTML pdf2html = new PDF2HTML();
				s = pdf2html.convertPDFToHTML(new URL(src));
				break;
			case 2:
				PS2HTML ps2html = new PS2HTML();
				s = ps2html.convertPSToHTML(new URL(src));
				break;
			case 3:
				DOC2HTML doc2html = new DOC2HTML();
				s = doc2html.convertDOCToHTML(new URL(src));
				break;
			case 4:
				PPT2HTML ppt2html = new PPT2HTML();
				s = ppt2html.convertPPTToHTML(new URL(src));
				break;
			case 5:
				TEX2HTML tex2html = new TEX2HTML();
				s = tex2html.convertTEXToHTML(new URL(src));
				break;
			case 6:
				BIB2HTML bib2html = new BIB2HTML();
				s = bib2html.convertBIBToHTML(new URL(src));
				break;
			case 7:
				// DVI2HTML dvi2html = new DVI2HTML();
				DVI2HTML dvi2html = null;
				s = dvi2html.convertDVIToHTML(new URL(src));
				break;
			case 8:
				RTF2HTML rtf2html = new RTF2HTML();
				s = rtf2html.convertRTFToHTML(new URL(src));
				break;
			case 9:
				TXT2HTML txt2html = new TXT2HTML();
				s = txt2html.convertTXTToHTML(new URL(src));
				break;
			case 10:
				XLS2HTML xls2html = new XLS2HTML();
				s = xls2html.convertXLSToHTML(new URL(src));
				break;
			case 11:
				SWF2HTML swf2html = new SWF2HTML();
				s = swf2html.convertSWFToHTML(new URL(src));
				break;
			default:
			}
			if (s == null) {
				aux.initTokenizer(new URL(src));
			} else {
				aux.initTokenizer(new ByteArrayInputStream(s.getBytes()));
			}
		}
		return aux;
	}

	/**
	 * The main method of this class, parsing parameters from the command line,
	 * invoquing the parser to process the given input, and dumping the results in
	 * RDF.
	 *
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String args[]) throws Exception {
		String src;
		HTMLParser aux;
		if (args.length < 1 || args.length > 2) {
			System.out.println("METADATA EXTRACTOR FOR WEB RESOURCES");
			System.out.println("Usage: Parser [language_models_path] url");
			System.exit(-1);
		}
		if (args.length == 2) {
			src = args[1];
			aux = new HTMLParser(args[0]);
		} else {
			src = args[0];
			aux = new HTMLParser(src);
		}
		initParser(src, aux);
		aux.processData();
		System.out.println(getRDFData(aux));
	}

}

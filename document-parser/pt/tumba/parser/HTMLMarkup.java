package pt.tumba.parser;

import java.util.*;

/**
 * @author Bruno Martins
 */
public class HTMLMarkup {


	/**
	 *  Gets the anchor attribute of the HTMLParser object
	 *
	 *@param  word  Description of the Parameter
	 *@return       The anchor value
	 */
	public boolean isAnchor(String word) {
		return ((contentInfo.getTermInfo(word) & 4096) != 0x00000000);
	}

	public int getAnchorCount(String term) {
		return contentInfo.getCountAux(term, 12);
	}

	/**
	 *  Gets the anchorTerms attribute of the HTMLParser object
	 *
	 *@return    The anchorTerms value
	 */
	public Iterator getAnchorTerms() {
		Vector v = new Vector();
		Iterator words = contentInfo.getTerms();
		String token;
		while (words.hasNext()) {
			token = words.next().toString();
			if (isAnchor(token)) {
				v.addElement(token);
			}
		}
		return v.iterator();
	}


	/**
	 *  Gets the big attribute of the HTMLParser object
	 *
	 *@param  word  Description of the Parameter
	 *@return       The big value
	 */
	public boolean isBig(String word) {
		return ((contentInfo.getTermInfo(word) & 4) != 0x00000000);
	}

	public int getBigCount(String term) {
		return contentInfo.getCountAux(term, 2);
	}

	/**
	 *  Gets the bigTerms attribute of the HTMLParser object
	 *
	 *@return    The bigTerms value
	 */
	public Iterator getBigTerms() {
		Vector v = new Vector();
		Iterator words = contentInfo.getTerms();
		String token;
		while (words.hasNext()) {
			token = words.next().toString();
			if (isBig(token)) {
				v.addElement(token);
			}
		}
		return v.iterator();
	}

	/**
	 *  Gets the bold attribute of the HTMLParser object
	 *
	 *@param  word  Description of the Parameter
	 *@return       The bold value
	 */
	public boolean isBold(String word) {
		return ((contentInfo.getTermInfo(word) & 2) != 0x00000000);
	}

	public int getBoldCount(String term) {
		return contentInfo.getCountAux(term, 1);
	}

	/**
	 *  Gets the boldTerms attribute of the HTMLParser object
	 *
	 *@return    The boldTerms value
	 */
	public Iterator getBoldTerms() {
		Vector v = new Vector();
		Iterator words = contentInfo.getTerms();
		String token;
		while (words.hasNext()) {
			token = words.next().toString();
			if (isBold(token)) {
				v.addElement(token);
			}
		}
		return v.iterator();
	}

	/**
	 *  Gets the emphasize attribute of the HTMLParser object
	 *
	 *@param  word  Description of the Parameter
	 *@return       The emphasize value
	 */
	public boolean isEmphasize(String word) {
		return ((contentInfo.getTermInfo(word) & 512) != 0x00000000);
	}

	public int getEmphasizeCount(String term) {
		return contentInfo.getCountAux(term, 9);
	}

	/**
	 *  Gets the emphasizeTerms attribute of the HTMLParser object
	 *
	 *@return    The emphasizeTerms value
	 */
	public Iterator getEmphasizeTerms() {
		Vector v = new Vector();
		Iterator words = contentInfo.getTerms();
		String token;
		while (words.hasNext()) {
			token = words.next().toString();
			if (isEmphasize(token)) {
				v.addElement(token);
			}
		}
		return v.iterator();
	}

	/**
	 *  Gets the heading1 attribute of the HTMLParser object
	 *
	 *@param  word  Description of the Parameter
	 *@return       The heading1 value
	 */
	public boolean isHeading1(String word) {
		return ((contentInfo.getTermInfo(word) & 8) != 0x00000000);
	}

	public int getHeading1Count(String term) {
		return contentInfo.getCountAux(term, 3);
	}

	/**
	 *  Gets the heading1Terms attribute of the HTMLParser object
	 *
	 *@return    The heading1Terms value
	 */
	public Iterator getHeading1Terms() {
		Vector v = new Vector();
		Iterator words = contentInfo.getTerms();
		String token;
		while (words.hasNext()) {
			token = words.next().toString();
			if (isHeading1(token)) {
				v.addElement(token);
			}
		}
		return v.iterator();
	}

	/**
	 *  Gets the heading2 attribute of the HTMLParser object
	 *
	 *@param  word  Description of the Parameter
	 *@return       The heading2 value
	 */
	public boolean isHeading2(String word) {
		return ((contentInfo.getTermInfo(word) & 16) != 0x00000000);
	}

	public int getHeading2Count(String term) {
		return contentInfo.getCountAux(term, 4);
	}

	/**
	 *  Gets the heading2Terms attribute of the HTMLParser object
	 *
	 *@return    The heading2Terms value
	 */
	public Iterator getHeading2Terms() {
		Vector v = new Vector();
		Iterator words = contentInfo.getTerms();
		String token;
		while (words.hasNext()) {
			token = words.next().toString();
			if (isHeading2(token)) {
				v.addElement(token);
			}
		}
		return v.iterator();
	}

	/**
	 *  Gets the heading3 attribute of the HTMLParser object
	 *
	 *@param  word  Description of the Parameter
	 *@return       The heading3 value
	 */
	public boolean isHeading3(String word) {
		return ((contentInfo.getTermInfo(word) & 32) != 0x00000000);
	}

	public int getHeading3Count(String term) {
		return contentInfo.getCountAux(term, 5);
	}

	/**
	 *  Gets the heading3Terms attribute of the HTMLParser object
	 *
	 *@return    The heading3Terms value
	 */
	public Iterator getHeading3Terms() {
		Vector v = new Vector();
		Iterator words = contentInfo.getTerms();
		String token;
		while (words.hasNext()) {
			token = words.next().toString();
			if (isHeading3(token)) {
				v.addElement(token);
			}
		}
		return v.iterator();
	}

	/**
	 *  Gets the heading4 attribute of the HTMLParser object
	 *
	 *@param  word  Description of the Parameter
	 *@return       The heading4 value
	 */
	public boolean isHeading4(String word) {
		return ((contentInfo.getTermInfo(word) & 64) != 0x00000000);
	}

	public int getHeading4Count(String term) {
		return contentInfo.getCountAux(term, 6);
	}

	/**
	 *  Gets the heading4Terms attribute of the HTMLParser object
	 *
	 *@return    The heading4Terms value
	 */
	public Iterator getHeading4Terms() {
		Vector v = new Vector();
		Iterator words = contentInfo.getTerms();
		String token;
		while (words.hasNext()) {
			token = words.next().toString();
			if (isHeading4(token)) {
				v.addElement(token);
			}
		}
		return v.iterator();
	}

	/**
	 *  Gets the heading5 attribute of the HTMLParser object
	 *
	 *@param  word  Description of the Parameter
	 *@return       The heading5 value
	 */
	public boolean isHeading5(String word) {
		return ((contentInfo.getTermInfo(word) & 128) != 0x00000000);
	}

	public int getHeading5Count(String term) {
		return contentInfo.getCountAux(term, 7);
	}

	/**
	 *  Gets the heading5Terms attribute of the HTMLParser object
	 *
	 *@return    The heading5Terms value
	 */
	public Iterator getHeading5Terms() {
		Vector v = new Vector();
		Iterator words = contentInfo.getTerms();
		String token;
		while (words.hasNext()) {
			token = words.next().toString();
			if (isHeading5(token)) {
				v.addElement(token);
			}
		}
		return v.iterator();
	}

	/**
	 *  Gets the heading6 attribute of the HTMLParser object
	 *
	 *@param  word  Description of the Parameter
	 *@return       The heading6 value
	 */
	public boolean isHeading6(String word) {
		return ((contentInfo.getTermInfo(word) & 256) != 0x00000000);
	}

	public int getHeading6Count(String term) {
		return contentInfo.getCountAux(term, 8);
	}

	/**
	 *  Gets the heading6Terms attribute of the HTMLParser object
	 *
	 *@return    The heading6Terms value
	 */
	public Iterator getHeading6Terms() {
		Vector v = new Vector();
		Iterator words = contentInfo.getTerms();
		String token;
		while (words.hasNext()) {
			token = words.next().toString();
			if (isHeading6(token)) {
				v.addElement(token);
			}
		}
		return v.iterator();
	}

	/**
	 *  Gets the italic attribute of the HTMLParser object
	 *
	 *@param  word  Description of the Parameter
	 *@return       The italic value
	 */
	public boolean isItalic(String word) {
		return ((contentInfo.getTermInfo(word) & 2048) != 0x00000000);
	}

	public int getItalicCount(String term) {
		return contentInfo.getCountAux(term, 11);
	}

	/**
	 *  Gets the italicTerms attribute of the HTMLParser object
	 *
	 *@return    The italicTerms value
	 */
	public Iterator getItalicTerms() {
		Vector v = new Vector();
		Iterator words = contentInfo.getTerms();
		String token;
		while (words.hasNext()) {
			token = words.next().toString();
			if (isItalic(token)) {
				v.addElement(token);
			}
		}
		return v.iterator();
	}

	/**
	 *  Gets the small attribute of the HTMLParser object
	 *
	 *@param  word  Description of the Parameter
	 *@return       The small value
	 */
	public boolean isMeta(String word) {
		return ((contentInfo.getTermInfo(word) & 16384) != 0x00000000);
	}

	public int getMetaCount(String term) {
		return contentInfo.getCountAux(term, 14);
	}

	/**
	 *  Gets the italicTerms attribute of the HTMLParser object
	 *
	 *@return    The italicTerms value
	 */
	public Iterator getMetaTerms() {
		Vector v = new Vector();
		Iterator words = contentInfo.getTerms();
		String token;
		while (words.hasNext()) {
			token = words.next().toString();
			if (isMeta(token)) {
				v.addElement(token);
			}
		}
		return v.iterator();
	}

	/**
	 *  Gets the small attribute of the HTMLParser object
	 *
	 *@param  word  Description of the Parameter
	 *@return       The small value
	 */
	public boolean isSmall(String word) {
		return ((contentInfo.getTermInfo(word) & 8192) != 0x00000000);
	}

	public int getSmallCount(String term) {
		return contentInfo.getCountAux(term, 13);
	}

	/**
	 *  Gets the smallTerms attribute of the HTMLParser object
	 *
	 *@return    The smallTerms value
	 */
	public Iterator getSmallTerms() {
		Vector v = new Vector();
		Iterator words = contentInfo.getTerms();
		String token;
		while (words.hasNext()) {
			token = words.next().toString();
			if (isSmall(token)) {
				v.addElement(token);
			}
		}
		return v.iterator();
	}

	/**
	 *  Gets the strong attribute of the HTMLParser object
	 *
	 *@param  word  Description of the Parameter
	 *@return       The strong value
	 */
	public boolean isStrong(String word) {
		return ((contentInfo.getTermInfo(word) & 1024) != 0x00000000);
	}

	public int getStrongCount(String term) {
		return contentInfo.getCountAux(term, 10);
	}

	/**
	 *  Gets the strongTerms attribute of the HTMLParser object
	 *
	 *@return    The strongTerms value
	 */
	public Iterator getStrongTerms() {
		Vector v = new Vector();
		Iterator words = contentInfo.getTerms();
		String token;
		while (words.hasNext()) {
			token = words.next().toString();
			if (isStrong(token)) {
				v.addElement(token);
			}
		}
		return v.iterator();
	}

	/**
	 *  Gets the title attribute of the HTMLParser object
	 *
	 *@param  word  Description of the Parameter
	 *@return       The title value
	 */
	public boolean isTitle(String word) {
		return ((contentInfo.getTermInfo(word) & 1) != 0x00000000);
	}

	public int getTitleCount(String term) {
		return contentInfo.getCountAux(term, 0);
	}

	/**
	 *  Gets the titleTerms attribute of the HTMLParser object
	 *
	 *@return    The titleTerms value
	 */
	public Iterator getTitleTerms() {
		Vector v = new Vector();
		Iterator words = contentInfo.getTerms();
		String token;
		while (words.hasNext()) {
			token = words.next().toString();
			if (isTitle(token)) {
				v.addElement(token);
			}
		}
		return v.iterator();
	}

	protected HTMLMarkup (Content contentInfo) {
	 	this.contentInfo = contentInfo;
	}
	
	private Content contentInfo;

}

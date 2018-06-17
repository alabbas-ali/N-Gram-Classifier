package pt.tumba.parser;

import java.util.*;
import java.net.*;

/**
 * @author Bruno Martins
 */
public class HyperLinks {

	private Map links;

	/**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
	public Map getLinkMap() {
		return links;
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
	public Iterator getLinks() {
		return links.keySet().iterator();
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
	public int getNumLinks() {
		return links.size();
	}

	/**
	 * Return the URL-Strings for all the URLs extracted from the document
	 *
	 * @return An iterator over the URL-Strings extracted from the document
	 */
	public Iterator getURLs() {
		Iterator it = links.keySet().iterator();
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
	 * Gets the anchorText attribute of the HTMLParser object
	 *
	 * @param url
	 *            Description of the Parameter
	 * @return The anchorText value
	 */
	public String getAnchorText(String url2) {
		try {
			String url = url2;
			if (!url.startsWith("http://")) {
				url = "http://";
			}
			String aux = (String) (links.get(url));
			return ((aux == null) ? "" : aux);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Gets the anchorText attribute of the HTMLParser object
	 *
	 * @param url
	 *            Description of the Parameter
	 * @return The anchorText value
	 */
	public String getAnchorText(URL url) {
		return getAnchorText(url.toString());
	}

	protected HyperLinks() {
		links = new HashMap();
	}

	protected void addLink(String url, String alt2) {
		String alt = alt2;
		if (alt == null)
			alt = "";
		String anchor = (String) (getAnchorText(url.toString()));
		if (anchor == null)
			anchor = alt;
		else if (alt.length() > 0)
			anchor += ";" + alt;
		links.put(url, alt);
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
	public Iterator getAnchors() {
		Iterator it = links.values().iterator();
		Vector v = new Vector();
		String aux;
		boolean error = false;
		while (it.hasNext()) {
			try {
				aux = it.next().toString();
				if (aux.length() > 0) {
					v.addElement(aux);
				}
			} catch (Exception e) {
				error = true;
			}
		}
		return v.iterator();
	}

}

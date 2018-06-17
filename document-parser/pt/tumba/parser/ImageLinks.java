package pt.tumba.parser;

import java.util.*;
import java.net.*;

/**
 * @author Bruno Martins
 */
public class ImageLinks {

	private Map images;
	
	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public Map getImageMap() {
		return images;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public Iterator getImages() {
		return images.keySet().iterator();
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public int getNumImages() {
		return images.size();
	}

	/**
	 *  Gets the caption attribute of the HTMLParser object
	 *
	 *@param  url  Description of the Parameter
	 *@return      The caption value
	 */
	public String getCaption(String url2) {
		try {
			String url = url2;
			if (!url.startsWith("http://")) {
				url = "http://";
			}
			String aux = (String) (images.get(url));
			return ((aux == null) ? "" : aux);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 *  Gets the caption attribute of the HTMLParser object
	 *
	 *@param  url  Description of the Parameter
	 *@return      The caption value
	 */
	public String getCaption(URL url) {
		return getCaption(url.toString());
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public Iterator getCaptions() {
		Iterator it = images.values().iterator();
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

	protected ImageLinks () {
		images = new HashMap();
	}
	
	protected void addImage(String url, String alt) {
		images.put(url,alt);
	}
	
}

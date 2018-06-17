package pt.tumba.parser;

import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;

/**
 * Description of the Class
 *
 * @author Bruno Martins
 * @created 22 de Agosto de 2002
 */
public class URLUtils {

	private static final URLUtils _theInstance = new URLUtils();

	/**
	 * Description of the Method
	 *
	 * @param url
	 *            Description of the Parameter
	 * @param host
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static boolean chechHost(URL url, String host) {
		return url.getHost().endsWith(host);
	}

	/**
	 * Description of the Method
	 *
	 * @param urlstr
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static boolean checkHomePage(String urlstr2) {
		String urlstr = urlstr2;
		if (urlstr.startsWith("http://")) {
			urlstr = urlstr.substring(7);
		}
		if (!urlstr.endsWith("/") && !urlstr.endsWith("/index.html") && !urlstr.endsWith("/index.htm")
				&& !urlstr.endsWith("/index1.html") && !urlstr.endsWith("/index1.htm")
				&& !urlstr.endsWith("/homepage.html") && !urlstr.endsWith("/homepage.htm")
				&& !urlstr.endsWith("/home.html") && !urlstr.endsWith("/home.htm") && !urlstr.endsWith("/default.html")
				&& !urlstr.endsWith("/default.htm") && !urlstr.endsWith("/main.html")
				&& !urlstr.endsWith("/main.htm")) {
			return false;
		}
		int numOcurrences = 0;
		int index = -1;
		String str3 = urlstr;
		while ((index = str3.indexOf("/")) != -1) {
			numOcurrences++;
			str3 = str3.substring(index + 1);
		}
		if (numOcurrences > 3) {
			return false;
		}
		return true;
	}

	/**
	 * Description of the Method
	 *
	 * @param url
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static boolean checkHomePage(URL url) {
		return checkHomePage(url.toString());
	}

	/**
	 * Description of the Method
	 *
	 * @param url
	 *            Description of the Parameter
	 * @param hosts
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static boolean checkHost(URL url, Iterator hosts) {
		while (hosts.hasNext()) {
			if (url.getHost().endsWith(hosts.next().toString())) {
				return true;
			}
		}
		return false;
	}

	public static URLUtils getInstance() {
		return _theInstance;
	}

	/**
	 * Description of the Method
	 *
	 * @param aux
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static URL normalizeURL(String aux) {
		return normalizeURL((String) null, aux);
	}

	/**
	 * Description of the Method
	 *
	 * @param base
	 *            Description of the Parameter
	 * @param aux
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static URL normalizeURL(String base2, String aux) {
		if (aux == null) {
			return null;
		}
		String base = base2;
		if (base != null) {
			if (base.startsWith("mailto:")) {
				return null;
			}
			if (base.startsWith("javascript:")) {
				return null;
			}
			if (!base.startsWith("http://")) {
				base = "http://" + base;
			}
		}
		if (aux.startsWith("mailto:")) {
			return null;
		}
		if (aux.startsWith("javascript:")) {
			return null;
		}
		int i;
		int pos;
		int cur;
		String aux2 = aux;
		while ((i = aux2.indexOf(" ")) != -1) {
			aux2 = aux2.substring(0, i) + aux2.substring(i + 1);
		}
		try {
			URL url;
			if (base == null) {
				if (!aux2.startsWith("http://")) {
					aux2 = "http://" + aux2;
				}
				url = new URL(aux2);
			} else {
				url = new URL(new URL(base), aux2);
			}
			String host = url.getHost().toLowerCase();
			for (i = 0; i < host.length(); i++) {
				char auxc = host.charAt(i);
				if (!((auxc >= 'a' && auxc <= 'z') || (auxc >= '0' && auxc <= '9') || (auxc == '.') || (auxc == '-')))
					return null;
			}
			if (host.length() > 67)
				return null;
			URI uri = new URI("http://" + host + url.getFile());
			String str = uri.normalize().toString();
			while ((i = str.indexOf("/../")) != -1) {
				aux2 = str.substring(i + 3);
				str = str.substring(0, i) + aux2;
			}
			if ((i = str.indexOf("?")) == -1) {
				return new URL(str);
			}
			String params = str.substring(i + 1);
			str = str.substring(0, i + 1);
			Vector paramsv = new Vector();
			Vector paramsv2 = new Vector();
			while ((i = params.indexOf("&")) != -1) {
				paramsv.addElement(params.substring(0, i));
				params = params.substring(i + 1);
			}
			paramsv.addElement(params);
			while (paramsv.size() != 0) {
				for (pos = 0, cur = 1; cur < paramsv.size(); cur++) {
					if (paramsv.elementAt(pos).toString().compareTo(paramsv.elementAt(cur).toString()) > 0) {
						pos = cur;
					}
				}
				if (paramsv.elementAt(pos).toString().length() > 0) {
					paramsv2.addElement(paramsv.elementAt(pos));
				}
				paramsv.removeElementAt(pos);
			}
			if (paramsv2.size() != 0) {
				str += paramsv2.elementAt(0);
				for (i = 1; i < paramsv2.size(); i++) {
					str += "&" + paramsv2.elementAt(i);
				}
			}
			return new URL(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Description of the Method
	 *
	 * @param aux
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static URL normalizeURL(URL aux) {
		return normalizeURL(aux.toString());
	}

	/**
	 * Description of the Method
	 *
	 * @param base
	 *            Description of the Parameter
	 * @param aux
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static URL normalizeURL(URL base, String aux) {
		return normalizeURL(base.toString(), aux);
	}

	/**
	 * Description of the Method
	 *
	 * @param base
	 *            Description of the Parameter
	 * @param target
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String relativeURL(URL base, URL target) {
		int chop;
		if (target == null) {
			return "(null destination)";
		}
		if (base == null || !base.getProtocol().equals(target.getProtocol()) || !base.getHost().equals(target.getHost())
				|| base.getPort() != target.getPort()) {
			return target.toString();
		}
		if (base.equals(target)) {
			String bref = base.getRef();
			String tref = target.getRef();
			if (bref != null && bref.equals(tref)) {
				return "(you are here)";
			} else {
				return tref;
			}
		} else {
			String baseFile = base.getFile();
			String targetFile = target.getFile();
			String tref = (target.getRef() == null ? "" : "#" + target.getRef());
			if (baseFile.equals(targetFile)) {
				return tref;
			}
			if ((chop = baseFile.lastIndexOf('/')) != -1 && targetFile.regionMatches(0, baseFile, 0, chop)) {
				return targetFile.substring(chop + 1) + tref;
			}
			if (baseFile.length() != 0
					&& (chop = baseFile.substring(0, baseFile.length() - 1).lastIndexOf('/')) != -1) {
				baseFile = baseFile.substring(0, chop + 1);
				if (targetFile.startsWith(baseFile)) {
					return "../" + targetFile.substring(baseFile.length()) + tref;
				}
			}
			return targetFile;
		}
	}

	private URLUtils() {
	}

}

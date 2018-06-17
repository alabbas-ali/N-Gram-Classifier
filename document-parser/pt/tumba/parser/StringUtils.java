package pt.tumba.parser;

/**
 * A collection of <code>String</code> handling utility methods.
 *
 * Some of the methods available in this class have equivalents in the
 * <code>java.lang.String</code> class. However, the implementations provided
 * here are a lot faster, since they do not deal with character
 * internationalization issues.
 *
 * @author Bruno Martins
 *
 */
public class StringUtils {

	/** The single instance of this class */
	private static final StringUtils _theInstance = new StringUtils();

	/**
	 * Tests whether a given character is alphabetic, numeric or the hyphen
	 * character.
	 *
	 * @param c
	 *            The character to be tested
	 * @return whether the given character is alphameric or not
	 */
	public static boolean isAlphaNumeric(char c) {
		return c == '-' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9');
	}

	/**
	 * Counts the occurrence of the given char in a String.
	 *
	 * @param str
	 *            The string to be tested
	 * @param c
	 *            the char to be counted
	 * @return the frequency of occurrence for the character in the String.
	 */
	public static int count(String str, char c) {
		int index = 0;
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == c)
				index++;
		}
		return index;
	}

	/**
	 * Matches two strings.
	 *
	 * @param a
	 *            The first string
	 * @param b
	 *            The second string
	 * @return the index where the two strings stop matching starting from 0
	 */
	public static int matchStrings(String a, String b) {
		int i;
		char[] ca = a.toCharArray();
		char[] cb = b.toCharArray();
		int len = (ca.length < cb.length) ? ca.length : cb.length;
		for (i = 0; i < len; i++)
			if (ca[i] != cb[i])
				break;
		return i;
	}

	/**
	 * Return the single instance of this class
	 * 
	 * @return An instance of <code>StringUtils</code>
	 */
	public static StringUtils getInstance() {
		return _theInstance;
	}

	/**
	 * Reverse a given String
	 *
	 * @param s
	 *            The String to reverse
	 * @return The reversed string
	 */
	public static String invertString(String s) {
		if ((s == null) || (s == ""))
			return "";
		byte[] b = s.getBytes();
		byte[] c = new byte[b.length];
		int x = b.length;
		for (int i = 0; i < x; i++)
			c[x - i - 1] = b[i];
		return new String(c);
	}

	/**
	 * Returns a new string resulting from replacing all occurrences of the String
	 * search in the String source, with the string replace.
	 *
	 * @param source
	 *            The original String
	 * @param search
	 *            The string to be replaces
	 * @param replace
	 *            The replacement String
	 * @return The resulting String
	 */
	public static String replace(String source, String search, String replace) {
		int sind = -1;
		String aux = "", s = source;
		while (!s.equals("")) {
			sind = s.indexOf(search);
			if (sind != -1) {
				aux += s.substring(0, sind) + replace;
				s = s.substring(sind + 1);
			} else {
				aux += s;
				s = "";
			}
		}
		return aux;
	}

	/**
	 * Replaces accented characters with their variations without the diacritics.
	 * 
	 * TODO: add more non-Portuguese diacritic characters.
	 *
	 * @param chr
	 *            the character to check
	 * @return The character without the diacritic
	 */
	public static char replaceAccent(char chr) {
		switch (chr) {
		case 'á':
		case 'à':
		case 'ã':
		case 'â':
			return 'a';
		case 'é':
		case 'è':
		case 'ê':
			return 'e';
		case 'í':
		case 'ì':
		case 'ĩ':
			return 'i';
		case 'ó':
		case 'ò':
		case 'õ':
		case 'ô':
			return 'o';
		case 'ú':
		case 'ù':
		case 'ũ':
			return 'u';
		case 'ç':
			return 'c';
		case 'ñ':
			return 'n';
		default:
			return chr;
		}
	}

	/**
	 * Checks if a given character has diacritics. For instance, isAccent('a') would
	 * return false, whereas isAccent('á') would return true.
	 *
	 * @param chr
	 *            the char to check
	 * @return true if the character has a diacritic and false otherwise.
	 */
	public static boolean isAccent(char chr) {
		for (int i = 0; i < specialChars.length; i++)
			if (chr == specialChars[i])
				return true;
		return false;
	}

	/**
	 * Checks if a given character is uppercase. For instance, isUpperCase('a')
	 * would return false, whereas isUpperCase('A') would return true.
	 *
	 * @param chr
	 *            the char to check
	 * @return true if the character is uppercase and false otherwise
	 */
	public static boolean isUpperCase(char chr) {
		return chr == Character.toUpperCase(chr);
	}

	/**
	 * Takes a numeric string and separates groups of 3 characters with a '.'
	 * character. For instance separateNumberWithDots(n) would return "1.000"
	 *
	 * @param n
	 *            A numeric String
	 *
	 * @return The resulting String
	 */
	public static String separateNumberWithDots(String n) {
		return separateNumberWithDots(n, 3);
	}

	/**
	 * Takes a numeric string and separates groups of "n" characters with a '.'
	 * character. For instance separateNumberWithDots(n,3) would return "1.000"
	 *
	 * @param n
	 *            A numeric String
	 * @param s
	 *            The number of characters to group
	 * @return The resulting String
	 */
	public static String separateNumberWithDots(String n, int s) {
		int c = 0;
		String saux = "";
		for (int i = n.length() - 1; i > 0; i--) {
			saux = n.charAt(i) + saux;
			c++;
			if (c == s) {
				saux = '.' + saux;
				c = 0;
			}
		}
		saux = n.charAt(0) + saux;
		return saux;
	}

	/**
	 * Converts all of the characters in a given String to lower case.
	 * 
	 * @param str
	 *            A String
	 * @param accents
	 *            if true, then besides converting the string to lower case accented
	 *            characters are also replaces with their versions without the
	 *            diacritics
	 * @return The resulting String
	 */
	public static String toLowerCase(String str, boolean accents) {
		int len = str.length();
		int different = -1;
		int i;
		char ch;
		char ch2;
		for (i = len - 1; i >= 0; i--) {
			ch = str.charAt(i);
			ch2 = Character.toLowerCase(ch);
			if (accents) {
				ch2 = replaceAccent(ch2);
			}
			if (ch2 != ch) {
				different = i;
				break;
			}
		}
		if (different == -1) {
			return str;
		} else {
			char[] chars = new char[len];
			str.getChars(0, len, chars, 0);
			for (i = different; i >= 0; i--) {
				ch = Character.toLowerCase(chars[i]);
				if (accents) {
					ch = replaceAccent(ch);
				}
				chars[i] = ch;
			}
			return new String(chars);
		}
	}

	/**
	 * Return an array with all the valid accented characters
	 *
	 * TODO: add more non-Portuguese diacritic characters.
	 *
	 * @return An array with all the valid accented characters
	 */
	public static char[] getSpecialChars() {
		return specialChars;
	}

	/** An array with all the valid accented characters */
	private static char specialChars[] = { 'á', 'à', 'ã', 'â', 'é', 'è', 'ê', 'í', 'ì', 'ĩ', 'ó', 'ò', 'õ', 'ô', 'ú',
			'ù', 'ũ', 'ç', 'ñ' };

	/**
	 * Checks if the character at a given position of a given string is a vowel. The
	 * Y character is also considered.
	 * 
	 * TODO: Should portuguese accented characters be considered vowels?
	 *
	 * @param in
	 *            A String
	 * @param at
	 *            The position in the String
	 * @return true if the the character at position at of the string in is a vowel
	 *         and false otherwise
	 */
	public final static boolean isVowel(String in, int at) {
		return isVowel(in, at, in.length());
	}

	/**
	 * Checks if the character at a given position of a given string is a vowel The
	 * Y character is also considered.
	 * 
	 * TODO: Should portuguese accented characters be considered vowels?
	 * 
	 * @param in
	 *            A String
	 * @param at
	 *            The position in the String
	 * @param length
	 *            The maximum lengh of the String to check
	 * @return true if the the character at position at of the string in is a vowel
	 *         and false otherwise
	 */
	public static boolean isVowel(String in, int at, int length) {
		if ((at < 0) || (at >= length))
			return false;
		char it = Character.toLowerCase(in.charAt(at));
		if ((it == 'A') || (it == 'E') || (it == 'I') || (it == 'O') || (it == 'U') || (it == 'Y'))
			return true;
		return false;
	}

	/**
	 * Checks if a given String is capitalizated
	 * 
	 * @param str
	 *            A String
	 * @return true if the given String is capitalizated and false otherwise
	 */
	public static boolean isCapitalizated(String str) {
		if (str.length() == 0)
			return false;
		String capitalizated = capitalizate(str, false);
		return capitalizated.equals(str);
	}

	/**
	 * Capitalizates a given String
	 * 
	 * @param str
	 *            A String
	 * @return The capitalizated String
	 */
	public static String capitalizate(String str, boolean accents) {
		String lowerCase = toLowerCase(str, accents);
		int index = lowerCase.indexOf(" ");
		if (index == -1) {
			if (lowerCase.length() == 0)
				return lowerCase;
			if (lowerCase.trim().equals("da") || lowerCase.trim().equals("das") || lowerCase.trim().equals("do")
					|| lowerCase.trim().equals("dos") || lowerCase.trim().equals("de") || lowerCase.trim().equals("a")
					|| lowerCase.trim().equals("as") || lowerCase.trim().equals("e") || lowerCase.trim().equals("o")
					|| lowerCase.trim().equals("os") || lowerCase.trim().equals("ou") || lowerCase.trim().equals("d'el")
					|| lowerCase.trim().equals("of") || lowerCase.trim().equals("and") || lowerCase.trim().equals("or")
					|| lowerCase.trim().equals("the"))
				return lowerCase;
			char ch = str.charAt(0);
			return ((char) Character.toUpperCase(ch)) + lowerCase.substring(1);
		} else {
			String result = capitalizate(lowerCase.substring(0, index), accents) + " "
					+ capitalizate(lowerCase.substring(index + 1), accents);
			if (result.trim().equals("a") || result.trim().startsWith("a ") || result.trim().equals("as")
					|| result.trim().startsWith("as ") || result.trim().equals("o") || result.trim().startsWith("o ")
					|| result.trim().equals("os") || result.trim().startsWith("os ") || result.trim().equals("the")
					|| result.trim().startsWith("the ")) {
				result = Character.toUpperCase(result.charAt(0)) + result.substring(1);
			}
			return result;
		}
	}

	/**
	 * Sole constructor, private because this is a Singleton class
	 */
	private StringUtils() {
	}

}
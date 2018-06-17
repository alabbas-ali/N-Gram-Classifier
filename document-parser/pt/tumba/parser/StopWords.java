package pt.tumba.parser;

import java.util.Hashtable;

/**
 * @author bmartins
 * @created 22 de Agosto de 2002
 * @description loads the location information from XML file
 */
public class StopWords {

	/**
	 * a list of internet specific stopwords
	 */
	private static String[] internetStopwords = { "jar", "zip", "exe", "nbsp", "com", "link", "previous", "and", "a",
			"web", "next", "page", "of", "click", "site", "topic", "here", "website", "domain", "prev", "obj",
			"download", "tar", "homepage", "internet", "explorer", "mozilla", "netscape", "browser", "inicial",
			"voltar", "atr�s", "asp", "aspx", "jsp", "rar", "gz2", "gz", "pdf", "doc", "ps", "ppt", "tgz", "hqx",
			"bin", "cab", "zoo", "mov", "mpg", "mpeg", "avi", "mp3", "wav", "mid", "xls", "rm", "dll", "rpm", "class",
			"swf", "doc", "xls", "tex", "txt", "ps", "pdf", "xml", "xhtml", "htm", "xsl", "xslt", "previous", "janela",
			"windows", "window", "press", "pressione", "pressionar", "select", "seleccionar", "seleccione", "frame",
			"site", "web", "website", "frames", "mail", "email", "home", "page", "pagina", "p�gina", "http", "html",
			"index", "links", "titulo", "t�tulo", "ftp", "w3", "www", "com", "edu", "gov", "int", "mil", "net", "org",
			"arpa", "aero", "biz", "coop", "info", "name", "pro", "ac", "ad", "ae", "af", "ag", "ai", "al", "am", "an",
			"ao", "aq", "ar", "as", "at", "au", "aw", "az", "ba", "bb", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bm",
			"bn", "bo", "br", "bs", "bt", "bv", "bw", "by", "bz", "ca", "cc", "cd", "cf", "cg", "ch", "ci", "ck", "cl",
			"cm", "cn", "co", "cr", "cu", "cv", "cx", "cy", "cz", "de", "dj", "dk", "dm", "do", "dz", "ec", "ee", "eg",
			"eh", "er", "es", "et", "fi", "fj", "fk", "fm", "fo", "fr", "ga", "gd", "ge", "gf", "gg", "gh", "gi", "gl",
			"gm", "gn", "gp", "gq", "gr", "gs", "gt", "gu", "gw", "gy", "hk", "hm", "hn", "hr", "ht", "hu", "id", "ie",
			"il", "im", "in", "io", "iq", "ir", "is", "it", "je", "jm", "jo", "jp", "ke", "kg", "kh", "ki", "km", "kn",
			"kp", "kr", "kw", "ky", "kz", "la", "lb", "lc", "li", "lk", "lr", "ls", "lt", "lu", "lv", "ly", "ma", "mc",
			"md", "mg", "mh", "mk", "ml", "mm", "mn", "mo", "mp", "mq", "mr", "ms", "mt", "mu", "mv", "mw", "mx", "my",
			"mz", "na", "nc", "ne", "nf", "ng", "ni", "nl", "no", "np", "nr", "nu", "nz", "om", "pa", "pe", "pf", "pg",
			"ph", "pk", "pl", "pm", "pn", "pr", "ps", "pt", "pw", "py", "qa", "re", "ro", "ru", "rw", "sa", "sb", "sc",
			"sd", "se", "sg", "sh", "si", "sj", "sk", "sl", "sm", "sn", "so", "sr", "st", "sv", "sy", "sz", "tc", "td",
			"tf", "tg", "th", "tj", "tk", "tm", "tn", "to", "tp", "tr", "tt", "tv", "tw", "tz", "ua", "ug", "uk", "um",
			"us", "uy", "uz", "va", "vc", "ve", "vg", "vi", "vn", "vu", "wf", "ws", "ye", "yt", "yu", "za", "zm", "zr",
			"zw" };

	/**
	 * the hastable that stores the dictionary of stopwords
	 */
	private static Hashtable indexStopwordDictionary = new Hashtable();

	/**
	 * a list of stopwords for portuguese, spanish, english, german and french
	 */

	private static String[] indexStopwords = { "a", "ao", "aos", "abaixo", "adiante", "agora", "ali", "antes", "aqui",
			"as", "�s", "�s", "atr�s", "and", "bem", "but", "by", "com", "como", "de", "do", "da", "dos", "das",
			"debaixo", "demais", "depois", "e", "ela", "elas", "ele", "eles", "em", "entre", "eu", "este", "for",
			"fora", "in", "is", "junto", "longe", "l�", "mais", "mas", "menos", "muito", "nunca", "n�o", "n�s",
			"no", "na", "nos", "nas", "o", "of", "onde", "os", "at�", "ou", "or", "on", "para", "por", "porque",
			"pouco", "pt", "qual", "quando", "quanto", "que", "quem", "se", "ser", "sem", "sempre", "sim", "sob",
			"sobre", "sua", "the", "this", "talvez", "todas", "todos", "toda", "todo", "to", "um", "uma", "voc�",
			"voc�s", "v�s", "b", "c", "d", "f", "g", "h", "i", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t",
			"u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "�", "�", "�", "�",
			"�", "�", "�", "�", "�", "�", "�", "�", "�", "�", "�", "�", "�", "�", "�",
			"http", "www" };

	/**
	 * the hastable that stores the dictionary of stopwords
	 */
	private static Hashtable stopwordDictionary = new Hashtable();

	/**
	 * a list of stopwords for portuguese, spanish, english, german and french
	 */
	private static String[] stopwords = { "meu", "vais", "quais", "vai", "a", "ao", "aos", "abaixo", "adiante", "agora",
			"ali", "antes", "aqui", "as", "�s", "�s", "de", "da", "esse", "isso", "do", "das", "dos", "&", "em",
			"sob", "sobre", "no", "ou", "na", "nos", "nas", "neste", "nestes", "nesta", "nestas", "deste", "daquela",
			"desta", "daquele", "e", "ao", "aos", "naquele", "naquela", "naqueles", "naquelas", "atr�s", "and", "bem",
			"but", "by", "com", "como", "de", "do", "da", "dos", "das", "debaixo", "demais", "depois", "e", "ela",
			"elas", "ele", "eles", "em", "entre", "eu", "este", "for", "fora", "in", "is", "junto", "longe", "l�",
			"mais", "mas", "menos", "muito", "assim", "isto", "aquilo", "tamb�m", "tambem", "nunca", "n�o", "n�s",
			"no", "na", "por�m", "porem", "numa", "numas", "nos", "nas", "o", "of", "onde", "os", "at�", "ou", "or",
			"on", "para", "por", "porque", "pouco", "pt", "qual", "quando", "quanto", "que", "quem", "se", "ser", "sem",
			"sempre", "sim", "sob", "sobre", "sua", "the", "this", "talvez", "todas", "todos", "toda", "todo", "to",
			"um", "uma", "voc�", "voc�s", "v�s", "tem", "t�m", "b", "c", "d", "f", "g", "h", "i", "j", "k", "l",
			"m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "0", "�", "�", "�", "�", "�", "�", "�", "�", "�", "�", "�", "�", "�", "�",
			"�", "�", "�", "�", "�", "http", "www", "pela", "pelas", "pelo", "pelos", "ser�", "ser�o",
			"dos", "a", "bdzie", "gdzie", "jest", "lat", "nawet", "ponad", "s", "tym", "aby", "bd", "http", "jeszcze",
			"lecz", "nich", "poniewa", "t", "tys", "albo", "chce", "i", "jeli", "lub", "nie", "poza", "ta", "tzw",
			"ale", "cho", "ich", "jeeli", "m", "niej", "proc", "tak", "t", "ani", "co", "im", "ju", "ma", "nim",
			"przed", "takich", "u", "a", "coraz", "inne", "j", "maj", "ni", "przede", "takie", "w", "b", "czy", "i",
			"k", "mamy", "nowe", "przez", "take", "we", "bardzo", "czyli", "ja", "kiedy", "mial", "np", "przy", "tam",
			"wie", "bez", "czsto", "jak", "kilku", "mimo", "nr", "r", "te", "wic", "bo", "d", "jakie", "kto", "mln",
			"o", "raz", "tego", "wr�d", "bowiem", "dla", "jako", "kt�ra", "mog", "od", "razie", "tej", "z", "by",
			"do", "java", "kt�re", "moe", "ok", "roku", "temu", "za", "byli", "dwie", "je", "kt�rego", "mona", "on",
			"rz", "ten", "za", "bym", "dw�ch", "jeden", "kt�rej", "mu", "one", "r�wnie", "te", "ze", "byl", "e",
			"jednak", "kt�ry", "musi", "oraz", "s", "to", "zl", "byla", "g", "jednym", "kt�rych", "na", "p", "si",
			"trzy", "e", "bylo", "gdy", "jedynie", "kt�rym", "nad", "pl", "sobie", "tu", "eby", "byly", "gdyby",
			"jego", "kt�rzy", "nam", "po", "strona", "tych", "by", "gdy", "jej", "l", "nas", "pod", "swoje", "tylko",
			"a", "be", "each", "had", "less", "off", "side", "thin", "where", "about", "became", "eg", "has", "ltd",
			"often", "since", "third", "whereafter", "above", "because", "eight", "have", "made", "on", "sincere",
			"this", "whereas", "across", "become", "either", "he", "many", "once", "six", "those", "whereby", "after",
			"becomes", "eleven", "hence", "may", "one", "sixty", "though", "wherein", "afterwards", "becoming", "else",
			"her", "me", "only", "so", "three", "whereupon", "again", "been", "elsewhere", "here", "meanwhile", "onto",
			"some", "through", "wherever", "against", "before", "empty", "hereafter", "might", "or", "somehow",
			"throughout", "whether", "all", "beforehandenough", "hereby", "mill", "other", "someone", "thru", "which",
			"almost", "behind", "etc", "herein", "mine", "others", "something", "thus", "while", "alone", "being",
			"even", "hereupon", "more", "otherwise", "sometime", "to", "whither", "along", "below", "ever", "hers",
			"moreover", "our", "sometimes", "together", "who", "already", "beside", "every", "herself", "most", "ours",
			"somewhere", "too", "whoever", "also", "besides", "everyone", "him", "mostly", "ourselves", "still", "top",
			"whole", "although", "between", "everything", "himself", "move", "out", "such", "toward", "whom", "always",
			"beyond", "everywherehis", "much", "over", "system", "towards", "whose", "am", "both", "except", "how",
			"must", "own", "take", "twelve", "why", "among", "bottom", "few", "however", "my", "part", "ten", "twenty",
			"will", "amongst", "but", "fifteen", "hundred", "myself", "per", "th", "two", "with", "amoungst", "by",
			"fill", "i", "name", "perhaps", "than", "un", "within", "amount", "call", "find", "ie", "namely", "please",
			"that", "under", "without", "an", "can", "first", "if", "neither", "put", "the", "until", "would", "and",
			"cannot", "five", "in", "never", "rather", "their", "up", "yet", "another", "cant", "for", "inc",
			"neverthelesre", "them", "upon", "you", "any", "co", "former", "indeed", "next", "s", "themselvesus",
			"your", "anyhow", "con", "formerly", "interest", "nine", "same", "then", "very", "yours", "anyone", "could",
			"forty", "into", "no", "see", "thence", "via", "yourself", "anything", "couldn", "found", "is", "nobody",
			"seem", "there", "was", "yourselves", "anyway", "cry", "four", "it", "none", "seemed", "thereafter", "we",
			"anywhere", "describe", "from", "its", "noone", "seeming", "thereby", "well", "are", "detail", "front",
			"itself", "nor", "seems", "therefore", "were", "around", "do", "full", "keep", "not", "serious", "therein",
			"what", "as", "done", "further", "last", "nothing", "several", "there", "upon", "whatever", "at", "down",
			"get", "latter", "now", "she", "these", "when", "away", "due", "give", "latterly", "nowhere", "should",
			"they", "whence", "back", "during", "go", "least", "of", "show", "thick", "whenever", "no", "a", "about",
			"again", "against", "ago", "ahora", "ainda", "ainsi", "al", "algum", "alguma", "alguna", "algunas",
			"alguno", "algunos", "all", "alla", "alors", "alrededor", "also", "alta", "altas", "alto", "altos", "among",
			"an", "and", "anho", "anhos", "annee", "anness", "ano", "anos", "another", "anothers", "ans", "ante",
			"antes", "any", "anymore", "ao", "aos", "apos", "apres", "aqui", "na", "are", "aren't", "around", "as",
			"asi", "asses", "at", "ate", "au", "aucun", "aucunne", "aun", "aupres", "aussi", "autant", "autrefois",
			"autres", "aux", "avaient", "avais", "avec", "avons", "away", "ayant", "back", "baisse", "baixa", "baixo",
			"baja", "bajas", "bajo", "bajos", "be", "because", "before", "begin", "beginning", "begins", "behavior",
			"behind", "belong", "below", "best", "between", "bien", "big", "bigger", "biggest", "bis", "bon", "bonne",
			"bonnes", "buen", "buena", "buenas", "bueno", "buenos", "but", "by", "cabe", "cada", "can", "cause", "ce",
			"celui", "certa", "certain", "certaine", "certaines", "certains", "certas", "certe", "certes", "certo",
			"certos", "ces", "cese", "cet", "cette", "cettes", "ceux", "chacun", "chacunne", "chaque", "ci", "cierta",
			"ciertas", "cierto", "ciertos", "com", "comme", "como", "con", "contra", "contre", "corta", "cortas",
			"corte", "cortes", "corto", "cortos", "cosa", "cosas", "cost", "costo", "costos", "costs", "course", "cuya",
			"cuyas", "cuyo", "cuyos", "dans", "das", "de", "dehors", "deja", "del", "dela", "demas", "des", "desde",
			"despues", "desquels", "di", "do", "don", "donc", "donde", "dont", "down", "dr", "du", "due", "durante",
			"during", "e", "each", "el", "ella", "ellas", "elles", "ellos", "else", "elsewere", "em", "encore", "seu",
			"end", "enfrente", "entonces", "entrante", "entre", "es", "esa", "esas", "eso", "esos", "est", "esta",
			"estando", "estar", "este", "esto", "estos", "estoy", "et", "etaient", "etc", "ete", "etes", "etre", "even",
			"ever", "every", "everybody", "facon", "faire", "falta", "fast", "fe", "felt", "few", "fija", "fijas",
			"fijo", "fijos", "fil", "fill", "fin", "font", "for", "form", "forma", "formacao", "formacion", "formar",
			"formas", "formation", "forms", "forte", "fortes", "frente", "from", "fue", "fuer", "fuera", "full", "go",
			"good", "gral", "great", "greather", "hace", "hacer", "hacia", "had", "half", "has", "hasta", "haut",
			"haute", "hautes", "hauts", "have", "hay", "he", "hechos", "her", "here", "high", "higher", "highest",
			"him", "himself", "his", "how", "hui", "i", "I've", "ici", "if", "ii", "iii", "il", "ils", "in", "input",
			"into", "ir", "is", "isn't", "it", "it's", "its", "itself", "jusqu", "l", "la", "las", "late", "nossa",
			"nossas", "le", "leida", "less", "leur", "leurs", "lieu", "lieux", "lo", "lors", "lorsque", "los", "ma",
			"main", "maintenant", "mais", "many", "mas", "me", "meanwhile", "meme", "menos", "mes", "mi", "mia", "mias",
			"mien", "mienne", "mine", "minha", "minhas", "minho", "minhos", "minus", "mio", "mios", "moi", "moins",
			"mon", "more", "most", "mucha", "muchas", "mucho", "muchos", "my", "ne", "mext", "mi", "no", "non", "nor",
			"nos", "nosotras", "nosotros", "not", "notre", "notres", "nous", "now", "nuestra", "nuestras", "nuestro",
			"nuestros", "nul", "o", "of", "off", "on", "once", "ont", "onto", "os", "other", "others", "otra", "otras",
			"otro", "otros", "ou", "oui", "out", "outra", "outras", "outro", "outros", "outside", "over", "overall",
			"own", "owner", "par", "para", "parce", "pas", "pendant", "per", "pero", "peu", "peut", "plus", "poca",
			"pocas", "poco", "pocos", "por", "porque", "pour", "prochain", "prochaine", "prochaines", "prochains",
			"quand", "que", "quel", "quelle", "quelles", "quelque", "quelques", "quels", "qui", "round", "sa", "sans",
			"segun", "selon", "senor", "ses", "she", "si", "sien", "sienne", "sin", "small", "smallest", "so", "sob",
			"sobra", "sobre", "some", "something", "somethings", "sommes", "son", "sont", "sou", "sous", "susu", "sua",
			"suas", "such", "suis", "suo", "suos", "sur", "sus", "suya", "suyas", "suyo", "suyos", "ta", "tant", "tel",
			"telle", "telles", "tels", "tes", "than", "that", "the", "their", "them", "then", "they", "thing", "things",
			"this", "those", "through", "thus", "to", "toda", "todas", "todavia", "todo", "todos", "toi", "ton", "too",
			"touda", "toudas", "toudo", "toudos", "tous", "tout", "toute", "toutes", "tras", "tu", "tus", "tuya",
			"tuyas", "tuyo", "tuyos", "um", "uma", "umas", "umo", "umos", "un", "una", "unas", "une", "uno", "unos",
			"until", "up", "upon", "us", "vers", "versus", "vosotras", "vosotros", "votre", "votres", "vous", "vs",
			"vuestra", "vuestras", "vuestro", "vuestros", "was", "we", "were", "what", "when", "where", "whether",
			"while", "why", "with", "without", "worth", "worthwhile", "would", "y", "year", "yes", "yo", "you", "aber",
			"als", "am", "an", "auch", "auf", "aus", "bei", "bin", "bis", "bist", "da", "dadurch", "daher", "darum",
			"das", "da�", "dein", "deine", "dem", "den", "der", "des", "deshalb", "die", "dies", "dieser", "dieses",
			"doch", "dort", "du", "durch", "ein", "eine", "einem", "einen", "einer", "eines", "er", "es", "euer",
			"eure", "f�r", "hatte", "hatten", "hattest", "hattet", "hier", "hinter", "ich", "ihr", "ihre", "im", "in",
			"ist", "ja", "jede", "jedem", "jeden", "jeder", "jedes", "jener", "jenes", "jetzt", " kann", "kannst",
			"k�nnen", "k�nnt", "machen", "mein", "meine", "mit", "mu�", "mu�t", "m�ssen", "m��t", "nach",
			"nachdem", " nein", "nicht", "nun", "oder", "seid", "sein", "seine", "sich", "sie", "sind", "soll",
			"sollen", "sollst", "sollt", "sonst", "soweit", "sowie", "und", "unser", "unsere", "unter", "vom", "von",
			"vor", "wann", "warum", "was", "weiter", "weitere", " wenn", "wer", "werde", "werden", "werdet", "weshalb",
			"wie", "wieder", "wieso", "wir", "wird", "wirst", "wo", "zu", "zum", " zur", "�ber", "your", "yours",
			"abaixo", "adiante", "agora", "ali", "atr�s", "bastante", "bem", "contra", "debaixo", "demais", "depois",
			"depressa", "devagar", "direito", "ela", "elas", "ele", "eles", "entre", "eu", "fora", "junto", "longe",
			"muito", "ninguem", "nunca", "n�o", "n�s", "onde", "pouco", "pr�ximo", "qual", "quando", "quanto",
			"quem", "se", "sem", "sempre", "sim", "talvez", "vagarosamente", "voc�", "voc�s", "v�s", "a", "about",
			"above", "according", "across", "actually", "adj", "after", "afterwards", "again", "against", "all",
			"almost", "alone", "along", "already", "also", "although", "always", "among", "amongst", "an", "and",
			"another", "any", "anyhow", "anyone", "anything", "anywhere", "are", "aren't", "around", "as", "at", "b",
			"be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "begin",
			"beginning", "behind", "being", "below", "beside", "besides", "between", "beyond", "billion", "both", "but",
			"by", "c", "can", "can't", "cannot", "caption", "co", "co.", "could", "couldn't", "d", "did", "didn't",
			"do", "does", "doesn't", "don't", "down", "during", "e", "each", "eg", "eight", "eighty", "either", "else",
			"elsewhere", "end", "ending", "enough", "etc", "even", "ever", "every", "everyone", "everything",
			"everywhere", "except", "f", "few", "fifty", "first", "five", "for", "former", "formerly", "forty", "found",
			"four", "from", "further", "g", "h", "had", "has", "hasn't", "have", "haven't", "he", "he'd", "he'll",
			"he's", "hence", "her", "here", "here's", "hereafter", "hereby", "herein", "hereupon", "hers", "herself",
			"him", "himself", "his", "how", "however", "hundred", "i", "i'd", "i'll", "i'm", "i've", "ie", "if", "in",
			"inc.", "indeed", "instead", "into", "is", "isn't", "it", "it's", "its", "itself", "j", "k", "l", "last",
			"later", "latter", "latterly", "least", "less", "let", "let's", "like", "likely", "ltd", "m", "made",
			"make", "makes", "many", "maybe", "me", "meantime", "meanwhile", "might", "million", "miss", "more",
			"moreover", "most", "mostly", "mr", "mrs", "much", "must", "my", "myself", "n", "namely", "neither",
			"never", "nevertheless", "next", "nine", "ninety", "no", "nobody", "none", "nonetheless", "noone", "nor",
			"not", "nothing", "now", "nowhere", "o", "of", "off", "often", "on", "once", "one", "one's", "only", "onto",
			"or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "overall", "own", "p",
			"per", "perhaps", "q", "r", "rather", "recent", "recently", "s", "same", "seem", "seemed", "seeming",
			"seems", "seven", "seventy", "several", "she", "she'd", "she'll", "she's", "should", "shouldn't", "since",
			"six", "sixty", "so", "some", "s�o", "foi", "somehow", "someone", "something", "sometime", "sometimes",
			"somewhere", "still", "stop", "such", "t", "taking", "ten", "than", "that", "that'll", "that's", "that've",
			"the", "their", "them", "themselves", "then", "thence", "there", "there'd", "there'll", "there're",
			"there's", "there've", "thereafter", "thereby", "therefore", "therein", "thereupon", "pr�", "pre", "pos",
			"p�s", "these", "they", "they'd", "they'll", "they're", "they've", "thirty", "this", "those", "though",
			"thousand", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "toward", "towards",
			"trillion", "twenty", "two", "u", "under", "unless", "unlike", "unlikely", "until", "up", "upon", "us",
			"used", "using", "v", "very", "via", "w", "was", "wasn't", "we", "we'd", "we'll", "we're", "we've", "well",
			"were", "weren't", "what", "what'll", "what's", "what've", "whatever", "when", "whence", "whenever",
			"where", "where's", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether",
			"which", "while", "whither", "acima", "abaixo", "est�", "who", "who'd", "who'll", "who's", "whoever",
			"whole", "whom", "whomever", "whose", "why", "will", "with", "within", "without", "won't", "would",
			"wouldn't", "x", "y", "yes", "yet", "you", "you'd", "you'll", "you're", "you've", "your", "yours",
			"yourself", "yourselves", "z", "aber", "als", "am", "an", "auch", "auf", "aus", "bei", "bin", "bis", "bist",
			"da", "dadurch", "daher", "darum", "das", "da�", "dein", "deine", "dem", "den", "der", "des", "deshalb",
			"die", "dies", "dieser", "dieses", "doch", "dort", "du", "durch", "ein", "eine", "einem", "einen", "einer",
			"eines", "er", "es", "euer", "eure", "f�r", "hatte", "hatten", "hattest", "hattet", "hier", "hinter",
			"ich", "ihr", "ihre", "necessita", "im", "in", "ist", "ja", "jede", "jedem", "jeden", "jeder", "jedes",
			"jener", "jenes", "jetzt", "kann", "kannst", "k�nnen", "k�nnt", "machen", "mein", "meine", "mit",
			"mu�", "mu�t", "m�ssen", "m��t", "nach", "nachdem", "nein", "nicht", "nun", "oder", "seid",
			"sein", "seine", "sich", "sie", "sind", "soll", "sollen", "sollst", "sollt", "sonst", "soweit", "sowie",
			"und", "unser", "unsere", "unter", "vom", "von", "vor", "wann", "warum", "was", "weiter", "weitere", "wenn",
			"wer", "werde", "werden", "werdet", "weshalb", "wie", "wieder", "wieso", "wir", "wird", "wirst", "wo", "zu",
			"zum", "zur", "�ber", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "�", "�", "�", "�", "�",
			"�", "�", "�", "�", "�", "�", "�", "�", "�", "�", "�", "�", "�", "�" };

	/**
	 * the hastable that stores the dictionary of internet specific stopwords
	 */
	private static Hashtable internetStopwordDictionary = new Hashtable();

	private static final StopWords _theInstance = new StopWords();

	public static StopWords getInstance() {
		return _theInstance;
	}

	/**
	 * checks if a given term is an internet specific stopword
	 *
	 * @param term
	 *            a string with the term to be checked
	 * @return true if the term in the string is an internet specific stopword and
	 *         false otherwhise
	 * @pre the string contains only one word
	 */
	public static boolean isInternetStopWord(String t) {
		if (t == null) {
			return false;
		}
		String term = t.toLowerCase();
		return internetStopwordDictionary.containsKey(term);
	}

	public static boolean isStopWord(String t) {
		if (t == null) {
			return false;
		}
		String term = t.toLowerCase();
		return stopwordDictionary.containsKey(term);
	}

	/**
	 * checks if a given term is a stopword
	 *
	 * @param term
	 *            a string with the term to be checked
	 * @return true if the term in the string is a stopword and false otherwhise
	 * @pre the string contains only one word
	 */
	public static boolean isIndexStopWord(String t) {
		if (t == null) {
			return false;
		}
		String term = t.toLowerCase();
		return indexStopwordDictionary.containsKey(term);
	}

	private StopWords() {
		Integer int1 = new Integer(1);
		stopwordDictionary = new Hashtable();
		for (int i = stopwords.length - 1; i >= 0; i--) {
			stopwordDictionary.put(stopwords[i], int1);
		}
		stopwords = null;
		internetStopwordDictionary = new Hashtable();
		for (int i = internetStopwords.length - 1; i >= 0; i--) {
			internetStopwordDictionary.put(internetStopwords[i], int1);
		}
		internetStopwords = null;
		indexStopwordDictionary = new Hashtable();
		for (int i = indexStopwords.length - 1; i >= 0; i--) {
			indexStopwordDictionary.put(indexStopwords[i], int1);
		}
		indexStopwords = null;
	}

}
